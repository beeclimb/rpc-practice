package github.beeclimb.utils;

import java.util.Collection;

/**
 * @author jun.ma
 * @date 2022/6/9 23:11:00
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

}
