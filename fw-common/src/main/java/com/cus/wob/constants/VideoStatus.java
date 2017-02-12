package com.cus.wob.constants;

/**
 * @author Andy  2016/2/26.
 * @description
 */
public class VideoStatus {
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
}
