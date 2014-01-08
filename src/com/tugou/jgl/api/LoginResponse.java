package com.tugou.jgl.api;

import com.plugin.internet.core.ResponseBase;
import com.plugin.internet.core.json.JsonCreator;
import com.plugin.internet.core.json.JsonProperty;

public class LoginResponse extends ResponseBase {
	public String user_id;
	public String result;
	public String message;
	
    @JsonCreator
    public LoginResponse(
            @JsonProperty("user_id") String user_id,
            @JsonProperty("result") String result,
            @JsonProperty("message") String message) {
        this.user_id = user_id;
        this.result = result;
        this.message = message;
    }
}
