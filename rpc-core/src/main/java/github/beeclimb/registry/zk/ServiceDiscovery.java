package github.beeclimb.registry.zk;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author jun
 * @date 2022/3/7 21:29:00
 */
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @return service address
     */
    InetSocketAddress lookupService();

}
