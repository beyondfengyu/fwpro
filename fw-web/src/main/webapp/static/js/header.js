$(document).ready(function(){$("#btnSearch").click(function(){var a=$.trim($("input[name=command]").val());if(a){$("#searchForm").submit()}});$(".myfixbar").mouseover(function(){$(this).find(".glyphicon").hide();$(this).find(".bartext").show()});$(".myfixbar").mouseout(function(){$(this).find(".glyphicon").show();$(this).find(".bartext").hide()});$("#gotop").click(function(a){$("body,html").animate({scrollTop:0},300)});goTop()});function goTop(){$(window).scroll(function(a){if($(window).scrollTop()>100){$(".fixbar-gotop").fadeTo(300,1)}else{$(".fixbar-gotop").stop(true,true).fadeTo(10,0.001)}})};