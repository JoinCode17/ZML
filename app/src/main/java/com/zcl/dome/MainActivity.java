package com.zcl.dome;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.widget.*;
import android.widget.SimpleAdapter.*;
import java.io.*;
import java.util.*;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity
{	
	/**
	  *Create 2017.3.14 by Tokyonth
	  */
	private final String initColor = "#FF448866";
	private final String sdPath = "/sdcard/ZTiny/";
	private final String ConfPath = "/data/data/com.zcl.dome/shared_prefs/Configuration.xml";
	private ProgressDialog progressDialog;
	private TextView local;
	private TextView tiny;
	private TextView tv;
	private View setColor;
	private View btn1;
	private View btn2;
	private View btn3;
	private View btn4;
	private View btn5;
	private View btn6;
	private View btn7;
	private View btn8;
	private View btn9;
	private View mview;
	private View vc;
	private EditText edit;
	private LinearLayout container;
	private WebView webView;
	private AlertDialog.Builder builder;

	private void initview(){
		local = (TextView) findViewById(R.id.mainTextViewLocal);
		tiny = (TextView) findViewById(R.id.mainTextViewTiny);
		tv = (TextView) findViewById(R.id.mainTextViewzt);
		setColor = findViewById(R.id.mainbj);
		btn1 = findViewById(R.id.start);
		btn2 = findViewById(R.id.stop);
		btn3 = findViewById(R.id.check);
		btn4 = findViewById(R.id.tinycc);
		btn5 = findViewById(R.id.ipt);
		btn6 = findViewById(R.id.ms);
		btn7 = findViewById(R.id.setting);
		btn8 = findViewById(R.id.setColor);
		btn9 = findViewById(R.id.mainfab);
		container = (LinearLayout) findViewById(R.id.container);
	}
	
	private void State() {
		tiny.setText("流量 : " + ObUtil.gets() + "\n内网 : " + ObUtil.getIp());
		//String str1 = ProcessModel.execute(new String[]{"pgrep","Tiny"});
		//String str2 = ProcessModel.execute(new String[]{"pgrep","tiny"});
		String str1 = "tiny";
		String str2 = ;
			if (str1 == "" && str2 == ""){
				tv.setText("未运行 ...");
				tv.setTextColor(Color.parseColor("#FF3300"));
			}else{
				tv.setText("运行中 ...");
				tv.setTextColor(Color.parseColor("#33CC33"));
			}
		if (ObUtil.isVpn() == true){
			local.setText("VPN模式!");
		}else{
			local.setText(ObUtil.readApn(MainActivity.this,"APN")/* + "\n" + readApn("IP")*/);
		 }
	}
	
	private void getUID() {
					List installedPackages = MainActivity.this.getPackageManager().getInstalledPackages(0);
					List arrayList = new ArrayList();
					for (int i = 0;i < installedPackages.size();i++) {
						if ((((PackageInfo) installedPackages.get(i)).applicationInfo.flags & 1) <= 0) {
							Map hashMap = new HashMap();
							hashMap.put("name", ((PackageInfo) installedPackages.get(i)).applicationInfo.loadLabel(getPackageManager()).toString());
							hashMap.put("icon", ((PackageInfo) installedPackages.get(i)).applicationInfo.loadIcon(getPackageManager()));
							hashMap.put("uid", String.valueOf(((PackageInfo) installedPackages.get(i)).applicationInfo.uid));
							arrayList.add(hashMap);
						}
					}
					View inflate = getLayoutInflater().inflate(R.layout.app_layout, null, false);
					ListView listView = (ListView) inflate.findViewById(R.id.app_list);
					SimpleAdapter simpleAdapter = new SimpleAdapter(inflate.getContext(),arrayList, R.layout.app_info, new String[]{"icon", "name", "uid"}, new int[]{R.id.app_icon, R.id.app_name, R.id.app_uid});
					simpleAdapter.setViewBinder(new ViewBinder() {
							public boolean setViewValue(View view, Object obj, String str) {
								if (!(view instanceof ImageView) || !(obj instanceof Drawable)) {
									return false;
								}
								((ImageView) view).setImageDrawable((Drawable) obj);
								return true;
							}
						});
					listView.setAdapter(simpleAdapter);
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setView(inflate).show();
  			  }
	
	private void View(){
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					while (true){
						try
						{
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
							Thread.sleep(1000);
						}catch (InterruptedException e){}
					}
				}
			}).start();
	}
	
	private void RunShellShow(final String[] str){
		SharedPreferences sharedPreferences = getSharedPreferences("Configuration",Activity.MODE_PRIVATE);
		int Int = sharedPreferences.getInt("CustomSwitch",0); 
		File tfe = new File("/data/data/com.zcl.dome/files");
		if (tfe.exists() == true || Int != 0){
		progressDialog = ProgressDialog.show(MainActivity.this,null, "执行中，请稍候……");
		new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = 2;
					Bundle bundle = new Bundle();
					bundle.putString("num",ProcessModel.execute(str));
					msg.setData(bundle);
					handler.sendMessage(msg);
					handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
				}
			}).start();	
		}else{
			ObUtil.mToast(container,"请先在设置中安装文件或者自定义!");
		}
	}
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						State();
						break;
					case 2:
				String str = msg.getData().getString("num") + "";
				if (ObUtil.isRoot() == false){
					ObUtil.mToast(container,"未获得ROOT!");
				}else{
					builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle("执行结果:");
					builder.setMessage(str);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					progressDialog.dismiss();
				}
				}).show();
			}
			break;
			}
		};
	};

	private void Tinycc()
	{
		webView = new WebView(this);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(false);
		webView.loadUrl("http://tiny.cc");
		builder = new AlertDialog.Builder(this);
		builder.setView(webView).show();
	}

	private void SetColor()
	{
		builder = new AlertDialog.Builder(this);
		mview = View.inflate(MainActivity.this, R.layout.coloredit, null);	
		edit = (EditText) mview.findViewById(R.id.editEditText1);
		vc = mview.findViewById(R.id.editbj);
		edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
		edit.setHint("当前为:" + ObUtil.Conf(this,0,"Color",""));
		edit.setHintTextColor(Color.parseColor(ObUtil.Conf(this,0,"Color","")));
		vc.setBackgroundColor(Color.parseColor(ObUtil.Conf(this,0,"Color","")));
		builder.setView(mview);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String mColor = edit.getText().toString();
				if (mColor.length() < 9 ){
				ObUtil.mToast(container,"输入有误!");
				}else{
				if (mColor.equals("#ffffffff") | mColor.equals("#FFFFFFFF")){
					ObUtil.mToast(container,"不建议设置为纯白色!");
				}else{
					setColor.setBackgroundColor(Color.parseColor(mColor));
					ObUtil.Conf(MainActivity.this,1,"Color",mColor);
				}
				}
			}
		}).create().show();
		}
		
		private void FabMenu(){
			String items[] = {"查看UID","切换APN","内网控制","测试网络"};
			builder = new AlertDialog.Builder(this);
			builder.setItems(items,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which){
							case 0:
								getUID();
								break;
							case 1:
								Intent intent = new Intent(android.provider.Settings.ACTION_APN_SETTINGS);
								MainActivity.this.startActivity(intent);
								break;
							case 2:
								
								break;
							case 3:
										
								break;
						}
					}
				});
			builder.create().show();
		}
		
		private void SElinux(){
			String s = ProcessModel.execute("getenforce");
			if (Build.VERSION.SDK_INT >= 23 && s.equals("Enforcing\n")){
			builder = new AlertDialog.Builder(MainActivity.this);
			builder.setCancelable(false);
			builder.setMessage("检测到系统 => Android 6.0\n并且SELinux状态为Enforcing\n建议关闭SELinux\n是否关闭?");
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
				builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							ProcessModel.execute(new String[]{"su","-c","setenforce 0"});
						}
					});
			builder.show();
			}
		}
	/**
	主界面按钮点击事件
	*/
	private void Click(){
		final Intent intent = new Intent();
		btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RunShellShow(new String[]{"su","-c",ObUtil.Conf(MainActivity.this,0,"Start","")});
				}});
		btn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RunShellShow(new String[]{"su","-c",ObUtil.Conf(MainActivity.this,0,"Stop","")});
				}});
		btn3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RunShellShow(new String[]{"su","-c",ObUtil.Conf(MainActivity.this,0,"Check","")});
				}});
		btn4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Tinycc();
				}});
		btn5.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					intent.setClass(MainActivity.this, ConfigML.class);
					intent.putExtra("Color",ObUtil.Conf(MainActivity.this,0,"Color",""));  
					startActivity(intent);	
				}});
		btn6.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					intent.setClass(MainActivity.this, Pattern.class);
					intent.putExtra("Color",ObUtil.Conf(MainActivity.this,0,"Color",""));  
					startActivity(intent);
				}});
		btn7.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					intent.setClass(MainActivity.this, Setting.class);
					intent.putExtra("Color",ObUtil.Conf(MainActivity.this,0,"Color",""));  
					startActivity(intent);
				}});
		btn8.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					SetColor();
					}
				});
		btn9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				FabMenu();
				}
		});
	}
	
	private void init(){
		final SharedPreferences sharedPreferences = getSharedPreferences("Configuration",Activity.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPreferences.edit(); 
		File cflie = new File(ConfPath);
		if (cflie.exists() == false){
			editor.putInt("CustomSwitch",0); 
			editor.commit(); 
			ObUtil.Conf(this,1,"Color",initColor);
			ObUtil.Conf(this,1,"Path","/data/data/com.zcl.dome/files");		
		}
		new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						int Int = sharedPreferences.getInt("CustomSwitch",0);
						if (Int == 0){
							ObUtil.Conf(MainActivity.this,1,"Start","/data/data/com.zcl.dome/files/Start.sh");
							ObUtil.Conf(MainActivity.this,1,"Stop","/data/data/com.zcl.dome/files/Stop.sh");
							ObUtil.Conf(MainActivity.this,1,"Check","/data/data/com.zcl.dome/files/State.sh");
							ObUtil.Conf(MainActivity.this,1,"Config","/data/data/com.zcl.dome/files/Start.sh");
						}else if (Int == 1){
							ObUtil.Conf(MainActivity.this,1,"Start",ObUtil.Conf(MainActivity.this,0,"StartCustomPath",""));
							ObUtil.Conf(MainActivity.this,1,"Stop",ObUtil.Conf(MainActivity.this,0,"StopCustomPath",""));
							ObUtil.Conf(MainActivity.this,1,"Check",ObUtil.Conf(MainActivity.this,0,"CheckCustomPath",""));
							ObUtil.Conf(MainActivity.this,1,"Config",ObUtil.Conf(MainActivity.this,0,"ConfigCustomPath",""));
							}
							}}}).start();	
		String rColor = ObUtil.Conf(this,0,"Color","");
		setColor.setBackgroundColor(Color.parseColor(rColor));
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState){	
        super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		initview();
		init();
		View();
		Click();
		SElinux();
	}
}
