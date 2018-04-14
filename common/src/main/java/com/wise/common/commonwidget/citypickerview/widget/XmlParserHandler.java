package com.wise.common.commonwidget.citypickerview.widget;


import android.content.Context;
import android.content.res.AssetManager;

import com.wise.common.commonwidget.citypickerview.model.ProvinceModel;

import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class XmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

	public XmlParserHandler() {

	}

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}
//
//	@Override
//	public void startDocument() throws SAXException {
//		// 当读到第一个开始标签的时候，会触发这个方法
//	}
//
//	ProvinceModel provinceModel = new ProvinceModel();
//	CityModel cityModel = new CityModel();
//	DistrictModel districtModel = new DistrictModel();
//
//	@Override
//	public void startElement(String uri, String localName, String qName,
//							 Attributes attributes) throws SAXException {
//		// 当遇到开始标记的时候，调用这个方法
//		if (qName.equals("province")) {
//			provinceModel = new ProvinceModel();
//			provinceModel.setName(attributes.getValue(0));
//			provinceModel.setCityList(new ArrayList<CityModel>());
//		} else if (qName.equals("city")) {
//			cityModel = new CityModel();
//			cityModel.setName(attributes.getValue(0));
//			cityModel.setDistrictList(new ArrayList<DistrictModel>());
//		} else if (qName.equals("district")) {
//			districtModel = new DistrictModel();
//			districtModel.setName(attributes.getValue(0));
//			districtModel.setZipcode(attributes.getValue(1));
//		}
//	}
//
//	@Override
//	public void endElement(String uri, String localName, String qName)
//			throws SAXException {
//		// 遇到结束标记的时候，会调用这个方法
//		if (qName.equals("district")) {
//			cityModel.getDistrictList().add(districtModel);
//		} else if (qName.equals("city")) {
//			provinceModel.getCityList().add(cityModel);
//		} else if (qName.equals("province")) {
//			provinceList.add(provinceModel);
//		}
//	}
//
//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//	}

	public String readAssertResource(Context context, String strAssertFileName) {
		AssetManager assetManager = context.getAssets();
		String strResponse = "";
		try {
			InputStream ims = assetManager.open(strAssertFileName);
			strResponse = getStringFromInputStream(ims);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strResponse;
	}

	private String getStringFromInputStream(InputStream a_is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(a_is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
        return sb.toString();
	}
}
