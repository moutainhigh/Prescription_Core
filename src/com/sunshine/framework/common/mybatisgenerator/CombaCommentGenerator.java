/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月6日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.mybatisgenerator;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.common.mybatisgenerator
 * @ClassName MyCommentGenerator.java
 * @Description 修改mybatisGenerator生成的注释
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年6月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class CombaCommentGenerator implements CommentGenerator {
	private Properties properties;
	private Properties systemPro;
	private boolean suppressDate;
	private boolean suppressAllComments;

	public CombaCommentGenerator() {
		super();
		properties = new Properties();
		suppressDate = false;
		suppressAllComments = false;
		systemPro = System.getProperties();
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		if (suppressAllComments) {
			return;
		}
		Calendar cal = Calendar.getInstance();
		compilationUnit.addFileCommentLine("/**");
		compilationUnit.addFileCommentLine("* <html>");
		compilationUnit.addFileCommentLine("*  <body>");
		compilationUnit.addFileCommentLine("*   <P> Copyright 2017 阳光康众 </p>");
		compilationUnit.addFileCommentLine("*   <p> All rights reserved.</p>");
		compilationUnit.addFileCommentLine("*   <p> Created on " + cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月"
				+ cal.get(Calendar.DAY_OF_MONTH) + "日" + "</p>");
		compilationUnit.addFileCommentLine("*   <p> Created by " + systemPro.getProperty("user.name") + "</p>");
		compilationUnit.addFileCommentLine("*  </body>");
		compilationUnit.addFileCommentLine("* </html>");
		compilationUnit.addFileCommentLine("*/");
	}

	/**
	 * Adds a suitable comment to warn users that the element was generated, and when it was generated.
	 */
	@Override
	public void addComment(XmlElement xmlElement) {
	}

	@Override
	public void addRootComment(XmlElement rootElement) {
		// add no document level comments by default
		return;
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);

		suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

		suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
	}

	/**
	 * This method adds the custom javadoc tag for. You may do nothing if you do not wish to include the Javadoc tag - however, if you do not include
	 * the Javadoc tag then the Java merge capability of the eclipse plugin will break.
	 * 
	 * @param javaElement
	 *            the java element
	 */
	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
		javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		sb.append(" * "); //$NON-NLS-1$
		sb.append(MergeConstants.NEW_ELEMENT_TAG);
		if (markAsDoNotDelete) {
			sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
		}
		String s = getDateString();
		if (s != null) {
			sb.append(' ');
			sb.append(s);
		}
		javaElement.addJavaDocLine(sb.toString());
	}

	/**
	 * This method returns a formated date string to include in the Javadoc tag and XML comments. You may return null if you do not want the date in
	 * these documentation elements.
	 * 
	 * @return a string representing the current timestamp, or null
	 */
	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else {
			return new Date().toString();
		}
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable table) {
		Calendar cal = Calendar.getInstance();
		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine("* @Project: ChuFangLiuZhuan_PlatForm_Platorm");
		innerClass.addJavaDocLine("* @Package: " + innerClass.getType().getPackageName());
		innerClass.addJavaDocLine("* @ClassName: " + innerClass.getType().getFullyQualifiedName());
		innerClass.addJavaDocLine("* @TableName:" + table.getTableConfiguration().getTableName());
		innerClass.addJavaDocLine("* @Description:" + table.getRemarks());
		innerClass.addJavaDocLine("* @JDK version used: " + systemPro.getProperty("java.runtime.version"));
		innerClass.addJavaDocLine("* @Author:" + systemPro.getProperty("user.name"));
		innerClass.addJavaDocLine(
				"* @Create Date: " + cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
		innerClass.addJavaDocLine("* @modify By:");
		innerClass.addJavaDocLine("* @modify Date:");
		innerClass.addJavaDocLine("* @Why&What is modify:");
		innerClass.addJavaDocLine("* @Version: 1.0");
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		innerEnum.addJavaDocLine("/**"); //$NON-NLS-1$
		innerEnum.addJavaDocLine(" * This enum was generated by MyBatis Generator."); //$NON-NLS-1$

		sb.append(" * This enum corresponds to the database table "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerEnum.addJavaDocLine(sb.toString());

		addJavadocTag(innerEnum, false);

		innerEnum.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (introspectedColumn.getRemarks() != null && !introspectedColumn.getRemarks().equals("")) {
			field.addJavaDocLine("/**");
			field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
			// addJavadocTag(field, false);
			field.addJavaDocLine(" */");
		}
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		field.addJavaDocLine("/**"); //$NON-NLS-1$
		field.addJavaDocLine(" * This field was generated by MyBatis Generator."); //$NON-NLS-1$

		sb.append(" * This field corresponds to the database table "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		field.addJavaDocLine(sb.toString());

		addJavadocTag(field, false);

		field.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		method.addJavaDocLine("/**"); //$NON-NLS-1$
		method.addJavaDocLine(" * This method was generated by MyBatis Generator."); //$NON-NLS-1$
		sb.append(" * This method corresponds to the database table "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		method.addJavaDocLine(sb.toString());

		addJavadocTag(method, false);

		method.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		method.addJavaDocLine("/**");
		method.addJavaDocLine("* @return " + introspectedColumn.getRemarks());
		method.addJavaDocLine("*/");

	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		method.addJavaDocLine("/**");
		Parameter parm = method.getParameters().get(0);
		method.addJavaDocLine(" * @param " + parm.getName() + "  " + introspectedColumn.getRemarks());
		method.addJavaDocLine(" */");
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable table, boolean markAsDoNotDelete) {
		if (suppressAllComments) {
			return;
		}
		Calendar cal = Calendar.getInstance();
		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine("* @Project: ChuFangLiuZhuan_PlatForm_Platorm");
		innerClass.addJavaDocLine("* @Package: " + innerClass.getType().getPackageName());
		innerClass.addJavaDocLine("* @ClassName: " + innerClass.getType().getFullyQualifiedName());
		innerClass.addJavaDocLine("* @TableName:" + table.getTableConfiguration().getTableName());
		innerClass.addJavaDocLine("* @Description:" + table.getRemarks());
		innerClass.addJavaDocLine("* @JDK version used: " + systemPro.getProperty("java.runtime.version"));
		innerClass.addJavaDocLine("* @Author:" + systemPro.getProperty("user.name"));
		innerClass.addJavaDocLine(
				"* @Create Date: " + cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
		innerClass.addJavaDocLine("* @modify By:");
		innerClass.addJavaDocLine("* @modify Date:");
		innerClass.addJavaDocLine("* @Why&What is modify:");
		innerClass.addJavaDocLine("* @Version: 1.0");
		innerClass.addJavaDocLine("*/");
	}

	@Override
	public void addModelClassComment(TopLevelClass innerClass, IntrospectedTable table) {
		// TODO Auto-generated method stub
		if (suppressAllComments) {
			return;
		}
		Calendar cal = Calendar.getInstance();
		innerClass.addJavaDocLine("/**");
		innerClass.addJavaDocLine("* @Project ChuFangLiuZhuan_PlatForm_Platorm");
		innerClass.addJavaDocLine("* @Package " + innerClass.getType().getPackageName());
		innerClass.addJavaDocLine("* @ClassName " + innerClass.getType().getFullyQualifiedName());
		innerClass.addJavaDocLine("* @TableName " + table.getTableConfiguration().getTableName());
		innerClass.addJavaDocLine("* @Description" + table.getRemarks());
		innerClass.addJavaDocLine("* @JDK version used " + systemPro.getProperty("java.runtime.version"));
		innerClass.addJavaDocLine("* @Author " + systemPro.getProperty("user.name"));
		innerClass.addJavaDocLine(
				"* @Create Date " + cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
		innerClass.addJavaDocLine("* @modify By ");
		innerClass.addJavaDocLine("* @modify Date ");
		innerClass.addJavaDocLine("* @Why&What is modify ");
		innerClass.addJavaDocLine("* @Version 1.0");
		innerClass.addJavaDocLine("*/");
	}
}
