/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.mvc.mongodb.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.mvc.mongodb.DBConstant;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.MongoDBFactory;
import com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao;
import com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package mongodb.dao.impl
 * @ClassName BaseMongoDaoImpl.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月27日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@SuppressWarnings("unchecked")
public class BaseMongoDaoImpl<T extends BaseMongoEntity, PK extends Serializable> implements BaseMongoDao<T, PK> {
	private static Logger logger = LoggerFactory.getLogger(BaseMongoDaoImpl.class);
	private static final String PK_IS_NOT_NULL = "id不能为空";
	private static final String PK_NAME_MONGODB = "_id";
	private static final String PK_NAME_ENTITY = "id";
	@Resource
	protected MongoClient mongoClient;

	protected String dataBase;

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public MongoCollection<Document> getConCollection() {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = getGenericClass().getName();
		MongoCollection<Document> collection = db.getCollection(className);
		return collection;
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> getGenericClass() {
		Type t = getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ( (ParameterizedType) t ).getActualTypeArguments();
			return ( (Class<?>) p[0] );
		}
		return null;
	}

	@Override
	public T find(T entity) {
		// TODO Auto-generated method stub
		return find(entity, null);
	}

	@Override
	public T find(T entity, List<String> queryProperties) {
		Assert.notEmpty(queryProperties, "查询的属性列表不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = entity.getClass().getName();
		MongoCollection<Document> collection = db.getCollection(className);

		Document document = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			document = collection.find(Filters.eq(DBConstant.PK_FIELD, entity.getId())).first();
		} else {
			document = collection.find(Filters.eq(DBConstant.PK_FIELD, entity.getId())).projection(buildFiedFilter(queryProperties)).first();
		}
		if (document != null) {
			return (T) com.alibaba.fastjson.JSON.parseObject(document.toJson(), entity.getClass());
		} else {
			return null;
		}

	}

	@Override
	public T findById(PK id) {
		// TODO Auto-generated method stub
		return findById(id, null);
	}

	@Override
	public T findById(PK id, List<String> queryProperties) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();

