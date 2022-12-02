package org.shortner.service;

import lombok.extern.log4j.Log4j2;
import org.shortner.repository.CleanUpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CleanUpService {
    @Autowired
    private CleanUpDao cleanUpDao;

    @Scheduled(cron = "0 0 */1 * * *")
    public void cleanUp() {
        log.info("Clean Up called");
        int num = cleanUpDao.cleanExpiredURL();
        log.info("Delete result : " + num);
    }
}
