package com.njk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Parcelable;
import android.view.WindowManager.LayoutParams;

import com.njk.R;
import com.njk.WelcomActivity;
import com.njk.view.LoadingDialog;


public class DialogUtil {

	private static LoadingDialog loadingDialog;

	public static void progressDialogShow(Activity context, String message){
		progressDialogDismiss();
		loadingDialog = new LoadingDialog(context,R.style.LoadingDialog);
		loadingDialog.setLoadingText(message);
		loadingDialog.show();
	}

	public static void progressDialogDismiss(){
		if(loadingDialog!=null){
			loadingDialog.dismiss();
			loadingDialog= null;
		}
	}

	public static boolean isShow(Activity context){
		if(loadingDialog==null){
			return false;
		}
		return loadingDialog.isShowing();
	}

	/**
     * 全局性进度对话框
     */
	private static ProgressDialog progressDialog;
	public static void progressDialogShow2(Activity context, String message){
		progressDialogDismiss2();
		
		LayoutParams layoutParams = new LayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.getWindow().setAttributes(layoutParams);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
//		progressDialog.setTitle("");
		progressDialog.setMessage(message);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminateDrawable(context.getResources().getDrawable( R.anim.loading_anim2));

//		progressDialog = CustomProgressDialog.createDialog(context);  
		progressDialog.setMessage(message); 
		progressDialog.show();
	}
	
	public static void progressDialogDismiss2(){
		try {progressDialog.dismiss();} catch (Exception e) {};
	}
	
	/**
     * 网络异常对话框
     */
//	public static void checkNetworkDialog(final Activity activity){
//		AlertDialog checkNetworkDialog = new AlertDialog.Builder(activity)
//		.setTitle("")//R.string.network_ex
//		.setMessage("")//R.string.network_ex_des
//		.setPositiveButton("", new DialogInterface.OnClickListener() {//R.string.network_ex_confirm
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				activity.finish();
//			}
//		})
//		.create();
//	checkNetworkDialog.setCancelable(false);
//	checkNetworkDialog.setCanceledOnTouchOutside(false);
//	checkNetworkDialog.show();
//	}
	
	/**
     * 程序退出确认对话框
     */
	public static void appExitDialog(final Activity activity){
		AlertDialog appExitDialog = new AlertDialog.Builder(activity)
			.setTitle(R.string.toast)
			.setMessage(R.string.app_exit)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					activity.stopService(new Intent(activity, LocationService.class));
					activity.finish();
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.create();
		appExitDialog.setCancelable(true);
		appExitDialog.setCanceledOnTouchOutside(true);
		appExitDialog.show();
	}
	
	/**
     * 程序退出确认对话框
     */
	public static void addShortCutDialog(final Activity activity){
		AlertDialog addShortCutDialog = new AlertDialog.Builder(activity)
			.setTitle(R.string.toast)
			.setMessage(R.string.app_exit)
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					addShortCut(activity);
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.create();
		addShortCutDialog.setCancelable(true);
		addShortCutDialog.setCanceledOnTouchOutside(true);
		addShortCutDialog.show();
	}
	


	public static void addShortCut(Activity acitvity) {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				acitvity.getResources().getString(R.string.app_name));
		ShortcutIconResource iconRes = ShortcutIconResource.fromContext(
				acitvity.getApplicationContext(), R.mipmap.app_icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, iconRes);

		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);

		// 设置桌面快捷方式的图标
		Parcelable icon = ShortcutIconResource.fromContext(acitvity,
				R.mipmap.app_icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(acitvity, WelcomActivity.class);

		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 广播通知桌面去创建
		acitvity.sendBroadcast(shortcut);
	}
}
