package github.beeclimb.remoting.dto;

import java.io.Serializable;

/**
 * @author jun
 * @date 2022/3/8 11:00:00
 */
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 2L;
    private String requestId;
    private Integer responseCode;
    private String message;
    private T data;



}
