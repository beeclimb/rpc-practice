package github.beeclimb;

import lombok.*;

import java.io.Serializable;

/**
 * @author jun
 * @date 2022/3/14 22:38:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Sing implements Serializable {
    private String singer;
    private String song;
}
