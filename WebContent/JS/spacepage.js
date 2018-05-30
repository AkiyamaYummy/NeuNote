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

var pageshowing = 1;
var contentsize = 14;
function changePageInFrondend(page) {
    var stapage = Math.max(page-2,1);
    var buttons = $(".main-page-switch");
    for(var i=0;i<buttons.length;i++){
        $(buttons[i]).html(stapage+i);
    }
}
function loadNotesFromBackend(contentpanel,id,page) {
    if(page <= 0)return;
    changePageInFrondend(page)
    pageshowing = page;
    var lo = contentsize*(page-1)+1,hi = contentsize*page;
    var c_code = get_cookie("c_code");
    if(c_code === null){
        location.href = "login.jsp";
    }
    $.ajax({
        type: 'POST',
        dataType: 'text',
        url: "ShareNote",
        data:{"c_code":c_code, "type":"search", "strategies":"user","user":id,"low":lo,"high":hi},
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