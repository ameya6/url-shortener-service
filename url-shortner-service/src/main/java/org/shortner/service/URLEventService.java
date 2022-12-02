package org.shortner.service;

import lombok.extern.log4j.Log4j2;
import org.data.model.entity.URLInfo;
import org.data.model.event.URLEvent;
import org.shortner.dao.URLEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Log4j2
public class URLEventService {

    @Autowired
    private URLEventDao eventDao;

    private static final Long MILLION = 1_000_000l;

    public void save(URLInfo urlInfo, URLEvent event) {
        if(urlInfo != null) {
            event.setShortCode(urlInfo.getShortCode());
            event.setUrlId(urlInfo.getId());
            event.setUrlUUID(urlInfo.getUuid());
            event.setDistributedUID(urlInfo.getDistributedUID());
        } else {
            event.setMessage("urlInfo object is null");
        }
        event.setProcessEndTime(LocalDateTime.now());
        event.setTotalProcessTime((double) Duration.between(event.getProcessStartTime(), event.getProcessEndTime()).toNanos() / MILLION);
        eventDao.save(event);
    }
}
