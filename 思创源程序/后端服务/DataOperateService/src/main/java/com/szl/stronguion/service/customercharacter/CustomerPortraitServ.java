package com.szl.stronguion.service.customercharacter;

import com.szl.stronguion.model.customercharacter.AllCycleUsersFlag;
import com.szl.stronguion.model.customercharacter.LifeCycleUsers;
import com.szl.stronguion.model.customercharacter.customerportrait.Attention;
import com.szl.stronguion.model.customercharacter.customerportrait.AttentionGoods;
import com.szl.stronguion.model.customercharacter.customerportrait.UserPortraitTfidf;
import com.szl.stronguion.model.pagecontent.KeyWordsSearch;
import com.szl.stronguion.model.salesanalysis.AnalysisOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyfunwang on 2015/7/20.
 */
public class CustomerPortraitServ {
    private LifeCycleUsers cycleUsers = new LifeCycleUsers();
    private AnalysisOrder analysisOrder = new AnalysisOrder();
    private AllCycleUsersFlag allCycleUsersFlag = new AllCycleUsersFlag();
    private Attention attention = new Attention();
    private AttentionGoods attentionGoods = new AttentionGoods();
    private KeyWordsSearch keyWordsSearch = new KeyWordsSearch();
    private UserPortraitTfidf userPortraitTfidf = new UserPortraitTfidf();
    //获取用户画像
//    public Map<String, Object> getCustPortrait(int uid, String sex) {
//        Map<String, Object> maps = new HashMap<String, Object>();
//        maps.put("attention", userAttention.getUserAttention(uid));
//        maps.put("baseinfo", cycleUsers.getBaseInfo(uid));
//        maps.put("portraitaction", portraitAction.getPortraitAction(uid));
//        maps.put("conpreference", conPreference.getConPrefer(uid));
//        return maps;
//    }

    //获取用户画像六个维度
    public Map<String, Object> getCustPortraits(int uid) {
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("baseinfo", cycleUsers.getBaseInfo(uid));//基本信息
        maps.put("conBehavior", analysisOrder.getConBehavior(uid));//用户消费行为
        maps.put("activation", allCycleUsersFlag.getActivation(uid));//用户活跃度

        //用户关注
        maps.put("hobbyShop", attention.getHobbyShop(uid));//用户关注--喜好商铺
        maps.put("hobbyGood", attentionGoods.getHobbyGood(uid));//用户关注--喜好商品
        
        maps.put("userGroup", cycleUsers.getUserGroup(uid));//用户群
        
        maps.put("browseHobby", userPortraitTfidf.getPortrait(uid));//用户浏览爱好
        maps.put("searchHobby", keyWordsSearch.getSearchKeyWord(uid));//用户搜索爱好
        return maps;
    }
}
