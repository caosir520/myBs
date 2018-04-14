package com.wise.common.commonwidget.citypickerview.model;

import java.io.Serializable;
import java.util.List;

public class CityModel implements Serializable{
	private String name;
	private String code;
	private String zipcode;
	private String id;
	private List<String> sub;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CityModel() {
		super();
	}

	public CityModel(String name, List<String> districtList) {
		super();
		this.name = name;
		this.sub = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public List<String> getSub() {
		return sub;
	}

	public void setSub(List<String> sub) {
		this.sub = sub;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + sub
				+ "]";
	}
	
}
