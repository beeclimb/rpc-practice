package github.beeclimb.utils;

/**
 * @author jun.ma
 * @date 2022/6/9 23:06:00
 */
public class StringUtil {

    public static boolean isBlank(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
