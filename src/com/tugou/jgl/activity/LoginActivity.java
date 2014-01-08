package com.tugou.jgl.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.plugin.common.utils.CustomThreadPool;
import com.plugin.common.utils.CustomThreadPool.TaskWrapper;
import com.plugin.internet.InternetUtils;
import com.tugou.jgl.R;
import com.tugou.jgl.api.LoginRequest;
import com.tugou.jgl.api.LoginResponse;
import com.tugou.jgl.utils.Debug;

public class LoginActivity extends BaseActivity {
	private EditText etEmail;
	private EditText etPwd;
	private Button buttonLogin;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.ju_gei_li, menu);
        return true;
    }
    
	private void initUI(){
		//setTitle(this.getString(R.string.app));
		setTitle(this.getString(R.string.app_name));
		buttonLogin = (Button)findViewById(R.id.button_login);
		etEmail = (EditText)findViewById(R.id.username);
		etPwd = (EditText)findViewById(R.id.pwd);
		//checkboxRemember =(CheckBox)findViewById(R.id.auto_login_Checkbox);
		
//		etEmail.setText(SettingManager.getInstance().getUserName());
//		etPwd.setText(SettingManager.getInstance().getPassword());
		
		buttonLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString();
				String pwd = etPwd.getText().toString();
				
		    	if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
		    	    Toast.makeText(LoginActivity.this
		    	            , getString(R.string.email_password_error_tips)
		    	            , Toast.LENGTH_SHORT).show();
		    	    return;
		    	}
				
				//remember username and password
//		    	if(checkboxRemember.isChecked()){
//					SettingManager.getInstance().setUserName(email);
//			    	SettingManager.getInstance().setPassword(pwd);			
//		    	}else{
//		    		SettingManager.getInstance().setUserName("");
//			    	SettingManager.getInstance().setPassword("");
//		    	}
		    	Login(email, pwd);
			}
			
		});
	}
	
	private void Login(final String email, final String pwd) {
        CustomThreadPool.getInstance().excute(new TaskWrapper(new Runnable() {
            @Override
            public void run() {
                try {                	
					final LoginResponse response = InternetUtils.request( 
							LoginActivity.this.getApplicationContext(), new LoginRequest(pwd, email));//versionCode));
                    if (response != null) {
                    	if(Integer.valueOf(response.result) == 0){
                    		Debug.LOGD("login success!!");
                    	}else if(Integer.valueOf(response.result) == 1){
                    		Toast.makeText(LoginActivity.this.getApplicationContext(), response.message,
                                    Toast.LENGTH_LONG).show();
                    	}
                    }
                } catch (Exception e) {
                	runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        	Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.network_error_tips),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }));
    }
}
