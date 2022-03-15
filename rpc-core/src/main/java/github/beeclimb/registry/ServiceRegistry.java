package github.beeclimb.registry;

import github.beeclimb.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration
 *
 * @author jun
 * @date 2022/3/7 21:30:00
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}