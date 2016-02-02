package com.szl.stronguion.model.pagecontent;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/20.
 */
public class KeyWordsSearch extends Model<KeyWordsSearch> {

    public List<Record> getKeyWords(String startTime, String endTime) {
        return Db.use("main2").find("select t.word_content as 'keyword', count(*) as 'searchtimes'\n" +
                "  from sl_ods_keywords_search t\n" +
                " where FROM_UNIXTIME(t.datetime, '%Y%m%d') >= '" + startTime + "'\n" +
                "    and FROM_UNIXTIME(t.datetime, '%Y%m%d') <= '" + endTime + "'\n" +
                "    and (t.datetime is not null or t.datetime <> 0)\n" +
                "  group by t.word_content \n" +
                " order by count(*) desc;");
    }

    //获取用户搜索关键词信息
    public Record getSearchKeyWord(int uid) {
        return Db.use("main2").findFirst("select word_content from sl_ods_keywords_search \n" +
                "\t\t\t\twhere uid = '" + uid + "' and uid <> '-1'\n" +
                "\t\t\t\tGROUP BY word_content\n" +
                "\t\t\t\t\tORDER BY count(word_content) desc LIMIT 1;");

    }
}
