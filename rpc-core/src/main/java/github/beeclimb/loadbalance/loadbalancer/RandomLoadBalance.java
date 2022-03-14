package github.beeclimb.loadbalance.loadbalancer;

import github.beeclimb.loadbalance.AbstractLoadBalance;
import github.beeclimb.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * Implementation of random load balancing strategy
 *
 * @author jun
 * @date 2022/3/11 20:11:00
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddress, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceAddress.get(random.nextInt(serviceAddress.size()));
    }
}
