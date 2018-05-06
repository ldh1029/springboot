package com.xhcoding.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;

/**
 * Created by xin on 2018/1/15.
 */
public class OutputToExcel {
    public static void outputToExcel() throws Exception{
        //创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作类
        HSSFSheet sheet = workbook.createSheet("poi");
        //创建行
        HSSFRow row = sheet.createRow(2);
        //创建单元格
        HSSFCell cell = row.createCell(2);
        cell.setCellValue("test poi");

        String filename = "c://users//xin//desktop//poi.xls";
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();
    }

    public static void main(String[] args) {
        try {
            //创建xls
            outputToExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
