package com.qianwang.util.Editor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DcFileUtil {

	/**
	 * 上传临时文件重命名
	 * @param name
	 * @return
	 */
	public static String rename(String name){
		Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		Long random = (long) (Math.random() * now);
		String fileName = now + "" + random;
		if (name.indexOf(".") != -1) {
			fileName += name.substring(name.lastIndexOf("."));
		}
		return fileName;
	}
}
