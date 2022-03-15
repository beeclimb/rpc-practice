package github.beeclimb.remoting.dto;

import github.beeclimb.enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @author jun
 * @date 2022/3/8 11:00:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 2L;
    private String requestId;

    /**
     * response code
     */
    private Integer code;
    /**
     * response message
     */
    private String message;
    /**
     * response body
     */
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (data != null) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail() {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(RpcResponseCodeEnum.FAIL.getCode());
        rpcResponse.setMessage(RpcResponseCodeEnum.FAIL.getMessage());
        return rpcResponse;
    }
}
