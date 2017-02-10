var editor;//kindeditor插件对象
KindEditor.ready(function(K) {
    //更新模式下初始化已选择的分类
    // initCheckedCls();
    //初始化kindeditor插件
    editor = initKindeditor(K,'content2');
    //分类选择事件
    $("input[name=clsId]").click(function(){
        var all = [];
        $("input[name=clsId]:checked").each(function () {
            all.push($.trim($(this).parent().text()));
        });
        $("#category").val(all.join(','));
    });
    //标签点击事件
    $(".taglist").click(function(){
        var $tag = $("#tags");
        var vale = $.trim($tag.val());
        if(vale==''){
            $tag.val($(this).text());
        }else{
            var arr = vale.split(',');
            //限制标签为5个之内
            if(arr.length<5){
                var v = ','+vale+',';
                //判断点击的标签文本是否存在于#tag表单中
                if(v.indexOf(','+$(this).text()+',')<0){
                    $tag.val(vale+','+$(this).text());
                }
            }
        }
    });
    //发布按钮点击事件
    $("#publish").click(function(){
        $("#publishState").val("1");
        publish();
    });
    //保存按钮点击事件
    $("#save").click(function(){
        $("#publishState").val("0");
        publish();
    });
    //重置按钮事件
    $("#reset").click(function(){
        $("#form")[0].reset();
        editor.html('');
    });
});
//发表博客
function publish(){
    //同步kindeditor插件的内容到textare
    editor.sync();
    var url = contextPath + "web/blog/publish";
    //如果blogId值存在，代表处于更新模式
    if($("#blogId").val()){
        url = contextPath + "web/blog/update";
    }
    //提交
    $.ajax({
        url: url,
        data: $("#form").serializeArray(),
        type: "POST",
        dataType:"json",
        success: function(json){
            if(BooleanHelper.isTrue(json.success)){
                $("#info").show().removeClass("alert-danger").addClass("alert-warning");
                var results = splitMsg(json.msg);
                //显示提示信息
                $("#msg").text(results[0]);
                //跳转到成功页面
                location.replace(contextPath+"view/front/blog/result.jsp?id="+results[1]);
            }else{
                $("#info").show().removeClass("alert-warning").addClass("alert-danger");
                $("#msg").text(json.msg);
            }
        },
        error: function(data){
            $("#info").show().removeClass("alert-warning").addClass("alert-danger");
            $("#msg").text("系统异常，请稍后重试");
            console.log(data);
        }
    });
};
//初始化kindeditor编辑器
function initKindeditor(K ,editorId){
    console.log("editorId : "+editorId);
    editor =  K.create('#'+editorId,{
        resizeType: 1,//只能改变高度
        // uploadJson: contextPath + 'web/blog/image/upload',//上传路径
        // fileManagerJson: contextPath + 'web/blog/image/scan',
        // allowFileManager: true,
        extraFileUploadParams: {//上传文件额外的参数

        }
    });
    K('#filemanager').click(function() {
        editor.loadPlugin('filemanager', function() {
            editor.plugin.filemanagerDialog({
                viewType : 'VIEW',
                dirName : 'image',
                clickFn : function(url, title) {
                    K('#url').val(url);
                    editor.hideDialog();
                }
            });
        });
    });
    return editor;
}
//初始化已经选择的分类
function initCheckedCls(){
    if($("#blogId").val()){
        var clsIds = $("#checkedClsIds").val();
        if(clsIds){
            var arr = clsIds.split(',');
            $.each(arr,function(n,id){
                $("#cb"+id).attr('checked',true);
            });
        }
    }
};