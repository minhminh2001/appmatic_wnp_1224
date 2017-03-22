package com.whitelabel.app.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;

import java.util.regex.Pattern;

public class ColorUtils {
	private static final int ENABLE_ATTR = android.R.attr.state_enabled;
	private static final int CHECKED_ATTR = android.R.attr.state_checked;
	private static final int PRESSED_ATTR = android.R.attr.state_pressed;

	public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
		int[][] states = new int[][]{
				{-ENABLE_ATTR, CHECKED_ATTR},
				{-ENABLE_ATTR},
				{PRESSED_ATTR, -CHECKED_ATTR},
				{PRESSED_ATTR, CHECKED_ATTR},
				{CHECKED_ATTR},
				{-CHECKED_ATTR}
		};

		int[] colors = new int[] {
				tintColor - 0xAA000000,
				0xFFBABABA,
				tintColor - 0x99000000,
				tintColor - 0x99000000,
				tintColor | 0xFF000000,
				0xFFEEEEEE
		};
		return new ColorStateList(states, colors);
	}

	public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
		int[][] states = new int[][]{
				{-ENABLE_ATTR, CHECKED_ATTR},
				{-ENABLE_ATTR},
				{CHECKED_ATTR, PRESSED_ATTR},
				{-CHECKED_ATTR, PRESSED_ATTR},
				{CHECKED_ATTR},
				{-CHECKED_ATTR}
		};

		int[] colors = new int[] {
				tintColor - 0xE1000000,
				0x10000000,
				tintColor - 0xD0000000,
				0x20000000,
				tintColor - 0xD0000000,
				0x20000000
		};
		return new ColorStateList(states, colors);
	}

	/**
	 * 将十六进制 颜色代码 转换为 int
	 *
	 * @return
	 */
	public static int HextoColor(String color) {

		// #ff00CCFF
		String reg = "#[a-f0-9A-F]{6}";
		if (!Pattern.matches(reg, color)) {
			color = "#00ffffff";
		}

		return Color.parseColor(color);
	}

	/**
	 * 修改颜色透明度
	 * @param color
	 * @param alpha
	 * @return
	 */
	public static int changeAlpha(int color, int alpha) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);

		return Color.argb(alpha, red, green, blue);
	}

	/**
	 * 十进制颜色值转为十六进制
	 * @param color
	 * @return
     */
	public static String toHexEncoding(int color) {
		String R, G, B;
		StringBuffer sb = new StringBuffer();
		R = Integer.toHexString(Color.red(color));
		G = Integer.toHexString(Color.green(color));
		B = Integer.toHexString(Color.blue(color));
		//判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
		R = R.length() == 1 ? "0" + R : R;
		G = G.length() == 1 ? "0" + G : G;
		B = B.length() == 1 ? "0" + B : B;
		sb.append("0x");
		sb.append(R);
		sb.append(G);
		sb.append(B);
		return sb.toString();
	}

}
