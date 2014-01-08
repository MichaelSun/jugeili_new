package com.tugou.jgl.api;

import com.plugin.internet.core.RequestBase;
import com.plugin.internet.core.annotations.OptionalParam;
import com.plugin.internet.core.annotations.RestMethodUrl;

@RestMethodUrl("http://www.23jugeili.com:90/jgl/index.php?m=api&a=login")
public class LoginRequest extends RequestBase<LoginResponse> {
	@OptionalParam("password")
    private String password;
    
    @OptionalParam("username")
    private String username;

    public LoginRequest(String password, String username) {
    	this.password = password ;
    	this.username = username;
    }
}
