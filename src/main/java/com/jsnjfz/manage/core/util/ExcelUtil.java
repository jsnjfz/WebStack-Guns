package com.jsnjfz.manage.core.util;

import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出Excel公共方法
 *
 * @author lihao
 * @version 1.0
 */
public class ExcelUtil {

    //显示的导出表的标题
    private String title;
    //导出表的列名
    private String[] rowName;

    private List<Object[]> dataList = new ArrayList<Object[]>();

    private ServletOutputStream outputStream;

    //构造方法，传入要导出的数据
    public ExcelUtil(String title, String[] rowName, List<Object[]> dataList, ServletOutputStream outputStream) {
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
        this.outputStream = outputStream;
    }

    /*
     * 导出数据
     * */
    public void export() throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();                        // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title);                     // 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            rowm.setHeight((short) (25 * 35)); //设置高度

            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
            //HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
            // HSSFCellStyle style = this.getStyle(workbook);                    //单元格样式对象

            // sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (rowName.length-1)));
            //cellTiltle.setCellStyle(columnTopStyle);
            //cellTiltle.setCellValue(title);

            // 定义所需列数
            int columnNum = rowName.length;
            HSSFRow rowRowName = sheet.createRow(0);                // 在索引2的位置创建行(最顶端的行开始的第二行)

            rowRowName.setHeight((short) (25 * 25)); //设置高度

            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                HSSFCell cellRowName = rowRowName.createCell(n);                //创建列头对应个数的单元格
                cellRowName.setCellType(CellType.STRING);     //设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                cellRowName.setCellValue(text);                                    //设置列头单元格的值
                //cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
            }
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFDataFormat format = workbook.createDataFormat();
            //HSSFCell  cell = sheet.createRow(0;   //设置单元格的数据类型

            //将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {

                Object[] obj = dataList.get(i);//遍历每个对象
                HSSFRow row = sheet.createRow(i + 1);//创建所需的行数

                row.setHeight((short) (25 * 20)); //设置高度

                for (int j = 0; j < obj.length; j++) {
                    HSSFCell cell = null;
                  /*  if(row.getCell(j).getCellType()==CellType.STRING){
                        cell = row.createCell(j,CellType.STRING);
                        if(!"".equals(obj[j]) && obj[j] != null){
                            cell.setCellValue(obj[j].toString());                        //设置单元格的值
                        }
                    }*/
/*
                    if(row.getCell(j).getCellType()==CellType.NUMERIC){
                        cell = row.createCell(j,CellType.NUMERIC);
                        cell.setCellValue(i+1);
                    }

                    if(row.getCell(j).getCellStyle()==style.getDataFormat()){
                        cell = row.createCell(j,CellType.NUMERIC);
                        cell.setCellValue(i+1);
                    }*/


                    if (j == 0) {
                        cell = row.createCell(j, CellType.NUMERIC);
                        cell.setCellValue(i + 1);
                    } else {
                        cell = row.createCell(j, CellType.STRING);
                        if (!"".equals(obj[j]) && obj[j] != null) {
                            cell.setCellValue(obj[j].toString());                        //设置单元格的值
                        }
                    }
                    // cell.setCellStyle(style);                                    //设置单元格样式
                }
            }
            //让列宽随着导出的列长自动适应
          /*  for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    //当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == CellType.STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if(colNum == 0){
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 128);
                }else{
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                }


            }*/

            if (workbook != null) {
                try {
                    workbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 14);
        //字体加粗
        font.setFontHeight(FontRecord.sid);
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(ExtendedFormatRecord.BRICKS);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(ExtendedFormatRecord.BRICKS);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(ExtendedFormatRecord.BRICKS);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(ExtendedFormatRecord.BRICKS);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置单元格背景颜色
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(ExtendedFormatRecord.BRICKS);
        //设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(ExtendedFormatRecord.BRICKS);
        //设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        style.setRightBorderColor(ExtendedFormatRecord.BRICKS);
        //设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(ExtendedFormatRecord.BRICKS);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}