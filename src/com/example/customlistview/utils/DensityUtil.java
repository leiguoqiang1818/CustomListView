package com.example.customlistview.utils;

import android.content.Context;

/**
 * 适配时候使用，在java代码中动态的去调用
 * @author wsd_leiguoqiang
 */
public class DensityUtil {
	/**
	 * dp转换成px
	 * @return
	 */
	public static float dp2px(Context context,int dpValues){
		float scale = context.getResources().getDisplayMetrics().density;
		return dpValues*scale+0.5f;
	}
	/**
	 * px转换成dp
	 * @param context
	 * @param pxValues
	 * @return
	 */
	public static float px2dp(Context context,float pxValues){
		float scale = context.getResources().getDisplayMetrics().density;
		return pxValues/scale+0.5f;
	}
}
