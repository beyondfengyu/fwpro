package com.cus.cms.common.mongo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * @author Andy
 */
public class DataStoreFactoryBean extends AbstractFactoryBean<Datastore> {

    private Morphia morphia; //morphia实例，最好是单例
    private MongoClient mongo; //mongo实例，最好是单例
    private String dbName; //数据库名
    private boolean toEnsureIndexes = false; //是否确认索引存在，默认false
    private boolean toEnsureCaps = false; //是否确认caps存在，默认false

    @Override
    protected Datastore createInstance() throws Exception {
        Datastore ds = morphia.createDatastore(mongo, dbName);
        if (toEnsureIndexes) {
            ds.ensureIndexes();
        }
        if (toEnsureCaps) {
            ds.ensureCaps();
        }
        return ds;
    }

    @Override
    public Class<?> getObjectType() {
        return Datastore.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (mongo == null) {
            throw new IllegalStateException("mongo is not set");
        }
        if (morphia == null) {
            throw new IllegalStateException("morphia is not set");
        }
    }

    /*----------------------setters-----------------------*/
    public Morphia getMorphia() {
        return morphia;
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public MongoClient getMongo() {
        return mongo;
    }

    public void setMongo(MongoClient mongo) {
        this.mongo = mongo;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isToEnsureIndexes() {
        return toEnsureIndexes;
    }

    public void setToEnsureIndexes(boolean toEnsureIndexes) {
        this.toEnsureIndexes = toEnsureIndexes;
    }

    public boolean isToEnsureCaps() {
        return toEnsureCaps;
    }

    public void setToEnsureCaps(boolean toEnsureCaps) {
        this.toEnsureCaps = toEnsureCaps;
    }
}
