package github.beeclimb.remoting.dto;

import lombok.*;

/**
 * @author jun.ma
 * @date 2022/5/25 14:59:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcMessage {
    /**
     * rpc message type
     */
    private byte messageType;

    /**
     * serialization type
     */
    private byte codec;

    /**
     * compress type
     */
    private byte compress;

    private int requestId;
    private Object data;

}
