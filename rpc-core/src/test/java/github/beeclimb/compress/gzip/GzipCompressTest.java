package github.beeclimb.compress.gzip;


import github.beeclimb.compress.Compress;
import github.beeclimb.remoting.dto.RpcRequest;
import github.beeclimb.serialize.kryo.KryoSerializer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class GzipCompressTest {
    @Test
    void gzipCompressTest() {
        RpcRequest rpcRequest = RpcRequest.builder().methodName("sing")
                .parameters(new Object[]{"sing", "singsing"})
                .interfaceName("github.beeclimb.SingService")
                .parametersType(new Class[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .group("group1")
                .version("verison1")
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        Compress gzipCompress = new GzipCompress();
        byte[] rpcRequestBytes = kryoSerializer.serialize(rpcRequest);
        byte[] compressRpcRequestBytes = gzipCompress.compress(rpcRequestBytes);
        byte[] decompressRpcRequestBytes = gzipCompress.decompress(compressRpcRequestBytes);
        System.out.println(rpcRequestBytes.length);
        System.out.println(compressRpcRequestBytes.length);
        System.out.println(decompressRpcRequestBytes.length);
        System.out.println(rpcRequestBytes.length == decompressRpcRequestBytes.length);

    }
}
