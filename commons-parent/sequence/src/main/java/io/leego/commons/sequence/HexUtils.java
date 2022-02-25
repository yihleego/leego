package io.leego.commons.sequence;

/**
 * @author Leego Yih
 */
public final class HexUtils {
    /** Table for byte to hex string translation. */
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /** Table for HEX to DEC byte translation. */
    private static final int[] DEC = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};

    private HexUtils() {
    }

    public static int getDec(int index) {
        // Fast for correct values, slower for incorrect ones
        try {
            return DEC[index - 48];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public static byte getHex(int index) {
        return (byte) HEX[index];
    }

    public static String encode(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        int i = 0;
        char[] chars = new char[bytes.length << 1];
        for (byte b : bytes) {
            chars[i++] = HEX[(b & 0xf0) >> 4];
            chars[i++] = HEX[b & 0x0f];
        }
        return new String(chars);
    }

    public static byte[] decode(String input) {
        if (input == null) {
            return null;
        }
        if ((input.length() & 1) == 1) {
            // Odd number of characters
            throw new IllegalArgumentException("Odd digits");
        }
        char[] inputChars = input.toCharArray();
        byte[] result = new byte[input.length() >> 1];
        for (int i = 0; i < result.length; i++) {
            int upperNibble = getDec(inputChars[2 * i]);
            int lowerNibble = getDec(inputChars[2 * i + 1]);
            if (upperNibble < 0 || lowerNibble < 0) {
                // Non hex character
                throw new IllegalArgumentException("Non hex");
            }
            result[i] = (byte) ((upperNibble << 4) + lowerNibble);
        }
        return result;
    }
}
