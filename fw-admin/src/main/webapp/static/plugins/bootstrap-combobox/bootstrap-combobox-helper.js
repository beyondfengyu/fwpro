ComboboxHelper = {
    getSelected: function(idstr){
       return $(idstr).btComboBox('value');
    },
    // 设置默认值
    setDef: function(idstr,defval){
        if (defval) {
            $(idstr).btComboBox({'action': "select", 'value': defval});
        }
    },
    // 构建下拉组合框
    build: function(url,idstr,defval){
        var _this = this;
        if(url) {
            $.ajax({
                type: 'post',
                url: url,
                dataType: 'json',
                success: function (json) {
                    $(idstr).empty();
                    $.each(json, function (n, value) {
                        $(idstr).append("<option value='" + value.oval + "'>" + value.otxt + "</option>");
                    });
                    $(idstr).btComboBox();
                    _this.setDef(idstr,defval);
                }
            });
        }else{
            $(idstr).btComboBox();
            _this.setDef(idstr,defval);
        }
    }

}