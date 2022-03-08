package github.beeclimb.serialize.kryo;

import github.beeclimb.remoting.dto.RpcRequest;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KryoSerializerTest {

    @Test
    public void kryoSerializerTest() {
        RpcRequest target = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName("helloService")
                .methodName("hello")
                .parameters(new Object[]{"sayHello", "sayGoodbye"})
                .parametersType(new Class<?>[]{String.class, String.class})
                .version("version1")
                .group("group1")
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] bytes = kryoSerializer.serialize(target);
        RpcRequest rpcRequest = kryoSerializer.deserialize(bytes, RpcRequest.class);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        assertEquals(target.getRequestId(), rpcRequest.getRequestId());
        assertEquals(target.getInterfaceName(), rpcRequest.getInterfaceName());
        assertEquals(target.getMethodName(), rpcRequest.getMethodName());

    }
}
