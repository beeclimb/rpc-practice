package github.beeclimb.registry.zk;

import github.beeclimb.registry.zk.util.CuratorUtils;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

public class ZkServiceRegistryTest {

    @Test
    public void registerService() {
        String rpcServiceName = "github.beeclimb.SeyHelloService";
        InetSocketAddress inetSocketAddress = new InetSocketAddress("10.150.110.66", 22);
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        System.out.println(servicePath);
        System.out.println(inetSocketAddress.toString());
    }
}
