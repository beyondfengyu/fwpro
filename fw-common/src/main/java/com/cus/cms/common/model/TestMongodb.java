package com.cus.cms.common.model;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;


/**
 * @author Andy
 */
@Entity(value="TEST_MONGODB",noClassnameStored=true)
public class TestMongodb implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private ObjectId id;

    private Long lid;

    private Long createTime;

    /**
     * @return the lid
     */
    public Long getLid() {
        return lid;
    }
    /**
     * @param lid the lid to set
     */
    public void setLid(Long lid) {
        this.lid = lid;
    }
    private String name;
    @Embedded
    private List<Address> list = Arrays.asList();

    /**
     * @return the id
     */
    public ObjectId getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(ObjectId id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the list
     */
    public List<Address> getList() {
        return list;
    }
    /**
     * @param list the list to set
     */
    public void setList(List<Address> list) {
        this.list = list;
    }
    /**
     * @return the createTime
     */
    public Long getCreateTime() {
        return createTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public TestMongodb() {
        super();
    }
}
