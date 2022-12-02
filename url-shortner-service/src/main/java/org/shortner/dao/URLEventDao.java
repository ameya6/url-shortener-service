package org.shortner.dao;

import lombok.extern.log4j.Log4j2;
import org.data.dao.URLEventRepository;
import org.data.model.event.URLEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
public class URLEventDao {

    @Autowired
    private URLEventRepository eventRepository;

    public void save(URLEvent event) {
        eventRepository.save(event)
                .doOnSuccess(response -> log.info(response))
                .doOnError(error -> log.info(error))
                .subscribe();
    }
}
