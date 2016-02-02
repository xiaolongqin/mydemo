package com.szl.stronguion.utils;

import com.jfinal.plugin.activerecord.Record;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.*;
import org.apache.commons.lang.StringUtils;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15-12-17.
 */
public class JxlTest {
    //导出获取细分市场价格区间热销产品扫描数据
    public static WritableWorkbook toExcel(List<Record> list, int saleType, OutputStream stream) throws IOException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(stream);
        WritableSheet sheet = book.createSheet("热销产品排名", 0);
        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setAlignment(Alignment.CENTRE);
        sheet.addCell(new Label(0, 0, "排名", cellFormat));
        sheet.addCell(new Label(1, 0, "商品名", cellFormat));
        sheet.addCell(new Label(2, 0, "销售额", cellFormat));
        sheet.addCell(new Label(3, 0, "销售量", cellFormat));
        sheet.addCell(new Label(4, 0, "渠道名", cellFormat));
        sheet.addCell(new Label(5, 0, "排名变化", cellFormat));
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            sheet.addCell(new Label(0, i + 1, (i+1)+"", cellFormat));
            sheet.addCell(new Label(1, i + 1, record.getStr("goodsname"), cellFormat));
            sheet.addCell(new Label(2, i + 1, Double.toString(record.getNumber("total_sale").doubleValue()), cellFormat));
            sheet.addCell(new Label(3, i + 1,Double.toString(record.getNumber("total_num").doubleValue()), cellFormat));
            sheet.addCell(new Label(4, i + 1, record.getStr("channel"), cellFormat));
            sheet.addCell(new Label(5, i + 1, record.get("goods_sales_flag").toString(), cellFormat));
        }
        return book;
    }


    //导出店铺扫描数据
    public static WritableWorkbook StoreToExcel(List<Record> list, int saleType, OutputStream stream) throws IOException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(stream);
        WritableSheet sheet = book.createSheet("线上店铺销售排行", 0);
        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setAlignment(Alignment.CENTRE);
        sheet.addCell(new Label(0, 0, "排名", cellFormat));
        sheet.addCell(new Label(1, 0, "店铺名", cellFormat));
        sheet.addCell(new Label(2, 0, "销售额", cellFormat));
        sheet.addCell(new Label(3, 0, "销售量", cellFormat));
        sheet.addCell(new Label(4, 0, "所属渠道", cellFormat));
        sheet.addCell(new Label(5, 0, "排名变化", cellFormat));
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            sheet.addCell(new Label(0, i + 1, (i+1)+"", cellFormat));
            sheet.addCell(new Label(1, i + 1, record.getStr("store_name"), cellFormat));
            sheet.addCell(new Label(2, i + 1, Double.toString(record.getNumber("total_sale").doubleValue()), cellFormat));
            sheet.addCell(new Label(3, i + 1,Double.toString(record.getNumber("total_num").doubleValue()), cellFormat));
            sheet.addCell(new Label(4, i + 1, record.getStr("channel"), cellFormat));
            sheet.addCell(new Label(5, i + 1, record.get("goods_sales_flag").toString(), cellFormat));
        }
        return book;
    }


    //导出品牌扫描数据
    public static WritableWorkbook GoodBrandToExcel(List<Record> list, int saleType, OutputStream stream) throws IOException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(stream);
        WritableSheet sheet = book.createSheet("线上品牌销售排行", 0);
        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setAlignment(Alignment.CENTRE);
        sheet.addCell(new Label(0, 0, "排名", cellFormat));
        sheet.addCell(new Label(1, 0, "品牌名", cellFormat));
        sheet.addCell(new Label(2, 0, "销售额", cellFormat));
        sheet.addCell(new Label(3, 0, "销售量", cellFormat));
        sheet.addCell(new Label(4, 0, "所属渠道", cellFormat));
        sheet.addCell(new Label(5, 0, "排名变化", cellFormat));
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            sheet.addCell(new Label(0, i + 1, (i+1)+"", cellFormat));
            sheet.addCell(new Label(1, i + 1, record.getStr("goodsbrand"), cellFormat));
            sheet.addCell(new Label(2, i + 1, Double.toString(record.getNumber("total_sale").doubleValue()), cellFormat));
            sheet.addCell(new Label(3, i + 1,Double.toString(record.getNumber("total_num").doubleValue()), cellFormat));
            sheet.addCell(new Label(4, i + 1, record.getStr("channel"), cellFormat));
            sheet.addCell(new Label(5, i + 1, record.get("goods_sales_flag").toString(), cellFormat));
        }
        return book;
    }

    //导出商品扫描数据
    public static WritableWorkbook GoodsToExcel(List<Record> list, int saleType, OutputStream stream) throws IOException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(stream);
        WritableSheet sheet = book.createSheet("全网商品扫描", 0);
        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setAlignment(Alignment.CENTRE);
        sheet.addCell(new Label(0, 0, "排名", cellFormat));
        sheet.addCell(new Label(1, 0, "商品名", cellFormat));
        sheet.addCell(new Label(2, 0, "销售额", cellFormat));
        sheet.addCell(new Label(3, 0, "销售量", cellFormat));
        sheet.addCell(new Label(4, 0, "渠道名", cellFormat));
        sheet.addCell(new Label(5, 0, "排名变化", cellFormat));
        for (int i = 0; i < list.size(); i++) {
            Record record = list.get(i);
            sheet.addCell(new Label(0, i + 1, (i+1)+"", cellFormat));
            sheet.addCell(new Label(1, i + 1, record.getStr("goodsname"), cellFormat));
            sheet.addCell(new Label(2, i + 1, Double.toString(record.getNumber("total_sale").doubleValue()), cellFormat));
            sheet.addCell(new Label(3, i + 1,Double.toString(record.getNumber("total_num").doubleValue()), cellFormat));
            sheet.addCell(new Label(4, i + 1, record.getStr("channel"), cellFormat));
            sheet.addCell(new Label(5, i + 1, record.get("goods_sales_flag").toString(), cellFormat));
        }
        return book;
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
//                String newFileName = URLEncoder.encode(filename, "ISO8859-1");
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



    public static void main(String[] args) {
        List<Record> records = new ArrayList<Record>();
        for (int i = 0; i < 10; i++) {
            Record record = new Record();
            record.set("goodsname", "五粮液");
            record.set("category", "白酒");
            record.set("total", 321.312);
            record.set("goodsname", "五粮液");
            record.set("goodsname", "白酒");
            record.set("percent", 98.1);
            records.add(record);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream("221.xls");
//            toExcel(records, 1, outputStream);
            WritableWorkbook writableWorkbook = toExcel(records, 1, outputStream);
            writableWorkbook.write();
            writableWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
