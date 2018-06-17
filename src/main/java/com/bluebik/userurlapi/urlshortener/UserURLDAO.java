package com.bluebik.userurlapi.urlshortener;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserURLDAO extends CrudRepository<UserURL,Integer> {

    UserURL findByKey(String key);

    UserURL findByActualURL(String shortURL);

}
