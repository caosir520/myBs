package com.wise.common.commonutils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.text.MessageFormat;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;


public class ToastUtil {

	/**
	 * 提示信息 黑色背景 下方显示
	 * @param msg
	 */
	public static void showBottomtoast(Context context, String msg) {
		if(context!=null&&!TextUtils.isEmpty(msg)){
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 提示信息 黑色背景 中间显示
	 * @param msg
	 */
	public static void showCentertoast(Context context, String msg)  {
		if(context!=null&&!TextUtils.isEmpty(msg)){
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	private static void show(final Activity activity, final int resId, final int duration) {
		if(activity == null)
			return;

		final Context context = activity.getApplication();
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast.makeText(context, resId, duration).show();

			}
		});
	}

	private static void show(final Activity activity, final String message, final int duration) {
		if(activity == null)
			return;
		if(TextUtils.isEmpty(message))
			return;

		final Context context = activity.getApplication();
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast.makeText(context, message, duration).show();
			}
		});
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * @param activity
	 * @param resId
	 */
	public static void showLong(final Activity activity, int resId) {
		show(activity, resId, LENGTH_LONG);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * @param activity
	 * @param resId
	 */
	public static void showShort(final Activity activity, final int resId) {
		show(activity, resId, LENGTH_SHORT);
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * @param activity
	 * @param message
	 */
	public static void showLong(final Activity activity, final String message) {
		show(activity, message, LENGTH_LONG);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * @param activity
	 * @param message
	 */
	public static void showShort(final Activity activity, final String message) {
		show(activity, message, LENGTH_SHORT);
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * @param activity
	 * @param message
	 * @param args
	 */
	public static void showLong(final Activity activity, final String message, final Object... args) {
		String formatted = MessageFormat.format(message, args);
		show(activity, formatted, LENGTH_LONG);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * @param activity
	 * @param message
	 * @param args
	 */
	public static void showShort(final Activity activity, final String message, final Object... args) {
		String formatted = MessageFormat.format(message, args);
		show(activity, formatted, LENGTH_SHORT);
	}

	/**
	 * 长展示message{@link Toast}{@link Toast#LENGTH_LONG}
	 * @param activity
	 * @param resId
	 * @param args
	 */
	public static void showLong(final Activity activity, final int resId, final Object... args) {
		if(activity == null)
			return;

		String message = activity.getString(resId);
		showLong(activity, message, args);
	}

	/**
	 * 短展示message{@link Toast}{@link Toast#LENGTH_SHORT}
	 * @param activity
	 * @param resId
	 * @param args
	 */
	public static void showShort(final Activity activity, final int resId, final Object... args) {
		if(activity == null)
			return;

		String message = activity.getString(resId);
		showShort(activity, message, args);
	}
}
