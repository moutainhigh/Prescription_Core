/**
 * <html>
 * <body>
 *  <P>  Copyright 2017 阳光康众</p>
 *  <p>  All rights reserved.</p>
 *  <p> Created on 2015-7-20</p>
 *  <p> Created by 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.hash;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import redis.clients.util.Hashing;
import redis.clients.util.MurmurHash;

/**
 * @Package: com.sunshine.mobileapp.common.hash
 * @ClassName: HashingTableNameGenerator
 * @Statement: <p>
 *             hash一致性算法分表
 *             </p>
 * @JDK version used: 1.6
 * @Author: Yuce
 * @Create Date: 2015-7-20
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HashingTableNameGenerator {
	/**
	 * 虚拟节点数
	 */
	public static final int DUMMY_NODE_NUM = 3000;

	/**
	 * MurmurHash算法
	 */
	public static final Hashing MURMUR_HASH = new MurmurHash();

	/**
	 * hash存储
	 */
	private static final Map<String, TreeMap<Long, String>> resources = new LinkedHashMap<String, TreeMap<Long, String>>();
	public static final int subTableCount = 10;

	/**
	 * 挂号记录的表名
	 */
	public static final String REGISTER_RECORD_TABLE_NAME = "BIZ_REGISTER_RECORD";

	/**
	 * 就诊卡的表名
	 */
	public static final String MEDICAL_CARD_TABLE_NAME = "BIZ_MEDICAL_CARD";

	public static String[] tableNames = new String[] { REGISTER_RECORD_TABLE_NAME, MEDICAL_CARD_TABLE_NAME };

	static {
		TreeMap<Long, String> nodes = null;
		List<String> subTableNames = null;
		for (String tableName : tableNames) {
			subTableNames = new ArrayList<String>();
			for (int i = 1; i < subTableCount + 1; i++) {
				subTableNames.add(tableName.concat("_") + i);
			}
			nodes = new TreeMap<Long, String>();
			for (int i = 0; i != subTableNames.size(); ++i) {
				final String subTableName = subTableNames.get(i);
				for (int n = 0; n < DUMMY_NODE_NUM; n++) {
					nodes.put(MURMUR_HASH.hash("hashing" + i + "-table-" + n), subTableName);
				}
			}
			resources.put(tableName, nodes);
		}
	}

	/**
	 * @Description 得到分表的表名
	 * @param tableName
	 *            主表的表名
	 * @param hashKey
	 *            hash分表的属性
	 * @return
	 * @date 2017年6月26日
	 */
	public static String getDefHashSubTableName(String tableName, String hashKey) {
		String hashTableName = null;
		TreeMap<Long, String> nodes = resources.get(tableName);
		SortedMap<Long, String> tail = nodes.tailMap(MURMUR_HASH.hash(hashKey));
		if (tail.isEmpty()) {
			hashTableName = nodes.get(nodes.firstKey());
		} else {
			hashTableName = tail.get(tail.firstKey());
		}
		return hashTableName;
	}
}
