package github.beeclimb.remoting.transport.socket;

import github.beeclimb.config.CustomShutdownHook;
import github.beeclimb.config.RpcServiceConfig;
import github.beeclimb.factory.SingletonFactory;
import github.beeclimb.provider.ServiceProvider;
import github.beeclimb.provider.impl.ZkServiceProviderImpl;
import github.beeclimb.remoting.transport.netty.server.NettyRpcServer;
import github.beeclimb.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author jun
 * @date 2022/3/8 20:50:00
 */
@Slf4j
public class SocketRpcServer {
    private final ServiceProvider serviceProvider;
    private final ExecutorService threadPool;

    public SocketRpcServer() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, NettyRpcServer.PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected: [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException: [{}]", e.getMessage());
        }
    }
}
