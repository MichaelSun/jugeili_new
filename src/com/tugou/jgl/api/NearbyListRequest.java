package com.tugou.jgl.api;

import com.plugin.internet.core.RequestBase;
import com.plugin.internet.core.annotations.OptionalParam;
import com.plugin.internet.core.annotations.RequiredParam;
import com.plugin.internet.core.annotations.RestMethodUrl;

@RestMethodUrl("http://www.23jugeili.com:90/jgl/index.php?m=api&a=nearbyList")
public class NearbyListRequest extends RequestBase<NearbyListResponse> {
	@RequiredParam("lat")
    private double lat;
    
	@RequiredParam("lon")
    private double lon;
    
	@RequiredParam("dest")
    private float dest;
    
    @RequiredParam("category")
    private int category;
    
    @OptionalParam("order")
    private int order;
    
    @RequiredParam("city")
    private int city;
    
    @OptionalParam("is_no_order")
    private int is_no_order;
    
    @OptionalParam("is_holiday_can_use")
    private int is_holiday_can_use;

    public NearbyListRequest(double lat, double lon, float dest, int category, int order, int city, int is_no_order, int is_holiday_can_use) {
    	this.lat = lat ;
    	this.lon = lon;
    	this.dest = dest;
    	this.category = category;
    	this.order = order;
    	this.city = city;
    	this.is_no_order = is_no_order;
    	this.is_holiday_can_use = is_holiday_can_use;
    }
}
