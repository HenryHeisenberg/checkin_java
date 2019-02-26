package cn.ssm.shiro;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroFilterRegisterConfig {

	/*@Bean
	public FilterRegistrationBean shiroLoginFilteRegistration(MyAuthenticationFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Bean
	public FilterRegistrationBean shiroRolesFilterRegistration(RoleAuthorizationFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}*/

	/*
	 * @Bean public FilterRegistrationBean
	 * shiroPermsFilterRegistration(ShiroPermsFilter filter) {
	 * FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	 * registration.setEnabled(false); return registration; }
	 */

	/*
	 * @Bean public FilterRegistrationBean
	 * kickoutSessionFilterRegistration(KickoutSessionFilter filter) {
	 * FilterRegistrationBean registration = new FilterRegistrationBean(filter);
	 * registration.setEnabled(false); return registration; }
	 */
}