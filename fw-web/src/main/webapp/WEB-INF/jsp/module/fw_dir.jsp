<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Content Header (Page header) -->
<section class="content-header">
    <h1 id="itemTitle"></h1>
    <input type="hidden" name="oneDir" id="${oneDir}"/>
    <input type="hidden" name="param" id="param" value=-1/>
</section>
<!-- .content -->
<section class="content">
    <div id="dirTable"></div>
    <div id="toolbar">
        <button id="add" class="btn btn-default">
            <i class="glyphicon glyphicon-plus"></i>增加
        </button>
        <div class="btn-group" id="dirGrp">
            <button type="button" class="btn btn-warning status sts-all" data-val="0">全部</button>
            <button type="button" class="btn btn-default status sts-one" data-val="1">一级栏目</button>
            <button type="button" class="btn btn-default status sts-two" data-val="2">二级栏目</button>
        </div>
    </div>
    <div class="modal fade" id="dirModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="modalLabel">栏目信息</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="dirForm">
                        <input type="hidden" name="id" id="id"/>
                        <input type="hidden" name="lastCode" id="lastCode" value="${oneDir}"/>
                        <div class="form-group">
                            <label for="dirName" class="col-sm-2 control-label">名称:</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control validate[required]" name="dirName" id="dirName">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dirCode" class="col-sm-2 control-label">编码:</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control validate[required]" name="dirCode" id="dirCode">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dirType" class="col-sm-2 control-label">类型:</label>
                            <div class="col-sm-10">
                                <select name="dirType" id="dirType" data-btn-class="btn-warning">
                                    <option value="1">一级栏目</option>
                                    <option value="2">二级栏目</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="showorder" class="col-sm-2 control-label">排序:</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control validate[required]" name="showOrder"
                                       id="showOrder" value="0">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-sm-2 control-label">状态:</label>
                            <div class="col-sm-10">
                                <select name="status" id="status" data-btn-class="btn-warning">
                                    <option value="1">有效</option>
                                    <option value="0">无效</option>
                                </select>
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
    $(function () {
        $('#dirTable').bootstrapTable('destroy');
        $('#dirTable').bootstrapTable({
            columns: [
                {field: 'id', title: 'ID', align: 'center',  width: '10%', visible: false},
                {field: 'dirName', title: '名称', align: 'center', width: '10%'},
                {field: 'dirCode', title: '编码', align: 'center', width: '10%'},
                {field: 'lastCode', title: '父ID', align: 'center', visible: false},
                {field: 'lastName', title: '上级', align: 'center', width: '10%'},
                {field: 'dirType', title: '类型', width: '10%', formatter: function (val, row, index) {
                    if (val == 1) {
                        return '一级栏目';
                    }
                    return '二级栏目';
                }},
                {field: 'showOrder', title: '排序', align: 'center', width: '5%'},
                {
                    field: 'status', title: '状态', align: 'center', width: '5%', formatter: function (val, row, index) {
                    if (val == 1) {
                        return '有效';
                    }
                    return '无效';
                }
                },
                {
                    field: 'id',
                    title: '操作',
                    align: 'center',
                    width: '20%',
                    formatter: function (val, row, index) {
                        return '<button class="btn btn-sm btn-success opt-edit" data-id=' + val + '>' +
                                '<i class="glyphicon glyphicon-edit"></i>编辑</button>' +
                                '&nbsp;&nbsp;<button class="btn btn-sm btn-danger opt-remove" data-id=' + val +
                                '><i class="glyphicon glyphicon-remove"></i>删除</button>';
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
                    dirType: $('#param').val(),
                    oneDir: $('#oneDir').val(),
                    searchText: params.searchText
                };
                return param;
            },
            uniqueId: 'id',
            toolbar: '#toolbar',
            url: '/wen/getFwDirs.action',
            onLoadSuccess: function () {  //加载成功时执行
                console.log("load success");
            },
            onLoadError: function () {  //加载失败时执行
                console.log("load fail");
            }
        });


        $("#dirForm").validationEngine();


        $("#dirTable").on("click", '.opt-remove', function () {
            var id = $(this).attr("data-id");
            if (confirm("你确认删除【" + TableHelper.getRowByUniqueId("#dirTable", id).dirName + "】吗?" +
                            "\r\n删除后再也不能找回，请谨慎操作！")) {
                $.ajax({
                    type: 'post',
                    url: "/wen/delFwDirs.action",
                    data: {'id': id},
                    dataType: "json",
                    success: function (json) {
                        if (json.result == 1) {
                            commonModal.openMessage({body: "删除成功"});
                            TableHelper.doRefresh("#dirTable");
                        } else {
                            commonModal.openWarning({body:"删除失败"}, json.result);
                        }
                    }
                });
            }
        });

        $("#dirTable").on("click", '.opt-edit', function () {
            var id = $(this).attr("data-id");
            isEdit = true;
            $.ajax({
                type: "post",
                url: "/wen/getFwDirById.action",
                data: {'id': id},
                dataType: "json",
                success: function (json) {
                    if (json.entity) {
                        $("#id").val(json.entity.id);
                        $("#dirCode").val(json.entity.dirCode);
                        $("#dirName").val(json.entity.dirName);
                        $("#dirType").val(json.entity.dirType);
                        $("#status").val(json.entity.status);
                        $("#showOrder").val(json.entity.showOrder);

                        $("#dirModal").modal('show');
                    } else {
                        commonModal.openMessage({body:"获取信息出错"});
                    }
                }
            });
        });

        $("#add").click(function () {
            isEdit = false;
            $("#dirForm")[0].reset();
            $("#dirModal").modal('show');
        });

        $('#dirGrp').on('click', '.sts-all',function(){
            switchStatus('.sts-all');
        });

        $('#dirGrp').on('click', '.sts-one', function(){
            switchStatus('.sts-one');
        });

        $('#dirGrp').on('click', '.sts-two', function(){
            switchStatus('.sts-two');
        });

        $("#save").click(function () {
            if ($("#dirForm").validationEngine('validate')) {
                $.ajax({
                    type: "post",
                    url: "/wen/saveFwDir.action?isEdit=" + isEdit,
                    data: $('#dirForm').serialize(),
                    dataType: "json",
                    success: function (json) {
                        if (json.result == 1) {
                            $("#dirModal").modal('hide');
                            commonModal.openMessage({body:"保存成功"});
                            TableHelper.doRefresh("#dirTable");
                        } else {
                            commonModal.openWarning({body:"保存失败"}, json.result);
                        }
                    }
                });
            }
            ;
        });

    });

    function switchStatus(selt){
        isSwitch = true;
        $('#dirGrp button').removeClass('btn-warning').addClass('btn-default');
        $(selt).addClass('btn-warning').removeClass('btn-default');
        $('#param').val($(selt).attr('data-val'));
        TableHelper.doRefresh("#dirTable");
    }
</script>
