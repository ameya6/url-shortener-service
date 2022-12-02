package org.shortner.dao;

import lombok.extern.log4j.Log4j2;
import org.data.model.entity.URLInfo;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Repository
@Log4j2
public class URLShortnerDBDao {

    @Autowired
    private Session session;

    @Transactional("transactionManager")
    public void save(URLInfo urlInfo) {
        session.save(urlInfo);
    }

    public URLInfo getByHash(String urlHash) {
        Query<URLInfo> query=session.createQuery("from URLInfo where urlHash=:urlHash");
        query.setParameter("urlHash", urlHash);
        URLInfo urlInfo = query.uniqueResult();
        return urlInfo;
    }

    public URLInfo getByCode(String code) {
        Query<URLInfo> query=session.createQuery("from URLInfo where shortCode=:shortCode");
        query.setParameter("shortCode", code);
        URLInfo urlInfo = query.uniqueResult();
        return urlInfo;
    }

}
