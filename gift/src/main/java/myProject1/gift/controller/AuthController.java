package myProject1.gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    //==거부페이지 display==//
    @GetMapping("/denied")
    public String dispDeniedPage(){
        return "auth/denied";
    }
}
