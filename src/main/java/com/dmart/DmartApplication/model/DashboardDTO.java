package com.dmart.DmartApplication.model;

import java.util.List;



public class DashboardDTO {

	  private int totalProducts;
	    private int totalCustomers;
	    private int totalBills;
	    private double todayRevenue;
	    

	    private List<ProductDTO> latestProducts;
	    private List<LowStockDTO> lowStockProducts;
	    private List<ExpiryProductDTO> expiringProducts;

	    public int getTotalProducts() {
	        return totalProducts;
	    }

	    public void setTotalProducts(int totalProducts) {
	        this.totalProducts = totalProducts;
	    }

	    public int getTotalCustomers() {
	        return totalCustomers;
	    }

	    public void setTotalCustomers(int totalCustomers) {
	        this.totalCustomers = totalCustomers;
	    }

	    public int getTotalBills() {
	        return totalBills;
	    }

	    public void setTotalBills(int totalBills) {
	        this.totalBills = totalBills;
	    }

	    public double getTodayRevenue() {
	        return todayRevenue;
	    }

	    public void setTodayRevenue(double todayRevenue) {
	        this.todayRevenue = todayRevenue;
	    }


	    public List<LowStockDTO> getLowStockProducts() {
	        return lowStockProducts;
	    }

	    public void setLowStockProducts(List<LowStockDTO> lowStockProducts) {
	        this.lowStockProducts = lowStockProducts;
	    }

	    public List<ExpiryProductDTO> getExpiringProducts() {
	        return expiringProducts;
	    }

	    public void setExpiringProducts(List<ExpiryProductDTO> expiringProducts) {
	        this.expiringProducts = expiringProducts;
	    }
	    public List<ProductDTO> getLatestProducts() {
	        return latestProducts;
	    }

	    public void setLatestProducts(List<ProductDTO> latestProducts) {
	        this.latestProducts = latestProducts;
	    }


}
