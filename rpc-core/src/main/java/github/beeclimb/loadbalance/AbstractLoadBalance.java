package github.beeclimb.loadbalance;

import github.beeclimb.remoting.dto.RpcRequest;
import github.beeclimb.utils.CollectionUtil;

import java.util.List;

/**
 * Abstract class for a load balancing policy
 *
 * @author jun
 * @date 2022/3/11 20:04:00
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddress, RpcRequest rpcRequest) {
        if (CollectionUtil.isEmpty(serviceAddress)) {
            return null;
        }
        if (serviceAddress.size() == 1) {
            return serviceAddress.get(0);
        }
        return doSelect(serviceAddress, rpcRequest);
    }

    /**
     * selecting optimum service address
     *
     * @param serviceAddress Available service address list
     * @param rpcRequest     Requested data entity
     * @return Optimal service address
     */
    protected abstract String doSelect(List<String> serviceAddress, RpcRequest rpcRequest);
}