		Document document = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			document = collection.find(new Document(DBConstant.PK_FIELD, id)).first();
		} else {
			document = collection.find(Filters.eq(DBConstant.PK_FIELD, id)).projection(buildFiedFilter(queryProperties)).first();
		}
		if (document != null) {
			return (T) com.alibaba.fastjson.JSON.parseObject(document.toJson(), getGenericClass());
		} else {
			return null;
		}
	}

	@Override
	public List<T> findByIds(List<PK> ids) {
		// TODO Auto-generated method stub
		return findByIds(ids, null);
	}

	@Override
	public List<T> findByIds(List<PK> ids, List<String> queryProperties) {
		List<T> list = new ArrayList<>();
		MongoCollection<Document> collection = getConCollection();

		MongoCursor<Document> cursor = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(Filters.in(DBConstant.PK_FIELD, ids)).iterator();
		} else {
			cursor = collection.find(Filters.in(DBConstant.PK_FIELD, ids)).projection(buildFiedFilter(queryProperties)).iterator();
		}
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
			}
		}
		return list;
	}

	@Override
	public PageInfo<T> findListByPage(Bson condition, Bson sort, Page<T> page) {
		MongoCollection<Document> collection = getConCollection();
		int skip = page.getPageSize() * ( page.getPageNum() - 1 );
		MongoCursor<Document> cursor = collection.find(condition).sort(sort).skip(skip).limit(page.getPageSize()).iterator();
		while (cursor.hasNext()) {
			page.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
		}
		Long count = collection.count(condition);
		page.setTotal(count);
		PageInfo<T> info = new PageInfo<>(page);
		return info;
	}

	@Override
	public PageInfo<T> findListByPage(List<Condition> conditions, Map<String, Integer> sortMap, Page<T> page) {
		Bson filter = null;
		if (CollectionUtils.isEmpty(conditions)) {
			filter = new Document();
		} else {
			LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			filter = logicalCondition.buildConditionBson();
		}

		Bson sort = null;
		if (CollectionUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}

		return findListByPage(filter, sort, page);
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, String> unwindMap, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, String groupJson, Page<T> page) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());
		int skip = page.getPageSize() * ( page.getPageNum() - 1 );
		List<Bson> pipeline = buildPipeline(condition, unwindMap, groupJson, sortMap, projectMap, skip, page.getPageSize());
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		while (cursor.hasNext()) {
			page.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
		}
		Long count = collection.count(condition.buildConditionBson());
		page.setTotal(count);
		PageInfo<T> info = new PageInfo<>(page);
		return info;
	}

	@Override
	public PageInfo<T> findListByPage(Condition condition, Map<String, Integer> sortMap, Map<String, Integer> projectMap, String groupJson,
			Page<T> page) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());
		int skip = page.getPageSize() * ( page.getPageNum() - 1 );
		List<Bson> pipeline = buildPipeline(condition, null, groupJson, sortMap, projectMap, skip, page.getPageSize());
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		while (cursor.hasNext()) {
			page.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
		}
		Long count = collection.count(condition.buildConditionBson());
		page.setTotal(count);
		PageInfo<T> info = new PageInfo<>(page);
		return info;
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return findAll(null);
	}

	@Override
	public List<T> findAll(List<String> queryProperties) {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<>();
		MongoCollection<Document> collection = getConCollection();

		MongoCursor<Document> cursor = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find().iterator();
		} else {
			cursor = collection.find().projection(buildFiedFilter(queryProperties)).iterator();
		}
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
			}

		}
		return list;
	}

	@Override
	public Long count(Condition condition) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		return collection.count(condition.buildConditionBson());
	}

	@Override
	public PK insert(T entity) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(entity.getId())) {
			entity.setId(PKGenerator.generateId());
		}
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = entity.getClass().getName();
		Document document = Document.parse(com.alibaba.fastjson.JSON.toJSONString(entity));
		MongoCollection<Document> collection = db.getCollection(className);
		collection.insertOne(document);
		return (PK) entity.getId();
	}

	@Override
	public Long delete(T entity) {
		// TODO Auto-generated method stub
		Assert.notNull(entity.getId(), PK_IS_NOT_NULL);
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = entity.getClass().getName();
		MongoCollection<DBObject> collection = db.getCollection(className, DBObject.class);
		DeleteResult result = collection.deleteOne(new Document(DBConstant.PK_FIELD, entity.getId()));
		return result.getDeletedCount();
	}

	@Override
	public Long deleteById(PK id) {
		// TODO Auto-generated method stub
		Assert.notNull(id, PK_IS_NOT_NULL);
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteOne(new Document(DBConstant.PK_FIELD, id));
		return result.getDeletedCount();
	}

	@Override
	public Long deleteByIds(List<PK> ids) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteMany(Filters.in(DBConstant.PK_FIELD, ids));
		return result.getDeletedCount();
	}

	@Override
	public Long deleteAll() {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteMany(new Document());
		return result.getDeletedCount();
	}

	@Override
	public Boolean check(Map<String, Serializable> params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long batchDelete(List<T> entities) {
		// TODO Auto-generated method stub
		Long delCount = 0L;
		List<PK> pkIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entities)) {
			for (T entity : entities) {
				if (StringUtils.isNotEmpty(entity.getId())) {
					pkIds.add((PK) entity.getId());
				}
				if (!CollectionUtils.isEmpty(pkIds)) {
					delCount = deleteByIds(pkIds);
				}
			}
		}
		return delCount;
	}

	@Override
	public void batchInsert(List<T> entitys) {
		// TODO Auto-generated method stub
		if (!CollectionUtils.isEmpty(entitys)) {
			MongoDatabase db = mongoClient.getDatabase(currentDataBase());
			String className = entitys.get(0).getClass().getName();
			List<Document> documents = new ArrayList<>();
			for (T entity : entitys) {
				if (StringUtils.isBlank(entity.getId())) {
					entity.setId(PKGenerator.generateId());
				}
				Document document = Document.parse(com.alibaba.fastjson.JSON.toJSONString(entity));
				documents.add(document);
			}
			MongoCollection<Document> collection = db.getCollection(className);
			collection.insertMany(documents);
		}
	}

	@Override
	public Long batchUpdate(List<T> entitys) {
		// TODO Auto-generated method stub
		if (!CollectionUtils.isEmpty(entitys)) {
			MongoDatabase db = mongoClient.getDatabase(currentDataBase());
			String className = entitys.get(0).getClass().getName();

			// 根据mongoDB 官方文档db.runCommand语法封装
			Document command = new Document();
			command.put("update", className);

			List<Document> updates = new ArrayList<>();
			for (T entity : entitys) {
				Document update = new Document();
				if (StringUtils.isBlank(entity.getId())) {
					logger.warn("exec batchUpdate warn.entity's id is null,update is canceled.data:" + JSON.toJSONString(entity));
					continue;
				}
				update.put("q", new Document(DBConstant.PK_FIELD, entity.getId()));

				Document dataMap = Document.parse(JSON.toJSONString(entity));
				Document updateDocument = new Document("$set", dataMap);
				update.put("u", updateDocument);
				update.put("upsert", false);
				update.put("multi", true);
				updates.add(update);
			}

			command.put("updates", updates);
			command.put("ordered", false);

			Document result = db.runCommand(command);
			float status = Float.parseFloat(result.get("ok").toString());
			if (status == 1) {
				return Long.parseLong(result.get("n").toString());
			} else {
				logger.error("exec batchUpdate error.datas:" + JSON.toJSONString(entitys));
				return 0L;
			}
		}
		return 0L;
	}

	private String currentDataBase() {
		if (StringUtils.isBlank(dataBase)) {
			dataBase = MongoDBFactory.DEFAULT_DATA_BASE;
		}
		return dataBase;
	}

	/**
	 * @Description 生成多个属性的索引
	 * @param fieldNames
	 * @return 索引Name
	 * @date 2017年6月5日
	 */
	@Override
	public List<String> createIndex(List<String> fieldNames) {
		List<String> indexNames = new ArrayList<>();
		if (!CollectionUtils.isEmpty(fieldNames)) {
			MongoCollection<Document> collection = getConCollection();
			String indexName = null;
			for (String fieldName : fieldNames) {
				indexName = collection.createIndex(Indexes.ascending(fieldName));
				indexNames.add(indexName);
			}
		}
		return indexNames;
	}

	@Override
	public List<T> findByCondition(Bson queryCond) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = collection.find(queryCond).iterator();
		List<T> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
		}
		return findByCondition(queryCond, null);
	}

	/**
	 * @Description 根据查询条件查找对象
	 * @param queryCond
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年6月5日
	 */
	@Override
	public List<T> findByCondition(Bson queryCond, List<String> queryProperties) {
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = null;
		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(queryCond).iterator();
		} else {
			cursor = collection.find(queryCond).projection(buildFiedFilter(queryProperties)).iterator();
		}

		List<T> list = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
			}
		}
		return list;
	}

	/**
	 * @Description 根据条件删除数据
	 * @param queryCond
	 * @return
	 * @date 2017年8月7日
	 */
	@Override
	public Long deleteByCondition(Bson queryCond) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		DeleteResult result = collection.deleteMany(queryCond);
		return result.getDeletedCount();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao#update(com.sunshine.framework.mvc.mongodb.entity.BaseMongoEntity) 更新所有属性
	 *      包括entity中为null的字段 为null的字段将更新为空白字符
	 */
	@Override
	public Long updateWithNull(T entity) {
		// TODO Auto-generated method stub
		Assert.notNull(entity.getId(), "id不允许为空!");
		return updateAllField(entity.getId(), Document.parse(JSON.toJSONString(entity)));
	}

	/**
	 * @Description 根据Id更新dataMap中的数据
	 * @param id
	 * @param updateMap
	 * @return
	 * @date 2017年8月7日
	 */
	@Override
	public Long updateById(String id, Map<String, Object> updateMap) {
		return updateByIdForDataMap(id, updateMap);
	}

	@Override
	public Long updateByIds(List<String> ids, Map<String, Object> updateMap) {
		return updateByIdsForDataMap(ids, updateMap);
	}

	/**
	 * @Description 根据Id更新所有属性 包括entity中为null的字段 为null的字段将更新为空白字符
	 * @param id
	 * @param document
	 * @return
	 * @date 2017年8月7日
	 */
	private Long updateAllField(String id, Document document) {
		Assert.notNull(id, PK_IS_NOT_NULL);
		MongoCollection<Document> collection = getConCollection();
		UpdateResult result = collection.replaceOne(new Document(DBConstant.PK_FIELD, id), document);
		return result.getModifiedCount();
	}

	@Override
	public Long updateNotWithNull(T entity) {
		// TODO Auto-generated method stub
		Assert.notNull(entity.getId(), PK_IS_NOT_NULL);
		Map<String, Object> dataMap = JSON.parseObject(JSON.toJSONString(entity, SerializerFeature.NotWriteDefaultValue));
		return updateByIdForDataMap(entity.getId(), dataMap);
	}

	@Override
	public Long updateByIdForDataMap(String id, Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		Assert.notEmpty(dataMap, "更新信息dataMap不能为空");
		Assert.notNull(id, "id不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = Filters.eq(DBConstant.PK_FIELD, id);

		Document document = Document.parse(JSON.toJSONString(dataMap));
		List<Bson> setBsons = new ArrayList<>();

		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			if (val instanceof JSONObject) {
				val = jsonObjectConvertToBsonString((JSONObject) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateOne(filter, update);
		return result.getModifiedCount();
	}

	@Override
	public Long updateByIdsForDataMap(List<String> ids, Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		Assert.notEmpty(dataMap, "更新信息dataMap不能为空");
		Assert.notNull(ids, "id集合不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = Filters.in(DBConstant.PK_FIELD, ids);

		Document document = Document.parse(JSON.toJSONString(dataMap));
		List<Bson> setBsons = new ArrayList<>();

		for (String key : document.keySet()) {
			setBsons.add(Updates.set(key, document.get(key)));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	private BsonArray jsonArrayConvertToBsonArray(JSONArray jsonArray) {
		String jsonData = jsonArray.toJSONString();
		return BsonArray.parse(jsonData);
	}

	private BsonString jsonObjectConvertToBsonString(JSONObject jsonObject) {
		String jsonData = jsonObject.toJSONString();
		return new BsonString(jsonData);
	}

	/**
	 * @Description 根据多个eq条件 and连接查询
	 * @param conditionMap
	 * @param updateMap
	 * @date 2017年6月6日
	 */
	@Override
	public Long updateByParams(Map<String, Object> conditionMap, Map<String, Object> updateMap) {
		Assert.notEmpty(updateMap, "更新信息不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (CollectionUtils.isEmpty(conditionMap)) {
			filter = new Document();
		} else {
			List<Bson> filters = new ArrayList<>();
			for (String key : conditionMap.keySet()) {
				filters.add(Filters.eq(key, conditionMap.get(key)));
			}
			filter = Filters.and(filters);
		}

		List<Bson> setBsons = new ArrayList<>();
		Document document = Document.parse(JSON.toJSONString(updateMap));
		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	/**
	 * @Description 根据多个条件更新信息 and连接
	 * @param conditions
	 * @param updateMap
	 * @date 2017年8月8日
	 * @return 更新记录的条数
	 */
	@Override
	public Long updateByAndCondition(List<Condition> conditions, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		Assert.notEmpty(updateMap, "更新信息不能为空");
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (CollectionUtils.isEmpty(conditions)) {
			filter = new Document();
		} else {
			LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			filter = logicalCondition.buildConditionBson();
		}

		List<Bson> setBsons = new ArrayList<>();
		Document document = Document.parse(JSON.toJSONString(updateMap));
		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			setBsons.add(Updates.set(key, val));
		}
		Bson update = Updates.combine(setBsons);

		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	@Override
	public List<T> findByLogicalCondition(LogicalCondition condition) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = collection.find(condition.buildConditionBson()).iterator();
		List<T> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
		}
		return list;
	}

	/**
	 * @Description 根据查询条件查询
	 * @param condition
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年8月8日
	 */
	@Override
	public List<T> findByLogicalCondition(LogicalCondition condition, List<String> queryProperties) {
		MongoCollection<Document> collection = getConCollection();
		MongoCursor<Document> cursor = null;

		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(condition.buildConditionBson()).iterator();
		} else {
			cursor = collection.find(condition.buildConditionBson()).projection(buildFiedFilter(queryProperties)).iterator();
		}
		List<T> list = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), getGenericClass()));
			}
		}
		return list;
	}

	@Override
	public Long updateByLogicalCondition(LogicalCondition condition, Map<String, Object> updateMap) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (condition == null) {
			filter = new Document();
		} else {
			filter = condition.buildConditionBson();
		}

		List<Bson> setBsons = new ArrayList<>();
		Document document = Document.parse(JSON.toJSONString(updateMap));
		for (String key : document.keySet()) {
			Object val = document.get(key);
			if (val instanceof JSONArray) {
				val = jsonArrayConvertToBsonArray((JSONArray) val);
			}
			setBsons.add(Updates.set(key, val));
			// setBsons.add(Updates.set(key, updateMap.get(key)));
		}
		Bson update = Updates.combine(setBsons);
		UpdateResult result = collection.updateMany(filter, update);
		return result.getModifiedCount();
	}

	@Override
	public Long deleteByLogicalCondition(LogicalCondition condition) {
		// TODO Auto-generated method stub
		MongoCollection<Document> collection = getConCollection();
		Bson filter = null;
		if (condition == null) {
			filter = new Document();
		} else {
			filter = condition.buildConditionBson();
		}
		DeleteResult result = collection.deleteMany(filter);
		return result.getDeletedCount();
	}

	/**
	 * @Description 根据条件查询指定实体类
	 * @param clazz
	 * @param condition
	 * @return
	 * @date 2017年9月27日
	 */
	@Override
	public <E> List<E> query(Class<E> clazz, Condition condition) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(clazz.getName());
		MongoCursor<Document> cursor = collection.find(condition.buildConditionBson()).iterator();
		List<E> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), clazz));
		}
		return list;
	}

	/**
	 * @Description 根据条件查询指定实体类
	 * @param clazz
	 * @param condition
	 * @param queryProperties
	 *            需要查询的实体类属性列表
	 * @return
	 * @date 2017年9月27日
	 */
	@Override
	public <E> List<E> query(Class<E> clazz, Condition condition, List<String> queryProperties) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(clazz.getName());
		MongoCursor<Document> cursor = null;

		if (CollectionUtils.isEmpty(queryProperties)) {
			cursor = collection.find(condition.buildConditionBson()).iterator();
		} else {
			cursor = collection.find(condition.buildConditionBson()).projection(buildFiedFilter(queryProperties)).iterator();
		}

		List<E> list = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				list.add(com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), clazz));
			}
		}
		return list;
	}

	/**
	 * @Description
	 * @param queryProperties
	 *            需要查询的属性名
	 * @return
	 * @date 2017年10月18日
	 */
	private Document buildFiedFilter(List<String> queryProperties) {
		int index = 0;
		boolean isHadPK = false;
		for (String property : queryProperties) {
			if (property.equalsIgnoreCase(PK_NAME_ENTITY) || property.equalsIgnoreCase(PK_NAME_MONGODB)) {
				isHadPK = true;
				break;
			}
			index++;
		}
		if (isHadPK) {
			queryProperties.remove(index);
		}
		queryProperties.add(PK_NAME_MONGODB);

		Document fieldFilter = new Document();
		for (String propertyName : queryProperties) {
			fieldFilter.put(propertyName, 1);
		}

		return fieldFilter;
	}

	/**
	 * @Description
	 * @param condition
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @return
	 * @date 2017年10月20日
	 */
	@Override
	public List<String> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit) {
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, null, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<String> jsonList = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				jsonList.add(cursor.next().toJson());
			}
		}
		return jsonList;

	}

	@Override
	public Long aggregatesCount(Condition condition, Map<String, String> unwindMap, String groupJson) {
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, null, groupJson, null, null, null, null);
		Bson countBson = new Document("$count", "count");
		pipeline.add(countBson);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		String count = null;
		if (cursor != null) {
			while (cursor.hasNext()) {
				count = cursor.next().toJson();
			}
		}
		if (count != null) {
			JSONObject object = JSON.parseObject(count);
			count = object.getString("count");
			return Long.valueOf(count);
		}
		return 0L;
	}

	private List<Bson> buildPipeline(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit) {
		List<Bson> pipeline = new ArrayList<>();

		if (condition != null) {
			Document match = new Document();
			match.put("$match", condition.buildConditionDocument());
			pipeline.add(match);
		}

		Bson unwind = null;
		if (!CollectionUtils.isEmpty(unwindMap)) {
			unwind = Document.parse(JSON.toJSONString(unwindMap));
			pipeline.add(unwind);
		}

		Bson sortBson = null;
		if (!CollectionUtils.isEmpty(sortMap)) {
			sortBson = Document.parse(JSON.toJSONString(sortMap));
			Document sortDocument = new Document();
			sortDocument.put("$sort", sortBson);
			pipeline.add(sortDocument);
		}

		Bson project = null;
		if (!CollectionUtils.isEmpty(projectMap)) {
			project = Document.parse(JSON.toJSONString(projectMap));
			Document projectdDoc = new Document();
			projectdDoc.put("$project", project);
			pipeline.add(projectdDoc);
		}

		Document group = Document.parse(groupJson);
		pipeline.add(group);

		Bson skipBson = null;
		if (limit != null) {
			skipBson = new Document("$skip", skip);
			pipeline.add(skipBson);
		}

		Bson limitBson = null;
		if (limit != null) {
			limitBson = new Document("$limit", limit);
			pipeline.add(limitBson);
		}
		return pipeline;
	}

	/**
	 * @Description
	 * @param condition
	 * @param groupJson
	 *            分组的Json表达式
	 * @param sortMap
	 *            排序 propertyName:1/-1 不使用排序 传入null
	 * @param projectMap
	 *            字段过滤 propertyName:1/-1 不使用字段过滤 传入null
	 * @param limit
	 *            传null 表示不进行limit操作
	 * @param skip
	 *            传null 表示不进行skip操作 limit+skip 可进行分页操作
	 * @param resultClass
	 *            查询结果自动转换为的类型
	 * @return
	 * @date 2017年10月20日
	 */
	@Override
	public <E> List<E> aggregatesQuery(Condition condition, String groupJson, Map<String, Integer> sortMap, Map<String, Integer> projectMap,
			Integer skip, Integer limit, Class<E> resultClass) {

		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, null, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<E> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), resultClass));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao#aggregatesQuery(com.sunshine.framework.mvc.mongodb.vo.Condition,
	 * com.sunshine.framework.mvc.mongodb.vo.Condition, java.lang.String, java.util.Map, java.util.Map, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<String> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit) {
		// TODO Auto-generated method stub
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<String> jsonList = new ArrayList<>();
		if (cursor != null) {
			while (cursor.hasNext()) {
				jsonList.add(cursor.next().toJson());
			}
		}
		return jsonList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.framework.mvc.mongodb.dao.BaseMongoDao#aggregatesQuery(com.sunshine.framework.mvc.mongodb.vo.Condition,
	 * com.sunshine.framework.mvc.mongodb.vo.Condition, java.lang.String, java.util.Map, java.util.Map, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public <E> PageInfo<E> aggregatesQuery(Condition condition, Map<String, String> unwindMap, String groupJson, Map<String, Integer> sortMap,
			Map<String, Integer> projectMap, Integer skip, Integer limit, Class<E> clazz) {
		// TODO Auto-generated method stub
		PageInfo<E> info = new PageInfo<>(new ArrayList<E>());
		Assert.notNull(groupJson, "分组表达式不能为空");
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		MongoCollection<Document> collection = db.getCollection(getGenericClass().getName());

		List<Bson> pipeline = buildPipeline(condition, unwindMap, groupJson, sortMap, projectMap, skip, limit);
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		if (cursor != null) {
			while (cursor.hasNext()) {
				info.getList().add(com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), clazz));
			}
		}
		Long count = collection.count(condition.buildConditionBson());
		info.setTotal(count);
		info.setPageSize(limit);
		return info;
	}

}
