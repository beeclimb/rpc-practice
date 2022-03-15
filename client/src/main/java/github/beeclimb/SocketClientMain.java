package github.beeclimb;

import github.beeclimb.remoting.dto.RpcRequest;
import github.beeclimb.remoting.transport.socket.SocketRpcClient;

import java.util.UUID;

public class SocketClientMain {
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

    }
}
