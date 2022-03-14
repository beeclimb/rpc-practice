package github.beeclimb.extension;

import github.beeclimb.loadbalance.LoadBalance;
import github.beeclimb.loadbalance.loadbalancer.ConsistentHashLoadBalance;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ExtensionLoaderTest {
    @Test
    public void loadDirectoryTest() throws IOException {
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
        System.out.println(ConsistentHashLoadBalance.class.getName());
        Assert.assertNotNull(loadBalance);

    }
}
