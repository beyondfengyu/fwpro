package com.cus.cms.common.constants;

/**
 * @author Andy
 * @date 2016/8/2
 */
public class BlgConstants {

    /*不符合，丢弃*/
    public static final int CHECK_DISCARD = -1;
    /*编辑状态*/
    public static final int CHECK_EDIT = 0;
    /*等待审核*/
    public static final int CHECK_READY = 1;
    /*审核不通过*/
    public static final int CHECK_REFUSE = 2;
    /*审核通过*/
    public static final int CHECK_AGREE = 3;
    /*生成静态*/
    public static final int STATIC_FINISH = 4;

    /*列表表分页的大小*/
    public static final int LIST_PAGE_SIZE = 10;
    /*静态化时列表页默认生成的条数*/
    public static final int LIST_GENER_SIZE = 20;

}
