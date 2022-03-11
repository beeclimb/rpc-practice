package github.beeclimb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jun
 * @date 2022/3/10 20:54:00
 */
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;

}