package github.beeclimb.loadbalance.loadbalancer;

import github.beeclimb.loadbalance.AbstractLoadBalance;
import github.beeclimb.remoting.dto.RpcRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jun
 * @date 2022/3/11 20:18:00
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    // 根据请求得到可用的服务器列表，通过对比服务器列表的一致性hash值，判断节点机器是否有新增，删减。
    // 如果有变化，就重新映射服务器列表在TreeMap中的位置。最后映射请求到TreeMap，取出大于此位置的第一个位置的ip+port。
    @Override
    public String doSelect(List<String> serviceAddress, RpcRequest rpcRequest) {
        int identityHashCode = System.identityHashCode(serviceAddress);
        // build rpc service name by rpcRequest
        String rpcServiceName = rpcRequest.getRpcServiceName();
        ConsistentHashSelector selector = selectors.get(rpcServiceName);
        // check for updates
        if (selector == null || selector.identityHashCode != identityHashCode) {
            selectors.put(rpcServiceName, new ConsistentHashSelector(serviceAddress, 16, identityHashCode));
            selector = selectors.get(rpcServiceName);
        }
        return selector.select(rpcServiceName + Arrays.stream(rpcRequest.getParameters()));
    }

    static class ConsistentHashSelector {
        private final TreeMap<Long, String> virtualInvokers;
        private final int identityHashCode;

        ConsistentHashSelector(List<String> invokers, int replicaNumber, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;

            /*
            1. 遍历服务器列表
            2. 需要复制的虚拟节点 / 4
            3. md(ip:port)
            4. hash()，放进TreeMap，4次
            */
            for (String invoker : invokers) {
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(invoker + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        static byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            return md.digest();
        }

        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }

        public String select(String rpcServiceKey) {
            byte[] digest = md5(rpcServiceKey);
            return selectForKey(hash(digest, 0));
        }

        public String selectForKey(long hashCode) {
            Map.Entry<Long, String> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();

            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }
    }
}
