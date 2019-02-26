package cn.ssm.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.ssm.utils.http.AliHttpUtils;

@Service
public class BaiduApiService {

	@Autowired
	ApplicationContext applicationContext;

	@Value("${baidu.character-recognition.apiKey}")
	private String clientId;
	@Value("${baidu.character-recognition.secretKey}")
	private String clientSecret;

	/**
	 * 获取权限token
	 * 
	 * @return 返回示例： { "access_token":
	 *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
	 *         "expires_in": 2592000 }
	 */
	@Cacheable
	public String getAuth() {
		// 官网获取的 API Key 更新为你注册的
//		String clientId = "百度云应用的AK";
		// 官网获取的 Secret Key 更新为你注册的
//		String clientSecret = "百度云应用的SK";
		return getAuth(clientId, clientSecret);
	}

	/**
	 * 获取API访问token 该token有一定的有效期，需要自行管理，当失效时需重新获取.
	 * 
	 * @param ak - 百度云官网获取的 API Key
	 * @param sk - 百度云官网获取的 Securet Key
	 * @return assess_token 示例：
	 *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
	 */
	public String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// 1. grant_type为固定参数
				+ "grant_type=client_credentials"
				// 2. 官网获取的 API Key
				+ "&client_id=" + ak
				// 3. 官网获取的 Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}

	/**
	 * 车牌识别
	 * 
	 * @return
	 * @throws Exception
	 */
	public String license_plate(String image) throws Exception {
		String host = "https://aip.baidubce.com";
		String path = "/rest/2.0/ocr/v1/license_plate";
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> querys = new HashMap<String, String>();
		String auth = applicationContext.getBean(BaiduApiService.class).getAuth();
		querys.put("access_token", auth);
		Map<String, String> bodys = new HashMap<String, String>();
		// 放入base64加密的图片信息
		bodys.put("image", image);
		HttpResponse doPost = AliHttpUtils.doPost(host, path, "POST", headers, querys, bodys);
		HttpEntity entity = doPost.getEntity();
		// 得到response的内容
		String content = EntityUtils.toString(entity);
		JSONObject parseObject = JSONObject.parseObject(content);
		String errno = (String) parseObject.get("errno");
		if (StringUtils.isEmpty(errno) || !errno.equals(0)) {
			throw new RuntimeException("获取失败");
		}
		String number = parseObject.getJSONObject("data").parseObject("words_result").getString("number");
		return number;
	}

}
