package com.bluebik.userurlapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    void saveUser(ApplicationUser applicationUser) {
        applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser findUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

}
