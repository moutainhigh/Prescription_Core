/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年2月15日</p>
 *  <p> Created by 51397</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.framework.utils.DynamicLoadUtils;

/**
 * @Project: ChuFangLiuZhuan_PlatForm 
 * @Package: com.sunshine.framework.utils
 * @ClassName: DynamicLoadUtils
 * @Description: <p>动态加载jar,class文件</p>
 * @JDK version used: 
 * @Author: 天竹子
 * @Create Date: 2017年2月15日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class DynamicLoadUtils {

	private static Logger logger = LoggerFactory.getLogger(DynamicLoadUtils.class);

	/**
	 * 动态加载jar文件
	 * @param path
	 */
	public static void loadJar(String path) {
		// 系统类库路径  
		File libPath = new File(path);

		// 获取所有的.jar和.zip文件  
		File[] jarFiles = libPath.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar") || name.endsWith(".zip");
			}
		});

		if (jarFiles != null) {
			// 从URLClassLoader类中获取类所在文件夹的方法  
			// 对于jar文件，可以理解为一个存放class文件的文件夹  
			Method method = null;
			boolean accessible = false;
			try {
				method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				accessible = method.isAccessible(); // 获取方法的访问权限  
				if (accessible == false) {
					method.setAccessible(true); // 设置方法的访问权限  
				}
				// 获取系统类加载器  
				URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
				for (File file : jarFiles) {
					URL url = file.toURI().toURL();
					method.invoke(classLoader, url);
				}
			} catch (Exception exp) {
				exp.printStackTrace();
				logger.error("读取jar文件[name={}]失败");
			} finally {
				method.setAccessible(accessible);
			}
		}
	}

	/**
	 * 动态加载class文件
	 * @param classPath：需要加载类的文件夹路径
	 * @param absolutePath:classes根路径
	 */
	public static void loadClass(String absolutePath, String classPath) {
		// 设置class文件所在根路径  
		// 例如/usr/java/classes下有一个test.App类，则/usr/java/classes即这个类的根路径，而.class文件的实际位置是/usr/java/classes/test/App.class  
		File clazzPath = new File(absolutePath + classPath);
		File absoluteClazzPath = new File(absolutePath);

		// 记录加载.class文件的数量  
		int clazzCount = 0;
		if (clazzPath.exists() && clazzPath.isDirectory()) {
			// 获取路径长度  
			int clazzPathLen = absoluteClazzPath.getAbsolutePath().length() + 1;

			Stack<File> stack = new Stack<>();
			stack.push(clazzPath);

			// 遍历类路径  
			while (stack.isEmpty() == false) {
				File path = stack.pop();
				File[] classFiles = path.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory() || pathname.getName().endsWith(".class");
					}
				});
				for (File subFile : classFiles) {
					if (subFile.isDirectory()) {
						stack.push(subFile);
					} else {
						if (clazzCount++ == 0) {
							Method method = null;
							boolean accessible = false;
							try {
								method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
								accessible = method.isAccessible();
								if (accessible == false) {
									method.setAccessible(true);
								}
								// 设置类加载器  
								URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
								// 将当前类路径加入到类加载器中  
								method.invoke(classLoader, clazzPath.toURI().toURL());
							} catch (Exception exp) {
								exp.printStackTrace();
								logger.error("读取应用程序类文件[method={}]失败", method);
							} finally {
								method.setAccessible(accessible);
							}
						}
						// 文件名称  
						String className = subFile.getPath();
						className = className.substring(clazzPathLen, className.length() - 6);
						className = className.replace(File.separatorChar, '.');
						// 加载Class类  
						try {
							Class.forName(className);
						} catch (ClassNotFoundException e) {
							logger.error("读取应用程序类文件[class={}]", className);
						}
					}
				}
			}
		}
	}

}
