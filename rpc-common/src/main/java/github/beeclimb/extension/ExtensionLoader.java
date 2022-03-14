package github.beeclimb.extension;

import java.security.cert.Extension;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jun
 * @date 2022/3/8 18:53:00
 */
public class ExtensionLoader<T> {
    private final Class<?> type;
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";


    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @SPI");
        }
        // firstly get from cache, if not hit, create one.
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<S>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    public T getExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name should not be null or empty.");
        }
        // firstly get from cache, if not hit, create one
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        // create a singleton if no instance exists
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    public T createExtension(String name) {
        // load all extension classes of type T from file and get specific one by name

        return (T) "a";
    }

    public void loadDirectory(Map<String, Class<?>> extensionClasses) {
        String fileName = ExtensionLoader.SERVICE_DIRECTORY + type.getName();
        System.out.println(fileName);
    }



























}
