package github.beeclimb.annotation;

import java.lang.annotation.*;

/**
 * rpc reference annotation, autowire the service implementation class
 *
 * @author jun.ma
 * @date 2022/6/2 23:21:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {
    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

}
