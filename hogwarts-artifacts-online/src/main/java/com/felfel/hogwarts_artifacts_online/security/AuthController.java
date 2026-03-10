package com.felfel.hogwarts_artifacts_online.security;

import com.felfel.hogwarts_artifacts_online.system.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthController {

    private final AuthService authService;

    private final Logger LOGGER= LoggerFactory.getLogger(AuthController.class);

    /**
     * Instantiates a new Auth controller.
     *
     * @param authService the auth service
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login result.
     *
     * @param authentication the authentication
     * @return the result
     */
    @PostMapping("/login")
    public Result login(Authentication authentication)
    {
        LOGGER.debug("{} logged in successfully", authentication.getName());
        return new Result(true, HttpStatus.OK.value(), "User Info and JSON Web Token",authService.loginInfo(authentication));
    }
}
