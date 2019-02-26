package cn.ssm.config;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.ssm.interceptor.LogInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private LogInterceptor logInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 上传的图片在当前文件夹的OTA文件夹下，访问路径如：http://localhost:8080/OTA/myimage.jpg
		// 其中OTA表示访问前缀。"file:C:/***/OTA/"是文件的真实的存储路径
		/***** 获取jar所在目录 *****/
		WebMvcConfig.class.getClassLoader().getResource("");
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		path = new File(path).getParentFile().toString();
		try {
			path = URLDecoder.decode(path, "utf-8") + "/OTA/";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		ApplicationHome home = new ApplicationHome(getClass());
//		File jarFile = home.getSource();
//		String path = jarFile.getParentFile().toString() + "/OTA/";
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			// 不存在则创建
			pathFile.mkdirs();
			System.out.println("文件夹不存在");
		}
		/***** 获取jar所在目录 *****/
		registry.addResourceHandler("/OTA/**").addResourceLocations("file:" + path);
		System.out.println("OTA路径：" + path);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor).addPathPatterns("/**");
	}
}