var DISPLAY_DEMO =
    '<div class="note-display">' +
    '<div class="note-display-other-message">' +
    '<span><b>作者</b></span><br/>' +
    '<span class="note-display-author"></span><br/>' +
    '<span><b>页数</b></span><br/>' +
    '<span class="note-display-pages"></span><br/>' +
    '<span><b>笔记创建时间</b></span><br/>' +
    '<span class="note-display-create-time"></span><br/>' +
    '<span><b>最后编辑时间</b></span><br/>' +
    '<span class="note-display-edit-time"></span>' + '</div>' +
    '<div class="note-display-title-mask">' + '<div>' +
    '<span class="note-display-title"></span>' +
    '</div>' + '</div>' + '</div>';

var me = null;
var contentsize = 14;
var maindisplaypage = 1;
var maindisplaystrategies = "latestPublish";
var searchdisplaypage = 1;
var searchkeyword = "";
function noteSearch(button){
    if($("#note-search-input").val().trim() === "")return;
    if("搜&nbsp;索" === $(button).html()){
        $(button).html("收&nbsp;起");
        searchkeyword = $("#note-search-input").val().trim();
        loadNotesFromBackend($("#note-search-display-content"),"keyword",searchkeyword,1);
        $("#note-search-display-panel").slideDown();
    } else {
        $(button).html("搜&nbsp;索");
        $("#note-search-display-panel").slideUp();
    }
}
function bindSearchPanel(){
    $("#note-search-button").click(function () {
        noteSearch(this);
    });
    $("#note-search-input").focus(function () {
        $("#note-search-button").html("搜&nbsp;索");
        $("#note-search-display-panel").slideUp();
    });
    $("#note-search-input").keydown(function (event) {
        if(event.which === 13)
            noteSearch($("#note-search-button"));
    })
}
function loadUserMessage() {
    var c_code = get_cookie("c_code");
    if(c_code === null){
        location.href = "login.jsp";
    }
    $.ajax({
        type: 'POST',
        dataType: 'text',
        url: "PersonalInfo",
        data:{"c_code":c_code,"type":"get"},
        async:false,
        success:function(data){
            if((data+"").trim() === "You are not logged in."){
                location.href = "login.jsp";
            }else{
                me = JSON.parse((data+"").trim());
                if(me.headphoto == "")$("#main-top-head").css({'background-image':'url("PIC/headdefault.png")'});
                else $("#main-top-head").css({'background-image':'url("'+me.headphoto+'")'});
                $("#main-top-space-button").parent().attr({
                    "href":"user.html?id="+me.stu_id
                });
            }
        }.bind(this)
    });
}
function userLogout() {
    document.cookie="c_code=''";
    location.href = 'login.jsp';
}
function changePageInFrondend(contentpanel,page) {
    var stapage = Math.max(page-2,1);
    var buttons = $(contentpanel).find(".main-page-switch");
    for(var i=0;i<buttons.length;i++){
        $(buttons[i]).html(stapage+i);
    }
}
function loadNotesFromBackend(contentpanel,strategies,keyword,page) {
    if(page <= 0)return;
    changePageInFrondend(contentpanel,page);
    var lo = contentsize*(page-1)+1,hi = contentsize*page;
    var c_code = get_cookie("c_code");
    if(c_code === null){
        location.href = "login.jsp";
    }
    var datatopost = {"c_code":c_code,"type":"search","strategies":strategies,"low":lo,"high":hi};
    if(strategies === "keyword")datatopost.keyword = keyword;
    $.ajax({
        type: 'POST',
        dataType: 'text',
        url: "ShareNote",
        data:datatopost,
        async:false,
        success:function(data){
            if((data+"").trim() === "You are not logged in."){
                location.href = "login.jsp";
            }else{
                var notes = JSON.parse((data+"").trim());
                loadNotes(contentpanel,notes);
            }
        }.bind(this)
    });
}
function loadNotes(contentpanel,message) {
    var displaydemo = $(DISPLAY_DEMO);
    $(contentpanel).find(".note-display").remove();
    for(var i=0;i<Math.min(message.length,14);i++){
        var display = $(displaydemo).clone();
        var newa = $("<a href='note.html?id="+message[i].noteId+"'></a>")
        $(display).find(".note-display-author").html(message[i].nickname);
        $(display).find(".note-display-pages").html(message[i].pages);
        $(display).find(".note-display-create-time").html(message[i].releaseTime);
        $(display).find(".note-display-edit-time").html(message[i].lastEditTime);
        $(display).find(".note-display-title").html(message[i].title);
        $(newa).append(display);
        $(contentpanel).append(newa);
    }
}
function loadOwnerMessage(id) {
    var c_code = get_cookie("c_code");
    if(c_code === null){
        location.href = "login.jsp";
    }
    $.ajax({
        type: 'POST',
        dataType: 'text',
        url: "ShareNote",
        data:{"c_code":c_code, "type":"Enter", "id":id},
        async:false,
        success:function(data){
            if((data+"").trim() === "You are not logged in."){
                location.href = "login.jsp";
            }else{
                //{"stu_id":"20164929","nickname":"儿童玩具开发招商引资","headphoto":"http://www.akiyamamio.top:81/pics/mainpage/displays/display4.png","introduction":"从今往后告别前端。"}
                var me = JSON.parse((data+"").trim());
                $("#owner-message-nickname-panel").html(me.nickname);
                $("#sub-nickname-panel").html(me.nickname);
                $($("#owner-message-introduce-panel").find("span")[1]).html(me.introduction);
                $("#owner-message-head-panel").find("div").css({
                    "background-image":"url('"+me.headphoto+"')"
                });
            }
        }.bind(this)
    });
}
function bindPageSwitchButton() {
    $("#search-display-prev-button").click(function () {
        loadNotesFromBackend($("#note-search-display-content"),"keyword",searchkeyword,searchdisplaypage-1);
        searchdisplaypage -= 1;
    });
    $("#search-display-next-button").click(function () {
        loadNotesFromBackend($("#note-search-display-content"),"keyword",searchkeyword,searchdisplaypage+1);
        searchdisplaypage += 1;
    });
    $("#main-display-prev-button").click(function () {
        loadNotesFromBackend($("#note-main-display-content"),maindisplaystrategies,"",maindisplaypage-1);
        maindisplaypage -= 1;
    });
    $("#main-display-next-button").click(function () {
        loadNotesFromBackend($("#note-main-display-content"),maindisplaystrategies,"",maindisplaypage+1);
        maindisplaypage += 1;
    });
    $("#note-search-display-content").find(".main-page-switch").click(function () {
        var page = parseInt($(this).html());
        loadNotesFromBackend($("#note-search-display-content"),"keyword",searchkeyword,page);
        searchdisplaypage = page;
    });
    $("#note-main-display-content").find(".main-page-switch").click(function () {
        var page = parseInt($(this).html());
        loadNotesFromBackend($("#note-main-display-content"),maindisplaystrategies,"",page);
        maindisplaypage = page;
    });
}
function bindStrategiesSwitchButton(){
    $("#latest-note-button").click(function () {
        maindisplaystrategies = "latestPublish";
        loadNotesFromBackend($("#note-main-display-content"),maindisplaystrategies,"",1);
        maindisplaypage = 1;
    });
    $("#high-gpa-note-button").click(function () {
        maindisplaystrategies = "highGPA";
        loadNotesFromBackend($("#note-main-display-content"),maindisplaystrategies,"",1);
        maindisplaypage = 1;
    });
    $("#same-major-note-button").click(function () {
        maindisplaystrategies = "sameMajor";
        loadNotesFromBackend($("#note-main-display-content"),maindisplaystrategies,"",1);
        maindisplaypage = 1;
    });
}