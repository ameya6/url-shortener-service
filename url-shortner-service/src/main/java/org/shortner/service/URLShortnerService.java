package org.shortner.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;
import org.data.model.entity.URLInfo;
import org.data.model.request.URLInfoRequest;
import org.data.model.response.DUIDResponse;
import org.data.model.response.URLInfoResponse;
import org.shortner.dao.URLShortnerCacheDao;
import org.shortner.dao.URLShortnerDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private URLShortnerCacheDao shortnerCacheDao;

    @Autowired
    private URLShortnerDBDao shortnerDBDao;

    private static final Long EXPIRY_SECONDS_IN_WEEK = 86400 * 90l;

    private final String map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     *
     * Below function is responsible for handling the url shortner activities
     * 1. Creates the short url.
     * 2. Stores the object temporarily in cache.
     * 3. Stores the short code in cache to verify if code already exists.
     * 4. Stores the url hash in cache to verify the url already exists.
     * 5. Stores the object in persistent database.
     *
    **/
    public URLInfoResponse shortner(URLInfoRequest urlInfoRequest) {
        try {
            if(aliasExists(urlInfoRequest.getAlias())) {
                return urlInfoErrorResponse("Alias " + urlInfoRequest.getAlias() + " already exists");
            }
            URLInfo urlInfo = createShortURL(urlInfoRequest);
            URLInfoResponse response = urlInfoResponse(urlInfo);
            log.debug("url info " + urlInfo + "\nurl response " + response);
            save(urlInfo);
            return response;
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage(), e);
            return URLInfoResponse.builder().message("Failed to create short URL").build();
        } finally {
            // log events
        }
    }

    private boolean aliasExists(String alias) {
        return alias != null && getAlias(alias) != null;
    }

    private String getAlias(String alias) {
        return shortnerCacheDao.get(URLInfo.SHORT_CODE + alias);
    }

    public void save(URLInfo urlInfo) {
        shortnerCacheDao.set(URLInfo.URL_HASH + urlInfo.getUrlHash(), urlInfo.getUrlHash(), EXPIRY_SECONDS_IN_WEEK);
        shortnerCacheDao.set(URLInfo.SHORT_CODE + urlInfo.getShortCode(), urlInfo.getLongURL(), EXPIRY_SECONDS_IN_WEEK);
        shortnerCacheDao.save(URLInfo.KEY + urlInfo.getUuid(), urlInfo, true);
        shortnerDBDao.save(urlInfo);
    }

    private String hash(String originalString) {
        return digestUtils.digestAsHex(originalString);
    }

    private URLInfo createShortURL(URLInfoRequest urlInfoRequest) throws Exception {
        String urlHash = hash(urlInfoRequest.getUrl());
        if(hashExists(urlHash)){
            return shortnerDBDao.getByHash(urlHash);
        }

        Long uid = distributedUID();
        String shortCode = base62(uid);
        LocalDateTime expireAt = urlInfoRequest.getExpireAt() == null ? LocalDateTime.now().plusDays(90) : urlInfoRequest.getExpireAt();

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

    private boolean hashExists(String urlHash) {
        return shortnerCacheDao.get(URLInfo.URL_HASH + urlHash) != null;
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

    public URLInfoResponse urlInfoErrorResponse(String message) {
        return URLInfoResponse
                .builder()
                .message(message)
                .build();
    }
}
