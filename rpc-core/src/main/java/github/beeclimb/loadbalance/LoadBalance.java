package github.beeclimb.loadbalance;

import github.beeclimb.extension.SPI;
import github.beeclimb.remoting.dto.RpcRequest;

import java.util.List;

/**
 * Interface to the load balance policy
 *
 * @author jun
 * @date 2022/3/11 18:00:00
 */
@SPI
public interface LoadBalance {
    /**
     * Choose one from the list of existing service addresses list
     *
     * @param serviceAddress Available service address list
     * @param rpcRequest Requested data entity
     * @return Optimal service address
     */
    String selectServiceAddress(List<String> serviceAddress, RpcRequest rpcRequest);
}
