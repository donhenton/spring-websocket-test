package com.dhenton9000.spring.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.*;


/**
 * The controller for the homepage
 * @author Don
 *
 */
@Controller
public class HomePageController {
	
	
	
	private static Logger log = LogManager.getLogger(HomePageController.class);
	
	
	@RequestMapping("/home")
	public ModelAndView homePage() {
		String message = "Hello World, Spring 3.0!";
		return new ModelAndView("tiles.homepage", "message", message);
	}
	
	@RequestMapping("/credits")
	public ModelAndView creditsPage() {
		
		return new ModelAndView("tiles.creditspage");
	}	
	
}
