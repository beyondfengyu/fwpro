package com.cus.cms.service;

import com.cus.cms.common.model.ResultMap;
import com.cus.cms.common.model.TestMongodb;

/**
 * @author Andy
 */
public interface TestRemoteService {

    /**
     * 保存收货人地址
     * @param mongodb
     * @return
     */
    ResultMap saveTestMongodb(TestMongodb mongodb);

    /**
     * 通过Id 查询 对象
     * @param id
     * @return
     * @throws Exception
     */
    TestMongodb findTestMongodbById(Long id) throws Exception;


    /**
     * 更新测试 testMongodb
     * @param mongodb
     * @return
     * @throws Exception
     */
    void updateMongodb(TestMongodb mongodb) throws Exception;

    /**
     * 删除测试testMongodb
     * @param id
     * @return
     * @throws Exception
     */

    ResultMap delMongodb(Long id) throws Exception;

}