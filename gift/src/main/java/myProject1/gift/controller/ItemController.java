package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import myProject1.gift.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public String createItemForm(){

        return "item/admin";
    }
}
