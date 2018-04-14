package com.wise.common.commonwidget.citypickerview.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunpeng on 17/7/20.
 */
public class RegionBean implements Serializable{
    private String id;
    private String name;
    private List<RegionBean> children;
    private boolean hasAll;
    private int level;
    private String zipcode = "";

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public List<RegionBean> getChildren() {
        return children;
    }

    public void setChildren(List<RegionBean> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasAll() {
        return hasAll;
    }

    public void setHasAll(boolean hasAll) {
        this.hasAll = hasAll;
    }
}
