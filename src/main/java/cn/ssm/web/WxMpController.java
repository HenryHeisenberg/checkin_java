package cn.ssm.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ssm.Enum.ShiroEnum;
import cn.ssm.base.BaseResult;
import cn.ssm.config.WxMpConfiguration;
import cn.ssm.model.WxUser;
import cn.ssm.service.WxUserService;
import cn.ssm.shiro.UsernamePasswordByUserTypeToken;
import cn.ssm.shiro.WxUserRealm;
import cn.ssm.utils.util.UUIDUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@RestController
@RequestMapping("/wxMp")
public class WxMpController {

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private WxUserRealm wxUserRealm;

	@Value("${wx.mp.configs[0].appid}")
	private String appid;

	private Logger logger = LoggerFactory.getLogger(WxMpController.class);

	/**
	 * 每登录一次，会有一个新的token，如果有旧token传进来，会将该token移除
	 * 
	 * @param code
	 * @param user
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "登录注册", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "code", value = "code", required = true, dataType = "String", paramType = "query") })
	@RequestMapping("/login")
	public Object login(String code, @RequestParam(defaultValue = "mp") String processId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 登录前判断一下是否已经登录
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		WxUser sess_user = (WxUser) session.getAttribute(ShiroEnum.SESSION_USER_INFO.getCode());
		if (sess_user != null) {
			returnMap.put("user", sess_user);
			subject.hasRole("admin");// 将权限加入缓存
			AuthorizationInfo authorizationInfo = wxUserRealm.getAuthorizationCache().get(subject.getPrincipals());
			if (authorizationInfo != null) {
				returnMap.put("roles", authorizationInfo.getRoles());
			} else {
				returnMap.put("roles", null);
			}
			return BaseResult.success(returnMap);
		}
		if (StringUtils.isEmpty(code)) {
			return BaseResult.fail("缺少参数");
		}
		/********************* 登录 ************************/
		/**
		 * 获取session_key，openID，登录
		 */
		WxMpService mpService = WxMpConfiguration.getMpServices().get(appid);
		WxMpUser wxMpUser = null;
		try {
			WxMpOAuth2AccessToken accessToken = mpService.oauth2getAccessToken(code);
			wxMpUser = mpService.oauth2getUserInfo(accessToken, null);
		} catch (WxErrorException e) {
			e.printStackTrace();
			return BaseResult.fail("code无效");
		}

