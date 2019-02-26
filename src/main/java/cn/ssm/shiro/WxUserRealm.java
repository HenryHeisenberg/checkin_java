package cn.ssm.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import cn.ssm.Enum.WxUserEnum;
import cn.ssm.model.Permission;
import cn.ssm.model.Role;
import cn.ssm.model.RolePermission;
import cn.ssm.model.WxUser;
import cn.ssm.model.WxUserRole;
import cn.ssm.service.PermissionService;
import cn.ssm.service.RolePermissionService;
import cn.ssm.service.RoleService;
import cn.ssm.service.WxUserRoleService;
import cn.ssm.service.WxUserService;

@Component
public class WxUserRealm extends AuthorizingRealm {

	/**
	 * 注意此处需要添加@Lazy注解，否则Service缓存注解、事务注解不生效
	 */
	@Autowired
	@Lazy
	private WxUserService wxUserService;
	@Autowired
	@Lazy
	private WxUserRoleService wxUserRoleService;
	@Autowired
	@Lazy
	private RoleService roleService;
	@Autowired
	@Lazy
	private RolePermissionService rolePermissionService;
	@Autowired
	@Lazy
	private PermissionService permissionService;

	private Logger logger = LoggerFactory.getLogger(WxUserRealm.class);

	@Override
	public String getName() {
		return "WxUser";
	}

	public WxUserRealm() {
		super();
		/* 关闭shiro的默认缓存 使用自己的缓存 */
		// this.setAuthenticationCachingEnabled(false);
		// this.setAuthorizationCachingEnabled(false);
	}

	/**
	 * 清除当前用户的授权
	 */
	public void clearAuthz() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}

	/**
	 * 清除所有用户的授权
	 */
	public void clearAllAuthz() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		cache.clear();
	}

	/**
	 * 清除某个用户的授权
	 * 
	 * @param openId
	 */
	public void clearAuthzByKey(String openId) {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		cache.remove(openId);
	}

	/**
	 * 处理授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		// 获取user的角色
		String openId = (String) primaryPrincipal;
		WxUser wxUser = new WxUser();
		wxUser.setOpenId(openId);
		List<WxUser> select = wxUserService.select(wxUser);
		String id = null;
		if (select.size() > 0) {
			WxUser wxUser2 = select.get(0);
			id = wxUser2.getId();
		} else {
			// 没有权限信息
			return authorizationInfo;
		}
		// 1.通过userId先去user_role表查roleId
		WxUserRole wxUserRole = new WxUserRole();
		wxUserRole.setUserId(id);
		List<WxUserRole> userRoles = wxUserRoleService.select(wxUserRole);
		// 2.再通过roleId去role表查name
		for (WxUserRole r : userRoles) {
			String roleId = r.getRoleId();
			Role role = roleService.selectByKey(roleId);
			authorizationInfo.addRole(role.getSn());
			// 获取user的权限
			// 3.通过roleId去role_permission表查询
			RolePermission rolePermission = new RolePermission();
			rolePermission.setRoleId(roleId);
			List<RolePermission> rolePremissions = rolePermissionService.select(rolePermission);
			for (RolePermission p : rolePremissions) {
				String permissionId = p.getPermissionId();
				// 4.通过permissionId去找权限
				Permission permission = permissionService.selectByKey(permissionId);
				authorizationInfo.addStringPermission(permission.getResource());
			}
		}
		return authorizationInfo;
	}

	/**
	 * 身份认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取用户的输入的账号.
		String openId = (String) token.getPrincipal();// 获取身份
		logger.info("Authentication success : " + openId);
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		// 密码就是凭证
		char[] passwordChar = usernamePasswordToken.getPassword();
		String password = new String(passwordChar);// 获取到密码
		// 设置为肯定登录成功
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(openId, // 用户名
				password, // 密码
				// ByteSource.Util.bytes(user.getId()), // salt=username+salt
				getName() // realm name
		);
		// 将用户信息放入session中
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute(WxUserEnum.OPENID.getCode(), openId);
		return authenticationInfo;
	}

}
