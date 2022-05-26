package github.beeclimb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jun.ma
 * @date 2022/5/25 17:03:00
 */
@AllArgsConstructor
@Getter
public enum CompressTypeEnum {
    GZIP((byte) 0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}
