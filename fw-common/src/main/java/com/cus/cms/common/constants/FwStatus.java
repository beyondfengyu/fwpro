package com.cus.cms.common.constants;

/**
 * @author Andy  2016/2/26.
 * @description
 */
public class FwStatus {
    /*不符合，丢弃*/
    public static final int CHECK_DISCARD = 0;
    /*编辑状态*/
    public static final int CHECK_EDIT    = 1;
    /*等待审核*/
    public static final int CHECK_READY   = 2;
    /*审核不通过*/
    public static final int CHECK_REFUSE  = 3;
    /*审核通过*/
    public static final int CHECK_AGREE   = 4;
    /*生成静态*/
    public static final int STATIC_FINISH = 5;
}
