package github.beeclimb.compress;

import github.beeclimb.extension.SPI;

/**
 * @author jun.ma
 * @date 2022/5/26 11:05:00
 */
@SPI
public interface Compress {
    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
