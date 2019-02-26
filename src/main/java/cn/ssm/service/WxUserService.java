package cn.ssm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ssm.base.impl.BaseServiceImpl;
import cn.ssm.mapper.WxUserMapper;
import cn.ssm.model.WxUser;
import cn.ssm.service.WxUserService;

@Service
public class WxUserService extends BaseServiceImpl<WxUser> {

	@Autowired
	WxUserMapper wxUserMapper;

}
