package github.beeclimb;

import github.beeclimb.config.RpcServiceConfig;
import github.beeclimb.proxy.RpcClientProxy;
import github.beeclimb.remoting.dto.RpcRequest;
import github.beeclimb.remoting.dto.RpcResponse;
import github.beeclimb.remoting.transport.RpcRequestTransport;
import github.beeclimb.remoting.transport.netty.client.NettyRpcClient;
import lombok.SneakyThrows;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author jun.ma
 * @date 2022/5/30 12:58:00
 */
public class NettyClientMain {
    @SneakyThrows
    public static void main(String[] args) {
        NettyRpcClient nettyRpcClient = new NettyRpcClient();
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
        CompletableFuture<RpcResponse<Object>> result = (CompletableFuture<RpcResponse<Object>>) nettyRpcClient.sendRpcRequest(rpcRequest);
        System.out.println(result.get());


        Thread.sleep(5000);
        RpcRequestTransport rpcRequestTransport1 = new NettyRpcClient();
        RpcServiceConfig rpcServiceConfig1 = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport1, rpcServiceConfig1);
        SingService singService = rpcClientProxy.getProxy(SingService.class);
        String result1 = singService.sing(new Sing("mao bu yi ", " la la la"));
        System.out.println(result1);

    }
}
