package github.beeclimb.remoting.handler;

import github.beeclimb.exception.RpcException;
import github.beeclimb.factory.SingletonFactory;
import github.beeclimb.provider.ServiceProvider;
import github.beeclimb.provider.impl.ZkServiceProviderImpl;
import github.beeclimb.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequest processor
 *
 * @author jun
 * @date 2022/3/9 12:29:00
 */
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * Processing rpcRequest: call the corresponding method, adn return the method.
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * Get method execution results
     *
     * @param rpcRequest client request
     * @param service    service object
     * @return result of target method execution
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParametersType());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service: [{}] successful invoke method: [{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
