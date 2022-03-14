package github.beeclimb.registry.zk.util;

import github.beeclimb.enums.RpcConfigEnum;
import github.beeclimb.utils.PropertiesFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Curator(zookeeper client) utils
 *
 * @author jun
 * @date 2022/3/10 20:40:00
 */
@Slf4j
public class CuratorUtils {
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";
    private static CuratorFramework zkClient;
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    public static final String ZK_REGISTER_ROOT_PATH = "/rpc";

    private CuratorUtils() {
    }

    /**
     * Create persistent nodes. Unlike temporary nodes, persistent nodes are not removed when the client disconnects
     *
     * @param path node path e.g.: /rpc/github.beeclimb.SeyHelloService/10.150.110.66:22
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node already exists. The node is: [{}]", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The persistent node was created successfully. The node is: [{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.info("create persistent node for path [{}] fail", path);
        }
    }

    /**
     * Gets the children under a node
     *
     * @param rpcServiceName rpc service name.
     * @return All child nodes under the specified node. e.g.: [10.150.110.66:3000, 10.150.110.77:3000, 10.150.110.88:3000]
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        List<String> result = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            log.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }

    /**
     * Registers to listen for changes to the specified node
     *
     * @param rpcServiceName rpc service name.
     */
    public static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                List<String> serviceAddress = curatorFramework.getChildren().forPath(servicePath);
                SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddress);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    /**
     * Empty the registry of data on specified server.
     *
     * @param inetSocketAddress server ip:port
     */
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                if (p.endsWith(inetSocketAddress.toString())) {
                    zkClient.delete().forPath(p);
                }
            } catch (Exception e) {
                log.error("clear registry for path [{}] fail", p);
            }
        });
        log.info("All registered services on the server are cleared: [{}]", REGISTERED_PATH_SET.toString());
    }

    public static CuratorFramework getZkClient() {
        // check if user has set zk address
        Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
        String zookeeperAddress = properties != null
                && properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue()) != null
                ? properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getPropertyValue())
                : DEFAULT_ZOOKEEPER_ADDRESS;
        // if zkClient has been started, return directly
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        // Retry strategy. Retry 3 times, and will increase the sleep time between retries.
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        try {
            // wait 30s until connect to the zookeeper
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkClient;
    }
}
