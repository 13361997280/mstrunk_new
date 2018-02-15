package com.report.service;

import com.report.Config;
import com.report.dto.DataDTO;
import com.utils.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.utils.DateUtils.getDateByFormat;
import static com.utils.DateUtils.getYestDate;

/**
 * @author song.j
 * @create 2017-08-10 10:10:07
 **/
public class ExcelService {

    EsDataService dataService = new EsDataService();

    /**
     * excel 文件处理
     *
     * @return 文件路径
     */
    public String handlExcelPath() {
        XSSFWorkbook wb = null;
        try {
            wb = getShell();

            handShellMon(wb);
            handShellDay(wb);

            String path = Config.USER_DIR + "/file/oneday报表-" + DateUtils
                    .getDateByFormat(getYestDate(), "yyyyMMdd") + ".xlsx";

            FileOutputStream fileOut = new FileOutputStream(path);

            wb.write(fileOut);

            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public XSSFWorkbook getShell() throws IOException {
        //得到Excel工作簿对象

        return new XSSFWorkbook(new FileInputStream(Config.USER_DIR + "/file/oneday报表-" + getDateByFormat
                (org.apache.commons.lang.time.DateUtils.addDays(new Date(),-2), "yyyyMMdd") + "" +
                ".xlsx"));
    }


    public void handShellMon(XSSFWorkbook wb) throws IOException {

        List<DataDTO> dataDTOList = dataService.handPvUvData(0);
        //这里先设置10107为空数据。因为es里面不会有这个数据。 快递模块没有上线
//        dataDTOList.add(new DataDTO(10107, 0, 0));
        dataDTOList.sort(new DataComparator());

        //月份工作表
        XSSFSheet monShell = wb.getSheetAt(0);

        //第一行日期处理
        Row firstRow = monShell.getRow(0);
        int lastCell = firstRow.getLastCellNum();
        Cell cell = firstRow.createCell(lastCell);         //创建单元格
        Date yestToday = getYestDate();
        cell.setCellValue(getDateByFormat(yestToday, "MM") + "月" + getDateByFormat(yestToday, "dd") + "日");
        cell.setCellStyle(firstRow.getCell(0).getCellStyle());  //复制样式
        firstRow.createCell(lastCell + 1);
        monShell.addMergedRegion(new CellRangeAddress(0, 0, lastCell, lastCell + 1));

        monShell.setColumnWidth(lastCell,2660);

        //第二行pv/uv
        Row pvuvRow = monShell.getRow(1);
        Cell pvCell = pvuvRow.createCell(pvuvRow.getLastCellNum());
        pvCell.setCellValue("pv");
        pvCell.setCellStyle(pvuvRow.getCell(0).getCellStyle());
        Cell uvCell = pvuvRow.createCell(pvuvRow.getLastCellNum());
        uvCell.setCellValue("uv");
        uvCell.setCellStyle(pvuvRow.getCell(0).getCellStyle());


        XSSFCellStyle indexType = monShell.getRow(4).getCell(4).getCellStyle(); //样式复制

        //数据内容填充
        Integer totalPv = 0;
        Integer totalUv = 0;
        //从第2开始。因为前两行是日期和pv/uv头信息
        for (int i = 2; i < 14;i++) {

            Row inRow = monShell.getRow(i);
            String productId = inRow.getCell(1).getStringCellValue().toString();

            boolean flag = false;
            for (DataDTO dto : dataDTOList) {
                if (dto.getProductId().toString().equals(productId)){
                    totalPv += dto.getPv();     //pv总记
                    totalUv += dto.getUv();     //uv总记
                    fillMonData(inRow,dto, indexType);
                    flag = true;
                }
            }

            if (!flag){
                fillMonData(inRow,new DataDTO(0,0,0), indexType);
            }
        }

        //总计数据填充
        Row toRow = monShell.getRow(14);
        Cell toPVCell = toRow.createCell(toRow.getLastCellNum());
        toPVCell.setCellValue(totalPv);
        toPVCell.setCellStyle(indexType);

        Cell toUVCell = toRow.createCell(toRow.getLastCellNum());
        toUVCell.setCellValue(totalUv);
        toUVCell.setCellStyle(indexType);

    }

    public void fillMonData(Row inRow, DataDTO dataDTO ,XSSFCellStyle indexType) {
        Cell inPVCell = inRow.createCell(inRow.getLastCellNum());
        inPVCell.setCellValue(dataDTO.getPv());
        inPVCell.setCellStyle(indexType);

        Cell inUVCell = inRow.createCell(inRow.getLastCellNum());
        inUVCell.setCellValue(dataDTO.getUv());
        inUVCell.setCellStyle(indexType);
    }


    public static XSSFCellStyle style = null;

    public void handShellDay(XSSFWorkbook wb) {

        wb.setSheetName(1, getDateByFormat(getYestDate(), "yyyyMMdd"));
        //日期工作表
        XSSFSheet dayShell = wb.getSheetAt(1);

        style = dayShell.getRow(2).getCell(1).getCellStyle(); //样式复制

        for (int cell = 0; cell < 25; cell++) {
            List<DataDTO> datalist = dataService.handPvUvData(cell);
            datalist.sort(new DataComparator());
            handData(dayShell,datalist,cell);
        }
    }


    public void handData(XSSFSheet dayShell,List<DataDTO> datalist,int cell){

        for (int i = 2; i < 14; i++) {
            Row daRow = dayShell.getRow(i);

            boolean flag = false;

            for (DataDTO data : datalist) {
                if (daRow.getCell(1).getStringCellValue().toString().equals(data.getProductId().toString())) {
                    Cell pvCell = daRow.getCell(cell * 2 + 2);
                    pvCell.setCellValue(data.getPv());
                    pvCell.setCellStyle(style);
                    Cell uvCell = daRow.getCell(cell * 2 + 3);
                    uvCell.setCellValue(data.getUv());
                    uvCell.setCellStyle(style);
                    flag = true;

                    break;
                }
            }


            if (!flag){
                Cell pvCell = daRow.getCell(cell * 2 + 2);
                pvCell.setCellValue("0");
                pvCell.setCellStyle(style);
                Cell uvCell = daRow.getCell(cell * 2 + 3);
                uvCell.setCellValue("0");
                uvCell.setCellStyle(style);
            }



        }


    }



    class DataComparator implements Comparator<DataDTO> {
        @Override
        public int compare(DataDTO o1, DataDTO o2) {
            return o1.getProductId() - o2.getProductId();
        }
    }


    /**
     * 删除列
     * @param sheet
     * @param removeColumnNum
     * @param removeColumnTotal
     */
    public void removeColumn(XSSFSheet sheet, int removeColumnNum, int removeColumnTotal){
        if(sheet == null){
            return;
        }
        for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext();) {
            XSSFRow row = (XSSFRow)rowIterator.next();
            XSSFCell cell = row.getCell(removeColumnNum);
            if(cell == null){
                continue;
            }
            row.removeCell(cell);

            for(int n = removeColumnNum; n < (removeColumnTotal + removeColumnNum); n ++){
                int columnWidth = sheet.getColumnWidth(n + 1);

                XSSFCell cell2 = row.getCell(n + 1);

                if(cell2 == null){
                    break;
                }
                sheet.setColumnWidth(n, columnWidth);


            }
        }
    }

}
