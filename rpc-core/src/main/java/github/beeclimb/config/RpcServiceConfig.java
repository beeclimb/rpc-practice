package github.beeclimb.config;

import lombok.*;

/**
 * @author jun
 * @date 2022/3/14 21:52:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RpcServiceConfig {
    /**
     * service version
     */
    private String version = "";

    /**
     * when the interface has multiple implementation classes. distinguish by group.
     */
    private String group = "";

    /**
     * target service
     */
    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
