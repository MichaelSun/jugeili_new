package com.tugou.jgl.api;

import com.plugin.internet.core.RequestBase;
import com.plugin.internet.core.annotations.OptionalParam;
import com.plugin.internet.core.annotations.RequiredParam;
import com.plugin.internet.core.annotations.RestMethodUrl;

@RestMethodUrl("http://www.23jugeili.com:90/jgl/index.php?m=api&a=categoryList")
public class GetCategoryListRequest extends RequestBase<GetCategoryListResponse> {
    
	@OptionalParam("cate_id")
    private int cate_id;
    
    @OptionalParam("city")
    private int city;

    public GetCategoryListRequest(int cate_id, int city) {
    	this.cate_id = cate_id ;
    	this.city = city;
    }
}
