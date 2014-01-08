package com.tugou.jgl.api;

import com.plugin.internet.core.RequestBase;
import com.plugin.internet.core.annotations.OptionalParam;
import com.plugin.internet.core.annotations.RestMethodUrl;

@RestMethodUrl("http://www.23jugeili.com:90/jgl/index.php?m=api&a=areaList")
public class GetAreaListRequest extends RequestBase<GetAreaListResponse> {
    
	@OptionalParam("area")
    private int area;
    
    @OptionalParam("city")
    private int city;

    public GetAreaListRequest(int area, int city) {
    	this.area = area;
    	this.city = city;
    }
}
