package vn.minhquang.jobhunter.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    // @CrossOrigin // Allow all origins (CORS) of this api
    public String getHelloWorld() {
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
