package org.spark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.data.model.entity.User;
import org.data.model.request.URLInfoRequest;
import org.data.model.response.URLInfoResponse;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.spark.model.URLInfoResponseSpark;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Log4j2
public class URLShortenerSpark {

    public static void main(String[] args) {
        JavaSparkContext sc = sparkContext(esConfig(initConfig(), "192.168.0.108", "9200"));
        //List<Long> nums = new LinkedList<>();
        //LongStream.range(0, 1000000l).forEach(num -> nums.add(num));
        //log.info(nums.size());
        final long MILLION = 1000000;

        String local = "http://localhost:9876/shortner/";
        String remote = "http://192.168.0.104/duid/detail";
        String url = local;
        Map<Integer, User> users = createUsers();



        //JavaEsSpark.saveJsonToEs(responseRDD, "duid_responses");

        JavaRDD<String> processRDD = getUrl(sc, "/home/ameya/Downloads/20221202170000.LINKS.TXT")
                .mapPartitions(urlPartition -> {
                    Random random = new Random();
                    RestTemplate restTemplate = new RestTemplate();
                    Gson gson = new GsonBuilder().create();
                    List<String> responses = new LinkedList<>();

                    while (urlPartition.hasNext()) {
                        String link = urlPartition.next();
                        User user = users.get(random.nextInt(500));
                        URLInfoRequest request = request(link, user);
                        LocalDateTime startTime = LocalDateTime.now();
                        URLInfoResponseSpark response = restTemplate.postForObject(url, new HttpEntity<>(request), URLInfoResponseSpark.class);
                        LocalDateTime endTime = LocalDateTime.now();

                        Double processTimeInMilliSeconds = (double)Duration.between(startTime, endTime).toNanos() / MILLION;
                        response.setCreatedAt(LocalDateTime.now());
                        response.setProcessStartTime(startTime);
                        response.setProcessEndTime(endTime);
                        response.setTotalProcessTime(processTimeInMilliSeconds);
                        responses.add(gson.toJson(response));
                    }
                    return responses.iterator();
                });

        JavaEsSpark.saveJsonToEs(processRDD, "short_url_responses");

    }

    public static JavaRDD<String> getUrl(JavaSparkContext sc, String file) {
        return sc.textFile(file)
                .map(line -> line.split("\t"))
                .filter(arr -> arr.length > 1)
                .map(arr -> arr[4])
                .filter(link -> link.length() > 0);
    }

    public static Map<Integer, User> createUsers() {
        Map<Integer, User> users = new HashMap<>();

        IntStream.range(0, 500).forEach(num -> {
            User user = User.builder().id(System.nanoTime() << 5).userId(UUID.randomUUID()).build();
            users.put(num, user);
        });
        //log.info(users);
        return users;
    }

    public static URLInfoRequest request(String link, User user) {
        Random random = new Random();
        return URLInfoRequest.builder()
                .url(link)
                .userId(user.getId())
                .userUUID(user.getUserId().toString())
                .expireAt(LocalDateTime.now().plusDays(random.nextInt(1000))).build();
    }


    public static JavaSparkContext sparkContext(SparkConf conf) {
        return new JavaSparkContext(conf);
    }

    public static SparkConf initConfig() {
         return new SparkConf()
                 .setAppName("DUIDSpark")
                 .setMaster("local[*]")
                 .set("spark.executor.cores", "12")
                 .set("spark.executor.memory", "2G");
    }

    public static SparkConf esConfig(SparkConf conf, String host, String port) {
        conf.set("spark.es.nodes",host)
                .set("spark.es.port",port)
                .set("spark.es.nodes.wan.only","true");
        return conf;
    }
}
