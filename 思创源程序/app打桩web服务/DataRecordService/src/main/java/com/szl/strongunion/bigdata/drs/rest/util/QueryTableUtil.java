package com.szl.strongunion.bigdata.drs.rest.util;

/**
 * Created by 小龙
 * on 15-7-17
 * at 上午10:39.
 */

public class QueryTableUtil {
    public static String querySql(String realTableName,String table_schema){
        String querysql="SELECT COUNT(*) FROM information_schema.tables \n" +
                "WHERE lower(TABLE_SCHEMA) = lower(\'"+table_schema+"\')\n" +
                "and lower(TABLE_NAME) = lower(\'"+realTableName+"\')";
        return querysql;
    }

    public static String createVisitTable(String realTableName){
        String createSql="CREATE TABLE "+realTableName+"(\n" +
                "`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动递增的主键' ,\n" +
                "`uid`  int(11) NOT NULL COMMENT '用户id:如果当前用户是登录用户，记录登录用户id，否则，记录-1' ,\n" +
                "`parent_act_id`  bigint(20) NOT NULL COMMENT '上级行为id 与本表的自增id相关联：上级行为若有记录，记录上一次行为的id；如果没有上级行为，则记录-1(自关联)' ,\n" +
                "`parent_id`  bigint(20) NOT NULL COMMENT '上级页面id：记录parent_act_id行为对于id对应的page_id' ,\n" +
                "`page_id`  bigint(11) NOT NULL COMMENT '页面id：该次行为访问的页面id' ,\n" +
                "`page_action_id`  bigint(11) NOT NULL COMMENT '页面定义行为按钮ID：该次行为访问的按钮id' ,\n" +
                "`contents`  varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '页面浏览内容：以朱军华和高智胜沟通的存放内容为准，存放：浏览页面的商品名称、价格等数据。' ,\n" +
                "`sub_page_id`  bigint(20) NOT NULL DEFAULT '-1' COMMENT '下级页面id：记录sub_act_id行为对于id对应的page_id' ,\n" +
                "`sub_act_id`  bigint(20) NOT NULL DEFAULT '-1' COMMENT '下级行为id 与本表的自增id相关联：下级行为若有记录，记录上一次行为的id；如果没有下级行为，则记录-1(自关联)' ,\n" +
                "`channel_apk_id`  int(11) NOT NULL COMMENT '渠道id：对应：channel_apk 的 id' ,\n" +
                "`imei`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机身份码：手机唯一标示' ,\n" +
                "`ip_adress`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ip地址：用户的ip地址' ,\n" +
                "`pagestart_time`  bigint(20) NOT NULL COMMENT '进入页面时间:时间戳' ,\n" +
                "`pageend_time`  bigint(20) NOT NULL DEFAULT 0 COMMENT '离开页面时间:时间戳' ,\n" +
                "`add_time`  bigint(20) NOT NULL COMMENT '记录添加时间:时间戳' ,\n" +
                "PRIMARY KEY (`id`)\n" +
                ")\n" +
                "ENGINE=MyISAM\n" +
                "DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci\n" +
                "COMMENT='页面访问表 ：sl_ods_page_visit_record_201507 按月归档'\n" +
                "AUTO_INCREMENT=6\n" +
                "CHECKSUM=0\n" +
                "ROW_FORMAT=DYNAMIC\n" +
                "DELAY_KEY_WRITE=0";
        return createSql;
    }

    public static String createLogTable(String realTableName){
        String createSql="CREATE TABLE "+realTableName+" (\n" +
                "`id`  bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id：自增' ,\n" +
                "`state`  tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：1-成功 2-失败 默认1' ,\n" +
                "`url`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '页面url地址' ,\n" +
                "`post_value`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求参数值' ,\n" +
                "`return_value`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '返回值' ,\n" +
                "`add_time`  bigint(20) NOT NULL COMMENT '记录添加时间：时间戳' ,\n" +
                "PRIMARY KEY (`id`)\n" +
                ")\n" +
                "ENGINE=MyISAM\n" +
                "DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci\n" +
                "COMMENT='接口数据调度异常日志表'\n" +
                "AUTO_INCREMENT=3\n" +
                "CHECKSUM=0\n" +
                "ROW_FORMAT=DYNAMIC\n" +
                "DELAY_KEY_WRITE=0";
        return createSql;
    }
}
