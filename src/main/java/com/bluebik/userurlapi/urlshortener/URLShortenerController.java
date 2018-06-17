package com.bluebik.userurlapi.urlshortener;

import com.bluebik.userurlapi.constant.HTTPURLConstant;
import com.bluebik.userurlapi.util.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class URLShortenerController {

    private static final Logger logger = LoggerFactory.getLogger(URLShortenerController.class);

    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    @Autowired
    private UserURLService userURLService;

    @RequestMapping(value = "/{shortenUrl}", method = RequestMethod.GET)
    public ResponseEntity<Object> redirect(@PathVariable String shortenUrl, HttpServletResponse resp) throws Exception {
        UserURL userURL = userURLService.findByShortURL(shortenUrl);
        if(userURL != null) {
            userURL.setUrlAccessCounter(userURL.getUrlAccessCounter() + 1);
            userURLService.saveOrUpdateUserURL(userURL);
            logger.info(userURL.getActualURL());
            URI uri = new URI(userURL.getActualURL());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/saveLink",method = RequestMethod.POST)
    public ResponseEntity<String> save(@RequestBody Map<String, Object> payload, HttpServletRequest req) {
        RandomString randomString = new RandomString(8);
        String actualURL = (String) payload.get("actualURL");
        if(validateURL(actualURL)) {
            actualURL = sanitizeURL(actualURL.trim());
            UserURL existedURL = userURLService.findByActualURL(actualURL);
            String key = "";
            if (existedURL != null) {
                key = existedURL.getKey();
            } else {
                key = randomString.nextString();
                UserURL userURL = new UserURL();
                userURL.setActualURL(actualURL);
                userURL.setKey(key);
                userURLService.saveOrUpdateUserURL(userURL);
            }
            return new ResponseEntity<String>(HTTPURLConstant.DOMAIN + key, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Not URL Try Again" , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/dashboard",method = RequestMethod.GET)
    public ResponseEntity<List<UserURL>> dashboard(HttpServletRequest req) {
        List<UserURL> userURLs = userURLService.listAllUserURL();
        return new ResponseEntity<List<UserURL>>(userURLs, HttpStatus.OK);
    }

    private boolean validateURL(String actualURL) {
        boolean isURLMatch = actualURL.matches(URL_REGEX);
        return isURLMatch;
    }

    private String sanitizeURL(String actualURL) {
        if(isURLNoHTTP(actualURL)) {
            actualURL = HTTPURLConstant.HTTPS_URL_PREFIX+actualURL;
        }
        return actualURL;
    }

    private boolean isURLNoHTTP(String actualURL){
        return !(actualURL.startsWith(HTTPURLConstant.HTTPS_URL_PREFIX) || actualURL.startsWith(HTTPURLConstant.HTTP_URL_PREFIX));
    }
}