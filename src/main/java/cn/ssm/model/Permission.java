package cn.ssm.model;

import javax.persistence.*;

@Table(name = "permission")
public class Permission {
    @Id
    private String id;

    private String name;

    private String resource;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
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

    /**
     * @return resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }
}