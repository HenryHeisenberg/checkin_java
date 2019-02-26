package cn.ssm.shiro;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import cn.ssm.model.WxUser;

/**
 * 指定使用哪个Realm进行权限认证器(目前未使用)
 * 
 */
@Component
public class PointRealmAuthorizer extends ModularRealmAuthorizer {

	@Override
	public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
		super.assertRealmsConfigured();
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		for (Realm realm : getRealms()) {
			if (!(realm instanceof Authorizer))
				continue;
			if (primaryPrincipal instanceof WxUser) {
				if (realm instanceof WxUserRealm) {
					return ((WxUserRealm) realm).hasRole(principals, roleIdentifier);
				}
			}

		}
		return false;
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		super.assertRealmsConfigured();
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		for (Realm realm : getRealms()) {
			if (!(realm instanceof Authorizer))
				continue;
			if (primaryPrincipal instanceof WxUser) {
				if (realm instanceof WxUserRealm) {
					return ((WxUserRealm) realm).isPermitted(principals, permission);
				}
			}

		}
		return false;
	}

}
