<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    request.setAttribute("vEnter", "\n");
%>
<!-- Content Header (Page header) -->
<section class="content-header">
    <h1 id="itemTitle"></h1>
    <input type="hidden" name="oneDir" id="oneDir" value="${oneDir}"/>
    <input type="hidden" name="status" id="statusId" value="1"/>
</section>
<!-- .content -->
<section class="content">
    <div id="pageTable"></div>
    <div id="toolbar">
        <button id="add" class="btn btn-default">
            <i class="glyphicon glyphicon-plus"></i>增加
        </button>
        <div class="btn-group" id="statusGrp">
            <button type="button" class="btn btn-default status sts-allsts" data-val="-1">全部</button>
            <button type="button" class="btn btn-warning status sts-checks" data-val="1">审核</button>
            <button type="button" class="btn btn-default status sts-refuse" data-val="2">拒绝</button>
            <button type="button" class="btn btn-default status sts-succes" data-val="3">通过</button>
            <button type="button" class="btn btn-default status sts-remove" data-val="0">丢弃</button>
        </div>
    </div>
    <div class="modal fade" id="pageModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel">
        <div class="modal-dialog" role="document" style="width:980px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="modalLabel">文章信息</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="pageForm">
                        <input type="hidden" name="id" id="id"/>

                        <div class="form-group">
                            <label for="title" class="col-sm-1 control-label">标题:</label>

                            <div class="col-sm-11">
                                <input type="text" class="form-control validate[required]" name="title" id="title">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="oneDir" class="col-sm-1 control-label">一级栏目:</label>

                            <div class="col-sm-11">
                                <select name="oneDir" id="oneDir2" class="form-control">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="twoDir" class="col-sm-1 control-label">二级栏目:</label>

                            <div class="col-sm-11">
                                <select name="twoDir" id="twoDir" class="form-control">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="source" class="col-sm-1 control-label">源址:</label>

                            <div class="col-sm-11">
                                <a href="" id="source" target="_blank"></a>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="content" class="col-sm-1 control-label">正文:</label>

                            <div class="col-sm-11">
                                <textarea class="from-control" name="content" id="content"
                                          style="width:870px; height:600px;"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="save">保存</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="tipModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">提示信息</h4>
                </div>
                <div class="modal-body" id="tipMsg"></div>
            </div>
        </div>
    </div>
