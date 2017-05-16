package com.zcl.dome;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import java.lang.Process;

public class Setting extends AppCompatActivity
{
	private final String Path = "/data/data/com.zcl.dome/files/";
	//private final String UpdateLogName = "UpdateLog.txt";
	private TextView Ab;
	private View sv ;
	private ListView listView;
	private LinearLayout container;
	private int Aboutindex = 1;
	private AlertDialog.Builder builder;
	private Process	p;
	private ListAdapter adapter;
	
	private void initview(){
		container = (LinearLayout) findViewById(R.id.container);
		listView = (ListView) findViewById(R.id.mainListView1);
		Ab = (TextView)findViewById(R.id.settingTextView);
		sv = findViewById(R.id.settingview);
	}

	/*private void UpdateLog(){ 
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		try{ 
			InputStream in = getAssets().open(UpdateLogName); 
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close(); 
			String txt = new String (buffer); 
			dialog.setMessage(txt);
			dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
			}
		}).create().show();
	}catch(Exception e ){
		dialog.setMessage("加载失败!").create().show();
		}
	}*/
	
	private void TinyCore(){
		builder = new AlertDialog.Builder(this);
		builder.setMessage("测试");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			}).show();
	}
	/*
	安装文件
	*/
	private void install(){
		ObUtil.mToast(container,"正在安装...");
		ObUtil.copyAssetsFile(this,"File.zip");
		String FileDir = getFilesDir().toString();
		File f = new File(FileDir+"/File.zip");
		try
		{
			ObUtil.upZipFileTwo(f,FileDir);
			p = Runtime.getRuntime().exec(new String[]{"chmod","-R","777",FileDir});
			ObUtil.mToast(container,"安装成功");
			}catch (IOException e){
			ObUtil.mToast(container,"安装失败!或者已经存在文件");
		}
	}
	private boolean delete(){
		/*File f = new File(Path);
		return f.delete();*/
		try
		{
			p= Runtime.getRuntime().exec(new String[]{"rm","-rf",Path});
			return true;
		}
		catch (IOException e){
			return false;
		}
	}
	
	private void About(){
		switch (Aboutindex){
			case 0:
				Ab.setVisibility(View.GONE);
				Aboutindex ++;
				break;
			case 1:
				Ab.setVisibility(View.VISIBLE);
				Aboutindex --;
				break;
		}
		/*Ab.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					UpdateLog();
					// TODO: Implement this method
				}
		});*/
		Ab.setOnLongClickListener(new View.OnLongClickListener() {  
				@Override  
				public boolean onLongClick(View v) { 
					ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					cm.setText("1948226838");
					ObUtil.mToast(container,"已复制到粘贴板");
					return true;  
				}  
			});  
	}
	
	private void init(){
		Intent intent = getIntent();  
        String str = intent.getStringExtra("Color");
		sv.setBackgroundColor(Color.parseColor(str));
		Ab.setVisibility(View.GONE);
		String[] companies = new String[] {"安装文件","删除文件","更换核心","自定义","关于"};
		adapter = new ArrayAdapter<String>(this,R.layout.list,R.id.list1,companies);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){		
				@Override		
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{		
					switch (p3)
					{
						case 0:
							install();
							break;
						case 1:
							if (delete() == true){
								ObUtil.mToast(container,"删除成功!");
							}else if(delete() == false){
								ObUtil.mToast(container,"删除失败!");
							}
							break;
						case 2:
							TinyCore();
							break;
						case 3:
							Intent intent = new Intent();
							intent.setClass(Setting.this, Custom.class);
							startActivity(intent);
							break;
						case 4:
							About();
							break;
					}
				}
			});
	}
	
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		initview();
		init();
	}
}
