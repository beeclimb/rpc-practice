package github.beeclimb.provider;

import github.beeclimb.config.RpcServiceConfig;

/**
 * store and provider service object.
 *
 * @author jun
 * @date 2022/3/9 12:32:00
 */
public interface ServiceProvider {

    /**
     * @param rpcServiceConfig rpc service related attributes
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * @param rpcServiceConfig rpc service related attributes
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

}
