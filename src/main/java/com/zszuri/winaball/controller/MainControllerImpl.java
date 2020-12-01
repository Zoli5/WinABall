package com.zszuri.winaball.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class MainControllerImpl implements MainController {
	
	@Autowired
	public MainControllerImpl() {
	}

	@Override
	public String goToMainPage(Model model) {
		return MAIN_PAGE;
	}
}
