/*ajax url*/
var SLSCHTTP = 'http://115.182.16.122:8091/dataoperate';
//var SLSCHTTP = 'http://192.168.0.168:8091/dataoperate';
//var SLSCHTTP = 'http://192.168.0.220:8080/dataoperate';
var AJAXURL = {
    login:SLSCHTTP+"/account/login",						//登录
    getRoles: SLSCHTTP + '/roles/getRoles',                 //角色管理页面 - 加载时获取全部，不传参
    searchRoles: SLSCHTTP + '/roles/searchRole',            //角色管理页面 - 搜索时根据传参获取指定的内容
    logout:SLSCHTTP + "/account/logout",                    //登出
    saveCharacter:SLSCHTTP + '/roles/addRole',              //添加角色的弹窗，点击保存按钮执行
    delCharacter:SLSCHTTP + '/roles/deleteRoles',           //删除角色
    editCharacter:SLSCHTTP + '/module/getRoleModule',       //点击编辑角色，获取内容
    editCharacterRole:SLSCHTTP + '/roles/editRole',         //编辑角色弹窗时，点击保存按钮执行
    saveUser:SLSCHTTP + '/account/addAccount',              //添加用户 - 保存用户信息
    checkPageId:SLSCHTTP + '/page/checkPageId',             //判断页面ID是否重复
    checkActionId:SLSCHTTP + '/pageaction/checkActionId',   //判断按钮ID
    savePage:SLSCHTTP + '/page/addPage',
    searchPage:SLSCHTTP + '/page/searchPage',
    changeState:SLSCHTTP + '/page/changePage',              //在列表中点击改变页面状态生效作废
    getPageAction:SLSCHTTP + '/pageaction/getPageAction',   //修改时获得页面按钮
    editPage:SLSCHTTP + '/page/editPage',
    updatePwd:SLSCHTTP + '/account/updatePwd',              //修改密码
    getAllNotices:SLSCHTTP + '/notice/getAllNotices',       //获取全部公告
    addNotice:SLSCHTTP + '/notice/addNotice',               //添加公告
    searchNotice:SLSCHTTP + '/notice/searchNotice',         //搜索公告
    modifyNotice:SLSCHTTP + '/notice/modifyNotice',         //修改公告
    deleteNotice:SLSCHTTP + '/notice/deleteNotice',         //删除公告
    deleteAccount:SLSCHTTP + '/account/deleteAccount',      //删除用户
    searchAccount:SLSCHTTP + '/account/searchAccount',      //搜索用户（包括获取所有用户列表）：获取所有用户时：name为空即可
    editAccount:SLSCHTTP + '/account/modifyAccount',        //编辑用户
    getAccountById:SLSCHTTP + '/account/getAccountById',    //由id得到用户信息
    checkRole:SLSCHTTP + '/roles/checkRole',                 //检查该角色是否可以修改： true 可以修改 ，false不可修改
    getThreshold:SLSCHTTP + '/threshold/getThreshold',        //获取阀值
    saveYuzhi:SLSCHTTP + '/threshold/updateThreshold'        //保存阀值
};