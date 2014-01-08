package com.tugou.jgl.api;

import java.util.Arrays;

import com.plugin.internet.core.ResponseBase;
import com.plugin.internet.core.json.JsonCreator;
import com.plugin.internet.core.json.JsonProperty;

public class NearbyListResponse extends ResponseBase {
	@Override
	public String toString() {
		return "NearbyListResponse [groupInfo=" + Arrays.toString(groupInfo)
				+ "]";
	}

	public static final class GroupInfo {
		@Override
		public String toString() {
			return "GroupInfo [id=" + id + ", cover=" + cover + ", name="
					+ name + ", description=" + description + ", price="
					+ price + ", regular_price=" + regular_price
					+ ", num_bought=" + num_bought + ", distance=" + distance
					+ ", is_new=" + is_new + ", is_no_order=" + is_no_order
					+ ", is_holiday_can_use=" + is_holiday_can_use + "]";
		}

		public String id;
		public String cover;
		public String name;
		public String description;
		public String price;
		public String regular_price;
		public String num_bought;
		public String distance;
		public String is_new;
		public String is_no_order;
		public String is_holiday_can_use;
		
		@JsonCreator
        public GroupInfo(
        		@JsonProperty("id") String id,
                @JsonProperty("cover") String cover,
                @JsonProperty("name") String name, 
                @JsonProperty("description") String description,
                @JsonProperty("price") String price,
                @JsonProperty("regular_price") String regular_price,
                @JsonProperty("num_bought") String num_bought,
                @JsonProperty("num_bought") String distance,
                @JsonProperty("num_bought") String is_new,
                @JsonProperty("num_bought") String is_no_order,
                @JsonProperty("num_bought") String is_holiday_can_use){
			this.id = id;
			this.cover  = cover;
			this.name  = name;
			this.description  = description;
			this.price  = price;
			this.regular_price  = regular_price;
			this.num_bought  = num_bought; 
			this.distance  = distance; 			
			this.is_new = is_new;
			this.is_no_order = is_no_order; 
			this.is_holiday_can_use = is_holiday_can_use; 
		}
	}

	public GroupInfo[] groupInfo ;
	
    @JsonCreator
    public NearbyListResponse(
            @JsonProperty("result") GroupInfo[] result) {
        this.groupInfo = result;
    }
}
