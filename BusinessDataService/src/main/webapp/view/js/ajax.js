//var HTTP = 'http://192.168.0.220:8080/BusinessDataService/';
//var HTTP='http://192.168.0.168:8091/BusinessDataService/';
//var HTTP = 'http://118.123.173.70:8091/BusinessDataService/';
var HTTP = '/BusinessDataService/';
var AJAXURL = {
    color:['#1885e8','#219eef','#29b6f6','#2fc4f1','#34d3eb','#6ae6e7','#5bdaac','#4cce71','#9bce4a','#ced95d'],//图表颜色
    channel1:['淘宝','天猫','也买酒'],  //有销售量
    channel2:['淘宝','天猫','京东','苏宁','酒仙','1号店','也买酒'],  //有评论数

    login:HTTP+"account/login",                //登录
    //saveUser:HTTP + "account/addAccount",              //添加用户 - 保存用户信息
    checkExist:HTTP+"account/checkExist",           //检查用户名是否存在
    verifyCode:HTTP+"account/img",                  //获取系统图片
    getNow:HTTP+"account/getNow",
    //getCata:HTTP+"account/getCata",
    register:HTTP+"account/register",
    registMail:HTTP+"account/registMail",
    checkMail:HTTP+"account/checkMail",                    //邮箱验重
    passMail:HTTP+"account/passMail",                      //   邮箱密码重置
    passMailFor:HTTP+"account/passMailFor",                 //请求验证email
    modifyPass:HTTP+"account/modifyPass", 					//修改密码
    addTestAccount:HTTP+"account/addTestAccount",          //申请试用接口

    searchAccountDR:HTTP +'account/searchAccountDR',     //权限审核搜索
    modifyDR:HTTP +'account/modifyDR',              //权限审核列表
    modifyEx: HTTP +'account/modifyEx',
    getCata:HTTP +"account/getCata",
    saveUser1:HTTP + 'account/addAccount',             //添加用户 - 保存用户信息
    savePage:HTTP + 'page/addPage',
    searchPage:HTTP + 'page/searchPage',
    changeState:HTTP + 'page/changePage',              //在列表中点击改变页面状态生效作废
    getPageAction:HTTP + 'pageaction/getPageAction',   //修改时获得页面按钮
    editPage:HTTP + 'page/editPage',
    updatePwd:HTTP + 'account/updatePwd',              //修改密码
    getAllNotices:HTTP + 'notice/getAllNotices',       //获取全部公告
    addNotice:HTTP + 'notice/addNotice',               //添加公告
    searchNotice:HTTP + 'notice/searchNotice',         //搜索公告
    modifyNotice:HTTP + 'notice/modifyNotice',         //修改公告
    deleteNotice:HTTP + 'notice/deleteNotice',         //删除公告
    deleteAccount:HTTP + 'account/deleteAccount',      //删除用户
    searchAccount:HTTP + 'account/searchAccount',      //搜索用户（包括获取所有用户列表）：获取所有用户时：name为空即可
    editAccount:HTTP + 'account/modifyAccount',        //编辑用户
    getAccountById:HTTP + 'account/getAccountById',    //由id得到用户信息
    checkRole:HTTP + 'roles/checkRole',                //检查该角色是否可以修改： true 可以修改 ，false不可修改
    modifyAccount:HTTP + 'account/modifyAccount',      //编辑用户的保存
    saveUser:HTTP + 'account/addAccount',              //添加用户 - 保存用户信息
    getAccount:HTTP + 'account/getAccount ',           //管理用户权限
    searchFeedBack:HTTP+'feed/searchFeedBack',         //查看意见
    getFeedBackByID:HTTP+'feed/getFeedBackByID',       //意见查找用户
    deleteFeedBack:HTTP+'feed/deleteFeedBack',

    logout:HTTP + 'account/logout',//退出
    getAccountMe:HTTP + 'account/getAccountMe',//获取账户
    getLastedNotice:HTTP + 'notice/getLastedNotice',//获取公告
    getAllChannelName:HTTP + 'channel/getAllChannelName',//获取所有渠道
    getDetailMarket:HTTP + 'electrial/getDetailMarket',//fangyan-shichanghangye页面 图1
    getDetailPrice:HTTP + 'electrial/getDetailPrice',//fangyan-shichanghangye 图2联动图
    getUserFocus:HTTP + 'electrial/getUserFocus',//fangyan-shichanghangye 图3,
    getOnlineChannelTop:HTTP + 'channel/getOnlineChannelTop',//qudao-qudaozonglan 上面两图
    getDetailChannelTop:HTTP + 'channel/getDetailChannelTop',//qudao-qudaozonglan 下面两图
    getOnlineChannelSale:HTTP + 'channel/getOnlineChannelSale',//qudao-qudaoxiangqing 图1
    getChannelShopTop:HTTP + 'channel/getChannelShopTop',//qudao-qudaoxiangqing 图2
    getChannelGoodTop:HTTP + 'channel/getChannelGoodTop',//qudao-qudaoxiangqing 图3
    getChannelduibi:HTTP + 'channel/getChannelduibi',//qudao-qudaoduibi页面 图1
    getChannelGoodDuibi:HTTP + 'channel/getChannelGoodDuibi',//qudao-qudaoduibi页面 图2
    getDetailPriceInfo:HTTP + 'electrialSecond/getDetailPriceInfo',//fangyan-shichanghangye-child页面 table
    getDetailMarketSale:HTTP + 'electrialSecond/getDetailMarketSale',//fangyan-shichanghangye-child 图1
    getGoodsBrandPercent:HTTP + 'electrialSecond/getGoodsBrandPercent',//fangyan-shichanghangye-child 图2
    exportDataExcel:HTTP + 'electrialSecond/exportDataExcel',//fangyan-shichanghangye-child导出
    getStoreSaleTotal: HTTP + 'storeAnalysis/getStoreSaleTotal',//dianpu-dianpukancha 表格
    exportDataExcel2: HTTP + 'storeAnalysis/exportDataExcel',//dianpu-dianpukancha 导出
    getStoreTopBychannel: HTTP + 'storeAnalysis/getStoreTopBychannel',//dianpu-dianpukancha 图1
    getStoreDetaiInfo: HTTP + 'storeAnalysis/getStoreDetaiInfo',//dianpu-dianpuxiangqing 图1
    getStoreComment: HTTP + 'storeAnalysis/getStoreComment',//dianpu-dianpuxiangqing 图2
    getStoreTop5: HTTP + 'storeAnalysis/getStoreTop5',//dianpu-dianpuduibi TOP5
    getStroreByName: HTTP + 'storeAnalysis/getStroreByName',//dianpu-dianpuduibi 表格
    getStoreSaleduibi: HTTP + 'storeAnalysis/getStoreSaleduibi',//dianpu-dianpuduibi-child 图1
    getStoreAttrduibi: HTTP + 'storeAnalysis/getStoreAttrduibi',//dianpu-dianpuduibi-child 图2
    getGoodsbrandSaleTotal: HTTP + 'goodsbrand/getGoodsbrandSaleTotal',//pinpai-pinpaisaomiao 表格
    exportDataExcel3:HTTP + 'goodsbrand/exportDataExcel',//pinpai-pinpaisaomiao 导出
    getGoodsbrandTopBycategory: HTTP + 'goodsbrand/getGoodsbrandTopBycategory',//pinpai-pinpaisaomiao 图1
    getStoreTop52: HTTP + 'goodsbrand/getStoreTop5',//pinpai-pinpaiduibi TOP5
    getGoodsbrandByName: HTTP + 'goodsbrand/getGoodsbrandByName',//pinpai-pinpaiduibi 表格
    getGoodsbrandAttrduibi: HTTP + 'goodsbrand/getGoodsbrandAttrduibi',//pinpai-pinpaiduibi-child 图1
    getStoreDetaiInfo2: HTTP + 'goodsbrand/getStoreDetaiInfo',//pinpai-pinpaixiangqing 图1
    getGoodsbrandSaleBysqudao: HTTP + 'goodsbrand/getGoodsbrandSaleBysqudao',//pinpai-pinpaixiangqing 图2
    getGoodsSaleByGoodsbrand: HTTP + 'goodsbrand/getGoodsSaleByGoodsbrand',//pinpai-pinpaixiangqing 图3
    getGoodsbrandPriceInfo: HTTP + 'goodsbrand/getGoodsbrandPriceInfo',//pinpai-pinpaixiangqing 图5
    getGoodsbrandFaceChange: HTTP + 'goodsbrand/getGoodsbrandFaceChange',//pinpai-pinpaixiangqing 图6
    getGoodsbrandFace: HTTP + 'goodsbrand/getGoodsbrandFace',//pinpai-pinpaixiangqing 图7图8
    getOverView:HTTP + 'electrial/getOverView',//首页,
    getGoodsScan:HTTP + 'goodsAnalysis/getGoodsScan',//shangpin-shangpinsaomiao 表格
    exportDataExcel4:HTTP + 'goodsAnalysis/exportDataExcel',//shangpin-shangpinsaomiao 导出
    getGoodsBrand: HTTP + 'goodsAnalysis/getGoodsBrand',//shangpin-shangpinpaihang 下拉框
    getGoodsTop: HTTP +'goodsAnalysis/getGoodsTop',//shangpin-shangpinpaihang 表格
    getOnlineGoodsduibi: HTTP + 'goodsAnalysis/getOnlineGoodsduibi',//shangpin-shangpinduibi TOP5
    getGoodsByGoodsName: HTTP + 'goodsAnalysis/getGoodsByGoodsName',//shangpin-shangpinduibi 表格
    getDegreeSale:HTTP + 'goodsAnalysis/getDegreeSale',//shangpin-shangpinxiaoliang 图1
    getXiangxinSale:HTTP + 'goodsAnalysis/getXiangxinSale',//shangpin-shangpinxiaoliang 图2
    getGoodsSaleduibi: HTTP + 'goodsAnalysis/getGoodsSaleduibi',//shangpin-shangpinduibi-child 图1
    getGoodsAttrduibi: HTTP + 'goodsAnalysis/getGoodsAttrduibi',//shangpin-shangpinduibi-child 图2,图3
    getDetailGoodsInfo:HTTP + 'goodsAnalysis/getDetailGoodsInfo',//shangpin-shangpinxiaoliang-child 图1
    getGoodsBrandInfo:HTTP + 'goodsAnalysis/getGoodsBrandInfo',//shangpin-shangpinxiaoliang-child 图2
    getGoodsNameInfo:HTTP + 'goodsAnalysis/getGoodsNameInfo',//shangpin-shangpinxiaoliang-child 3表格
    getGoodsFocus:HTTP + 'goodsAnalysis/getGoodsFocus',//shangpin-shangpinxiaoliang-child 图3,
    getGoodsSaleInfo:HTTP + 'goodsDetail/getGoodsSaleInfo',//shangpin-shangpinxiangqing 图1
    getChannelGoodDuibi2:HTTP + 'goodsDetail/getChannelGoodDuibi',//shangpin-shangpinxiangqing 图2
    getGoodsComment:HTTP + 'goodsDetail/getGoodsComment',//shangpin-shangpinxiangqing 图3
    getGoodsTopByCategory:HTTP + 'goodsDetail/getGoodsTopByCategory',//shangpin-shangpinxiangqing 表格1
    getGoodsTopByBrand:HTTP + 'goodsDetail/getGoodsTopByBrand',//shangpin-shangpinxiangqing 表格2
    getGoodsbrandSaleBychannel: HTTP + 'goodsbrandSecond/getGoodsbrandSaleBychannel',//qudao-qudaopinpaixiangqing 图1
    getStoreTopByBrand: HTTP + 'goodsbrandSecond/getStoreTopByBrand',//qudao-qudaopinpaixiangqing 图2
    getGoodsTopByBrand3: HTTP + 'goodsbrandSecond/getGoodsTopByBrand',//qudao-qudaopinpaixiangqing 图3
    getGoodsbrandSaleByPrice: HTTP + 'goodsbrandSecond/getGoodsbrandSaleByPrice',//qudao-pinpaijiage 图1
    getBrandSaleByChannel: HTTP + 'goodsbrandSecond/getBrandSaleByChannel',//qudao-pinpaijiage 图2
    getGoodsSaleByBrand: HTTP + 'goodsbrandSecond/getGoodsSaleByBrand',//qudao-pinpaijiage 图3
    getbrandFaceByPrice:HTTP + 'goodsbrandSecond/getbrandFaceByPrice'//qudao-pinpaijiage 图4
};