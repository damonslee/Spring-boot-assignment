package com.hanaset.luke.web.rest;

import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.service.LukeAuthService;
import com.hanaset.luke.web.rest.support.LukeApiRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LukeAuthRest extends LukeApiRestSupport {

    private final LukeAuthService lukeAuthService;

    public LukeAuthRest(LukeAuthService lukeAuthService) {
        this.lukeAuthService = lukeAuthService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity userSignUp(@RequestBody SignRequest request) {
        return success(lukeAuthService.userSignUp(request));
    }

    @PostMapping("/sign_in")
    public ResponseEntity userSignIn(@RequestBody SignRequest request) {
        return success(lukeAuthService.userSignIn(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity userRefreshToken(@RequestHeader("Authorization") String token) {
        return success(lukeAuthService.userRefreshToken(token));
    }


}
