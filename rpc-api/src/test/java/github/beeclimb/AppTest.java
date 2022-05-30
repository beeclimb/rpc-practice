package github.beeclimb;

import static org.junit.Assert.assertTrue;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.InputStreamReader;
import java.net.InetAddress;

public class AppTest {

    @Test
    @SneakyThrows
    public void shouldAnswerWithTrue() {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
