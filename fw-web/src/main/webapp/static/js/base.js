$(document).ready(function(){$(".its").hover(function(){var a=$(this).height();$(".htxt",this).stop().animate({height:a+"px"})},function(){$(".htxt",this).stop().animate({height:"30px"})});$(".its2").hover(function(){var a=$(this).height();$(".htxt2",this).stop().animate({height:a+"px",})},function(){$(".htxt2",this).stop().animate({height:"30px"})});$("#exbtn").click(function(a){console.log("exbtn...");$(".nav-area").toggle();a.stopPropagation()});$(".frg .is").mouseover(function(){$(this).children().hide();$(this).css("{line-height:16px;font-size:16px;background: #007ecc;}")});$(".frg .is").mouseout(function(){$(this).children().show()});$(".ic-top").click(function(a){$("body,html").animate({scrollTop:0},300)});$(".ic-cd").mouseover(function(a){$(".qrcode").show()});$(".ic-cd").mouseout(function(a){$(".qrcode").hide()});$("#moreBtn").click(function(){$(this).hide();$(".loading").show();$.ajax({type:"post",dataType:"json",url:"/art/loadmore.action",data:{type:$("#type").val(),page:$("#page").val()},success:function(b){if(b.list){var c=parseInt($("#page").val())+1;$("#page").val(c);for(var a=0;a<b.list.length;a++){var e=b.list[a];$(".lst").append('<div class="container its">\n                        <div class="row">\n                            <div class="col-md-3 col-sm-4 col-xs-4 limg">\n                                <div class="row">\n                                    <a href="'+e.staticLink+'" target="_blank">\n                                        <img class="lazy" src="'+e.smallImg+'">\n                                    </a>\n                                </div>\n                            </div>\n                            <div class="col-md-9 col-sm-8 col-xs-8 rmn">\n                                <div class="bt">\n                                    <a href="'+e.staticLink+'" target="_blank">\n'+e.title+'                                    </a>\n                                </div>\n                                <div class="fl">\n                                <span>分类:\n                                    <a href="'+e.typeLink+'" class="bg-classify">'+e.typeStr+'</a>\n                                </span>\n                                    <span>阅读:<a class="bg-classify">'+e.scanCount+'</a>次</span>\n                                    <span>发表于:<a class="bg-classify">'+e.createTime+'</a></span>\n                                    <span>作者:<a href="#" class="bg-classify">'+e.userStr+'</a></span>\n                                </div>\n                                <div class="gy">'+e.summary+"</div>\n                            </div><!-- .rmn-->\n                        </div>\n                    </div>")}if(b.list.length>9){$("#moreBtn").show()}}$(".loading").hide()},error:function(a){$("#moreBtn").show();$(".loading").hide()}})})});document.body.addEventListener("touchstart",function(){});function goTop(){$(window).scroll(function(a){if($(window).scrollTop()>100){$(".fixbar-gotop").fadeTo(300,1)}else{$(".fixbar-gotop").stop(true,true).fadeTo(10,0.001)}})}var _hmt=_hmt||[];(function(){var b=document.createElement("script");b.src="//hm.baidu.com/hm.js?6d98d3646c0bd7523f8bd2e8f48615a4";var a=document.getElementsByTagName("script")[0];a.parentNode.insertBefore(b,a)})();