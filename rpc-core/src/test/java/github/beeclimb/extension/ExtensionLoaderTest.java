package github.beeclimb.extension;

import github.beeclimb.loadbalance.LoadBalance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ExtensionLoaderTest {
    @Test
    public void loadDirectoryTest() {
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        ExtensionLoader.getExtensionLoader(LoadBalance.class).loadDirectory(extensionClasses);

    }
}
