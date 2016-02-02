package com.szl.stronguion.service.pagecontent;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.pagecontent.KeyWordsSearch;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/20.
 */
public class KeyWordsSearchServ {
    private KeyWordsSearch keyWordsSearch = new KeyWordsSearch();
    public List<Record> getKeyWords(String startTime, String endTime){
        return keyWordsSearch.getKeyWords(startTime, endTime);
    }
}
