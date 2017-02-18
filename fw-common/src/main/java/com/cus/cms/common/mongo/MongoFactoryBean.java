package com.cus.cms.common.mongo;

import com.mongodb.*;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Andy
 */
public class MongoFactoryBean extends AbstractFactoryBean<Mongo> {

    // 表示服务器列表(主从复制或者分片)的字符串数组
    private String[] serverStrings;
    // 表示认证参数
    private String[] credentialStrings;
    // mongoDB配置对象
    private MongoClientOption mongoOption;
    // 是否主从分离(读取从库)，默认读写都在主库
    private boolean readSecondary = false;
    // 设定写策略(出错时是否抛异常)，默认采用SAFE模式(需要抛异常)
    @SuppressWarnings("deprecation")
    private WriteConcern writeConcern = WriteConcern.SAFE;

    @Override
    public Class<?> getObjectType() {
        return Mongo.class;
    }

    @Override
    protected Mongo createInstance() throws Exception {
        Mongo mongo = initMongo();

        // 设定主从分离
        if (readSecondary) {
            mongo.setReadPreference(ReadPreference.secondaryPreferred());
        }

        // 设定写策略
        mongo.setWriteConcern(writeConcern);
        return mongo;
    }

    /**
     * 初始化mongo实例
     *
     * @return
     * @throws Exception
     */
    private MongoClient initMongo() throws Exception {
        // 根据条件创建Mongo实例
        MongoClient mongo = null;
        List<ServerAddress> serverList = getServerList();
        List<MongoCredential> credentialsList = getCredentialsList();
        MongoClientOptions mongoOptions = mongoOption.getMongoClientOptions();
        if (serverList.size() == 0) {
            mongo = new MongoClient();
        } else if (serverList.size() == 1) {
            if (null != mongoOptions) {
                if (credentialsList.size() == 0) {
                    mongo = new MongoClient(serverList.get(0), mongoOptions);
                } else {
                    mongo = new MongoClient(serverList.get(0), credentialsList, mongoOptions);
                }
            } else {
                if (credentialsList.size() == 0) {
                    mongo = new MongoClient(serverList.get(0));
                } else {
                    mongo = new MongoClient(serverList.get(0), credentialsList);
                }
            }
        } else {
            if (null != mongoOptions) {
                if (credentialsList.size() == 0) {
                    mongo = new MongoClient(serverList, mongoOptions);
                } else {
                    mongo = new MongoClient(serverList, credentialsList, mongoOptions);
                }
            } else {
                if (credentialsList.size() == 0) {
                    mongo = new MongoClient(serverList);
                } else {
                    mongo = new MongoClient(serverList, credentialsList);
                }
            }
        }
        return mongo;
    }

    /**
     * 根据服务器字符串列表，解析出服务器对象列表
     * <p/>
     *
     * @return
     * @throws Exception
     * @Title: getServerList
     * </p>
     */
    private List<ServerAddress> getServerList() throws Exception {
        List<ServerAddress> serverList = new ArrayList<ServerAddress>();
        try {
            for (String serverString : serverStrings) {
                String[] temp = serverString.split(":");
                String host = temp[0];
                if (temp.length > 2) {
                    throw new IllegalArgumentException(
                            "Invalid server address string: " + serverString);
                }
                if (temp.length == 2) {
                    serverList.add(new ServerAddress(host, Integer
                            .parseInt(temp[1])));
                } else {
                    serverList.add(new ServerAddress(host));
                }
            }
            return serverList;
        } catch (Exception e) {
            throw new Exception(
                    "Error while converting serverString to ServerAddressList",
                    e);
        }
    }

    /**
     * 根据服务器认证字符串列表，解析出服务器认证对象列表
     * <p/>
     *
     * @return
     * @throws Exception
     * @Title: getCredentialList
     * </p>
     */
    private List<MongoCredential> getCredentialsList() throws Exception {
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        try {
            if (null != credentialStrings) {
                for (String credentialString : credentialStrings) {
                    String[] temp = credentialString.split(":");
                    String database = temp[0];
                    if (temp.length > 3) {
                        throw new IllegalArgumentException(
                                "Invalid credential param string: " + credentialString);
                    }
                    if (temp.length == 3) {
                        MongoCredential credential = MongoCredential.createCredential(temp[1], database, temp[2].toCharArray());
                        credentialsList.add(credential);
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid credential param string: " + credentialString);
                    }
                }
            }
            return credentialsList;
        } catch (Exception e) {
            throw new Exception(
                    "Error while converting credentialString to MongoCredentialsList",
                    e);
        }
    }

    public String[] getServerStrings() {
        return serverStrings;
    }

    public void setServerStrings(String[] serverStrings) {
        this.serverStrings = serverStrings;
    }

    public String[] getCredentialStrings() {
        return credentialStrings;
    }

    public void setCredentialStrings(String[] credentialStrings) {
        this.credentialStrings = credentialStrings;
    }


    public boolean isReadSecondary() {
        return readSecondary;
    }

    public MongoClientOption getMongoOption() {
        return mongoOption;
    }

    public void setMongoOption(MongoClientOption mongoOption) {
        this.mongoOption = mongoOption;
    }

    public void setReadSecondary(boolean readSecondary) {
        this.readSecondary = readSecondary;
    }

    public WriteConcern getWriteConcern() {
        return writeConcern;
    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

     /* ------------------- setters --------------------- */
}
