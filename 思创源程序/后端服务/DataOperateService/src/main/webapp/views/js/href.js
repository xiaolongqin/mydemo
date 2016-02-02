$(window.parent.document).find('.list-chosen').removeClass('list-chosen');
$(window.parent.document).find('.triangle').addClass('list-hide');
var _iframeHref=window.parent.document.getElementById("iframepage").contentWindow.location.href;
var _iframeLast=_iframeHref.split('/').pop();
var _iframeA=window.parent.document.getElementById('left-nav');
var _aList=$(_iframeA).find('a');
var _iframeLen=_aList.length;
for(var _iframeI=0;_iframeI<_iframeLen;_iframeI++){
    var _aHref=_aList.eq(_iframeI).attr('href');
    if(_aHref==_iframeLast){
        _aList.eq(_iframeI).parent().parent().addClass('list-chosen');
        _aList.eq(_iframeI).parent().parent().find('.triangle').removeClass('list-hide');
    }
}