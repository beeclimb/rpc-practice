package github.beeclimb.serialize;

/**
 * 序列化接口，所有序列化类都要实现这个接口
 *
 * @author jun
 * @date 2022/3/8 9:34:00
 */
public interface Serializer {
    /**
     * serialize
     *
     * @param obj need serialized object
     * @return byte array
     */
    byte[] serialize(Object obj);

    /**
     * deserialize
     *
     * @param bytes byte array need deserialized
     * @param clazz target class
     * @param <T>   类的类型。举个例子,  {@code String.class} 的类型是 {@code Class<String>}.
     *              如果不知道类的类型的话，使用 {@code Class<?>}
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
