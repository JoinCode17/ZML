package com.zcl.dome;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import android.view.View.OnClickListener;

public class Custom extends AppCompatActivity
{
	private View view;
	private EditText et;
	private EditText et1;
	private EditText et2;
	private EditText et3;
	private EditText et4;
	private Switch sw;
	private Button bt;
	private LinearLayout container;
	
	private void initView(){
		et = (EditText) findViewById(R.id.customEditTextStart);
		et1 = (EditText) findViewById(R.id.customEditTextStop);
		et2 = (EditText) findViewById(R.id.customEditTextCheck);
		et3 = (EditText) findViewById(R.id.custommsedit);
		et4 = (EditText) findViewById(R.id.customconfedit);
		sw = (Switch) findViewById(R.id.customSwitch);
		bt = (Button) findViewById(R.id.customButton);
		view = findViewById(R.id.customTop);
		container = (LinearLayout) findViewById(R.id.container);
	}
	
	private void init(){
		SharedPreferences SharedPreferences= getSharedPreferences("Configuration",Activity.MODE_PRIVATE); 
		final SharedPreferences.Editor editor = SharedPreferences.edit(); 
		final int Int = SharedPreferences.getInt("CustomSwitch",0);
		if (Int == 1){
			et.setEnabled(false);
			et1.setEnabled(false);
			et2.setEnabled(false);
			et3.setEnabled(false);
			et4.setEnabled(false);
			bt.setEnabled(false);
			sw.setChecked(true);
		}else if(Int == 0){
			et.setEnabled(true);
			et1.setEnabled(true);
			et2.setEnabled(true);
			et3.setEnabled(true);
			et4.setEnabled(true);
			bt.setEnabled(true);
			sw.setChecked(false);
		}
		bt.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					String s = et.getText().toString();
					String s1 = et1.getText().toString();
					String s2 = et2.getText().toString();
					String s3 = et3.getText().toString();
					String s4 = et4.getText().toString();
					editor.putString("StartCustomPath",s); 
					editor.putString("StopCustomPath",s1); 
					editor.putString("CheckCustomPath",s2); 
					editor.putString("MsCustomPath",s3); 
					editor.putString("ConfigCustomPath",s4); 
					editor.commit(); 
					ObUtil.mToast(container,"设置路径成功!");
				}
			});
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
				@Override  
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					if (isChecked == true) {
						et.setEnabled(false);
						et1.setEnabled(false);
						et2.setEnabled(false);
						et3.setEnabled(false);
						et4.setEnabled(false);
						bt.setEnabled(false);
						editor.putInt("CustomSwitch",1); 
						editor.commit(); 
					} else if (isChecked == false){  
						et.setEnabled(true);
						et1.setEnabled(true);
						et2.setEnabled(true);
						et3.setEnabled(true);
						et4.setEnabled(true);
						bt.setEnabled(true);
						editor.putInt("CustomSwitch",0); 
						editor.commit(); 
					}  
				}  
			});  
	}
	
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		setContentView(R.layout.custom);
		initView();
		SharedPreferences sharedPreferences = getSharedPreferences("Configuration",Activity.MODE_PRIVATE);
		String color = sharedPreferences.getString("Color","");
		view.setBackgroundColor(Color.parseColor(color));
		String str = sharedPreferences.getString("StartCustomPath","");
		String str1 = sharedPreferences.getString("StopCustomPath","");
		String str2 = sharedPreferences.getString("CheckCustomPath","");
		String str3 = sharedPreferences.getString("MsCustomPath","");
		String str4 = sharedPreferences.getString("ConfigCustomPath","");
		et.setText(str);
		et1.setText(str1);
		et2.setText(str2);
		et3.setText(str3);
		et4.setText(str4);
		init();
	}
}
