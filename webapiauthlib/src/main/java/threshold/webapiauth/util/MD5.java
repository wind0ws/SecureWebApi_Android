package threshold.webapiauth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 Util
 * Created by Threshold on 2015/12/28.
 */
public class MD5 {

    private static final String UTF8 = "UTF-8";

    /**
     * This method md5 is the same as C# generate md5(byte[]) to BitConvert
     * @param str content
     * @param encoding encoding
     * @return md5 of content
     * @throws Exception
     */
    public static String getMD5(String str, String encoding) throws Exception {
        return getMD5(str.getBytes(encoding));
    }

    public static String getMD5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        byte[] md5Bytes = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (byte aMd5Byte : md5Bytes) {
            int val = aMd5Byte & 0xff;
            if (val < 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toUpperCase();
    }


    public static String getMD5(String content) throws Exception {
        return getMD5(content, UTF8);
    }

}
