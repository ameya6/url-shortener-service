package org.shortner.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.data.model.entity.URLInfo;
import org.data.model.request.URLInfoRequest;
import org.data.model.response.DUIDResponse;
import org.data.model.response.URLInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
*
* id
uuid
longUrl
shortCode
distributedUID
createdAt
hashCode
hashcodetype


convert long url to hashvalue
check if the hash exists
	- check in redis set
	if exists get the short url from the database
	else create a new short url
	store in db, redis and return the short url


store in url hach in redis set

get long url
convert it to hash
check in redis if hash exists
convert to short url
store in db, redis and return the short url


*
* */

@Service
@Log4j2
public class URLShortnerService {

    @Autowired
    private DigestUtils digestUtils;

    @Value("${domain.url}")
    private String domainURL;

    @Value("${duid-server}")
    private String duidServer;

    @Autowired
    private Gson gson;

    private final String map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public URLInfoResponse shortner(URLInfoRequest urlInfoRequest) {
        URLInfoResponse response = null;
        try {
            URLInfo urlInfo = createShortURL(urlInfoRequest);
            response = urlInfoResponse(urlInfo);
            log.info("url info " + urlInfo);
            log.info("url info " + response);
            return response;
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage(), e);
            response = URLInfoResponse.builder().message("Failed to create short URL").build();
        } finally {
            return response;
        }
    }

    private String hash(String originalString) {
        return digestUtils.digestAsHex(originalString);
    }

    private URLInfo createShortURL(URLInfoRequest urlInfoRequest) throws Exception {
        Long uid = distributedUID();
        String shortCode = base62(uid);
        LocalDateTime expireAt = urlInfoRequest.getExpireAt() == null ? LocalDateTime.now().plusYears(10) : urlInfoRequest.getExpireAt();
        String urlHash = hash(urlInfoRequest.getUrl());

        return URLInfo.builder()
                .longURL(urlInfoRequest.getUrl())
                .uuid(UUID.randomUUID())
                .distributedUID(uid)
                .urlHash(urlHash)
                .createdAt(LocalDateTime.now())
                .expireAt(expireAt)
                .shortCode(shortCode)
                .domain(domainURL)
                .build();
    }

    public Long distributedUID() throws Exception {
        //log.info(uidServer);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(duidServer))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), DUIDResponse.class).getDuid();
    }

    public String base62(long n)
    {
        StringBuilder shorturl = new StringBuilder("");
        for(int i = 0; i < 7; i++)
        {
            int mod = (int)(n % 62);
            shorturl.append(map.charAt(mod));
            n = n/62;
        }
        return shuffle(shorturl.reverse().toString());
    }

    public String shuffle(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size() != 0){
            int randomPicker = (int)(Math.random() * characters.size());
            output.append(characters.remove(randomPicker));
        }
        return output.toString();
    }

    private URLInfoResponse urlInfoResponse(URLInfo urlInfo) {
        return URLInfoResponse
                .builder()
                .url(urlInfo.getLongURL())
                .expireAt(urlInfo.getExpireAt())
                .shortUrl(urlInfo.getDomain() + urlInfo.getShortCode())
                .build();
    }

}
