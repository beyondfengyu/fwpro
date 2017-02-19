package com.cus.cms.dao.system;

import com.cus.cms.common.model.system.AdminMenu;
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
public class AdminMenuDao extends BasicDAO<AdminMenu, Long> {

    @Autowired
    protected AdminMenuDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public List<AdminMenu> queryMenuWithPage(String name, int offset, int size) {
        Query<AdminMenu> query = createQuery();
        if (!BlankUtil.isBlank(name)) {
            query.field("name").contains(name);
        }
        query.order("-id");
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public List<AdminMenu> queryParentMenus() {
        Query<AdminMenu> query = createQuery();
        query.field("parentId").equal(0);
        query.field("status").equal(1);
        query.order("-showorder");
        return query.asList();
    }

    public UpdateResults updateByKey(AdminMenu adminMenu) {
        Query<AdminMenu> query = createQuery();
        query.field("id").equal(adminMenu.getId());
        UpdateOperations<AdminMenu> updateOp = createUpdateOperations();
        updateOp.set("name", adminMenu.getName())
                .set("parentId", adminMenu.getParentId())
                .set("description", adminMenu.getDescription())
                .set("icon", adminMenu.getIcon())
                .set("path", adminMenu.getPath())
                .set("showorder", adminMenu.getShoworder())
                .set("status", adminMenu.getStatus());
        return updateFirst(query, updateOp);
    }

    public List<AdminMenu> queryMenuByRoles(List<Long> menuIds) {
        Query<AdminMenu> query = createQuery();
        query.field("id").in(menuIds);
        return find(query).asList();
    }
}
