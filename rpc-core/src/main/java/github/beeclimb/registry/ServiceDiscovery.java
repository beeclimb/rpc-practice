package github.beeclimb.registry;

import github.beeclimb.extension.SPI;
import github.beeclimb.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author jun
 * @date 2022/3/7 21:29:00
 */
@SPI
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);

}
