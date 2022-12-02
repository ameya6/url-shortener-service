package org.shortner.dao;

import org.data.dao.CacheDao;
import org.data.model.entity.URLInfo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class URLShortnerCacheDao {

    @Autowired
    private CacheDao<URLInfo> cacheDao;

    @Autowired
    private CacheDao<String> keysDao;

    @Value("${expiry-time}")
    private int EXPIRY_TIME;

    public String save(String key, URLInfo urlInfo, boolean expiry) {
        String response = cacheDao.save(key, urlInfo);
        if(expiry)
            cacheDao.expire(key, EXPIRY_TIME);
        return response;
    }

    public String save(String key, URLInfo urlInfo) {
        return save(key, urlInfo, false);
    }

    public String set(String key, String value, long seconds) {
        return seconds > 0 ? keysDao.setEx(key, seconds, value) : keysDao.set(key, value);
    }

    public String get(String key) {
        return keysDao.get(key);
    }
}
