package com.cus.cms.service;

import com.cus.cms.common.model.RedisKey;
import com.cus.cms.common.model.ResultMap;
import com.cus.cms.common.model.TestMongodb;
import com.cus.cms.dao.TestMongodbDAO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Andy
 */


@Service("testRemoteService")
public class TestRemoteServiceImpl implements TestRemoteService {
    private final Logger logger = LoggerFactory.getLogger(TestRemoteServiceImpl.class);

    @Autowired
    private TestMongodbDAO testMongodbDAO;

    @Override
    public ResultMap saveTestMongodb(TestMongodb mongodb) {
        ResultMap resultMap = ResultMap.SUCCESS;
        try {
            if (mongodb != null) {
                Integer a = testMongodbDAO.addTestMongodb(mongodb);
                System.out.println(a);
            }
        } catch (Exception e) {
            resultMap = ResultMap.FAILURE;
            logger.error(e.getMessage(), e);
        }
        return resultMap;
    }

    @Override
    public TestMongodb findTestMongodbById(Long id) {
        TestMongodb testMongodb = null;
        try {
            if (testMongodb == null) {//如果缓存为空
                testMongodb = testMongodbDAO.findTestMongod(id);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return testMongodb;
    }

    @Override
    public void updateMongodb(TestMongodb mongodb) {
        try {

            Map<String, Object> map = Maps.newHashMap();
            String redisKey = String.format(RedisKey.TEST_MONGODB_CACHE.keyformat, mongodb.getLid());
            map.put("lid", mongodb.getLid());
            map.put("name", mongodb.getName());
            map.put("list", mongodb.getList());
            map.put("createTime", mongodb.getCreateTime());
            testMongodbDAO.UpdateTestMongodb(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public ResultMap delMongodb(Long id) {
        ResultMap resultMap = ResultMap.SUCCESS;
        try {
            String redisKey = String.format(RedisKey.TEST_MONGODB_CACHE.keyformat, id);
            Integer n = testMongodbDAO.delTestMongodb(id);
            System.out.println(n);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultMap = ResultMap.FAILURE;
        }
        return resultMap;
    }
}
