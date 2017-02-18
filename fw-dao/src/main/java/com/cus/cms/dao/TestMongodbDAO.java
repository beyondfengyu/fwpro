package com.cus.cms.dao;

import com.cus.cms.common.model.TestMongodb;

import java.util.Map;

/**
 * @author Andy
 */
public interface TestMongodbDAO {
    Integer addTestMongodb(TestMongodb mongodb);


    TestMongodb findTestMongod(Long id);

    void UpdateTestMongodb(Map<String, Object> map);

    int delTestMongodb(Long id);
}
