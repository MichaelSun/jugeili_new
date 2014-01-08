package com.tugou.jgl.api;

import java.util.Arrays;

import com.plugin.internet.core.ResponseBase;
import com.plugin.internet.core.json.JsonCreator;
import com.plugin.internet.core.json.JsonProperty;
import com.tugou.jgl.api.GetCategoryListResponse.GroupCategoryInfo;
import com.tugou.jgl.api.GetCategoryListResponse.SubList;

public class GetAreaListResponse extends ResponseBase {

	@Override
	public String toString() {
		return "GetAreaListResponse [groupAreaInfo="
				+ Arrays.toString(groupAreaInfo) + "]";
	}

	public static final class SubList {

		@Override
		public String toString() {
			return "SubList [sub_id=" + sub_id + ", name=" + name + "]";
		}

		public String sub_id;
		public String name;
		
		@JsonCreator
        public SubList(
                @JsonProperty("id") String id,
                @JsonProperty("name") String name){
			this.sub_id  = id;
			this.name  = name;
		}
	}
	
	public static final class GroupAreaInfo {
		@Override
		public String toString() {
			return "GroupAreaInfo [area_id=" + area_id + ", area_name="
					+ area_name + ", sub_area=" + Arrays.toString(sub_area)
					+ "]";
		}

		public String area_id;
		public String area_name;
//		public String is_new;
//		public String is_hot;
		public SubList[] sub_area;
		
		@JsonCreator
        public GroupAreaInfo(
                @JsonProperty("id") String id,
                @JsonProperty("name") String name, 
//                @JsonProperty("is_new") String is_new,
//                @JsonProperty("is_hot") String is_hot,
                @JsonProperty("sub_area") SubList[] sub_area){
			
			this.area_id  = id;
			this.area_name  = name;
//			this.is_new  = is_new;
//			this.is_hot  = is_hot;
			this.sub_area  = sub_area;
		}
	}

	public GroupAreaInfo[] groupAreaInfo ;
	
    @JsonCreator
    public GetAreaListResponse(
            @JsonProperty("result") GroupAreaInfo[] result) {
        this.groupAreaInfo = result;
    }    
}
