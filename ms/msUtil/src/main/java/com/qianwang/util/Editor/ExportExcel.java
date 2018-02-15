package com.qianwang.util.Editor;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExportExcel {
	
	public void exportExcel(String title, String[] headers, String[] fields, Collection<?> dataset, OutputStream out, String datePattern){
		//声明一个工作薄
		Workbook workbook = new SXSSFWorkbook(1000);
		//生成一个表格
		Sheet sheet = workbook.createSheet(title);
		//设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		
		//表格标题样式
		CellStyle headStyle = workbook.createCellStyle();
		//设置这些样式
		headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		//生成一个字体
		Font headFont = workbook.createFont();
		headFont.setColor(HSSFColor.BLACK.index);
		headFont.setFontHeightInPoints((short) 12);
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		//把字体应用到当前的样式
		headStyle.setFont(headFont);
		
		//表格内容样式
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		bodyStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		bodyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		bodyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		bodyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		bodyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		bodyStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//表格内容字体
		Font bodyFont = workbook.createFont();
		bodyFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//表格内容应用到表格内容样式
		bodyStyle.setFont(bodyFont);
		
		//声明一个画图的顶级管理器
//		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//		//定义注释的大小和位置,详见文档
//		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
//		//设置注释内容
//		//comment.setString(new HSSFRichTextString(""));
//		//设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//		comment.setAuthor("datacenter");
		
		//产生表格标题行
		Row row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		
		//遍历集合数据，产生数据行
		Iterator<?> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object t = it.next();
			for (int i = 0; i < fields.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(bodyStyle);
				String fieldName = fields[i]; 
				try {
					BeanUtilsBean.setInstance(new BeanUtilsBean2());
					DateConverter converter = new DateConverter();
					converter.setPattern(datePattern);
					DoubleConverter dc = new DoubleConverter();
					dc.setPattern("#.00");
					ConvertUtils.register(converter, Date.class);
					ConvertUtils.register(dc, Double.class);
					String value = BeanUtils.getProperty(t, fieldName);
					if(value == null) value = "";
					HSSFRichTextString textValue = new HSSFRichTextString(value);
					cell.setCellValue(textValue); 
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
