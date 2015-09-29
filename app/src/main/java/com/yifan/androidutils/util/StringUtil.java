package com.yifan.androidutils.util;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {

    public final static String[] HEX_DIGITS = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"
    };

    public static final String encodeMD5(String string) {
        byte[] digest = encodeMD5Bytes(string);
        return digest != null ? byteArrayToString(digest) : null;
    }

    public static final byte[] encodeMD5Bytes(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digester.update(string.getBytes());
        return digester.digest();
    }

    public static String byteArrayToString(byte[] b) {
        return byteArrayToString(b, false);
    }

    public static String byteArrayToString(byte[] b, boolean upperCase) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;

        return HEX_DIGITS[d1] + HEX_DIGITS[d2];

    }
}
