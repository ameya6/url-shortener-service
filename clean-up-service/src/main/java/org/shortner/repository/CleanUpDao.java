package org.shortner.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CleanUpDao {

    @Autowired
    private Session session;

    @Transactional("transactionManager")
    public int cleanExpiredURL() {
        session.beginTransaction();
        Query query = session.createQuery("delete URLInfo where expire_at < now()");
        int result = query.executeUpdate();
        session.getTransaction().commit();
        return result;
    }


}
