package github.beeclimb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jun.ma
 * @date 2022/5/26 10:36:00
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {
    KYRO((byte) 0x01, "kyro");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum s : SerializationTypeEnum.values()) {
            if (s.getCode() == code) {
                return s.getName();
            }
        }
        return null;
    }

}
