package com.zcl.dome;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.app.AlertDialog;

public class Config extends AppCompatActivity
{
	private View Top;
	private ListView listView;
	private LinearLayout container;
	private AlertDialog.Builder builder;
	private EditText et;
	private ListAdapter adapter;
	private View mview;
	
	private void initview(){
		container = (LinearLayout)findViewById(R.id.container);
		Top = findViewById(R.id.ConfigMlTop);
		listView = (ListView) findViewById(R.id.ConfigMlListView);
	}
	
		private void init(){
			Intent intent = getIntent();  
			String str = intent.getStringExtra("Color");
			Top.setBackgroundColor(Color.parseColor(str));
			String[] companies = new String[] {"配置文件","开启脚本","关闭脚本","检测脚本"};
			adapter = new ArrayAdapter<String>(this,R.layout.list,R.id.list1,companies);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener(){		
				@Override		
					public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){
						switch (p3){
							case 0:
								FileDialog(ObUtil.Conf(Config.this,0,"Config",""));
							break;
							case 1:
								FileDialog(ObUtil.Conf(Config.this,0,"Start",""));
							break;
							case 2:
							FileDialog(ObUtil.Conf(Config.this,0,"Stop",""));
							break;
							case 3:
							FileDialog(ObUtil.Conf(Config.this,0,"Check",""));
							break;
					}
	}});
}

private void FileDialog(final String s){
	String res = FileUtil.readFile(s,"UTF-8");
	mview = View.inflate(this, R.layout.edit, null);	
	et = (EditText)mview.findViewById(R.id.EditText);
	et.setText(res);
	builder = new AlertDialog.Builder(this);
	builder.setView(mview);
	builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText et = (EditText) mview.findViewById(R.id.EditText);
				String str = et.getText().toString();
				if (FileUtil.writeFile(s,str,"UTF-8")){
					ObUtil.mToast(container,"保存成功!");
				}else{
					ObUtil.mToast(container,"保存失败!");

				}
			}
		});
	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		}).show();
}

	@Override
    public void onCreate(Bundle savedInstanceState){	
        super.onCreate(savedInstanceState);
		setContentView(R.layout.configml);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		initview();
		init();
	}
}
