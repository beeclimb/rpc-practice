package github.beeclimb.provider.impl;

import github.beeclimb.config.RpcServiceConfig;
import github.beeclimb.enums.RpcErrorMessageEnum;
import github.beeclimb.exception.RpcException;
import github.beeclimb.extension.ExtensionLoader;
import github.beeclimb.provider.ServiceProvider;
import github.beeclimb.registry.ServiceRegistry;
import github.beeclimb.remoting.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jun
 * @date 2022/3/15 10:34:00
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        registeredService.add(rpcServiceName);
        log.info("Add service: [{}] and interfaces: [{}]", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, NettyRpcServer.PORT));
            this.addService(rpcServiceConfig);
        } catch (UnknownHostException e) {
            log.info("occur exception when getHostAddress", e);
        }
    }
}
