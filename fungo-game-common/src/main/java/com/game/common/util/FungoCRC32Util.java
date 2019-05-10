package com.game.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;


/**
 * <p>
 *  CRC32 生成工具类
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class FungoCRC32Util {


    private static FungoCRC32Util crc32 = new FungoCRC32Util();

    public static FungoCRC32Util getInstance() {
        return crc32;
    }

    private FungoCRC32Util() {
    }

    private static final int STREAM_BUFFER_LENGTH = 1024;

    /**
     * 字符串获取CRC32值
     * @param data
     * @return
     */
    public long encrypt(String data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        return crc32.getValue();
    }

    /**
     * 字节流获取CRC32值
     * @param data 字节流
     * @return
     * @throws IOException
     */
    public long encrypt(InputStream data) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        CRC32 crc32 = new CRC32();
        while (read > -1) {
            crc32.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }
        return crc32.getValue();
    }

}
