package github.beeclimb.registry.zk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CuratorUtilsTest {
    @Test
    public void ZkClientTest() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework zkClient;
        zkClient = CuratorFrameworkFactory.builder()
                .connectString("47.101.50.78:2181")
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/my-rpc/1.1.1.1:1");
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/my-rpc/1.1.1.2:2");
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/my-rpc/1.1.1.3:3");
        List<String> forPath = zkClient.getChildren().forPath("/");
        System.out.println(forPath.toString());
        System.out.println(zkClient.getChildren().forPath("/my-rpc").toString());
    }

    @Test
    public void clearTest() {
        Set<String> setString = ConcurrentHashMap.newKeySet();
        setString.add("1");
        setString.add("2");
        setString.add("3");
        log.info("All registered services on the server are cleared: [{}]", setString);
    }

}
