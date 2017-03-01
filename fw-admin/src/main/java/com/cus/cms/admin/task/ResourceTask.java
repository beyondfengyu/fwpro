//package com.cus.cms.admin.task;
//
//import com.alibaba.fastjson.JSONObject;
//import com.cus.wob.constants.ResType;
//import com.cus.wob.constants.VideoStatus;
//import com.cus.wob.entity.video.ResVideo;
//import com.cus.wob.entity.video.VideoType;
//import com.cus.wob.frame.mvc.ContextUtil;
//import com.cus.wob.util.BlankUtil;
//import com.cus.wob.util.CodeUtil;
//import com.cus.wob.util.FileUtil;
//import com.cus.wob.util.PropertyUtil;
//import com.cus.wob.wrapper.SearchMapper;
//import com.cus.wob.wrapper.VideoMapper;
//import org.apache.log4j.Logger;
//import org.jsoup.Jsoup;
//
//import java.util.*;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * @author laochunyu  2016/2/25.
// * @description
// */
//public class ResourceTask extends BaseTask {
//    private static final Logger m_logger = Logger.getLogger(ResourceTask.class);
//    private static Lock typeLock = new ReentrantLock();
//
//    /**
//     * 生成最新资源数据的json文件，、
//     * 方便静态调用
//     */
//    public static void generNewJson() {
//        try {
//            Map<String, Object> datas = new HashMap<String, Object>();
//            List<SearchMapper> list = getResVideoDao().getSearchVideoMapper(0, " order by create_time desc ", 10);
//            if (!BlankUtil.isBlank(list)) {
//                String info = null;
//                for (SearchMapper entity : list) {
//                    info = Jsoup.parse(entity.getContent()).text().trim();//过滤HTML标签
//                    if (info.length() > 98) {
//                        info = info.substring(0, 98) + "  ...";
//                    }
//                    entity.setContent(info);
//                }
//                datas.put("datas", JSONObject.toJSONString(list));
//                generate(datas, "json.ftl", PropertyUtil.getValByKey("static_path") + "/json/new.json");
//            }
//            //生成最新数据的json文件
//        } catch (Exception e) {
//            m_logger.warn("generNewJson error", e);
//        }
//    }
//
//    /**
//     * 静态化首页页面
//     */
//    public static void generIndexPage() {
//        try {
//            Map<String, Object> datas = new HashMap<String, Object>();
//            List<VideoMapper> newList = getResVideoDao().getVideoWithChecked(0, " order by create_time desc ", 15);
//            datas.put("newList", newList);
//            generateMobileWithBasePath(datas, "mobile/index.ftl", "index.html");
//            List<VideoMapper> hotList = getResVideoDao().getVideoWithChecked(0, " order by scan_time desc ", 10);
//            List<VideoMapper> typeList = null;
//            List<VideoMapper> rankList = null;
//            for (int sign : ResType.types.keySet()) {
//                datas.put("resType" + sign, ResType.types.get(sign).getName());
//                datas.put("typeCode" + sign, ResType.types.get(sign).getScode());
//                rankList = getResVideoDao().getVideoWithChecked(sign, " order by scan_time desc ", 10);
//                typeList = getResVideoDao().getVideoWithChecked(sign, " order by create_time desc ", 10);
//                datas.put("rankList" + sign, rankList);
//                datas.put("typeList" + sign, typeList);
//            }
//            newList = newList.size()>14?newList.subList(0,14):newList;
//            datas.put("newList",newList);
//            datas.put("hotList", hotList);
//            generateWithBasePath(datas, "index.ftl", "index.html");
//
//        } catch (Exception e) {
//            m_logger.error("generIndexPage fail", e);
//        }
//    }
//
//    /**
//     * 静态化每种类型视频的首页
//     */
//    public static void generEveryTypeIndex() {
//        for (int sign : ResType.types.keySet()) {
//            if (sign == 0) {
//                createTypePage(0, "new.ftl", 25, 25);
//            } else {
//                createTypePage(sign, "item.ftl", 25, 20);
//            }
//        }
//    }
//
//    /**
//     * 生成每种类型资源最新收录的列表页面
//     */
//    public static void generEveryTypeNew() {
//        Map<String, Object> datas = new HashMap<String, Object>();
//        List<VideoMapper> newList = null;
//        try {
//            for (int sign : ResType.types.keySet()) {
//                datas.put("resType", ResType.types.get(sign).getScode());
//                newList = getResVideoDao().getVideoWithChecked(sign, " order by create_time desc ", 10);
//                datas.put("newList", newList);
//                String outPath = ContextUtil.getContextPath("template/html/") + "/new_" + ResType.types.get(sign).getScode() + ".html";
//                generate(datas, "type_new.ftl", outPath);
//                generateWithBasePath(datas, "type_new.ftl", "common/new_" + ResType.types.get(sign).getScode() + ".html");
//            }
//        } catch (Exception e) {
//            m_logger.error("generEveryTypeNew error,caouse by " + e.getMessage(), e);
//        }
//        datas = null;
//        newList = null;
//    }
//
//    /**
//     * 生成每种类型排行榜的列表页面
//     */
//    public static void generEveryTypeRank() {
//        Map<String, Object> datas = new HashMap<String, Object>();
//        List<VideoMapper> rankList = null;
//        int size = 10;
//        try {
//            for (int sign : ResType.types.keySet()) {
//                size = sign == 0 ? 35 : 10;
//                rankList = getResVideoDao().getVideoWithChecked(sign, " order by scan_time desc ", size);
//                datas.put("rankList", rankList);
//                String outPath = ContextUtil.getContextPath("template/html/") + "/rank_" + ResType.types.get(sign).getScode() + ".html";
//                generate(datas, "type_rank.ftl", outPath);
//                generateWithBasePath(datas, "type_rank.ftl", "common/rank_" + ResType.types.get(sign).getScode() + ".html");
//            }
//        } catch (Exception e) {
//            m_logger.error("generEveryTypeRank error, cause by " + e.getMessage(), e);
//        }
//        datas = null;
//        rankList = null;
//    }
//
//    /**
//     * 生成每种类型资源推荐的列表页面
//     */
//    public static void generEveryTypeRecommend() {
//        Map<String, Object> datas = new HashMap<String, Object>();
//        List<VideoMapper> recomList = null;
//        try {
//            for (int sign : ResType.types.keySet()) {
//                datas.put("resType", ResType.types.get(sign).getScode());
//                recomList = getRecommendVideoDao().getRecomendVideoMapper(sign, 10);
//                datas.put("recomList", recomList);
//                String outPath = ContextUtil.getContextPath("template/html/") + "/recom_" + ResType.types.get(sign).getScode() + ".html";
//                generate(datas, "type_recom.ftl", outPath);
//                generateWithBasePath(datas, "type_recom.ftl", "common/recom_" + ResType.types.get(sign).getScode() + ".html");
//            }
//        } catch (Exception e) {
//            m_logger.error("generEveryTypeRecommend error,cause by " + e.getMessage(), e);
//        }
//        datas = null;
//        recomList = null;
//    }
//
//    /**
//     * 生成每种类型资源推荐的JSON静态数据
//     */
//    public static void generTypeRecommendJson() {
//        Map<String, Object> datas = new HashMap<String, Object>();
//        List<VideoMapper> recomList = null;
//        try {
//            for (int sign : ResType.types.keySet()) {
////                datas.put("resType", ResType.types.get(sign).getScode());
//                recomList = getRecommendVideoDao().getRecomendVideoMapper(sign, 15);
//                datas.put("datas", JSONObject.toJSONString(recomList));
//                generate(datas, "json.ftl", PropertyUtil.getValByKey("static_path") + "/json/recom_"+sign+".json");
//            }
//        } catch (Exception e) {
//            m_logger.error("generTypeRecommendJson error,cause by " + e.getMessage(), e);
//        }
//        datas = null;
//        recomList = null;
//    }
//    /**
//     * 生成全局推荐的JSON静态数据
//     */
//    public static void generGlobalRecommendJson() {
//        Map<String, Object> datas = new HashMap<String, Object>();
//        List<VideoMapper> recomList = null;
//        try {
//            recomList = getRecommendVideoDao().getRecomendVideoMapper(ResType.GLOBAL_SIGN, 15);
//            if(recomList!=null && recomList.size()>0) {
//                datas.put("datas", JSONObject.toJSONString(recomList));
//                generate(datas, "json.ftl", PropertyUtil.getValByKey("static_path") + "/json/recom_" + ResType.GLOBAL_SIGN + ".json");
//                datas.put("datas",recomList);
//                generateMobileWithBasePath(datas, "mobile/recom.ftl", "recom.html");
//            }
//        } catch (Exception e) {
//            m_logger.error("generGlobalRecommendJson error,cause by " + e.getMessage(), e);
//        }
//        datas = null;
//        recomList = null;
//    }
//
//    /**
//     * 生成详情页
//     * 先生成静态页面，再修改数据表记录的状态，标识为完成
//     */
//    public static void generDetailPage() {
//        try {
//            List<ResVideo> list = getResVideoDao().getVideoWithAgree();
//            if (!BlankUtil.isBlank(list)) {
//                Map<String, Object> datas = new HashMap<String, Object>();
//                String filePath = null;
//                String filePathMobile = null;
//                String staticPath = null;
//                String qcodePath = "";
//                for (ResVideo bean : list) {
//                    try {
//                        if (BlankUtil.isBlank(bean.getResourceLink())) continue;
//                        if (!bean.getResourceLink().contains("网盘")) {
//                            try {
//                                // 构建生成二维码的路径
//                                qcodePath = FileUtil.buildPathByDate("/qcode/", bean.getId() + "_qc.jpg");
//                                filePath = FileUtil.createDirByPath(PropertyUtil.getValByKey("static_path") + qcodePath);
//                                CodeUtil.generIconQRCode(bean.getResourceLink().split(",")[0], 220, 220, 10,
//                                        PropertyUtil.getValByKey("static_path") + "/images/logo.png", filePath);
//                            }catch (Exception e){
//                                qcodePath = null;
//                                m_logger.info("generate qcode fail,sourceLink is: " + bean.getSourceLink());
//                            }
//                        }
//                        m_logger.info("generate qcode success,qcode path is: " + filePath);
//                        //获取生成的静态路径和存储生成文件的路径
//                        if (!BlankUtil.isBlank(bean.getStaticLink())) {
//                            staticPath = bean.getStaticLink();
//                        } else {
//                            staticPath = FileUtil.buildPathByDate("/detail/", bean.getId() + ".html");
//                            FileUtil.buildPathByDate("/mobile/detail/", bean.getId() + ".html");
//                        }
//                        bean.setQrcode(qcodePath);
//                        bean.setStaticLink(staticPath);
//                        filePath = FileUtil.createDirByPath(PropertyUtil.getValByKey("static_path") + staticPath);
//                        filePathMobile = FileUtil.createDirByPath(PropertyUtil.getValByKey("static_mobile_path") + staticPath);
//                        datas.put("resType", ResType.types.get(bean.getType()).getScode());
//                        datas.put("type", bean.getType());
//                        buildResourceLink(datas, bean.getResourceLink());
//                        datas.put("bean", bean);
//                        generate(datas, "detail.ftl", filePath);
//                        generate(datas, "mobile/detail.ftl", filePathMobile);
//                        getResVideoDao().finishResVideoStatic(bean);
//                    } catch (Exception e) {
//                        m_logger.error("generate static detail page fail,id is: " + bean.getId(), e);
//                    }
//                }
//                datas = null;
//            }
//            list = null;
//        } catch (Exception e) {
//            m_logger.error("generDetailPage error, caouse by " + e.getMessage(), e);
//        }
//
//    }
//
//    /**
//     * 获取所有视频类型，并初始到ResType常量类中
//     */
//    public static void initVideoType() {
//        try {
//            List<VideoType> list = getVideoTypeDao().selectAllVideoType();
//            if (!BlankUtil.isBlank(list)) {
//                for (VideoType type : list) {
//                    ResType.types.put(type.getSign(), type);
//                }
//            } else {
//                m_logger.error("initVideoType fail, queryVideoType is null");
//            }
//        } catch (Exception e) {
//            m_logger.error("initVideoType fail", e);
//        }
//    }
//
//    /**
//     * 生成子首页的静态文件
//     *
//     * @param sign     子首页的类型
//     * @param ftlPath  模板文件路径
//     * @param itemSize 分页大小
//     * @param rankSize 排行榜大小
//     */
//    private static void createTypePage(int sign, String ftlPath, int itemSize, int rankSize) {
//        Map<String, Object> datas = new HashMap<String, Object>();
//        datas.put("resType", ResType.types.get(sign).getScode());
//        datas.put("type", sign);
//        datas.put("name",ResType.types.get(sign).getName());
////        List<VideoMapper> rankList = getResVideoDao().getVideoWithChecked(sign," order by scan_time desc ",rankSize);
//        List<VideoMapper> typeList = getResVideoDao().getVideoWithChecked(sign, " order by create_time desc ", itemSize);
//        int total = getResVideoDao().getVideoWithCheckedTotal(sign);
////        datas.put("rankList",rankList);
//        datas.put("typeList", typeList);
//        datas.put("total", total);
//        generateWithBasePath(datas, ftlPath, ResType.types.get(sign).getScode() + "/index.html");
//        typeList = typeList.size()>15?typeList.subList(0,15):typeList;
//        datas.put("typeList", typeList);
//        generateMobileWithBasePath(datas, "mobile/"+ftlPath, ResType.types.get(sign).getScode() + "/index.html");
//    }
//
//    /**
//     * 下载资源的路径，可能有多个，所以要返回一个列表
//     *
//     * @param datas
//     * @param resourceLinks
//     */
//    private static void buildResourceLink(Map<String, Object> datas, String resourceLinks) {
//        if (!BlankUtil.isBlank(resourceLinks)) {
//            List<String> links = Arrays.asList(resourceLinks.split(","));
//            datas.put("links", links);
//        }
//    }
//
//}
