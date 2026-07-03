package com.dmart.DmartApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmart.DmartApplication.model.DashboardDTO;
import com.dmart.DmartApplication.repository.DashboardDAO;




	@Service
	public class DashboardService {

	    @Autowired
	    private DashboardDAO dashboardDAO;

	    public DashboardDTO getDashboardSummary() {

	        return dashboardDAO.getDashboardSummary();
	        
	        

	    }
}
