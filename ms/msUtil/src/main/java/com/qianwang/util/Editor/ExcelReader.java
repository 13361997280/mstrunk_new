package com.qianwang.util.Editor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class ExcelReader {

	protected int XLS_START_POSITION = 0;
	// 创建文件输入流
	protected BufferedReader reader = null;
	// 文件二进制输入流
	protected InputStream is = null;
	// 当前的Sheet
	protected int currSheet;
	// 当前位置
	protected int currPosition;
	// Sheet数量
	protected int numOfSheets;
	// 设置Cell之间以空格分割
	public static String EXCEL_LINE_DELIMITER = ",";
	// 设置最大列数
	protected static int MAX_EXCEL_COLUMNS = 64;
	//时间戳-格式
	public static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	protected static final Pattern PATTERN_INTEGER_MATCH = Pattern.compile("^[0-9]+\\.0$");
	protected static final DateFormat DATE_FORMATER = new SimpleDateFormat(TIMESTAMP_PATTERN);
	
	// 构造函数创建一个ExcelEditor
	public ExcelReader(String inputfile, int startLine) throws IOException, Exception {
		// 判断参数是否为空或没有意义
		if (inputfile == null || inputfile.trim().equals("")) {
			throw new IOException("no input file specified");
		}
		if(startLine > 0){
			XLS_START_POSITION = startLine;
		}
		// 设置开始行
		currPosition = XLS_START_POSITION;
		// 设置当前位置为0
		currSheet = 0;
		// 创建文件输入流
		is = new FileInputStream(inputfile);
	}
	
	public void close() {
		// 如果is不为空，则关闭InputSteam文件输入流
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				is = null;
			}
		}
		// 如果reader不为空则关闭BufferedReader文件输入流
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				reader = null;
			}
		}
	}

	public String readLine() {
		return null;
	}
}
