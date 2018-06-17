package com.bluebik.userurlapi.aspect;

import com.bluebik.userurlapi.urlshortener.UserURL;
import com.bluebik.userurlapi.user.ApplicationUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationUserAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserURLAspect.class);

    @Before(value = "execution(* com.bluebik.userurlapi.user.ApplicationUserService.*(..)) and args(applicationUser)")
    public void beforeCreateUserURL(JoinPoint joinPoint, ApplicationUser applicationUser){
        String username = applicationUser.getUsername();
        logger.info("Username "+ username + " will be created");
    }

    @After(value = "execution(* com.bluebik.userurlapi.user.ApplicationUserService.*(..)) and args(applicationUser)")
    public void AfterCreateUserURL(JoinPoint joinPoint, ApplicationUser applicationUser){
        String username = applicationUser.getUsername();
        logger.info("Username "+ username +  " has been created");
    }

}
