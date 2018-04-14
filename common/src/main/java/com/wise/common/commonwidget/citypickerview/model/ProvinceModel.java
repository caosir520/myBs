package com.wise.common.commonwidget.citypickerview.model;

import java.io.Serializable;
import java.util.List;

public class ProvinceModel implements Serializable{
	private String name;
	private String id;
	private String code;
	private List<CityModel> sub;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> sub) {
		super();
		this.name = name;
		this.sub = sub;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getSub() {
		return sub;
	}

	public void setSub(List<CityModel> sub) {
		this.sub = sub;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + sub + "]";
	}
	
}
