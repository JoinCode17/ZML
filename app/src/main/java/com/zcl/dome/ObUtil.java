package com.zcl.dome;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

public class ObUtil
{
	public static String readApn(Context context,String s) {
        String extraInfo;
        try {
            extraInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getExtraInfo() + "模式!";
        } catch (Exception e) { extraInfo = "没有网络"; }
		if (s == "IP"){
			return System.getProperty("http.proxyHost", "0") + ":" + System.getProperty("http.proxyPort", "0");
		}else if (s == "APN"){
			String str = extraInfo.toUpperCase();
			return str;
		}
		return null;
	}
	
	public static String getIp() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				Enumeration<InetAddress> enIp = ni.getInetAddresses();
				while (enIp.hasMoreElements()) {
					InetAddress inet = enIp.nextElement();
					if (!inet.isLoopbackAddress()&& (inet instanceof Inet4Address)) {
						return inet.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	public static boolean isVpn() {
		try {
			Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
			if(niList != null) {
				for (NetworkInterface intf : Collections.list(niList)) {
					if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
						continue;
					}
					if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
						return true;
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void mToast(View view,String str){
		Snackbar.make(view,str, Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Snackbar.make(container,"ActionClick",Snackbar.LENGTH_LONG).show();
				}
			}).show();
	}
	
	
	public static String Conf(Context context,int index,String s,String c){
		if (index == 1){
			SharedPreferences mySharedPreferences= context.getSharedPreferences("Configuration",Activity.MODE_PRIVATE); 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			editor.putString(s,c); 
			editor.commit(); 
		} else if (index == 0){
			SharedPreferences sharedPreferences = context.getSharedPreferences("Configuration",Activity.MODE_PRIVATE);
			String str = sharedPreferences.getString(s,""); 
			return str;
		}
		return "error";
	}
	
	public static void upZipFileTwo(File zipFile, String folderPath)throws ZipException,IOException {
		ZipFile zfile=new ZipFile(zipFile);
		Enumeration zList=zfile.entries();
		ZipEntry ze=null;
		byte[] buf=new byte[1024];
		while(zList.hasMoreElements()){
			ze=(ZipEntry)zList.nextElement();    
			if(ze.isDirectory()){
				 Log.d("upZipFile", "ze.getName() = "+ze.getName());
				 String dirstr = folderPath +"/"+ ze.getName();
				 dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				 Log.d("upZipFile", "str = "+dirstr);
				 File f=new File(dirstr);
			     f.mkdir();
				 continue;
	}
		Log.d("upZipFile", "ze.getName() = "+ze.getName());
		OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
		InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
		int readLen=0;
			while ((readLen=is.read(buf, 0, 1024))!=-1) {
			os.write(buf, 0, readLen);
			}
			is.close();
			os.close();    
		}
		    zfile.close();
		    Log.d("upZipFile", "finish");
	}
	
	public static File getRealFileName(String baseDir, String absFileName){
		String[] dirs=absFileName.split("/"); 
		String lastDir=baseDir;
		Log.d("Path",""+baseDir);
		if(dirs.length>1)
		{ 
			for (int i = 0; i < dirs.length-1;i++) 
			{ 
				lastDir += ("/"+dirs[i]);
				File dir = new File(lastDir); 
				if(!dir.exists())
				{
					dir.mkdirs();
					Log.d("getRealFileName", "create dir = "+(lastDir+"/"+dirs[i]));
				}
			} 
			File ret = new File(lastDir,dirs[dirs.length-1]);
			Log.d("upZipFile", "2ret = "+ret);
			Log.d("upZipFile","LastDir="+lastDir);
			return ret;
		}
		else 
		{
			return new File(baseDir,absFileName);
		}
	}
	
	public static String gets(){  
		long l = TrafficStats.getUidRxBytes(3004)==TrafficStats.UNSUPPORTED?0:(TrafficStats.getMobileRxBytes()); 
		if ( l == -1){
			return "不支持统计";
		}else{
		double l3 = l;
		double l4 = l3/(1024*1024);
		BigDecimal b = new BigDecimal(l4);  
		double index = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		String s = Double.toString(index);
		if (index >= 1000){
			double g = index/1024;
			BigDecimal b2 = new BigDecimal(g); 
			double gs = b2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			String ss = Double.toString(gs);
			return ss + "GB";
		}else{
			return s + "MB";
			}
		}
		//return "不支持统计!";
	}
	
	public static void copyAssetsFile(Context context,String filename) {
		AssetManager assetManager = context.getAssets();
		File FileDir = context.getFilesDir();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = FileDir + "/" + filename;
			out = new FileOutputStream(newFileName);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			//Log.e("tag", e.getMessage());
		}
	}
	
	public static void download(String url,Context context){
		String myHTTPUrl = "https://raw.githubusercontent.com/Tokyonth/File/master/ML.zip";
		DownloadManager.Request request=new DownloadManager.Request(Uri.parse(myHTTPUrl));
		//WIFI下载
		//request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
		String nameOfFile = URLUtil.guessFileName(myHTTPUrl,null,MimeTypeMap.getFileExtensionFromUrl(myHTTPUrl));
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
		DownloadManager manager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}
}
