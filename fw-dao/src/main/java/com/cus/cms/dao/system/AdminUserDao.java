package com.cus.cms.dao.system;

import com.cus.cms.common.model.AdminUser;
import com.cus.cms.common.util.BlankUtil;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Andy
 */
@Repository
public class AdminUserDao extends BasicDAO<AdminUser, Long>{

    @Autowired
    protected AdminUserDao(@Qualifier("datastore")Datastore ds) {
        super(ds);
    }

    public UpdateResults updateByKey(AdminUser adminUser) {
        Query<AdminUser> query = createQuery();
        query.field("id").equals(adminUser.getId());
        UpdateOperations<AdminUser> updateOp = createUpdateOperations();
        updateOp.set("username", adminUser.getUsername())
                .set("optUid", adminUser.getOptUid())
                .set("email", adminUser.getEmail())
                .set("headImg", adminUser.getHeadImg())
                .set("phone", adminUser.getPhone())
                .set("qq", adminUser.getQq());
        return updateFirst(query, updateOp);
    }

    public AdminUser queryByAccount(String account, String password) {
        Query<AdminUser> query = createQuery();
        query.field("username").equals(account);
        query.field("password").equals(password);
        return findOne(query);
    }

    public List<AdminUser> queryUserWithPage(String text, int offset, int size) {
        Query<AdminUser> query = createQuery();
        if (!BlankUtil.isBlank(text)) {
            query.or(query.criteria("username").contains(text),
                    query.criteria("phone").contains(text),
                    query.criteria("email").contains(text),
                    query.criteria("qq").contains(text));
        }
        query.order("-id");
        return query.asList(new FindOptions().skip(offset).limit(size));
    }

    public long queryCountWithPage(String text) {
        Query<AdminUser> query = createQuery();
        if (!BlankUtil.isBlank(text)) {
            query.or(query.criteria("username").contains(text),
                    query.criteria("phone").contains(text),
                    query.criteria("email").contains(text),
                    query.criteria("qq").contains(text));
        }
        return count(query);
    }

    public AdminUser queryUserByName(String name) {
        Query<AdminUser> query = createQuery();
        query.field("username").equals(name);
        return findOne(query);
    }

    public UpdateResults updateLastLogin(AdminUser adminUser) {
        Query<AdminUser> query = createQuery();
        query.field("id").equals(adminUser.getId());
        UpdateOperations<AdminUser> updateOp = createUpdateOperations();
        updateOp.set("lastLogin", adminUser.getLastLogin());
        return updateFirst(query, updateOp);
    }
}
