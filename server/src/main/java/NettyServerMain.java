import github.beeclimb.SingService;
import github.beeclimb.config.RpcServiceConfig;
import github.beeclimb.remoting.transport.netty.server.NettyRpcServer;
import github.beeclimb.serviceimpl.SingServiceImpl;

/**
 * @author jun.ma
 * @date 2022/5/30 13:06:00
 */
public class NettyServerMain {
    public static void main(String[] args) {
        SingService singService = new SingServiceImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(singService);
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
