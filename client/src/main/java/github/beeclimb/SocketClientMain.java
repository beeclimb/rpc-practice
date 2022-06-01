package github.beeclimb;

import github.beeclimb.config.RpcServiceConfig;
import github.beeclimb.proxy.RpcClientProxy;
import github.beeclimb.remoting.dto.RpcRequest;
import github.beeclimb.remoting.transport.RpcRequestTransport;
import github.beeclimb.remoting.transport.socket.SocketRpcClient;
import lombok.SneakyThrows;

import java.util.UUID;

public class SocketClientMain {
    @SneakyThrows
    public static void main(String[] args) {
        SocketRpcClient socketRpcClient = new SocketRpcClient();
        Sing sing = new Sing();
        sing.setSinger("zhou jie lun ");
        sing.setSong(" dao xiang");
        RpcRequest rpcRequest = RpcRequest.builder().methodName("sing")
                .parameters(new Object[]{sing})
                .interfaceName("github.beeclimb.SingService")
                .parametersType(new Class[]{sing.getClass()})
                .requestId(UUID.randomUUID().toString())
                .group("")
                .version("")
                .build();
        Object result = socketRpcClient.sendRpcRequest(rpcRequest);
        System.out.println(result);


        Thread.sleep(5000);
        RpcRequestTransport rpcRequestTransport1 = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig1 = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport1, rpcServiceConfig1);
        SingService singService = rpcClientProxy.getProxy(SingService.class);
        String result1 = singService.sing(new Sing("mao bu yi ", " la la la"));
        System.out.println(result1);

    }
}
