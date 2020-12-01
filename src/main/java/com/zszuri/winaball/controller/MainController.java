package com.zszuri.winaball.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface MainController {

	// Pages
    public static final String MAIN_PAGE = "main";
    
    @GetMapping
    public String goToMainPage(Model model);
}
