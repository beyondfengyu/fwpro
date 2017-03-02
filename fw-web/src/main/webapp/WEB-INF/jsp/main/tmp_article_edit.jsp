<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Main content -->
<section class="content">
    <div class="row">
        <div class="col-md-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">写文章
                        <small></small>
                    </h3>
                    <!-- tools box -->
                    <div class="pull-right box-tools">
                        <button type="button" class="btn btn-warning btn-sm scanbtn">
                            <i class="fa fa-scan"></i>&nbsp;浏览
                        </button>
                        <button type="button" class="btn btn-warning btn-sm savebtn">
                            <i class="fa fa-save"></i>&nbsp;保存
                        </button>
                        <button type="button" class="btn btn-success btn-sm publishbtn">
                            <i class="fa fa-send"></i>&nbsp;发布
                        </button>
                    </div>
                    <!-- /. tools -->
                </div>
                <!-- /.box-header -->
                <div class="box-body pad">
                    <form id="form" role="form">
                        <input id="blgId" name="id" type="hidden" value="${bean.id}"/>
                        <input id="userId" name="userId" type="hidden" value="${bean.userId}"/>
                        <input id="status" name="status" type="hidden" value="${bean.status}"/>
                        <input id="daoCount" name="daoCount" type="hidden" value="${bean.daoCount}"/>
                        <input id="staticLink" name="staticLink" type="hidden" value="${bean.staticLink}"/>
                        <div class="form-group">
                            <label class="control-label" for="title">文章标题</label>
                            <input id="title" name="title" value="${bean.title}" class="form-control" type="text" placeholder="标题">
                        </div>
                        <div class="form-group">
                            <label class="control-label">内容</label>
                            <textarea id="content2" name="content" class="form-control" style="width:100%;height:550px;"
                                      placeholder="在这里编辑">${bean.content}</textarea>
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="tags">标签(最多5个标签，多个标签之间用“，”分隔)</label>
                            <input id="tags" name="tags" value="${bean.tags}" class="form-control" type="text"
                                   placeholder="标签">
                            <div class="well checkbox-group">
                                <style>
                                    .taglist {
                                        margin-right: 5px;
                                        cursor: pointer;
                                    }
                                </style>
                                <c:forEach items="${tags}" var="tag" varStatus="i">
                                    <span class="label label-success taglist">${tag.name}</span>
                                </c:forEach>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="summary">文章摘要(默认提取文章的前200字作为摘要，你也可以在这里自行编辑)</label>
                            <textarea id="summary" name="summary" class="form-control" placeholder="摘要"
                                      value="${bean.summary}" rows="4">${bean.summary}</textarea>
                        </div>
                        <div class="form-group">
                            <label class="control-label">文章发表位置</label>
                            <div class="well checkbox-group navs">
                                <c:forEach items="${navs}" var="nav" varStatus="navStatus">
                                    <c:if test="${nav.id== bean.type}">
                                        <label class="radio-inline">
                                            <input type="radio" name="type" value="${nav.id}" checked="checked">
                                            ${nav.name}
                                        </label>
                                    </c:if>
                                    <c:if test="${nav.id != bean.type}">
                                        <label class="radio-inline">
                                            <input type="radio" name="type" value="${nav.id}">${nav.name}
                                        </label>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="smallImg">小图（列表页）</label>
                            <input id="smallImg" name="smallImg" value="${bean.smallImg}" class="form-control" type="text"
                                   placeholder="小图">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="bigImg">大图（文章头部）</label>
                            <input id="bigImg" name="bigImg" value="${bean.bigImg}" class="form-control" type="text"
                                   placeholder="大图">
                        </div>
                        <div class="well checkbox-group" style="float:left;clear:both;">
                            <div style="float:left;width:720px;">
                                <div>
                                    <input id="syncImg" value="${bean.smallImg}"  type="text" style="width:560px;">
                                    <input type="button" id="syncBtn" value="同步到云">
                                </div>
                                <div style="margin-top:10px;">
                                <input type="file" name="uploadFile" ><br>
                                <input type="button" value="上传" id="uploadBtn">
                                </div>
                            </div>
                            <div style="width:500px;float:left;">
                                <img id="imgshow" src="${bean.smallImg}" width="200"><br><br>
                                <span id="imgurl"></span>
                            </div>
                        </div>
                        <div style="clear:both;"></div>
                        <div class="form-group">
                            <label class="control-label" for="zaiCount">点赞次数</label>
                            <input id="zaiCount" name="zaiCount" value="${bean.zaiCount}" class="form-control" type="text"
                                   placeholder="点赞">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="scanCount">浏览次数</label>
                            <input id="scanCount" name="scanCount" value="${bean.scanCount}" class="form-control" type="text"
                                   placeholder="浏览次数">
                        </div>
                        <div class="form-group">
                            <label class="control-label" for="sourceLink">转载来源(原创文章可以忽略)</label>
                            <input id="sourceLink" name="sourceLink" value="${bean.sourceLink}" class="form-control" type="text"
                                   placeholder="转载的URL">
                        </div>
                        <div class="form-group text-center btn-area">
                            <a class="btn btn-warning scanbtn" role="button"><i class="fa fa-scan"></i>&nbsp;浏览</a>&nbsp;&nbsp;&nbsp;
                            <a class="btn btn-warning savebtn" role="button"><i class="fa fa-save"></i>&nbsp;保存</a>&nbsp;&nbsp;&nbsp;
                            <a class="btn btn-success publishbtn" role="button"><i class="fa fa-send"></i>&nbsp;发布</a>
                            <div id="info" class="alert alert-warning alert-dismissible" role="alert"
                                 style="display:none;">
                                <button type="button" class="close" data-dismiss="alert"><span
                                        aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <span id="msg">发布成功，正在跳转....</span>
                            </div>

                        </div>
                    </form><!-- /#form -->

                </div>
            </div>
        </div>
        <!-- /.col-->
    </div>
    <!-- ./row -->
