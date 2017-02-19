package com.cus.cms.dao.system;

import com.cus.cms.common.model.AdminRefUserRole;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Andy
 */
@Repository
public class AdminRefUserRoleDao extends BasicDAO<AdminRefUserRole, ObjectId> {

    @Autowired
    protected AdminRefUserRoleDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public List<AdminRefUserRole> queryRefRoleByAdminId(Long adminId) {
        Query<AdminRefUserRole> query = createQuery();
        query.field("adminId").equals(adminId);
        return find(query).asList();
    }

    public WriteResult delRefByUid(Long uid) {
        Query<AdminRefUserRole> query = createQuery();
        query.field("adminId").equals(uid);
        return deleteByQuery(query);
    }

}
