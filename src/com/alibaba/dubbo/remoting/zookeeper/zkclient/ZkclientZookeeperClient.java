package com.alibaba.dubbo.remoting.zookeeper.zkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.zookeeper.ChildListener;
import com.alibaba.dubbo.remoting.zookeeper.StateListener;
import com.alibaba.dubbo.remoting.zookeeper.support.AbstractZookeeperClient;

public class ZkclientZookeeperClient extends AbstractZookeeperClient<IZkChildListener> {

	private final ZkClient client;

	private volatile KeeperState state = KeeperState.SyncConnected;

	public ZkclientZookeeperClient(URL url) {
		super(url);
		client = new ZkClient(url.getBackupAddress(), url.getParameter(Constants.SESSION_TIMEOUT_KEY, Constants.DEFAULT_SESSION_TIMEOUT),
				url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_REGISTRY_CONNECT_TIMEOUT));
		client.subscribeStateChanges(new IZkStateListener() {
			@Override
			public void handleStateChanged(KeeperState state) throws Exception {
				ZkclientZookeeperClient.this.state = state;
				if (state == KeeperState.Disconnected) {
					stateChanged(StateListener.DISCONNECTED);
				} else if (state == KeeperState.SyncConnected) {
					stateChanged(StateListener.CONNECTED);
				}
			}

			@Override
			public void handleNewSession() throws Exception {
				stateChanged(StateListener.RECONNECTED);
			}

			@Override
			public void handleSessionEstablishmentError(Throwable arg0) throws Exception {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void createPersistent(String path) {
		try {
			client.createPersistent(path, true);
		} catch (ZkNodeExistsException e) {
		}
	}

	@Override
	public void createEphemeral(String path) {
		try {
			client.createEphemeral(path);
		} catch (ZkNodeExistsException e) {
		}
	}

	@Override
	public void delete(String path) {
		try {
			client.delete(path);
		} catch (ZkNoNodeException e) {
		}
	}

	@Override
	public List<String> getChildren(String path) {
		try {
			return client.getChildren(path);
		} catch (ZkNoNodeException e) {
			return null;
		}
	}

	@Override
	public boolean isConnected() {
		return state == KeeperState.SyncConnected;
	}

	@Override
	public void doClose() {
		client.close();
	}

	@Override
	public IZkChildListener createTargetChildListener(String path, final ChildListener listener) {
		return new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				listener.childChanged(parentPath, currentChilds);
			}
		};
	}

	@Override
	public List<String> addTargetChildListener(String path, final IZkChildListener listener) {
		return client.subscribeChildChanges(path, listener);
	}

	@Override
	public void removeTargetChildListener(String path, IZkChildListener listener) {
		client.unsubscribeChildChanges(path, listener);
	}

}
