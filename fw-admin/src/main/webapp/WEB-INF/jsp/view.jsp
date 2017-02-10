<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>系统</title>
    <script src="/static/js/jQuery-2.2.0.min.js"></script>
    <script src="/static/js/flexpager/flexpaper.js"></script>
    <script src="/static/js/flexpager/flexpaper_handlers.js"></script>
    <script src="/static/js/flexpager/flexpaper_handlers_debug.js"></script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div style="position:absolute;left:50px;top:10px;">
    <a id="viewerPlaceHolder" style="width:820px;height:650px;display:block"></a>

    <script type="text/javascript">
        var fp = new FlexPaperViewer(
                'FlexPaperViewer',
                'viewerPlaceHolder', { config : {
                    SwfFile : escape('<%=swfFilePath%>'),
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : true,
                    FitWidthOnLoad : false,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,
                    InitViewMode : 'SinglePage',

                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,

                    localeChain: 'en_US'
                }});
    </script>
</div>
</body>
</html>

