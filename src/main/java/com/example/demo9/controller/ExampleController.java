package com.example.demo9.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/example")
public class ExampleController {

  @GetMapping("/ex1")
  public String ex1Get() {
    return "example/ex1";
  }

  @GetMapping("/ex2")
  public String ex2Get() {
    return "example/ex2";
  }

}