</section>
<!-- .content -->
<script>
    var keditor = null;
    $(function () {
        keditor = KindEditor.create('textarea[name=content]',{
            filterMode: false,//是否开启过滤模式
            newlineTag:'br',
            uploadJson: '/admin/upload/editorimage.action',//上传路径
            fileManagerJson: '/admin/image/scan.action',
            allowFileManager: true,
            allowImageUpload : true,
            extraFileUploadParams: {//上传文件额外的参数

            }
        });
        keditor.html("<bean id=2>dfdsfd</bean>");
        $('#filemanager').click(function() {
            console.log("file manager click");
            editor.loadPlugin('filemanager', function() {
                editor.plugin.filemanagerDialog({
                    viewType : 'VIEW',
                    dirName : 'image',
                    clickFn : function(url, title) {
                        $('#url').val(url);
                        editor.hideDialog();
                    }
                });
            });
        });

        //发布文章
        $(".publishbtn").click(function () {
            saveArticle(false);
        });
        //保存文章
        $(".savebtn").click(function(){
            saveArticle(true);
        });
        //预览
        $(".scanbtn").click(function(){
            window.open('http://www.wolfbe.com/article/scan.action?source=admin&bid='+$("#blgId").val());
        });

        $("#uploadBtn").click(function(){
            console.log("upload btn click");
            var options = {
                type: 'post',
                url: '/admin/upload/image.action',
                dataType: 'json',
                success: function(json){
                    if(json.path){
                        $('#imgurl').text(json.path);
                        $('#syncImg').val(json.path);
                        $('#imgshow').attr("src",json.path);
                    }
                }
            };
            $("#form").ajaxSubmit(options);
        });

        $("#syncBtn").click(function(){
            console.log("upload btn click");
            var options = {
                type: 'post',
                url: '/admin/sync/image.action',
                data:{url:$('#syncImg').val()},
                dataType: 'json',
                success: function(json){
                    if(json.path){
                        $('#syncImg').val(json.path);
                        $('#imgurl').text(json.path);
                        $('#imgshow').attr("src",json.path);
                    }
                }
            };
            $("#form").ajaxSubmit(options);
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
    });


    function saveArticle(isSave) {
        //同步kindeditor插件的内容到textare
        keditor.sync();
        var url = "/admin/main/saveArticle.action";
        if(!isSave){
            url = "/admin/main/publishArticle.action";
        }
        console.log($("#form").serializeArray());
        $.ajax({
            type: "post",
            url: url,
            data: $("#form").serializeArray(),
            dataType: "json",
            success: function(json){
                if(json.result==1){
                    alert("操作成功！！！！");
                    $.ajax({
                        url:"/html/main/article_admin.html",
                        dataType:"html",
                        success: function(html){
                            $("#content").html(html);
                            // 设置标题
                            if($("#itemTitle")) {
                                $("#itemTitle").text(menuText);
                            }
                        },
                        error: function(data){
                            $("#content").html("<h3>404&nbsp;&nbsp;The page can not found.</h3>"+data);
                        }
                    });
                }else{
                    alert("操作失败，错误码： "+json.result);
                }
            }
        })
    }
</script>

