package com.qianwang.util.Editor;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxExcelReader extends ExcelReader {
	XSSFWorkbook workbook = null;
	FormulaEvaluator evaluator = null;
	public XlsxExcelReader(String inputfile, int startLine) throws IOException, Exception {
		super(inputfile, startLine);
		workbook = new XSSFWorkbook(is);
		numOfSheets = workbook.getNumberOfSheets();
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	}

	public String readLine() {
		XSSFSheet sheet = workbook.getSheetAt(currSheet);
		if (currPosition > sheet.getLastRowNum()) {
			currPosition = XLS_START_POSITION;
			while (currSheet != numOfSheets - 1) {
				sheet = workbook.getSheetAt(currSheet + 1);
				if (currPosition == sheet.getLastRowNum()) {
					currSheet++;
					continue;
				}else{
					int row = currPosition;
					currPosition++;
					return getLine(sheet, row);
				}
			}
			return null;
		}
		int row = currPosition;
		currPosition++;
		return getLine(sheet, row);
	}

	private String getLine(XSSFSheet sheet, int row){
		XSSFRow rowline = sheet.getRow(row);
		StringBuffer buffer = new StringBuffer();
		int filledColumns = rowline.getLastCellNum();
		XSSFCell cell = null;
		for (int i = 0; i < filledColumns; i++) {
			cell = rowline.getCell((short) i);
			buffer.append(getValue(cell)).append(EXCEL_LINE_DELIMITER);
		}
		String lineStr = buffer.toString();
		return lineStr.substring(0, lineStr.lastIndexOf(EXCEL_LINE_DELIMITER));
	}
	
	private String getValue(XSSFCell cell){
		String cellvalue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_NUMERIC: {
				double num = cell.getNumericCellValue();
				if(!DateUtil.isCellDateFormatted(cell)){
					Matcher numMatch = PATTERN_INTEGER_MATCH.matcher(String.valueOf(num));
					cellvalue = numMatch.matches()?String.valueOf((int)num):String.valueOf(num);
				}else{
					Date date = cell.getDateCellValue();
					cellvalue = DATE_FORMATER.format(date);
				}
				break;
			}
			case XSSFCell.CELL_TYPE_BLANK:
				cellvalue = "";
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				cellvalue = String.valueOf(cell.getBooleanCellValue());
				break;
			case XSSFCell.CELL_TYPE_STRING:
				cellvalue = cell.getStringCellValue().replaceAll("'", "''");
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				evaluator.evaluateFormulaCell(cell);
				cellvalue = String.valueOf(cell.getNumericCellValue());
				break;
			default:
				cellvalue = " ";
			}
		}else{
			cellvalue = "";
		}
		return StringUtils.trim(cellvalue);
	}
}
