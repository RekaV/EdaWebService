package com.eda.rest.model.dao;

import java.util.List;

import com.eda.rest.model.Insurance;
import com.eda.rest.model.Location;

public interface IInsuranceDao {

	public boolean createTravelInsurance(Insurance insurance);

	public List<Location> getLocation();

	public boolean createLocation(Location location);
	
	public boolean deleteInsurance(String application_no);
	
	public boolean deleteLocation(String location);
}
