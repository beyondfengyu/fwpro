package com.cus.cms.dao.wen;

import com.cus.cms.common.model.wen.FwDir;
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
public class FwDirDao extends BasicDAO<FwDir, String> {

    @Autowired
    protected FwDirDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public List<FwDir> queryFwDirs(String name, int offset, int size) {
        Query<FwDir> query = createQuery();
        if (!BlankUtil.isBlank(name)) {
            query.field(FwDir.FW_NAME).contains(name);
        }
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public List<FwDir> queryParentFwDirs() {
        Query<FwDir> query = createQuery();
        query.field(FwDir.LAST_CODE).equal("#");
        query.field(FwDir.STATUS).equal(1);
        query.order("-" + FwDir.SHOW_ORDER);
        return query.asList();
    }

    public UpdateResults updateByKey(FwDir fwDir) {
        Query<FwDir> query = createQuery();
        query.field(FwDir.ID).equal(fwDir.getId());
        UpdateOperations<FwDir> updateOp = createUpdateOperations();
        updateOp.set(FwDir.FW_NAME, fwDir.getDirName())
                .set(FwDir.FW_TYPE, fwDir.getDirType())
                .set(FwDir.LAST_CODE, fwDir.getLastCode())
                .set(FwDir.SHOW_ORDER, fwDir.getShoworder())
                .set(FwDir.STATUS, fwDir.getStatus());
        return updateFirst(query, updateOp);
    }

}