		/********************* 登录 ************************/
		/********************* 认证 ************************/
		/**
		 * 此处的 账号为：微信提供的openID 密码为：小程序ID，processId
		 */
		String openId = wxMpUser.getOpenId();
		UsernamePasswordByUserTypeToken usernamePasswordToken = new UsernamePasswordByUserTypeToken(openId, processId,
				ShiroEnum.WX_USER.getCode());
		Boolean loginFlag = false;// 用来判断是否登录成功
		String msg = "登录成功";
		try {
			// 只要进了这里，肯定登录成功
			subject.login(usernamePasswordToken);
			loginFlag = true;
			session = subject.getSession();
			returnMap.put("msg", msg);
			returnMap.put("token", session.getId());
		} catch (IncorrectCredentialsException e) {
			msg = "密码错误";
		} catch (LockedAccountException e) {
			msg = "登录失败，该用户已被冻结";
		} catch (AuthenticationException e) {
			msg = "该用户不存在";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "未知错误 ";
		}
		// 登录失败调用
		if (!loginFlag) {
			return BaseResult.fail(msg);
		}
		/********************* 认证 ************************/
		/********************* 保存数据到数据库，判断是新用户还是老用户 ************************/
		// 到数据库查一下是否存在该openID的对象
		WxUser selectWxUser = new WxUser();
		selectWxUser.setOpenId(openId);
		WxUser wxUser = wxUserService.selectOne(selectWxUser);
		// 如果存在，拿到对象，比较一下，是否有字段更新的，或者直接更新
		Boolean newUser = false;// 默认是老用户
		if (wxUser == null) {
			// 新用户
			newUser = true;
		}
		String nickName = wxMpUser.getNickname();
		String gender = wxMpUser.getSexDesc();
		String city = wxMpUser.getCity();
		String country = wxMpUser.getCountry();
		String province = wxMpUser.getProvince();
		String language = wxMpUser.getLanguage();
		String avatarUrl = wxMpUser.getHeadImgUrl();
		int lastIndexOf = avatarUrl.lastIndexOf("/");
		avatarUrl = avatarUrl.substring(0, lastIndexOf + 1);
		Date date = new Date();
		String uuid = UUIDUtils.getUUID();
		if (newUser) {
			// 新用户
			WxUser newWxUser = new WxUser();
			newWxUser.setId(uuid);
			newWxUser.setOpenId(openId);
			newWxUser.setNickName(nickName);
			newWxUser.setGender(gender);
			newWxUser.setCity(city);
			newWxUser.setCountry(country);
			newWxUser.setProvince(province);
			newWxUser.setLanguage(language);
			newWxUser.setAvatarUrl(avatarUrl);
			newWxUser.setLoginTime(date);
			newWxUser.setCreateTime(date);
			wxUserService.insert(newWxUser);
			session.setAttribute(ShiroEnum.SESSION_USER_INFO.getCode(), newWxUser);
			returnMap.put("user", newWxUser);
		} else {
			// 老用户
			wxUser.setNickName(nickName);
			wxUser.setGender(gender);
			wxUser.setCity(city);
			wxUser.setCountry(country);
			wxUser.setProvince(province);
			wxUser.setLanguage(language);
			wxUser.setAvatarUrl(avatarUrl);
			wxUser.setLoginTime(date);
			wxUserService.updateByPrimaryKeySelective(wxUser);
			session.setAttribute(ShiroEnum.SESSION_USER_INFO.getCode(), wxUser);
			returnMap.put("user", wxUser);
		}
		subject.hasRole("admin");// 将权限加入缓存
		AuthorizationInfo authorizationInfo = wxUserRealm.getAuthorizationCache().get(subject.getPrincipals());
		if (authorizationInfo != null) {
			returnMap.put("roles", authorizationInfo.getRoles());
		} else {
			returnMap.put("roles", null);
		}
		return BaseResult.success(returnMap);
	}

	@ApiOperation(value = "通过ID查询用户", httpMethod = "GET")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "wxId", required = true, dataType = "String", paramType = "query") })
	@GetMapping("/selectById")
	public WxUser selectById(String id) {
		WxUser selectByKey = wxUserService.selectByKey(id);
		return selectByKey;
	}

	@ApiOperation(value = "清除自己的授权", httpMethod = "GET")
	@GetMapping("/clearCachedAuthorizationInfo")
	public Map<String, Object> clearCachedAuthorizationInfo() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		wxUserRealm.clearAuthz();
		returnMap.put("status", "success");
		return returnMap;
	}

	@GetMapping("/check")
	public String check(@RequestParam(name = "signature", required = false) String signature,
			@RequestParam(name = "timestamp", required = false) String timestamp,
			@RequestParam(name = "nonce", required = false) String nonce,
			@RequestParam(name = "echostr", required = false) String echostr) {
		this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
		if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce)
				|| StringUtils.isEmpty(echostr)) {
			throw new IllegalArgumentException("请求参数非法，请核实!");
		}
		WxMpService wxService = WxMpConfiguration.getMpServices().get(appid);
		if (wxService == null) {
			throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
		}
		if (wxService.checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}
		return "非法请求";
	}

	@GetMapping("/getUrl")
	public String getUrl(@RequestParam(defaultValue = "http://localhost") String host,
			@RequestParam(defaultValue = "/wxMp/login") String url) {
		WxMpService wxMpService = WxMpConfiguration.getMpServices().get(appid);
		String oauth2buildAuthorizationUrl = wxMpService.oauth2buildAuthorizationUrl(host + url,
				WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
		return oauth2buildAuthorizationUrl;
	}

}
