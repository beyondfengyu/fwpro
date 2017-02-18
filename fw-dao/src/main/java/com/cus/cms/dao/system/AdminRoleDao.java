package com.cus.cms.dao.system;

import com.cus.cms.common.model.AdminRole;
import com.cus.cms.common.util.BlankUtil;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.util.List;

/**
 * @author Andy
 */
public class AdminRoleDao extends BasicDAO<AdminRole, Long> {

    protected AdminRoleDao(Datastore ds) {
        super(ds);
    }

    public UpdateResults updateByKey(AdminRole adminRole) {
        Query<AdminRole> query = createQuery();
        query.field("id").equals(adminRole.getId());
        UpdateOperations<AdminRole> updateOp = createUpdateOperations();
        updateOp.set("name", adminRole.getName())
                .set("description", adminRole.getDescription());
        return updateFirst(query, updateOp);
    }

    public List<AdminRole> queryRoleWithPage(String name, int offset, int size) {
        Query<AdminRole> query = createQuery();
        if (!BlankUtil.isBlank(name)) {
            query.field("name").contains(name);
        }
        query.order("-id");
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public AdminRole queryRoleByAdminId(long adminId) {
        return findOne("adminId", adminId);
    }
}
