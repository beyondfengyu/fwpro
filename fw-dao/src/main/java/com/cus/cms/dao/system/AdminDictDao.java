package com.cus.cms.dao.system;

import com.cus.cms.common.model.system.AdminDict;
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
public class AdminDictDao extends BasicDAO<AdminDict, Long> {

    @Autowired
    protected AdminDictDao(@Qualifier("datastore") Datastore ds) {
        super(ds);
    }

    public List<AdminDict> queryDictWithPage(String text, int offset, int size) {
        Query<AdminDict> query = createQuery();
        if (!BlankUtil.isBlank(text)) {
            query.or(query.criteria(AdminDict.CODE).contains(text),
                    query.criteria(AdminDict.CKEY).contains(text));
        }
        query.order(AdminDict.SHOWORDER);
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public long queryDictWithCount(String text) {
        Query<AdminDict> query = createQuery();
        if (!BlankUtil.isBlank(text)) {
            query.or(query.criteria(AdminDict.CODE).contains(text),
                    query.criteria(AdminDict.CKEY).contains(text));
        }
        return query.count();
    }


    public UpdateResults updateByKey(AdminDict adminDict) {
        Query<AdminDict> query = createQuery();
        query.field(AdminDict.ID).equal(adminDict.getId());
        UpdateOperations<AdminDict> updateOp = createUpdateOperations();
        updateOp.set(AdminDict.CODE, adminDict.getCode())
                .set(AdminDict.CKEY, adminDict.getCkey())
                .set(AdminDict.CVAL, adminDict.getCval())
                .set(AdminDict.DESC, adminDict.getDesc())
                .set(AdminDict.SHOWORDER, adminDict.getShowOrder());
        return updateFirst(query, updateOp);
    }

    public List<AdminDict> queryDictByCode(String code) {
        Query<AdminDict> query = createQuery();
        query.field(AdminDict.CODE).equal(code);
        query.order(AdminDict.SHOWORDER);
        return query.asList();
    }
}
