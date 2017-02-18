package com.cus.cms.common.model;

import org.mongodb.morphia.annotations.*;

@Entity(value="admin_role",noClassnameStored=true)
@Indexes(
        @Index(fields = {@Field("name")})
)
public class AdminRole {
    @Id
    private long id;

    private String name;

    private String description;

    @Transient
    private boolean isRole;

    public boolean isRole() {
        return isRole;
    }

    public void setRole(boolean role) {
        isRole = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**

     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}