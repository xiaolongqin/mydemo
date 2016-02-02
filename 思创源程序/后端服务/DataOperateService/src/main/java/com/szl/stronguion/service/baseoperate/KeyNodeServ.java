package com.szl.stronguion.service.baseoperate;

import com.jfinal.plugin.activerecord.Record;
import com.szl.stronguion.model.baseoperate.KeyNode;

import java.util.List;

/**
 * Created by Tyfunwang on 2015/7/7.
 */
public class KeyNodeServ {
    private KeyNode keyNode = new KeyNode();
    public List<Record> getNodes(String attri) {
        return keyNode.getNodes(attri);
    }
    public List<Record> getPageName(String attri) {
        return keyNode.getPageName(attri);
    }
    /**
     * 添加，修改关键路径和节点*
     * *
     */
    public List<Record> getLastNodeId(){
        //获取最大的一个node_id
        return keyNode.getLastNodeId();
    }

}
