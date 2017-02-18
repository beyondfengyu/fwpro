package com.cus.cms.dao;

import com.cus.cms.common.model.TestMongodb;
import com.cus.cms.common.model.TestMongodbMapper;
import com.google.common.base.Preconditions;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * @author Andy
 */
@Repository
public class TestMongodbDAOImpl extends BasicDAO<TestMongodb, String> implements TestMongodbDAO {

    @Autowired
    public TestMongodbDAOImpl(@Qualifier("datastore") Datastore ds) {//注入数据源
        super(ds);
    }

    @Override
    public Integer addTestMongodb(TestMongodb mongodb) {
        Query<TestMongodb> query = createQuery().field(TestMongodbMapper.LOOKER_ID).equal(mongodb.getLid());
        query.filter(TestMongodbMapper.LIST, mongodb.getList());
        UpdateOperations<TestMongodb> option = createUpdateOperations().set(TestMongodbMapper.LOOKER_NAME, mongodb.getName());
        UpdateResults u = getDatastore().update(query, option, true);
        if (u.getInsertedCount() > 0) {
            return u.getInsertedCount();
        }
        return 0;
    }

    @Override
    public TestMongodb findTestMongod(Long id) {
        Preconditions.checkNotNull(id);
        Query<TestMongodb> query = createQuery().field(TestMongodbMapper.LOOKER_ID).equal(id);
        TestMongodb mongodb = findOne(query);
        return mongodb;
    }

    @Override
    public void UpdateTestMongodb(Map<String, Object> map) {
        DBCollection collection = getDatastore().getDB().getCollection("TEST_MONGODB");
        BasicDBObject queryObj = new BasicDBObject();
        queryObj.put(TestMongodbMapper.LOOKER_ID, map.get("lid"));
        BasicDBObject filedObj = new BasicDBObject(map);
        BasicDBObject update = new BasicDBObject();
        update.put("$set", filedObj);
        //第一个true 表示 是否upsert=true， 找到这一行就 修改，没有就追加 为false 没有就不追加 第二false当找到多个只更新一个。
        collection.update(queryObj, update, true, false, WriteConcern.ACKNOWLEDGED);
    }

    @Override
    public int delTestMongodb(Long id) {
        Query<TestMongodb> query = createQuery().field(TestMongodbMapper.LOOKER_ID).equal(id);
        WriteResult wr = deleteByQuery(query);
        int n = wr.getN();
        if (n > 0) {
            return n;
        }
        return 0;
    }
}
