package com.cus.cms.dao.wen;

import com.cus.cms.common.model.wen.FwContent;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


/**
 * @author Andy
 */
@Repository
public class FwContentDao extends BasicDAO<FwContent, Long> {

    @Autowired
    protected FwContentDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public UpdateResults updateByKey(FwContent fwPage) {
        Query<FwContent> query = createQuery();
        query.field(FwContent.ID).equal(fwPage.getId());
        UpdateOperations<FwContent> updateOp = createUpdateOperations();
        updateOp.set(FwContent.CONTENT, fwPage.getContent());
        return updateFirst(query, updateOp);
    }

}
