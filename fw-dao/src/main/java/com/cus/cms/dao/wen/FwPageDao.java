package com.cus.cms.dao.wen;

import com.cus.cms.common.model.wen.FwPage;
import com.cus.cms.common.util.BlankUtil;
import org.bson.types.ObjectId;
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
public class FwPageDao extends BasicDAO<FwPage, Long> {

    @Autowired
    protected FwPageDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public List<FwPage> queryFwPages(String title, int status, int offset, int size) {
        Query<FwPage> query = createQuery();
        if (!BlankUtil.isBlank(title)) {
            query.field(FwPage.TITLE).contains(title);
        }
        if (status > -1) {
            query.field(FwPage.STATUS).equal(status);
        }
        query.order("-id");
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public long queryFwPageCount(String title, int status) {
        Query<FwPage> query = createQuery();
        if (!BlankUtil.isBlank(title)) {
            query.field(FwPage.TITLE).contains(title);
        }
        if (status > -1) {
            query.field(FwPage.STATUS).equal(status);
        }
        return count(query);
    }

    public UpdateResults updateByKey(FwPage fwPage) {
        Query<FwPage> query = createQuery();
        query.field(FwPage.ID).equal(fwPage.getId());
        UpdateOperations<FwPage> updateOp = createUpdateOperations();
        updateOp.set(FwPage.TITLE, fwPage.getTitle())
                .set(FwPage.CONTENT, fwPage.getContent())
                .set(FwPage.ONE_DIR, fwPage.getOneDir())
                .set(FwPage.TWO_DIR, fwPage.getTwoDir())
                .set(FwPage.STATUS, fwPage.getStatus());
        return updateFirst(query, updateOp);
    }

    public UpdateResults updateStatusByKey(long id, int status) {
        Query<FwPage> query = createQuery();
        query.field(FwPage.ID).equal(id);
        UpdateOperations<FwPage> updateOp = createUpdateOperations();
        updateOp.set(FwPage.STATUS, status);
        return updateFirst(query, updateOp);
    }

    public List<FwPage> queryFwPages(String title, String oneDir, int offset, int size) {
        Query<FwPage> query = createQuery();
        if (!BlankUtil.isBlank(title)) {
            query.field(FwPage.TITLE).contains(title);
        }
        query.field(FwPage.ONE_DIR).equal(oneDir).order("-id");
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public long queryFwPageCount(String title, String oneDir) {
        Query<FwPage> query = createQuery();
        query.field(FwPage.TITLE).equal(title)
                .field(FwPage.ONE_DIR).equal(oneDir);
        return count(query);
    }
}
