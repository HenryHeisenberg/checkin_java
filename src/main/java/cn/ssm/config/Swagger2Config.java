package cn.ssm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {

	/*
	 * 只支持单包
	 * 
	 * @Bean public Docket createRestApi() { return new
	 * Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
	 * .apis(RequestHandlerSelectors.basePackage("cn.ssm.web")).paths(
	 * PathSelectors.any()) .build(); }
	 */

	/**
	 * 支持多包扫描
	 * 
	 * @return
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(Swagger2Config.basePackage("cn.ssm.web,cn.ssm.test,cn.ssm.base")).paths(PathSelectors.any())
				.build();
	}

	public static Predicate<RequestHandler> basePackage(final String basePackage) {
		return new Predicate<RequestHandler>() {
			@Override
			public boolean apply(RequestHandler input) {
				return declaringClass(input).transform(handlerPackage(basePackage)).or(true);
			}
		};
	}

	/**
	 * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
	 * 
	 * @param basePackage
	 *            扫描包路径
	 * @return Function
	 */
	private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
		return new Function<Class<?>, Boolean>() {
			@Override
			public Boolean apply(Class<?> input) {
				for (String strPackage : basePackage.split(",")) {
					boolean isMatch = input.getPackage().getName().startsWith(strPackage);
					if (isMatch) {
						return true;
					}
				}
				return false;
			}
		};
	}

	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
		return Optional.fromNullable(input.declaringClass());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 页面标题
				.title("springboot利用swagger构建api文档")
				// 版本号
				.version("1.0")
				// 描述
				.description("API 描述").termsOfServiceUrl("http://www.baidu.com").build();
	}

}
