package github.beeclimb.annotation;

import java.lang.annotation.*;

/**
 * rpc service annotation, marked on the service implementation class
 *
 * @author jun.ma
 * @date 2022/6/2 23:03:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

}
