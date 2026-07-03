package com.dmart.DmartApplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmart.DmartApplication.model.DashboardDTO;
import com.dmart.DmartApplication.service.DashboardService;


 
	@RestController
	@RequestMapping("/api/dashboard")
	public class DashboardController {
		 private static final Logger logger =
		            LogManager.getLogger(ProductController.class);
	    @Autowired
	    private DashboardService service;

	    @GetMapping
	    public DashboardDTO getDashboardSummary(){
	    	logger.info("DashBoard API recevied");

	        return service.getDashboardSummary();

	    }
}
