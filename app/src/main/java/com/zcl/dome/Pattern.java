package com.zcl.dome;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.app.AlertDialog;

public class Pattern extends AppCompatActivity 
{
	private View mview;
	private Button bt;
	private View top;
	private EditText et;
	private CoordinatorLayout container;
	private AlertDialog.Builder builder;
	
	private void initview(){
		bt = (Button)findViewById(R.id.mainpattern);
		top = findViewById(R.id.patterntop);
		container = (CoordinatorLayout) findViewById(R.id.container);
	}

	private void inFile(final String filepath){
		mview = View.inflate(this, R.layout.edit,null);	
		et = (EditText)mview.findViewById(R.id.EditText);
		try{
			String res = FileUtil.readFile(filepath,"UTF-8");
			et.setText(res);
			builder = new AlertDialog.Builder(this);
			builder.setView(mview);
			builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						et = (EditText) mview.findViewById(R.id.EditText);
						String str = et.getText().toString();
						FileUtil.writeFile(filepath,str,"UTF-8");
						ObUtil.mToast(container,"保存成功！");
					}
				});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				}).show();
		}catch(Exception e){
			ObUtil.mToast(container,"文件读取失败！");
		}
	}
	
	private String path(){
		SharedPreferences sharedPreferences = getSharedPreferences("Configuration",Activity.MODE_PRIVATE);
		//SharedPreferences.Editor editor = sharedPreferences.edit(); 
		int Int = sharedPreferences.getInt("CustomSwitch",0);
		String s = sharedPreferences.getString("MsCustomPath","");
		if (Int == 0){
			return "/data/data/com.zcl.dome/files/Tiny.conf";
		}else if (Int == 1){
			return s;
		}
		return null;
	}
	
	private void init(){
		Intent intent = getIntent();  
        String str = intent.getStringExtra("Color");
		top.setBackgroundColor(Color.parseColor(str));
		bt.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					inFile(path());
					//Toast.makeText(getApplicationContext(),path(),0).show();
				}
		});
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		setContentView(R.layout.pattern);
		initview();
		init();
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					init();
				}
			}
		}).start();*/
	}
}
