import github.beeclimb.SingService;
import github.beeclimb.config.RpcServiceConfig;
import github.beeclimb.remoting.transport.socket.SocketRpcServer;
import github.beeclimb.serviceimpl.SingServiceImpl;

import javax.swing.*;

/**
 * @author jun
 * @date 2022/3/15 9:17:00
 */
public class SocketServiceMain {
    public static void main(String[] args) {
        SingService singService = new SingServiceImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(singService);
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
