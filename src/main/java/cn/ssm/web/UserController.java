package cn.ssm.web;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.ssm.base.BaseResult;
import cn.ssm.config.WxMaConfiguration;
import cn.ssm.model.UserInfo;
import cn.ssm.service.UserInfoService;
import cn.ssm.utils.util.UUIDUtils;
import me.chanjar.weixin.common.error.WxErrorException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${wx.miniapp.configs[0].appid}")
    private String appid;

    
    
    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/login")
    public Object login(UserInfo user,String code) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return BaseResult.fail("账号或密码为空");
        }
        //1
        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
        }
        String session_key = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult wxSession = wxService.getUserService().getSessionInfo(code);//user应该是getCode
            session_key = wxSession.getSessionKey();
            openId = wxSession.getOpenid();
            // TODO 可以增加自己的逻辑，关联业务相关数据
        } catch (WxErrorException e) {
            return BaseResult.fail("code无效");
        }
        //2
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        UserInfo selectOne = userInfoService.selectOne(userInfo);
        if (selectOne != null) {
            //return BaseResult.success(selectOne);
            userInfo.setOpenId(openId);
            UserInfo select = userInfoService.selectOne(userInfo);
            if(selectOne==null) {
                return BaseResult.fail("不可在其他微信号登陆此账号");
            }
            else {
                return BaseResult.success(selectOne);
            }
        }
        return BaseResult.fail("账号或密码错误");
    }

    @PostMapping("/update")
    public Object update(String id, String old, String newPassword) {
        if (id == null || StringUtils.isAnyBlank(old, newPassword)) {
            return BaseResult.fail("缺少参数");
        }
        UserInfo selectByKey = userInfoService.selectByKey(id);
        String password = selectByKey.getPassword();
        if (!password.equals(old)) {
            return BaseResult.fail("原密码错误");
        }
        selectByKey.setPassword(newPassword);
        userInfoService.updateByPrimaryKeySelective(selectByKey);
        return BaseResult.success(selectByKey);
    }

    @PostMapping("/updateInfo")
    public Object updateInfo(UserInfo user) {
        userInfoService.updateByPrimaryKeySelective(user);
        return BaseResult.success("更新成功");
    }

    @PostMapping("/register")
    public Object register(UserInfo user,String code) {
        String username = user.getUsername();
        String password = user.getPassword();
        final WxMaService wxService = WxMaConfiguration.getMaServices().get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appid));
        }
        String session_key = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult wxSession = wxService.getUserService().getSessionInfo(code);
            session_key = wxSession.getSessionKey();
            openId = wxSession.getOpenid();
            // TODO 可以增加自己的逻辑，关联业务相关数据
        } catch (WxErrorException e) {
            return BaseResult.fail("code无效");
        }
        if (StringUtils.isAnyBlank(username, password)) {
            return BaseResult.fail("账号或密码为空");
        }
        if (StringUtils.isEmpty(user.getRole().toString())) {
            return BaseResult.fail("角色设置不不能为空");
        }
        UserInfo user2 = new UserInfo();
        user2.setUsername(username);
        UserInfo selectOne = userInfoService.selectOne(user2);
        if (selectOne != null) {
            return BaseResult.fail("该用户已存在");
        }
        user.setId(UUIDUtils.getUUID());
        user.setOpenId(openId);
        userInfoService.insert(user);
        UserInfo selectOne2 = userInfoService.selectOne(user);
        return BaseResult.success(selectOne2);
    }

}
