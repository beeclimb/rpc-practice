package github.beeclimb.utils;

/**
 * @author jun
 * @date 2022/3/9 15:51:00
 */
public class RuntimeUtil {

    /**
     * @return cpu cores
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }

}
