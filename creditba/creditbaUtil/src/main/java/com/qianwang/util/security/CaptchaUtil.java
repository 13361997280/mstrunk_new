package com.qianwang.util.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 验证图片生成工具类
 * @author zou
 *
 */
public class CaptchaUtil {
	/**
	 * 验证码session存储标识
	 */
	public static final String VALID_CODE_MARK = "validCode";
	
	/**
	 * 生成验证图片图片流
	 * @return 返回验证code为key，BufferedImage图片流为值的map
	 */
	public static Map<String,BufferedImage> generate() {
		// 设定图片长宽
		int width = 70;
		int height = 27;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		// 得到缓冲区Graphics对象
		Graphics graphics = img.getGraphics();
		// 画背景,颜色浅
		graphics.setColor(getRandColor(200, 250));
		graphics.fillRect(0, 0, width, height);
		// 画边框
		graphics.setColor(getRandColor(0, 255));
		graphics.drawRect(0, 0, width - 1, height - 1);
		// 随机产生两条干扰线
		graphics.setColor(getRandColor(160, 200));
		Random random = new Random();
		for (int i = 0; i < 2; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			graphics.drawLine(x, y, x1, y1);
		}
		// 随机产生几个点
		graphics.setColor(getRandColor(160, 200));
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			graphics.drawLine(x, y, x, y);
		}
		// 随机产生四个字符，并设置其颜色、字体、大小以及位置
		Font font = new Font("Times New Roman", Font.ITALIC | Font.BOLD, 22);
		String validCode = getValidCode();
		int i = 0;
		for (char c : validCode.toCharArray()) {
			graphics.setColor(getRandColor(20, 130));
			graphics.setFont(font);
			graphics.drawString("" + c, (i++ * 15) + 3, 18);
		}
		Map<String,BufferedImage> v = new HashMap<String, BufferedImage>();
		v.put(validCode, img);
		return v;
	}
	
	/**
	 * 随机生成验证码
	 * @return 验证码
	 */
	private static String getValidCode(){
		StringBuffer sb = new StringBuffer();
		char[] ch = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
		int index, length = ch.length;
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			index = random.nextInt(length);
			sb.append(ch[index]);
		}
		return sb.toString();
	}
	
	/**
	 * 根据颜色取值范围 随机生成颜色
	 * @param lower 最小颜色取值
	 * @param upper 最大颜色取值
	 * @return 随机生成的颜色
	 */
	private static Color getRandColor(int lower, int upper) {
		Random random = new Random();
		lower = lower > 255 ? 255 : lower;
		lower = lower < 1 ? 1 : lower;
		upper = upper > 255 ? 255 : upper;
		upper = upper < 1 ? 1 : upper;
		if (lower > upper) {
			int temp = upper;
			upper = lower;
			lower = temp;
		}
		int r = lower + random.nextInt(upper - lower);
		int g = lower + random.nextInt(upper - lower);
		int b = lower + random.nextInt(upper - lower);
		;
		return new Color(r, g, b);
	}
}
