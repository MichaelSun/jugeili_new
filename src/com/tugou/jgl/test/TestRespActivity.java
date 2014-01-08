package com.tugou.jgl.test;

import android.os.Bundle;
import android.widget.TextView;

import com.tugou.jgl.R;
import com.tugou.jgl.activity.BaseActivity;

public class TestRespActivity extends BaseActivity {
	
	private String response;
	private TextView textViewResp;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_response);
        
        textViewResp = (TextView)findViewById(R.id.response);
        
        Bundle bundle = this.getIntent().getExtras();
        response = bundle.getString("response");
        
        textViewResp.setText(response);
	}
}
