package io.leego.commons.uuid;

import java.util.HexFormat;
import java.util.UUID;

/**
 * @author Leego Yih
 */
public final class UUIDUtils {
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    public static UUID randomUUID() {
        return UUID.randomUUID();
    }

    public static String randomUUIDString() {
        return UUID.randomUUID().toString();
    }

    public static byte[] randomUUIDBytes() {
        return toBytes(UUID.randomUUID());
    }

    public static String randomUUIDHex() {
        return toHex(UUID.randomUUID());
    }

    public static byte[] toBytes(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        return new byte[]{
                (byte) ((msb >> 56) & 0xffL), (byte) ((msb >> 48) & 0xffL), (byte) ((msb >> 40) & 0xffL), (byte) ((msb >> 32) & 0xffL),
                (byte) ((msb >> 24) & 0xffL), (byte) ((msb >> 16) & 0xffL), (byte) ((msb >> 8) & 0xffL), (byte) (msb & 0xffL),
                (byte) ((lsb >> 56) & 0xffL), (byte) ((lsb >> 48) & 0xffL), (byte) ((lsb >> 40) & 0xffL), (byte) ((lsb >> 32) & 0xffL),
                (byte) ((lsb >> 24) & 0xffL), (byte) ((lsb >> 16) & 0xffL), (byte) ((lsb >> 8) & 0xffL), (byte) (lsb & 0xffL),
        };
    }

    public static String toHex(UUID uuid) {
        return HEX_FORMAT.formatHex(toBytes(uuid));
    }
}
