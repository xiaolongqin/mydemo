package com.szl.stronguion.utils;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;

//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
/**
 * Created by Tyfunwang on 2015/9/2.
 */
public class Poi {
    public static HSSFWorkbook toExcel(List<Record> list) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("用户信息表");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("手机号码");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("省");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("市");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("区县");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("小区");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("地址");
        cell.setCellStyle(style);

        //第五步 ，把数据存入sheet
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(record.getInt("uid"));
            row.createCell(1).setCellValue(record.getStr("name"));
            row.createCell(2).setCellValue(record.getStr("phone"));
            row.createCell(3).setCellValue(record.getStr("province"));
            row.createCell(4).setCellValue(record.getStr("city"));
            row.createCell(5).setCellValue(record.getStr("town"));
            row.createCell(6).setCellValue(record.getStr("village"));
            row.createCell(7).setCellValue(record.getStr("address"));
        }
        return wb;
    }


    /**
     * 设置下载文件中文件的名称
     *
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息
         * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
         * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
         */
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (agent.toUpperCase().contains("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (agent.toLowerCase().contains("mozilla")))
                return MimeUtility.encodeText(filename, "UTF-8", "B");

            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }
}
