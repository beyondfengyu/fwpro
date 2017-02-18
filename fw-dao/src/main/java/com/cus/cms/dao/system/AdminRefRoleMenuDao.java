package com.cus.cms.dao.system;

import com.cus.cms.common.model.AdminRefRoleMenu;
import com.cus.cms.common.model.AdminRefUserRole;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.List;

/**
 * @author Andy
 */
public class AdminRefRoleMenuDao extends BasicDAO<AdminRefRoleMenu,ObjectId> {

    protected AdminRefRoleMenuDao(Datastore ds) {
        super(ds);
    }

    public List<AdminRefRoleMenu> queryMenuByRole(Long roleId) {
        Query<AdminRefRoleMenu> query = createQuery();
        query.field("roleId").equals(roleId);
        return find(query).asList();
    }

    public List<AdminRefRoleMenu> queryRefMenuByRoles(List<Long> roles) {
        Query<AdminRefRoleMenu> query = createQuery();
        query.filter("roleId in", roles);
        return find(query).asList();
    }

    public WriteResult delRefByRoleId(Long roleId) {
        Query<AdminRefRoleMenu> query = createQuery();
        query.field("roleId").equals(roleId);
        return deleteByQuery(query);
    }
}
