package github.beeclimb.extension;

/**
 * @author jun
 * @date 2022/3/13 20:19:00
 */
public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
