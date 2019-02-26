package cn.ssm.model;

import javax.persistence.*;

@Table(name = "role_permission")
public class RolePermission {
    @Id
    @Column(name = "role_id")
    private String roleId;

    @Id
    @Column(name = "permission_id")
    private String permissionId;

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return permission_id
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * @param permissionId
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}