<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/html/meta.html">

    <title></title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <!-- Custom styles for this template -->
    <link rel ="stylesheet" href="http://static.wolfbe.com/css/nav.css">
    <link rel ="stylesheet" href="http://static.wolfbe.com/css/index.css">

</head>

<body>
<!-- include nav code -->
<#include "/html/nav.html">
<div class="wrap w1024">
    <div class="main ad_l100">

    </div>
    <div class="main">
        <div class="item-title">
            <h2>近期收录</h2>
            <#--<a class="more" href="http://v.wolfbe.com/html/new/index.html">更多</a>-->
        </div>
        <div class="item-body ">
            <ul>
            <#if newList??>
                <#list newList as bean>
                <#if bean_index%7==0>
                <li class="left">
                <#elseif bean_index%7==6>
                <li class="right">
                <#else>
                <li>
                </#if>
                    <a href="${bean.url}" title="${bean.name}"  target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                    <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                </li>
                </#list>
            </#if>
            </ul>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType1}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode1}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList1??>
                <#list typeList1 as bean>
                    <#if bean_index%5==0>
                    <li class="left">
                    <#elseif bean_index==4>
                    <li class="right">
                    <#else>
                    <li>
                    </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList1??>
                    <#list rankList1 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <a href="#"><img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 /></a>
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType2}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode2}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList2??>
                    <#list typeList2 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList2??>
                    <#list rankList2 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType3}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode3}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList3??>
                    <#list typeList3 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList3??>
                    <#list rankList3 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType4}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode4}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList4??>
                    <#list typeList4 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList4??>
                    <#list rankList4 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType5}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode5}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList5??>
                    <#list typeList5 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList5??>
                    <#list rankList5 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType6}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode6}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList6??>
                    <#list typeList6 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList6??>
                    <#list rankList6 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType7}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode7}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList7??>
                    <#list typeList7 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList7??>
                    <#list rankList7 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType8}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode8}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList8??>
                    <#list typeList8 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList8??>
                    <#list rankList8 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType9}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode9}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList9??>
                    <#list typeList9 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList9??>
                    <#list rankList9 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main l">
        <div class="wrap-l w745">
            <div class="item-title">
                <h2>${resType10}片</h2>
                <a class="more" href="http://v.wolfbe.com/html/${typeCode10}/index.html">更多</a>
            </div>
            <div class="item-body  w745">
                <ul class="w745">
                <#if typeList10??>
                    <#list typeList10 as bean>
                        <#if bean_index%5==0>
                        <li class="left">
                        <#elseif bean_index==4>
                        <li class="right">
                        <#else>
                        <li>
                        </#if>
                        <a href="${bean.url}" title="${bean.name}" target="_blank"><img class="lazy" src="http://static.wolfbe.com/images/grey.gif" data-original="${bean.img}"  width=120 height=165 /></a>
                        <a class="title" href="${bean.url}" title="${bean.name}" target="_blank">${bean.name}</a>
                    </li>
                    </#list>
                </#if>
                </ul>
            </div>
        </div>
        <div class="wrap-r w279">
            <div class="item-title w279">
                <h2>排行榜</h2>
            </div>
            <div class="rank-body  w279">
                <ul class="top_list w279">
                <#if rankList10??>
                    <#list rankList10 as bean>
                        <li class="item-rank rank">
                            <a href="${bean.url}" title="${bean.name}" target="_blank"><i class="num num_${bean_index+1}" >${bean_index+1}</i><span class="inner">${bean.name}</span></a>
                        </li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="rank-ad">
                <img src="http://static.wolfbe.com/images/ad1.jpg" width=279 height=80 />
            </div>
        </div>
    </div><!-- /.main -->
    <div class="main ad_l101">

    </div>
</div><!-- /.wrap -->
<div class="ad_a100"></div><#--全站广告位-->
<#--<div class="ad_a200"></div>-->

<!--include footer code-->
<#include "/html/footer.html">


<!-- Bootstrap core JavaScript -->
<script src="http://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="http://static.wolfbe.com/js/jquery/jquery.lazyload.min.js"></script>
<script src="http://static.wolfbe.com/js/video/statistics.js"></script>
<script>
    statistic.send(1,1,1000);
    $("img.lazy").lazyload({threshold : 100,event : "sporty"});
    $(window).bind("load", function() {
        var timeout = setTimeout(function() {$("img.lazy").trigger("sporty")}, 1200);
    });
</script>
<script src="http://static.wolfbe.com/js/video/v_ad.js"></script>
<script src="http://static.wolfbe.com/js/video/search.js"></script>
</body>
</html>
