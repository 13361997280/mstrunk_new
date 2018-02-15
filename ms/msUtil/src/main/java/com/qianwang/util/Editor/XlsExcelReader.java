package com.qianwang.util.Editor;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class XlsExcelReader extends ExcelReader {

	HSSFWorkbook workbook = null;
	FormulaEvaluator evaluator = null;
	
	public XlsExcelReader(String inputfile, int startLine) throws IOException, Exception {
		super(inputfile, startLine);
		workbook = new HSSFWorkbook(is);
		numOfSheets = workbook.getNumberOfSheets();
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	}

	public String readLine() {
		HSSFSheet sheet = workbook.getSheetAt(currSheet);
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

	/**
	 * 获取一行数据,数据间以指定分隔符分隔
	 * @param sheet
	 * @param row
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getLine(HSSFSheet sheet, int row){
		HSSFRow rowline = sheet.getRow(row);
		StringBuffer buffer = new StringBuffer();
		int filledColumns = rowline.getLastCellNum();
		HSSFCell cell = null;
		for (int i = 0; i < filledColumns; i++) {
			cell = rowline.getCell((short) i);
			buffer.append(getValue(cell)).append(EXCEL_LINE_DELIMITER);
		}
		String lineStr = buffer.toString();
		return lineStr.substring(0, lineStr.lastIndexOf(EXCEL_LINE_DELIMITER));
	}
	
	/**
	 * 将Cell值转为String类型返回
	 * @param cell
	 * @return
	 */
	protected String getValue(HSSFCell cell) {
		
		String cellvalue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: {
				double num = cell.getNumericCellValue();
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					cellvalue = DATE_FORMATER.format(date);
				}
				else {
					Matcher numMatch = PATTERN_INTEGER_MATCH.matcher(String.valueOf(num));
					cellvalue = numMatch.matches()?String.valueOf((int)num):String.valueOf(num);
				}
				break;
			}
			case XSSFCell.CELL_TYPE_BLANK:
				cellvalue = "";
				break;
	        case HSSFCell.CELL_TYPE_BOOLEAN:
	        	cellvalue = String.valueOf(cell.getBooleanCellValue());
	        	break;
			case HSSFCell.CELL_TYPE_STRING:
				cellvalue = cell.getStringCellValue().replaceAll("'", "''");
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				evaluator.evaluateFormulaCell(cell);
				cellvalue = String.valueOf(cell.getNumericCellValue());
				break;
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return StringUtils.trim(cellvalue);
    }
}
