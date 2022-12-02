package org.shortner.service;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.data.model.entity.URLInfo;
import org.shortner.dao.URLShortnerCacheDao;
import org.shortner.dao.URLShortnerDBDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class URLAccessService {

    @Autowired
    private URLShortnerCacheDao shortnerCacheDao;

    @Autowired
    private URLShortnerDBDao shortnerDBDao;
    public String redirect(String shortCode) {
        String originalUrl = shortnerCacheDao.get(URLInfo.SHORT_CODE + shortCode);
        return !Strings.isEmpty(originalUrl) ? originalUrl : shortnerDBDao.getByCode(shortCode).getLongURL();
    }
}
