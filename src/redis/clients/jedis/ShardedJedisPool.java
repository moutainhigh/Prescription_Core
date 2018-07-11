package redis.clients.jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.util.Hashing;
import redis.clients.util.Pool;

public class ShardedJedisPool extends Pool<ShardedJedis> {
	private static final String CONFIG_SPLIT_CHAR = ",";

	public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards) {
		this(poolConfig, shards, Hashing.MURMUR_HASH);
	}

	public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Hashing algo) {
		this(poolConfig, shards, algo, null);
	}

	public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Pattern keyTagPattern) {
		this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
	}

	/**
	 * @param poolConfig
	 * @param hosts
	 * @param names
	 * @param ports
	 * @param passwords
	 * @param timeouts
	 * @param weights
	 */
	public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, String hosts, String names, String ports, String timeouts, String weights) {
		String[] hostArray = hosts.split(CONFIG_SPLIT_CHAR);
		String[] portsArray = ports.split(CONFIG_SPLIT_CHAR);
		String[] timeoutsArray = timeouts.split(CONFIG_SPLIT_CHAR);
		String[] weightsArray = weights.split(CONFIG_SPLIT_CHAR);
		String[] namesArray = null;
		if (StringUtils.isNotEmpty(names)) {
			namesArray = names.split(CONFIG_SPLIT_CHAR);
		}

		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		int index = 0;
		JedisShardInfo shardInfo = null;
		String name = null;
		for (String host : hostArray) {
			if (namesArray == null) {
				name = "shardPool-" + index;
			} else {
				if (namesArray.length > (index + 1)) {
					name = namesArray[index].trim();
				} else {
					name = "shardPool-" + index;
				}
			}

			shardInfo = new JedisShardInfo(host, name, Integer.valueOf(portsArray[index].trim()), Integer.valueOf(timeoutsArray[index].trim()),
					Integer.valueOf(weightsArray[index].trim()));
			index = index + 1;
			shards.add(shardInfo);
		}
	}

	public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
		super(poolConfig, new ShardedJedisFactory(shards, algo, keyTagPattern));
	}

	@Override
	public ShardedJedis getResource() {
		ShardedJedis jedis = super.getResource();
		jedis.setDataSource(this);
		return jedis;
	}

	@Override
	public void returnBrokenResource(final ShardedJedis resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	@Override
	public void returnResource(final ShardedJedis resource) {
		if (resource != null) {
			resource.resetState();
			returnResourceObject(resource);
		}
	}

	/**
	 * PoolableObjectFactory custom impl.
	 */
	private static class ShardedJedisFactory implements PooledObjectFactory<ShardedJedis> {
		private List<JedisShardInfo> shards;
		private Hashing algo;
		private Pattern keyTagPattern;

		public ShardedJedisFactory(List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
			this.shards = shards;
			this.algo = algo;
			this.keyTagPattern = keyTagPattern;
		}

		@Override
		public PooledObject<ShardedJedis> makeObject() throws Exception {
			ShardedJedis jedis = new ShardedJedis(shards, algo, keyTagPattern);
			return new DefaultPooledObject<ShardedJedis>(jedis);
		}

		@Override
		public void destroyObject(PooledObject<ShardedJedis> pooledShardedJedis) throws Exception {
			final ShardedJedis shardedJedis = pooledShardedJedis.getObject();
			for (Jedis jedis : shardedJedis.getAllShards()) {
				try {
					try {
						jedis.quit();
					} catch (Exception e) {

					}
					jedis.disconnect();
				} catch (Exception e) {

				}
			}
		}

		@Override
		public boolean validateObject(PooledObject<ShardedJedis> pooledShardedJedis) {
			try {
				ShardedJedis jedis = pooledShardedJedis.getObject();
				for (Jedis shard : jedis.getAllShards()) {
					if (!shard.ping().equals("PONG")) {
						return false;
					}
				}
				return true;
			} catch (Exception ex) {
				return false;
			}
		}

		@Override
		public void activateObject(PooledObject<ShardedJedis> p) throws Exception {

		}

		@Override
		public void passivateObject(PooledObject<ShardedJedis> p) throws Exception {

		}
	}
}