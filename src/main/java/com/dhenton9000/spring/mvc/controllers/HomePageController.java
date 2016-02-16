package com.dhenton9000.spring.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.*;


/**
 * The controller for the homepage
 * @author Don
 *
 */
@Controller
public class HomePageController {
	
	
	
	private static Logger log = LogManager.getLogger(HomePageController.class);
	
        @RequestMapping("/")
	public String homePage() {
		return "index";
	}
	
	
	 
	
}
