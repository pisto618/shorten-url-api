package com.bluebik.userurlapi.aspect;

import com.bluebik.userurlapi.urlshortener.UserURL;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserURLAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserURLAspect.class);

    @Before(value = "execution(* com.bluebik.userurlapi.urlshortener.UserURLService.*(..)) and args(userURL)")
    public void beforeCreateUserURL(JoinPoint joinPoint, UserURL userURL){
        String key = userURL.getKey();
        String actualURL = userURL.getActualURL();
        logger.info("URL "+actualURL+" has been shorten as "+key+" and will be saved");
    }

    @After(value = "execution(* com.bluebik.userurlapi.urlshortener.UserURLService.*(..)) and args(userURL)")
    public void AfterCreateUserURL(JoinPoint joinPoint, UserURL userURL){
        String key = userURL.getKey();
        String actualURL = userURL.getActualURL();
        logger.info("URL "+actualURL+"("+key+")  has been saved");
    }

}


