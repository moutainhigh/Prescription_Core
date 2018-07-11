/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年7月9日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.mybatisgenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.sunshine.framework.common.mybatisgenerator.CombaGeneratorTools;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.common.mybatisgenerator
 * @ClassName CombaGeneratorTools.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月9日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class CombaGeneratorTools {

	/**
	 * @Description
	 * @param args
	 * @date 2017年7月9日
	 */
	public static void generator(String congifFile) throws Exception {
		if (StringUtils.isBlank(congifFile)) {
			congifFile = CombaGeneratorTools.class.getClassLoader().getResource("mybatis/generatorConfig.xml").getFile();
			System.out.println("congifFile is null.user default file:" + congifFile);
		}
		File configFile = new File(congifFile);

		List<String> warnings = new ArrayList<>();
		boolean overwrite = true;
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		if (warnings.size() > 0) {
			System.out.println("执行失败,原因:");
			for (String s : warnings) {
				System.out.println(s);
			}
		} else {
			System.out.println("执行成功,生成文件:");
			for (GeneratedJavaFile file : myBatisGenerator.getGeneratedJavaFiles()) {
				System.out.println(file.getTargetPackage() + "." + file.getFileName());
			}
			for (GeneratedXmlFile file : myBatisGenerator.getGeneratedXmlFiles()) {
				System.out.println(file.getTargetPackage() + "." + file.getFileName());
			}
		}
	}

}
