package com.tugou.jgl.api;

import com.plugin.internet.core.RequestBase;
import com.plugin.internet.core.annotations.OptionalParam;
import com.plugin.internet.core.annotations.RequiredParam;
import com.plugin.internet.core.annotations.RestMethodUrl;

@RestMethodUrl("http://www.23jugeili.com:90/jgl/index.php?m=api&a=groupList")
public class GetListGroupRequest extends RequestBase<GetListGroupResponse> {
	@OptionalParam("category")
    private double category;
    
	@OptionalParam("area")
    private double area;
    
    @OptionalParam("order")
    private int order;
    
    @RequiredParam("city")
    private double city;
    
	@OptionalParam("is_no_order")
    private int is_no_order;
    
    @OptionalParam("is_holiday_can_use")
    private int is_holiday_can_use;

    public GetListGroupRequest(double category, double area, int order, double city, int is_no_order, int is_holiday_can_use) {
    	this.category = category ;
    	this.area = area;
    	this.order = order;
    	this.city = city;
    	this.is_no_order = is_no_order;
    	this.is_holiday_can_use = is_holiday_can_use;
    }
}
