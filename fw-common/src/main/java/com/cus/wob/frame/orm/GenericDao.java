package com.cus.wob.frame.orm;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 *
 * @author laochunyu   2015-11-12
 * @description 提取DAO层的通用方法，通过反射的方式注入实体类
 *
 */
public class GenericDao<T> {
    protected final Logger m_logger = LoggerFactory.getLogger(getClass());
    private Class<T> entityClass;

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取实体类对象
     * @return
     */
    @SuppressWarnings("unchecked")
	public Class<T> getEntityClass(){
        if(entityClass==null) {
            Type type = getClass().getGenericSuperclass();
            entityClass =  (Class<T>)((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return entityClass;
    }

    /**
     * 构建默认的Statement路径
     * @param ddlType
     * @return
     */
    public String getStatment(DDLType ddlType){
        String statment = getEntityClass().getName();
        return statment +"." +ddlType.name();
    }

    public int insert(T t){
        return insert(getStatment(DDLType.insert),t);
    }

    public int insert(String statement,T t){
//        m_logger.info("statment is "+getStatment( DDLType.insert ));
        return sqlSessionTemplate.insert(statement,t);
    }

    public int update(T t){
        return update(getStatment(DDLType.update), t);
    }

    public int update(String statment, T t){
        return sqlSessionTemplate.update(statment,t);
    }

    public int delete(T t){
        return delete(getStatment(DDLType.delete), t);
    }

    public int delete(String statment,T t){
        return sqlSessionTemplate.delete(statment,t);
    }

    public int deleteWithParam(String statment,Object param){
        return sqlSessionTemplate.delete(statment,param);
    }
    public List<T> select(Object object){
        return select(getStatment(DDLType.select),object);
    }

    public List<T> select(String statment,Object object){
        return sqlSessionTemplate.selectList(statment,object);
    }

    public T selectOne(String statment,Object object){
       return sqlSessionTemplate.selectOne(statment,object);
    }
}

