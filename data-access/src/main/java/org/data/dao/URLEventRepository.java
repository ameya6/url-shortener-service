package org.data.dao;

import org.data.model.event.URLEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 *
 * For the below class @Table("[table_name]") needs to be specified on the entity class
 * e.g. below is the class URLEvent else it will give an error
 * @Autowired -> required a bean of type
 *
 * */
public interface URLEventRepository extends ReactiveCrudRepository<URLEvent, Integer> {
}
