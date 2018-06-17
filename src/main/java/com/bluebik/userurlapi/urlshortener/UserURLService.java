package com.bluebik.userurlapi.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserURLService {

    @Autowired
    public UserURLDAO userURLDAO;

    public UserURL findByShortURL(String shortURL) {
        if (shortURL == null || shortURL.isEmpty()) {
            throw new IllegalArgumentException("Short URL Can't be null");
        }
        return userURLDAO.findByKey(shortURL);
    }

    public UserURL findByActualURL(String actualURL) {
        if (actualURL == null || actualURL.isEmpty()) {
            throw new IllegalArgumentException("Actual URL Can't be null");
        }
        return userURLDAO.findByActualURL(actualURL);
    }


    public List<UserURL> listAllUserURL() {

        List<UserURL> userURLs = new ArrayList<UserURL>();
        userURLDAO.findAll().forEach(userURLs::add);
        if (userURLs.size() > 0) {
            return userURLs;
        } else {
            return null;
        }
    }

    public void saveOrUpdateUserURL(UserURL userURL) {
        if (userURL == null) {
            throw new IllegalArgumentException("The passed object cna not be null.");
        }
        userURLDAO.save(userURL);
    }



}
