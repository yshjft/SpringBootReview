package com.jerry.springReview.web;

import com.jerry.springReview.web.dto.GreetingResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting() {
        return "greeting!";
    }

    @GetMapping("/greeting/dto")
    public GreetingResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new GreetingResponseDto(name, amount);
    }


}
