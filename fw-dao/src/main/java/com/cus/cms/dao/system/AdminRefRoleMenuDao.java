package com.cus.cms.dao.system;

import com.cus.cms.common.model.system.AdminRefRoleMenu;
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
public class AdminRefRoleMenuDao extends BasicDAO<AdminRefRoleMenu,ObjectId> {
    @Autowired
    protected AdminRefRoleMenuDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public List<AdminRefRoleMenu> queryMenuByRole(Long roleId) {
        Query<AdminRefRoleMenu> query = createQuery();
        query.field("roleId").equal(roleId);
        return find(query).asList();
    }

    public List<AdminRefRoleMenu> queryRefMenuByRoles(List<Long> roles) {
        Query<AdminRefRoleMenu> query = createQuery();
        query.filter("roleId in", roles);
        return find(query).asList();
    }

    public WriteResult delRefByRoleId(Long roleId) {
        Query<AdminRefRoleMenu> query = createQuery();
        query.field("roleId").equal(roleId);
        return deleteByQuery(query);
    }
}
