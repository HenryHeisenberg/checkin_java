package cn.ssm.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

	@Autowired
	WxUserRealm wxUserRealm;
	@Autowired
	PointRealmAuthenticator authenticator;
	@Autowired
	PointRealmAuthorizer authorizer;

	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 注意过滤器配置顺序 不能颠倒
		HashMap<String, Filter> FiltersMap = new HashMap<String, Filter>();
		// 添加表单认证过滤器
		FiltersMap.put("authc", new MyAuthenticationFilter());
		// FiltersMap.put("role", new RoleAuthorizationFilter());// 不知道为什么不生效
		shiroFilterFactoryBean.setFilters(FiltersMap);
		// 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
		// filterChainDefinitionMap.put("/logout", "logout");//后台自定义登出方式
		// 配置不会被拦截的链接 顺序判断
		// filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/", "anon");// 首页
		filterChainDefinitionMap.put("/static/**", "anon");// 静态资源访问
		filterChainDefinitionMap.put("/OTA/**", "anon");// 静态资源访问
		filterChainDefinitionMap.put("/images/**", "anon");// 静态资源访问
		filterChainDefinitionMap.put("/reload", "anon");
		filterChainDefinitionMap.put("/wxUser/login", "anon");// 登录接口
		filterChainDefinitionMap.put("/wxMp/login", "anon");// 登录接口
		
		// filterChainDefinitionMap.put("/**", "authc");

		// 配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
		// 这个已经替换成返回json，在MyAuthenticationFilter的onAccessDenied方法设置，不会再进行重定向
		// shiroFilterFactoryBean.setLoginUrl("/unauth");
		// 登录成功后要跳转的链接
		// shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");// 不知道为什么不生效，只会抛出异常，通过全局异常进行处理了
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 ）
	 * 
	 * @return
	 */
	/*
	 * @Bean public HashedCredentialsMatcher hashedCredentialsMatcher() {
	 * HashedCredentialsMatcher hashedCredentialsMatcher = new
	 * HashedCredentialsMatcher();
	 * hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
	 * hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于
	 * md5(md5("")); return hashedCredentialsMatcher; }
	 */

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm认证器，根据特定用户返回相应的realm
		securityManager.setAuthenticator(authenticator);
		// 设置realm授权器，根据特定用户选择相应的realm，只重写了hasRole和is...方法
		List<Realm> realms = new ArrayList<Realm>();
		realms.add(wxUserRealm);
		securityManager.setRealms(realms);
		// 设置单个realm
		// securityManager.setRealm(userRealm());
		// 自定义session管理
		securityManager.setSessionManager(sessionManager());
		/********** 自定义缓存实现 使用redis 或 ehcache ***********/
		// securityManager.setCacheManager(cacheManager());
		securityManager.setCacheManager(ehCacheManager());

		return securityManager;
	}

	/**
	 * 自定义sessionManager
	 * 
	 * 不使用shiro-redis开源插件
	 * 
	 * @return
	 */
	@Bean
	public SessionManager sessionManager() {
		MySessionManager mySessionManager = new MySessionManager();
		// mySessionManager.setSessionDAO(redisSessionDAO());
		return mySessionManager;
	}

	/**
	 * RedisSessionDAO shiro sessionDao层的实现 通过redis 用于保存认证信息 使用的是shiro-redis开源插件
	 */
	// @Bean
	// public RedisSessionDAO redisSessionDAO() {
	// RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
	// redisSessionDAO.setRedisManager(redisManager());
	// return redisSessionDAO;
	// }

	/**
	 * 缓存管理器
	 * 
	 * @return
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return cacheManager;
	}

	/**
	 * cacheManager 缓存 redis实现 用于保存权限 使用的是shiro-redis开源插件
	 * 
	 * @return
	 */
	// public MyRedisCacheManager cacheManager() {
	// MyRedisCacheManager redisCacheManager = new MyRedisCacheManager();
	// redisCacheManager.setRedisManager(redisManager());
	// return redisCacheManager;
	// }

	/**
	 * 配置shiro redisManager
	 * 
	 * 使用的是shiro-redis开源插件
	 * 
	 * @return
	 */
	// public RedisManager redisManager() {
	// RedisManager redisManager = new RedisManager();
	// redisManager.setHost("localhost");
	// redisManager.setPort(6379);
	// redisManager.setExpire(1800);// 配置缓存过期时间
	// redisManager.setTimeout(0);
	// // redisManager.setPassword(password);
	// return redisManager;
	// }

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 * 授权所用配置
	 * 
	 * @return
	 */
	// @Bean
	// public static DefaultAdvisorAutoProxyCreator
	// getDefaultAdvisorAutoProxyCreator() {
	// return new DefaultAdvisorAutoProxyCreator();
	// }

	/***
	 * 授权所用配置
	 *
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	/***
	 * 使授权注解起作用不如不想配置可以在pom文件中加入 <dependency>
	 * <groupId>org.springframework.boot</groupId>
	 * <artifactId>spring-boot-starter-aop</artifactId> </dependency>
	 *
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * Shiro生命周期处理器
	 *
	 */
	/** * Shiro生命周期处理器 * @return */
	// @Bean
	// public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
	// return new LifecycleBeanPostProcessor();
	// }

}
