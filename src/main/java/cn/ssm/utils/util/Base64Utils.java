package cn.ssm.utils.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;


/** */
/**
 * <p>
 * BASE64编码解码工具包
 * </p>
 * <p>
 * 依赖javabase64-1.3.1.jar
 * </p>
 * 
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {

	/** */
	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024;

	/** */
	/**
	 * <p>
	 * BASE64字符串解码为二进制数据
	 * </p>
	 * 
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String base64) throws Exception {
		return Base64.getDecoder().decode(base64.getBytes());
	}

	/** */
	/**
	 * <p>
	 * 二进制数据编码为BASE64字符串
	 * </p>
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(byte[] bytes) throws Exception {
		return new String(Base64.getEncoder().encode(bytes));
	}

	/** */
	/**
	 * <p>
	 * 将文件编码为BASE64字符串
	 * </p>
	 * <p>
	 * 大文件慎用，可能会导致内存溢出
	 * </p>
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeFile(String filePath) throws Exception {
		byte[] bytes = fileToByte(filePath);
		return encode(bytes);
	}

	/** */
	/**
	 * <p>
	 * BASE64字符串转回文件
	 * </p>
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @param base64
	 *            编码字符串
	 * @throws Exception
	 */
	public static void decodeToFile(String filePath, String base64) throws Exception {
		byte[] bytes = decode(base64);
		byteArrayToFile(bytes, filePath);
	}

	/** */
	/**
	 * <p>
	 * 文件转换为二进制数组
	 * </p>
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByte(String filePath) throws Exception {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
				out.flush();
			}
			out.close();
			in.close();
			data = out.toByteArray();
		}
		return data;
	}

	/** */
	/**
	 * <p>
	 * 二进制数据写文件
	 * </p>
	 * 
	 * @param bytes
	 *            二进制数据
	 * @param filePath
	 *            文件生成目录
	 */
	public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(filePath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		destFile.createNewFile();
		OutputStream out = new FileOutputStream(destFile);
		byte[] cache = new byte[CACHE_SIZE];
		int nRead = 0;
		while ((nRead = in.read(cache)) != -1) {
			out.write(cache, 0, nRead);
			out.flush();
		}
		out.close();
		in.close();
	}

	/**
	 * 将byte数组转成16进制字符串
	 * 
	 * @param src
	 *            byte数组
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/******* 学校教务系统提供的方法 *******/

	static String b64map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	static char b64pad = '=';

	// 先将16进制的字符串转成10进制，再进行encode，即可得到结果
	// String mi =
	// "78ee4c96921fb1d3841e43ecec3f96d5a7b10ee0728fb982b2ceea54a910b6756151996377203b67b2e5c5d9007d286880c5ee1c22a07b6f21170448a651574a814467fec209138e5788967524c994174b3cc4b85b2ee7406ae8f734d1a2ca774d157dce61e486dbf4e51555e957ec2a90184c82943605c9dd11028d7748a377";
	// String enPassword
	// ="eO5MlpIfsdOEHkPs7D+W1aexDuByj7mCss7qVKkQtnVhUZljdyA7Z7LlxdkAfShogMXuHCKge28hFwRIplFXSoFEZ/7CCROOV4iWdSTJlBdLPMS4Wy7nQGro9zTRosp3TRV9zmHkhtv05RVV6VfsKpAYTIKUNgXJ3RECjXdIo3c=";
	// BigInteger bigInteger = new BigInteger(mi, 16); String encode =
	// encode(bigInteger.toByteArray());

	public static String hex2b64(String h) {
		// 该方法无效
		return null;
	}

	// 该方法和先将base64解码，再转换成16进制字符串效果一样
	// // 解码
	// byte[] decode = decode(modulus);
	// // 转成10进制
	// // BigInteger bigInteger = new BigInteger(decode);
	// // //转成16进制，该方法有可能行不通，因为bigInteger.intValue()并不是完整的
	// // System.out.println(Integer.toHexString(bigInteger.intValue()));
	// // 直接将byte转成16进制字符串
	// String bytesToHexString = bytesToHexString(decode);
	// System.out.println(bytesToHexString);
	public static String b64tohex(String s) {
		String ret = "";
		int i;
		int k = 0; // b64 state, 0-3
		int slop = 0;
		for (i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == b64pad)
				break;
			int v = b64map.indexOf(s.charAt(i));
			if (v < 0)
				continue;
			if (k == 0) {
				ret += int2char(v >> 2);
				slop = v & 3;
				k = 1;
			} else if (k == 1) {
				ret += int2char((slop << 2) | (v >> 4));
				slop = v & 0xf;
				k = 2;
			} else if (k == 2) {
				ret += int2char(slop);
				ret += int2char(v >> 2);
				slop = v & 3;
				k = 3;
			} else {
				ret += int2char((slop << 2) | (v >> 4));
				ret += int2char(v & 0xf);
				k = 0;
			}
		}
		if (k == 1)
			ret += int2char(slop << 2);
		return ret;
	}

	// convert a base64 string to a byte/number array
	private static char int2char(int i) {
		String BI_RM = "0123456789abcdefghijklmnopqrstuvwxyz";
		return BI_RM.charAt(i);
	}

	/******* 学校教务系统提供的方法 *******/

	// String modulus =
	// "AI8Mv+S/NCzvUcu5WhlR/0U7WK16LLZEwBP9AZpY1S1sMpGfiqgBs+P0xthiqLMjAkU5DKbLRoOWq1VXubia8S1LfuVWf+slzzu/MGjXSmlcgiIrsQxPUFc7WMMuHjTe4+gpVxkBOL9VkLyXQy+EE61zkrxheUvffSmV0Be+aJPl";
	// String exponent = "AQAB";
	// String hexModulus =
	// "008f0cbfe4bf342cef51cbb95a1951ff453b58ad7a2cb644c013fd019a58d52d6c32919f8aa801b3e3f4c6d862a8b3230245390ca6cb468396ab5557b9b89af12d4b7ee5567feb25cf3bbf3068d74a695c82222bb10c4f50573b58c32e1e34dee3e82957190138bf5590bc97432f8413ad7392bc61794bdf7d2995d017be6893e5";
	// String hexExponent = "010001";
	// String ming = "liao1524";
	// // 经过测试发现，modulus和exponent不变，每一次产生的密文都不一样
	// String mi =
	// "78ee4c96921fb1d3841e43ecec3f96d5a7b10ee0728fb982b2ceea54a910b6756151996377203b67b2e5c5d9007d286880c5ee1c22a07b6f21170448a651574a814467fec209138e5788967524c994174b3cc4b85b2ee7406ae8f734d1a2ca774d157dce61e486dbf4e51555e957ec2a90184c82943605c9dd11028d7748a377";
	// // mi经过base64加密之后
	// String enPassword =
	// "eO5MlpIfsdOEHkPs7D+W1aexDuByj7mCss7qVKkQtnVhUZljdyA7Z7LlxdkAfShogMXuHCKge28hFwRIplFXSoFEZ/7CCROOV4iWdSTJlBdLPMS4Wy7nQGro9zTRosp3TRV9zmHkhtv05RVV6VfsKpAYTIKUNgXJ3RECjXdIo3c=";

	// @Test
	// public void test() throws Exception {
	//
	// String loginUri = "http://jw.jluzh.com/xtgl/login_slogin.html";
	// // 1.获取网页的jsessionid和csrftoken
	// Http http = new Http();
	// http.setUri(loginUri);
	// // 封装请求头
	// HashMap<String, String> headerMap = new HashMap<String, String>();
	// headerMap.put(Http.HOST, "jw.jluzh.com");
	// headerMap.put(Http.CONNECTION, "keep-alive");// 进过测试，没有该header不影响登录
	// headerMap.put(Http.CONTENTTYPE, "application/x-www-form-urlencoded");
	// headerMap.put(Http.UA, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0)
	// Gecko/20100101 Firefox/57.0");
	// headerMap.put(Http.REFERER, "http://jw.jluzh.com/xtgl/login_slogin.html");//
	// 进过测试，没有该header不影响登录
	// headerMap.put("Upgrade-Insecure-Requests", "1");// 进过测试，没有该header不影响登录
	// http.setHeaderMap(headerMap);
	// // 发送获取内容和Cookie请求
	// Map<String, String> contentAndCookie = HttpUtils.getContentAndCookie(http);
	// // 1.1获取session
	// String cookie = contentAndCookie.get("Cookie"); //
	// System.out.println(cookie);
	// // 1.2获取content
	// String content = contentAndCookie.get("Content"); //
	// System.out.println(content);
	// // 1.2.1用jsoup获取返回页面中的id为csrftoken的value值
	// Document document = Jsoup.parse(content);
	// // 1.2.2找到id为csrftoken的标签
	// Elements csrftokenElement = document.select("#csrftoken"); //
	// System.out.println(csrftoken);
	// // 1.2.3获取到属性为value的值
	// String csrftoken = csrftokenElement.attr("value");//
	// System.out.println(attr);
	// String publicKeyUri = "http://jw.jluzh.com/xtgl/login_getPublicKey.html";
	// // 2.获取modulus和exponent
	// // 2.1设置http对象的uri
	// http.setUri(publicKeyUri);
	// // 2.2设置http对象的Cookie
	// headerMap.put(Http.COOKIE, cookie);
	// String req = HttpUtils.getReq(http, null);// System.out.println(req);
	// // 2.3解析json
	// String modulus = null;
	// String exponent = null;
	// JSONArray jsonArray = JSONArray.fromObject("[" + req + "]");
	// for (int i = 0; i < jsonArray.size(); i++) {
	// JSONObject jsonObject = jsonArray.getJSONObject(i);
	// modulus = (String) jsonObject.get("modulus"); // System.out.println(modulus);
	// exponent = (String) jsonObject.get("exponent"); //
	// System.out.println(exponent);
	// }
	// String password = "liao1524";
	// String encodePassword = encodePassword(modulus, exponent, password);
	// // 3.构建表单
	// // 3.1设置uri
	// http.setUri(loginUri);
	// // 3.2构建表单内容
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("csrftoken", csrftoken));
	// params.add(new BasicNameValuePair("yhm", "04161524"));
	// params.add(new BasicNameValuePair("mm", encodePassword));
	// params.add(new BasicNameValuePair("mm", encodePassword));
	// // 3.3发送请求
	// String postReq = HttpUtils.sendPost(http, params);
	// System.out.println("-----------------成功登录-----------------");
	// System.out.println(postReq);
	// System.out.println("-----------------成功登录-----------------");
	//
	// /**
	// * 如果有账号已经登录，可能出现错误，成功登录的返回内容是空的，如果返回内容不为空，则可能出现错误
	// */
	//
	// // 获取首页内容
	// // String initmenturi =
	// // "http://jw.jluzh.com/xtgl/index_initMenu.html?jsdm=&_t=";
	// String kbUri = "http://jw.jluzh.com/kbcx/xskbcx_cxXsKb.html?&xnm=2017&xqm=1";
	// http.setUri(kbUri);
	// String kbJson = HttpUtils.sendGet(http);
	// System.out.println(kbJson);
	//
	// // 退出登录
	// String exitUri = "http://jw.jluzh.com/logout";
	// http.setUri(exitUri);
	// HttpUtils.sendGet(http);
	// }

	/**
	 * 获取加密之后的base64字符串
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @param password
	 *            明文密码
	 * @return
	 * @throws Exception
	 */
	public static String encodePassword(String modulus, String exponent, String password) throws Exception {
		// 解密
		byte[] decodemodulus = decode(modulus);
		byte[] decodeexponent = decode(exponent);
		// 传入10进制byte数组
		RSAPublicKey publicKey = RSAUtils.getPublicKey(decodemodulus, decodeexponent);
		// RSA加密
		String encryptByPublicKey = RSAUtils.encryptByPublicKey(password, publicKey);
		// System.out.println(encryptByPublicKey);
		// 将获取到的16进制字符串转成10进制
		BigInteger bigInteger = new BigInteger(encryptByPublicKey, 16);
		// base64加密
		String encode = encode(bigInteger.toByteArray());
		// System.out.println(encode);
		return encode;
	}

}
