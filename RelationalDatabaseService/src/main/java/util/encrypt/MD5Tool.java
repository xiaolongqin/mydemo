package util.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liweiqi on 14-8-20.
 */
public class MD5Tool implements EncryptPolicy {
    /**
     * @param plainText
     * @return the 32 length Hex String of enrypted plain text using md5
     */
    public static String md5(String plainText) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            for (int i : b) {
                if (i < 0) i += 256;
                if (i < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(i));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return sb.toString();
        }
    }

    @Override
    public String toCiphertext(String plainText) {
        return md5(plainText);
    }
}