</section>
<!-- .content -->
<script>
    var isEdit = false;
    var isSwitch = false;
    var keditor = null;
    $(function () {
        keditor = KindEditor.create('textarea[name=content]', {
            filterMode: false,//是否开启过滤模式
            newlineTag: 'br',
            uploadJson: '/admin/upload/editorimage.action',//上传路径
            fileManagerJson: '/admin/image/scan.action',
            allowFileManager: true,
            allowImageUpload: true,
            extraFileUploadParams: {//上传文件额外的参数

            }
        });

        $('#pageTable').bootstrapTable('destroy');
        $('#pageTable').bootstrapTable({
            columns: [
                {field: 'id', title: 'ID', align: 'center', width: '8%'},
                {field: 'title', title: '标题', align: 'center', width: '15%'},
                {field: 'oneDir', title: '一级栏目', align: 'center', width: '10%'},
                {field: 'twoDir', title: '二级栏目', align: 'center', width: '10%'},
                {
                    field: 'status', title: '状态', align: 'center', width: '5%', formatter: function (val, row, index) {
                    switch (val) {
                        case 0:
                            return "丢弃";
                        case 1:
                            return "审核";
                        case 2:
                            return "拒绝";
                        case 3:
                            return "通过";
                        case 4:
                            return "生成";
                    }
                }
                },
                {
                    field: 'source', title: '源址', align: 'center', width: '5%', formatter: function (val, row, index) {
                    return '<a href="' + val + '" target="_blank">查看</a>';
                }
                },
                {
                    field: 'id',
                    title: '操作',
                    align: 'center',
                    width: '20%',
                    formatter: function (val, row, index) {
                        return '<button class="btn btn-sm btn-primary opt-edit"  data-id=' + val + '>编辑</button>&nbsp;&nbsp;' +
                                '<button class="btn btn-sm btn-success opt-agree" data-id=' + val + '>通过</button>&nbsp;&nbsp;' +
                                '<button class="btn btn-sm btn-warning opt-refuse" data-id=' + val + '>拒绝</button>&nbsp;&nbsp;' +
                                '<button class="btn btn-sm btn-danger opt-remove" data-id=' + val + '>丢弃</button>';
                    }
                }
            ],
            cache: false,
            striped: true,
            showRefresh: true,
            pageSize: 10,
            pagination: true,
            pageList: [1, 10, 20, 30, 50],
            search: true,
            sidePagination: "server", //表示服务端请求
            //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
            //设置为limit可以获取limit, offset, search, sort, order
            queryParamsType: "undefined",
            queryParams: function queryParams(params) {   //设置查询参数
                if(isSwitch) {
                    params.pageNumber = 1;
                    isSwitch = false;
                }
                var param = {
                    pageNumber: params.pageNumber,
                    pageSize: params.pageSize,
                    status: $('#statusId').val(),
                    oneDir: $('#oneDir').val(),
                    searchText: params.searchText
                };
                return param;
            },
            uniqueId: 'id',
            toolbar: '#toolbar',
            url: '/wen/getFwPages.action',
            onLoadSuccess: function () {  //加载成功时执行
                console.log("load success");
            },
            onLoadError: function () {  //加载失败时执行
                console.log("load fail");
            }
        });


        $("#pageForm").validationEngine();

        $("#pageTable").on("click", '.opt-remove', function () {
            var id = $(this).attr("data-id");
            if (confirm("你确认丢弃【" + TableHelper.getRowByUniqueId("#pageTable", id).title + "】吗?" +
                            "\r\n丢弃后再也不能找回，请谨慎操作！")) {
                optStatus(id, 0, "丢弃");
            }
        });

        $("#pageTable").on("click", '.opt-refuse', function () {
            var id = $(this).attr("data-id");
            optStatus(id, 2, "拒绝");
        });

        $("#pageTable").on("click", '.opt-agree', function () {
            var id = $(this).attr("data-id");
            optStatus(id, 3, "通过");
        });

        $("#pageTable").on("click", '.opt-edit', function () {
            var id = $(this).attr("data-id");
            isEdit = true;
            $.ajax({
                type: "post",
                url: "/wen/getFwPageById.action",
                data: {'id': id},
                dataType: "json",
                success: function (json) {
                    if (json.entity) {
                        $("#id").val(json.entity.id);
                        $("#title").val(json.entity.title);
                        $("#oneDir").val(json.entity.oneDir);
                        $("#twoDir").val(json.entity.twoDir);
                        $("#source").text(json.entity.source);
//                        $("#content").text(json.entity.content);
                        $("#source").attr("href", json.entity.source);

                        $("#pageModal").modal('show');
                    } else {
                        $("#tipMsg").text("获取信息出错");
                        $("#tipModal").modal('show');
                    }
                }
            });
            return false;
        });

        $("#add").click(function () {
            isEdit = false;
            $("#pageForm")[0].reset();
            $("#pageModal").modal('show');
        });


        $('#statusGrp').on('click', '.sts-allsts', function () {
            switchStatus('.sts-allsts');
        });

        $('#statusGrp').on('click', '.sts-editss', function () {
            switchStatus('.sts-editss');
        });

        $('#statusGrp').on('click', '.sts-checks', function () {
            switchStatus('.sts-checks');
        });

        $('#statusGrp').on('click', '.sts-succes', function () {
            switchStatus('.sts-succes');
        });

        $('#statusGrp').on('click', '.sts-refuse', function () {
            switchStatus('.sts-refuse');
        });

        $('#statusGrp').on('click', '.sts-remove', function () {
            switchStatus('.sts-remove');
        });

        $("#save").click(function () {
            if ($("#pageForm").validationEngine('validate')) {
                $.ajax({
                    type: "post",
                    url: "/wen/saveFwDir.action?isEdit=" + isEdit,
                    data: $('#pageForm').serialize(),
                    dataType: "json",
                    success: function (json) {
                        if (json.result == 1) {
                            $("#pageModal").modal('hide');
                            $("#tipMsg").text("保存成功");
                            $("#tipModal").modal('show');
                            TableHelper.doRefresh("#pageTable");
                        } else {
                            $("#tipMsg").text("保存失败，错误码：" + json.result);
                            $("#tipModal").modal('show');
                        }
                    }
                });
            }
            ;
        });

    });

    function switchStatus(selt) {
        isSwitch = true;
        $('#statusGrp button').removeClass('btn-warning').addClass('btn-default');
        $(selt).addClass('btn-warning').removeClass('btn-default');
        $('#statusId').val($(selt).attr('data-val'));
        TableHelper.doRefresh("#pageTable");
    }

    function optStatus(id, status, action) {
        $.ajax({
            type: 'post',
            url: "/wen/optFwPage.action",
            data: {'id': id, 'status': status},
            dataType: "json",
            success: function (json) {
                if (json.result == 1) {
                    $("#tipMsg").text(action + "操作成功");
                    $("#tipModal").modal('show');
                    TableHelper.doRefresh("#pageTable");
                } else {
                    $("#tipMsg").text(action + "操作失败，错误码：" + json.result);
                    $("#tipModal").modal('show');
                }
            }
        });
    }
</script>

