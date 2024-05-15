package app.timepiece.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/getTest")
    public String getTest(){
        return "test";
    }

    @PostMapping("/getTest2")
    public String getTest2(){
        return "an beo";
    }
}
