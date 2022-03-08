package github.beeclimb.remoting.transport.socket;

import github.beeclimb.remoting.dto.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jun
 * @date 2022/3/8 20:50:00
 */
public class SocketRpcServer {
    private static final int PORT = 6666;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                    RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                    System.out.println(rpcRequest.getRequestId());
                    rpcRequest.setRequestId("9527");
                    objectOutputStream.writeObject(rpcRequest);
                    objectOutputStream.flush();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new SocketRpcServer().start();
    }
}
