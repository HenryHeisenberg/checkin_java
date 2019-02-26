package cn.ssm.base;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 逆向工程生成带有注解的entity代码，兼容通用mapper
 * 
 * @author 7-81
 *
 */
public class Generator {

	public static void main(String[] args) {
		try {
			run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void run() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		ConfigurationParser cp = new ConfigurationParser(warnings);
		// 配置文件位置，/就是相当于classpath
		InputStream resourceAsStream = Generator.class.getResourceAsStream("/generatorConfig.xml");
		System.out.println(resourceAsStream.toString());
		Configuration config = cp.parseConfiguration(resourceAsStream);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}

	public static void startDB() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String url = "jdbc:hsqldb:mem:generator";
		String user = "sa";
		String password = "";
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			InputStream inputStream = getResourceAsStream("CreateDB.sql");

			/*
			 * SqlFile sqlFile = new SqlFile(new InputStreamReader(inputStream), "init",
			 * System.out, "UTF-8", false, new File("."));
			 * sqlFile.setConnection(connection); sqlFile.execute();
			 */

			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static InputStream getResourceAsStream(String path) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}

}
