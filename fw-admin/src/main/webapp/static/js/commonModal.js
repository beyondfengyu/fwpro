/**
 *	通用的提示、警告、确定弹框
 *  参数：传入一个对象
 	data{
		title 		// 弹框顶部标题
		body 		// 弹框内容，可以是html代码
		onSuccess 	// 弹框点击‘确定’按钮触发的回调
		onClose		// 弹框点击‘关闭’按钮触发的回调
 	}
 */
$(function(){
	/*提示*/
	window.commonModal = {

		openMessage : function(data) {
			var title =  data.title || '提示',
				body = data.body || '',
				onClose = data.onClose || '',
				_modal = $("#openProp");
			_modal.find('.modal-title').html(title);
			_modal.find('.modal-body').html(body);
			_modal.find('.dismiss').unbind().on('click',function(){
				if (onClose) {
					onClose();
				}
			});
			_modal.modal('show');
		},

		openWarning : function(data, errCode) {
			var title =  data.title || '<span style="color: red">警告</span>',
				body = data.body || '',
				onClose = data.onClose || '',
				_modal = $("#openWarning");
			if (errCode) {
				body += ',<br> errCode: <span style="color: red">' + errCode + '</span>';
			}
			_modal.find('.modal-title').html(title);
			_modal.find('.modal-body').html(body);
			_modal.find('.dismiss').unbind().on('click',function(){
				if (onClose) {
					onClose();
				}
			});
			_modal.modal('show');
		},

		openConfirm : function(data) {
			var title =  data.title || '确认',
				body = data.body || '',
				onSuccess = data.onSuccess || '',
				onClose = data.onClose || '',
				_modal = $("#openConfirm");
			_modal.find('.modal-title').html(title);
			_modal.find('.modal-body').html(body);
			_modal.find('.save').unbind().on('click',function(){
				if (onSuccess) {
					onSuccess();
				}
			});
			_modal.find('.dismiss').unbind().on('click',function(){
				if (onClose) {
					onClose();
				}
			});
			_modal.modal('show');
		}
	}
});