package com.cus.cms.dao.wen;

import com.cus.cms.common.model.wen.StaticConfig;
import com.cus.cms.common.util.BlankUtil;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Andy
 */
@Repository
public class StaticConfigDao extends BasicDAO<StaticConfig, Long> {

    @Autowired
    protected StaticConfigDao(@Qualifier("datastore") Datastore ds) {
        super(ds);
    }

    public List<StaticConfig> queryConfigWithPage(String text, int offset, int size) {
        Query<StaticConfig> query = createQuery();
        if (!BlankUtil.isBlank(text)) {
            query.or(query.criteria(StaticConfig.CODE).contains(text),
                    query.criteria(StaticConfig.CKEY).contains(text));
        }
        query.order(StaticConfig.SHOWORDER);
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public long queryConfigWithCount(String text) {
        Query<StaticConfig> query = createQuery();
        if (!BlankUtil.isBlank(text)) {
            query.or(query.criteria(StaticConfig.CODE).contains(text),
                    query.criteria(StaticConfig.CKEY).contains(text));
        }
        return query.count();
    }


    public UpdateResults updateByKey(StaticConfig adminConfig) {
        Query<StaticConfig> query = createQuery();
        query.field(StaticConfig.ID).equal(adminConfig.getId());
        UpdateOperations<StaticConfig> updateOp = createUpdateOperations();
        updateOp.set(StaticConfig.CODE, adminConfig.getCode())
                .set(StaticConfig.CKEY, adminConfig.getCkey())
                .set(StaticConfig.CVAL, adminConfig.getCval())
                .set(StaticConfig.DESC, adminConfig.getDesc())
                .set(StaticConfig.SHOWORDER, adminConfig.getShowOrder());
        return updateFirst(query, updateOp);
    }

    public List<StaticConfig> queryConfigByCode(String code) {
        Query<StaticConfig> query = createQuery();
        query.field(StaticConfig.CODE).equal(code);
        query.order(StaticConfig.SHOWORDER);
        return query.asList();
    }
}
