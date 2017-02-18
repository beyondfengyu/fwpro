package com.cus.cms;


import java.util.ArrayList;
import java.util.List;

import com.cus.cms.common.model.Address;
import com.cus.cms.common.model.ResultMap;
import com.cus.cms.common.model.TestMongodb;
import com.cus.cms.service.TestRemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Andy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath*:config/spring.xml"
})
public class TestMongo {


    @Autowired
    private TestRemoteService testRemoteService;

    @Test
    public void testMongoDB(){
        TestMongodb mongodb = new TestMongodb();
        List<Address> addList = new ArrayList<Address>();
        Address address = new Address();


        address.setName("坤坤");
        address.setIphone("18123074271");
        address.setDefAdd(0);
        address.setAddress("成都市天府大道天府新区1");
        addList.add(address);
        mongodb.setLid(103456987L);
        mongodb.setName("坤坤3");
        mongodb.setList(addList);
        ResultMap map =	testRemoteService.saveTestMongodb(mongodb);
        System.out.println(map.getDesc());
    }



    @Test
    public void testFindMongodb() throws Exception{
        TestMongodb mongodb =testRemoteService.findTestMongodbById(103456987L);
        if(mongodb!=null){
            System.out.println(mongodb.getName());
            System.out.println(mongodb.getLid());
        }
    }

    @Test
    public void testUpdateMongodb() throws Exception{
        TestMongodb mongodb = new TestMongodb();
        List<Address> addList = new ArrayList<Address>();
        Address address = new Address();
        address.setName("坤坤1");
        address.setIphone("18123074271");
        address.setDefAdd(0);
        address.setAddress("成都市天府大道天府新区1");
        addList.add(address);
        mongodb.setLid(103456987L);
        mongodb.setName("坤坤2");
        mongodb.setList(addList);
        testRemoteService.updateMongodb(mongodb);
    }

    @Test
    public void testDelMongodb() throws Exception {
        ResultMap map = testRemoteService.delMongodb(103456987l);
        System.out.println(map.getDesc());
    }
}
