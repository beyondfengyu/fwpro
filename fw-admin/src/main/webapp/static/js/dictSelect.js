/**
 *  id : select元素的标识，如： #sele, .sele2
 *  code: 字典中的组
 *  hasNull: 是否允许出现空的选择项，默认为true
 *  url: 后端请求的url，默认为/auth/dict/getDictByCode.action
 */
$(function(){
	/*提示*/
	window.dictSelect = {
        select: function(id, code, hasNull, url) {
            var hasNull2 = hasNull || false;
            var url2 = url || "/admin/getDictByCode.action";

            $.ajax({
                url: url2,
                data: {'code': code, 'hasNull': hasNull2},
                dataType: "json",
                success: function (data) {
                    $(id).select2({data: data.items});
                }
            });
            return $(id);
        }
    }
});