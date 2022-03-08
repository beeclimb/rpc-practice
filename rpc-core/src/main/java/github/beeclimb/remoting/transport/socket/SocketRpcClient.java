package github.beeclimb.remoting.transport.socket;

import github.beeclimb.exception.RpcException;
import github.beeclimb.remoting.dto.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * @author jun
 * @date 2022/3/8 18:46:00
 */
public class SocketRpcClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6666;

    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);

        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream.
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Read RpcResponse from the input stream.
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
    }

    public static void main(String[] args) {
        RpcRequest target = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName("helloService")
                .methodName("hello")
                .parameters(new Object[]{"sayHello", "sayGoodbye"})
                .parametersType(new Class<?>[]{String.class, String.class})
                .version("version1")
                .group("group1")
                .build();
        RpcRequest rpcRequest = (RpcRequest) new SocketRpcClient().sendRpcRequest(target);
        System.out.println(rpcRequest.getRequestId());
    }
}
