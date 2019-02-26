package cn.ssm.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

/**
 * 认证器，根据特定用户返回相应的realm
 * 
 */
@Component
public class PointRealmAuthenticator extends ModularRealmAuthenticator {

	/**
	 * 根据用户类型判断使用哪个Realm
	 */
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		super.assertRealmsConfigured();
		// 使用自定义Token
		UsernamePasswordByUserTypeToken token = (UsernamePasswordByUserTypeToken) authenticationToken;
		// 判断用户类型
		// 登录类型
		String loginType = token.getUserType();
		// 所有Realm
		Collection<Realm> realms = getRealms();
		// 登录类型对应的所有Realm
		List<Realm> typeRealms = new ArrayList<>();
		for (Realm realm : realms) {
			if (realm.getName().contains(loginType))
				typeRealms.add(realm);
		}
		// 判断是单Realm还是多Realm
		if (typeRealms.size() == 1)
			return doSingleRealmAuthentication((Realm) typeRealms.iterator().next(), authenticationToken);
		else
			// ModularRealmAuthenticator默认是AtLeastOneSuccessfulStrategy策略，只要有一个Realm验证成功即可
			return doMultiRealmAuthentication(typeRealms, authenticationToken);
	}

}
