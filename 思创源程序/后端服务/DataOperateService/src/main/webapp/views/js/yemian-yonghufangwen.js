var labelType, useGradients, nativeTextSupport, animate;

(function() {
    var ua = navigator.userAgent,
        iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
        typeOfCanvas = typeof HTMLCanvasElement,
        nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
        textSupport = nativeCanvasSupport
            && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    //I'm setting this based on the fact that ExCanvas provides text support for IE
    //and that as of today iPhone/iPad current text support is lame
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();
$(function(){
    $('#search-content').focus();
});
//自动补全
$("#search-content").autocomplete({
    delay: 400,
    source: function(request, response) {
        $.ajax({
            url: SLSCHTTP+"/accesspath/searchByPageName",
            type:'post',
            data: {
                pageName: request.term
            },
            dataType: "json",
            success: function(data) {
                var i,_itemLen=data.data.length,itemList=[];
                for(i=0;i<_itemLen;i++){
                    itemList.push(data.data[i]['page_name']);
                }
                response($.map(itemList, function(item) {
                    return {
                        label: item
                    }
                }));
            }
        });
    },
    minLength: 1,
    open: function() {  //在下拉框被显示的时候触发
        $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
    },
    close: function() {  //在下拉框被隐藏的时候触发
        $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
    }
});
//查询结果
function accessPath(){
    var pageName= $.trim($('#search-content').val());
    var dateType=$('#time-select').val();
    if(pageName==''){
        alert('请输入要查询的内容');
    }else{
        $('.load-pic').css({display:'block'});
        $('#infovis').html('');  //jit不会自动清空canvas，每次点击将canvas的父元素的内容清空
        $.ajax({
            url:SLSCHTTP+'/accesspath/accessPath',
            type:'post',
            data:{pageName:pageName,dateType:dateType},
            dataType:'json',
            success:function(response){
                if(response.data.page.length!= 0){
                    var _i,
                        _left=response.data.parent,  //左边节点
                        _right=response.data.subPageList,  //右边节点
                        _leftLen=response.data.parent.length,
                        _rightLen=response.data.subPageList.length,
                        _totalLen,
                        _total,  //左右节点一起的数组
                        _children=[];  //数据结构处理好的子节点
                    _totalLen=_leftLen+_rightLen;
                    _total=_left.concat(_right);
                    for(_i=0;_i<_totalLen;_i++){
                        _children.push({
                            id: 'node0'+_i,
                            name: '<div class="top-words">'+_total[_i]['page_name']+'</div>'+
                                '<div>' +
                                '<span class="white-words">平均访问量：</span><span class="white-words">'+_total[_i]['page_pv']+'次</span><br/>' +
                                '<span class="white-words">平均独立访客：</span><span class="white-words">'+_total[_i]['page_uv']+'户</span><br/>' +
                                '<span class="white-words">访客占比：</span><span class="white-words">'+_total[_i]['percent']+'%</span>' +
                                '</div>',
                            data: {}
                        });
                    }
                    //访客占比
                    var json={
                        id: 'node1',
                        name: '<div class="top-words">'+response.data.page[0]['page_name']+'</div>'+
                            '<div>' +
                            '<span class="white-words">平均访问量：</span>' +
                            '<span class="white-words">'+response.data.page[0]['page_pv']+'次</span><br/>' +
                            '<span class="white-words">平均独立访客：</span>' +
                            '<span class="white-words">'+response.data.page[0]['page_uv']+'户</span><br/>' +
                            '<span class="white-words">访客占比：'+response.data.page[0]['percent']+'%</span>' +
                            '</div>',
                        data: {},
                        children: _children
                    };
                    //preprocess subtrees orientation  这里是控制节点左右显示的
                    var arr = json.children, len = arr.length;
                    for(var i=0; i < len; i++) {
                        //split half left orientation
                        if(i < _leftLen) {
                            arr[i].data.$orn = 'left';
                            $jit.json.each(arr[i], function(n) {
                                n.data.$orn = 'left';
                            });
                        } else {
                            //half right
                            arr[i].data.$orn = 'right';
                            $jit.json.each(arr[i], function(n) {
                                n.data.$orn = 'right';
                            });
                        }
                    }
                    $('.load-pic').css({display:'none'});
                    var st = new $jit.ST({
                        //id of viz container element  canvas所在div名称
                        injectInto: 'infovis',
                        num: _leftLen>_rightLen?_leftLen:_rightLen,  //jit2765行设置canvas高度
                        //multitree
                        multitree: true,
                        refresh:true,
                        //set duration for the animation
//        duration: 800,
                        autoHeight:true,
                        //set animation transition type
//        transition: $jit.Trans.Quart.easeInOut,
                        //set distance between node and its children
                        levelDistance: 150,
                        //sibling and subtrees offsets  父级和兄弟图形间隔
                        siblingOffset: 20,
                        subtreeOffset: 30,
                        //set node and edge styles
                        //set overridable=true for styling individual
                        //nodes or edges   节点样式
                        Node: {
                            height: 100,
                            width: 220,
                            type: 'rectangle',
                            color: '#92c1ed',
                            overridable: false,  //卧槽，这里必须设为false，不然360浏览器下面背景为黑色
                            //set canvas specific styles
                            //like shadows
                            CanvasStyles: {
                                shadowColor: '#ccc',
                                shadowBlur: 10
                            }
                        },
                        //连接线样式
                        Edge: {
                            type: 'bezier',
                            lineWidth: 2,
                            color:'#92c1ed',
                            overridable: true
                        },
                        //This method is called on DOM label creation.
                        //Use this method to add event handlers and styles to
                        //your node.
                        onCreateLabel: function(label, node){
                            label.id = node.id;
                            label.innerHTML = node.name;
                            //set label styles
                            var style = label.style;
                            style.width = 220 + 'px';
                            style.height = 70 + 'px';
                            style.cursor = 'pointer';
                            style.color = '#333';
                            style.fontSize = '16px';
                            style.textAlign= 'center';
                            style.padding='3px';
                        },

                        //This method is called right before plotting
                        //a node. It's useful for changing an individual node
                        //style properties before plotting it.
                        //The data properties prefixed with a dollar
                        //sign will override the global node style properties.
                        onBeforePlotNode: function(node){
                            //add some color to the nodes in the path between the
                            //root node and the selected node.
                            if (node.selected) {
                                node.data.$color = "#92c1ed";
                            }
                            else {
                                delete node.data.$color;
                                //if the node belongs to the last plotted level
                                if(!node.anySubnode("exist")) {
                                    //count children number
//                    var count = 0;
//                    node.eachSubnode(function(n) { count++; });
                                    //assign a node color based on
                                    //how many children it has
                                    node.data.$color = ['#92c1ed'];
                                }
                            }
                        },

                        //This method is called right before plotting
                        //an edge. It's useful for changing an individual edge
                        //style properties before plotting it.
                        //Edge data proprties prefixed with a dollar sign will
                        //override the Edge global style properties.
                        onBeforePlotLine: function(adj){
                            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                                adj.data.$color = "#eed";
                                adj.data.$lineWidth = 3;
                            }
                            else {
                                delete adj.data.$color;
                                delete adj.data.$lineWidth;
                            }
                        }
                    });
                    //load json data
                    st.loadJSON(json);
                    //compute node positions and layout
                    st.compute('end');
                    st.select(st.root);
                }else{
                    $('.load-pic').css({display:'none'});
                    alert('查无数据');
                }
            }
        });
    }
}
/*回车事件*/
$('#search-content').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        accessPath();
    }
});