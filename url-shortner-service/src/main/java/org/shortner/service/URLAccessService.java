package org.shortner.service;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.data.model.entity.URLInfo;
import org.data.model.event.EventType;
import org.data.model.event.URLEvent;
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
    private URLEventService eventService;

    @Autowired
    private URLShortnerDBDao shortnerDBDao;
    public String redirect(String shortCode) {
        URLEvent event = URLEvent.create(EventType.URL_ACCESS);
        URLInfo urlInfo = null;
        try {
            String originalUrl = shortnerCacheDao.get(URLInfo.SHORT_CODE + shortCode);
            urlInfo = shortnerDBDao.getByCode(shortCode);
            return !Strings.isEmpty(originalUrl) ? originalUrl : urlInfo.getLongURL();
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage(), e);
            event.setException(e.getMessage());
            return "";
        } finally {
            eventService.save(urlInfo, event);
        }
    }
}
