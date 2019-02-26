package cn.ssm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.annotation.MapperScan;

@PropertySource(value = { "classpath:init.properties" }, encoding = "UTF-8", ignoreResourceNotFound = true)
@EnableAsync
@EnableAutoConfiguration
@ServletComponentScan
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "cn.ssm.**.mapper", markerInterface = Mapper.class)
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

}
