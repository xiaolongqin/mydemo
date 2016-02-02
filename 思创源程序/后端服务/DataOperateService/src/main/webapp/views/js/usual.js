//екеж
function shade(){
    var divShade=window.parent.document.createElement('div');
    divShade.style.width='100%';
    divShade.style.height=window.parent.document.body.clientHeight+50+'px';
    divShade.style.position='absolute';
    divShade.style.top=0;
    divShade.style.left=0;
    divShade.style.zIndex=999;
    divShade.style.backgroundColor="#2C2C2C";
    divShade.style.filter="alpha(opacity=60)";
    divShade.style.opacity="0.6";
    $(divShade).addClass('shade');
    window.parent.document.body.appendChild(divShade);
}
//ШЅЕєекеж
function test(){
    $(window.parent.document.body).find('.shade').remove();
}