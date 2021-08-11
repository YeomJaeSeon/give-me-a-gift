package myProject1.gift.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class DeniedController {

    //==user가 admin페이지에 접근하려할시 display되는 페이지 (접근 거부 페이지)==//
    @GetMapping("/denied")
    public String dispDeniedPage(){
        return "denied/denied";
    }
}
