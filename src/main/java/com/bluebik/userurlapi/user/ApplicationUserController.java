package com.bluebik.userurlapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

    @Autowired
    private ApplicationUserService applicationUserService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApplicationUserController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @RequestMapping(value = "/signUp",method = RequestMethod.POST)
    public ResponseEntity<String> signUp(@RequestBody ApplicationUser user) {
        if(isValidUser(user)) {
            ApplicationUser existingUser = applicationUserService.findUserByUsername(user.getUsername());
            if (existingUser == null) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                applicationUserService.saveUser(user);
                return new ResponseEntity<String>("Add user success", HttpStatus.OK);
            }
            return new ResponseEntity<String>("User Existing", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Invalid User", HttpStatus.BAD_REQUEST);
    }

    private boolean isValidUser(ApplicationUser user) {
        boolean isUserValid = (user != null
                                    && (user.getUsername() != null && !user.getUsername().isEmpty())
                                    && (user.getPassword() != null && !user.getPassword().isEmpty()));

        return isUserValid;
    }
}
