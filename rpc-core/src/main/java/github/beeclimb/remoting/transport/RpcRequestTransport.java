package github.beeclimb.remoting.transport;

import github.beeclimb.remoting.dto.RpcRequest;

/**
 * send rpc request
 *
 * @author jun
 * @date 2022/3/8 18:37:00
 */
public interface RpcRequestTransport {
    /**
     * send rpc request to server and get result.
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
