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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.springframework.util.CollectionUtils;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.framework.common.mybatisgenerator
 * @ClassName CombaPluginAdapter.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年7月9日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class CombaPluginAdapter extends PluginAdapter {
	private static java.util.concurrent.ConcurrentHashMap<String, Boolean> addSerialUIDMap = new ConcurrentHashMap<>();
	private String searchString;
	private String replaceString;
	private Pattern pattern;

	public CombaPluginAdapter() {
	}

	@Override
	public boolean validate(List<String> warnings) {
		searchString = properties.getProperty("searchString", "Mapper"); //$NON-NLS-1$
		replaceString = properties.getProperty("replaceString", "Dao"); //$NON-NLS-1$

		boolean valid = stringHasValue(searchString) && stringHasValue(replaceString);

		if (valid) {
			pattern = Pattern.compile(searchString);
		} else {
			if (!stringHasValue(searchString)) {
				warnings.add(getString("ValidationError.18", //$NON-NLS-1$
						"CombaPluginAdapter", //$NON-NLS-1$
						"searchString")); //$NON-NLS-1$
			}
			if (!stringHasValue(replaceString)) {
				warnings.add(getString("ValidationError.18", //$NON-NLS-1$
						"CombaPluginAdapter", //$NON-NLS-1$
						"replaceString")); //$NON-NLS-1$
			}
		}

		return valid;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String oldType = introspectedTable.getMyBatis3JavaMapperType();
		Matcher matcher = pattern.matcher(oldType);
		oldType = matcher.replaceAll(replaceString);

		if (oldType.startsWith("Sys")) {
			oldType = oldType.replaceFirst("Sys", "");
		}

		introspectedTable.setMyBatis3JavaMapperType(oldType);

		introspectedTable.setDeleteByPrimaryKeyStatementId("deleteById");
		introspectedTable.setSelectByPrimaryKeyStatementId("findById");
		introspectedTable.setInsertSelectiveStatementId("insertWithoutNull");
		introspectedTable.setUpdateByPrimaryKeySelectiveStatementId("update");
		introspectedTable.setUpdateByPrimaryKeyStatementId("updateById");
		introspectedTable.setSelectAllStatementId("findAll");
	}

	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		return super.sqlMapSelectAllElementGenerated(element, introspectedTable);
	}

	/**
	 * 生成dao
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType("BaseDao<" + introspectedTable.getBaseRecordType() + ",String>");
		interfaze.addSuperInterface(superInterface);//
		interfaze.addImportedType(new FullyQualifiedJavaType("com.sunshine.framework.mvc.mysql.dao.BaseDao"));
		interfaze.getMethods().clear();
		return true;
	}

	@Override
	public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
			IntrospectedTable introspectedTable, ModelClassType modelClassType) {
		// TODO Auto-generated method stub
		Boolean isAdd = addSerialUIDMap.get(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		if (isAdd == null || !isAdd) {
			addSerialVersionUID(topLevelClass, introspectedTable);
			addSerialUIDMap.put(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime(), true);
		}
		return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	private void addSerialVersionUID(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		CommentGenerator commentGenerator = context.getCommentGenerator();
		Field field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setType(new FullyQualifiedJavaType("long"));
		field.setStatic(true);
		field.setFinal(true);
		field.setName("serialVersionUID");
		field.setInitializationString("" + UUID.randomUUID().toString().hashCode());
		commentGenerator.addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);
	}

	/**
	 * @Description 生成where
	 * @param introspectedTable
	 * @return
	 * @date 2017年7月9日
	 */
	private XmlElement generatWhereSql(IntrospectedTable introspectedTable) {
		// 添加sql——where
		XmlElement whereSql = new XmlElement("sql");
		whereSql.addAttribute(new Attribute("id", "Base_Column_Where"));
		XmlElement where = new XmlElement("where");
		StringBuilder sb = new StringBuilder();
		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" != null"); //$NON-NLS-1$
			isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
			where.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append(" and ");
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		whereSql.addElement(where);
		return whereSql;
	}

	/**
	 * @Description 生成findAll
	 * @param introspectedTable
	 * @return
	 * @date 2017年7月9日
	 */
	private XmlElement generatFindAll(IntrospectedTable introspectedTable) {
		// 添加getList
		XmlElement findAll = new XmlElement("select");
		findAll.addAttribute(new Attribute("id", "findAll"));
		findAll.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		findAll.addElement(new TextElement(" select "));
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "Base_Column_List"));
		findAll.addElement(include);
		findAll.addElement(new TextElement(" from " + introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase()));
		return findAll;
	}

	/**
	 * @Description 生成deleteByIds
	 * @param introspectedTable
	 * @return
	 * @date 2017年7月9日
	 */
	private XmlElement generatDeleteByIds(IntrospectedTable introspectedTable) {
		// 添加getList
		XmlElement deleteByIds = new XmlElement("delete");
		deleteByIds.addAttribute(new Attribute("id", "deleteByIds"));
		deleteByIds.addAttribute(new Attribute("parameterType", "java.util.List"));
		deleteByIds.addElement(new TextElement(" delete from "));
		deleteByIds.addElement(new TextElement(introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase()));

		XmlElement where = new XmlElement("where ");
		List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
		if (CollectionUtils.isEmpty(columns)) {
			columns = introspectedTable.getNonPrimaryKeyColumns();
		}
		where.addElement(new TextElement(columns.get(0).getActualColumnName() + " in "));

		XmlElement foreachElement = new XmlElement("foreach"); //$NON-NLS-1$
		foreachElement.addAttribute(new Attribute("collection", "list"));
		foreachElement.addAttribute(new Attribute("item", "item"));
		foreachElement.addAttribute(new Attribute("index", "index"));
		foreachElement.addAttribute(new Attribute("open", "("));
		foreachElement.addAttribute(new Attribute("separator", ","));
		foreachElement.addAttribute(new Attribute("close", ")"));
		foreachElement.addElement(new TextElement(" #{item} "));

		where.addElement(foreachElement);
		deleteByIds.addElement(where);
		return deleteByIds;
	}

	/**
	 * @Description 生成findByIds
	 * @param introspectedTable
	 * @return
	 * @date 2017年7月9日
	 */
	private XmlElement generatFindByIds(IntrospectedTable introspectedTable) {
		// 添加getList
		XmlElement findByIds = new XmlElement("select");
		findByIds.addAttribute(new Attribute("id", "findByIds"));
		findByIds.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		findByIds.addAttribute(new Attribute("parameterType", "java.util.List"));
		findByIds.addElement(new TextElement(" select "));
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "Base_Column_List"));
		findByIds.addElement(include);
		findByIds.addElement(new TextElement(" from " + introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase()));

		XmlElement where = new XmlElement("where ");
		List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
		if (CollectionUtils.isEmpty(columns)) {
			columns = introspectedTable.getNonPrimaryKeyColumns();
		}
		where.addElement(new TextElement(columns.get(0).getActualColumnName() + " in "));

		XmlElement foreachElement = new XmlElement("foreach"); //$NON-NLS-1$
		foreachElement.addAttribute(new Attribute("collection", "list"));
		foreachElement.addAttribute(new Attribute("item", "item"));
		foreachElement.addAttribute(new Attribute("index", "index"));
		foreachElement.addAttribute(new Attribute("open", "("));
		foreachElement.addAttribute(new Attribute("separator", ","));
		foreachElement.addAttribute(new Attribute("close", ")"));
		foreachElement.addElement(new TextElement(" #{item} "));

		where.addElement(foreachElement);
		findByIds.addElement(where);
		return findByIds;
	}

	/**
	 * @Description 生成findAll
	 * @param introspectedTable
	 * @return
	 * @date 2017年7月9日
	 */
	private XmlElement generatFindListByPage(IntrospectedTable introspectedTable) {
		// 添加getList
		XmlElement findListByPage = new XmlElement("select");
		findListByPage.addAttribute(new Attribute("id", "findListByPage"));
		findListByPage.addAttribute(new Attribute("resultMap", "BaseResultMap"));
		findListByPage.addAttribute(new Attribute("parameterType", "java.lang.String"));
		findListByPage.addElement(new TextElement(" select "));
		XmlElement include = new XmlElement("include");
		include.addAttribute(new Attribute("refid", "Base_Column_List"));
		findListByPage.addElement(include);
		findListByPage.addElement(new TextElement(" from " + introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase()));

		XmlElement where = new XmlElement("where");
		where.addElement(new TextElement(" 1=1 "));
		StringBuilder sb = new StringBuilder();
		XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
		sb.append("searchValue != null and searchValue != ''");
		isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
		isNotNullElement.addElement(new TextElement("and ( "));

		sb = new StringBuilder();
		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
			if (sb.length() > 0) {
				sb.append(" or ");
			}
			sb.append("(" + introspectedColumn.getActualColumnName() + " like CONCAT('%',trim('${searchValue}'),'%' ) ) ");
		}
		isNotNullElement.addElement(new TextElement(sb.toString()));
		isNotNullElement.addElement(new TextElement(" )"));
		where.addElement(isNotNullElement);

		findListByPage.addElement(where);
		return findListByPage;
	}

	/**
	 * @Description 生成batchInsert
	 * @param introspectedTable
	 * @return
	 * @date 2017年7月9日
	 */
	private XmlElement generatBatchInsert(IntrospectedTable introspectedTable) {
		// 添加getList
		XmlElement batchInsert = new XmlElement("insert");
		batchInsert.addAttribute(new Attribute("id", "batchInsert"));
		batchInsert.addAttribute(new Attribute("parameterType", "java.util.List"));
		batchInsert.addElement(new TextElement(" insert into "));
		batchInsert.addElement(new TextElement(introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase()));

		StringBuffer sb = new StringBuffer();
		sb.append("(");
		int index = 0;
		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			if (index > 0) {
				sb.append(",");
			}
			sb.append(" " + introspectedColumn.getActualColumnName());
			index++;
		}
		sb.append(")");
		batchInsert.addElement(new TextElement(sb.toString()));
		batchInsert.addElement(new TextElement("values"));

		XmlElement foreachElement = new XmlElement("foreach"); //$NON-NLS-1$
		foreachElement.addAttribute(new Attribute("collection", "list"));
		foreachElement.addAttribute(new Attribute("item", "entity"));
		foreachElement.addAttribute(new Attribute("index", "index"));
		foreachElement.addAttribute(new Attribute("separator", ","));

		foreachElement.addElement(new TextElement("("));
		sb.setLength(0);

		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			String column = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
			String subHead = column.substring(0, 2);
			String subOther = column.substring(2);
			column = subHead.concat("entity.").concat(subOther);
			sb.append(column);
		}
		foreachElement.addElement(new TextElement(sb.toString()));
		foreachElement.addElement(new TextElement(")"));

		batchInsert.addElement(foreachElement);
		return batchInsert;
	}

	/**
	 * 生成mapping 添加自定义sql
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement parentElement = document.getRootElement();

		// 添加sql——where
		XmlElement element = generatWhereSql(introspectedTable);
		parentElement.addElement(element);

		// 添加findAll
		element = generatFindAll(introspectedTable);
		parentElement.addElement(element);

		// findListByPage
		element = generatFindListByPage(introspectedTable);
		parentElement.addElement(element);

		// findByIds
		element = generatFindByIds(introspectedTable);
		parentElement.addElement(element);

		// deleteByIds
		element = generatDeleteByIds(introspectedTable);
		parentElement.addElement(element);

		// batchInsert
		element = generatBatchInsert(introspectedTable);
		parentElement.addElement(element);

		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

}
