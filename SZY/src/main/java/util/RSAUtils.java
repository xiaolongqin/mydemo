package util;

import javax.crypto.Cipher;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by Tyfunwang on 2014/12/25.
 */
public class RSAUtils {

    public static void main(String[] s) {
        String mi = RSAUtils.encryptByPrivateKey("drhstjykrt7867");
        System.out.println("mi:" + mi);
        String ming = RSAUtils.decryptByPublicKey(mi);
        System.out.println("ming:" + ming);
    }

    /**
     * 私钥加密
     *
     * @param data
     * @return mi
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data) {
        try {
            RSAPrivateKey privateKey = readPri();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            // 模长
            int key_len = privateKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11
            String[] datas = splitString(data, key_len - 11);
            String mi = "";
            //如果明文长度大于模长-11则要分组加密
            for (String s : datas) {
                mi += bcd2Str(cipher.doFinal(s.getBytes()));
            }
            return mi;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 公钥解密
     *
     * @param data
     * @return ming
     * @throws Exception
     */
    public static String decryptByPublicKey(String data) {
        try {
            RSAPublicKey publicKey = readPub();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            //模长
            int key_len = publicKey.getModulus().bitLength() / 8;
            byte[] bytes = data.getBytes();
            byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
            //System.err.println(bcd.length);
            //如果密文长度大于模长则要分组解密
            String ming = "";
            byte[][] arrays = splitArray(bcd, key_len);
            for (byte[] arr : arrays) {
                ming += new String(cipher.doFinal(arr));
            }
            return ming;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * read pubKey object
     * @return RSAPublicKey
     */
    private static RSAPublicKey readPub() {
        try {
            ObjectInputStream oop = new ObjectInputStream(RSAUtils.class.getClass().getResourceAsStream("pubKey.txt"));
            RSAPublicKey pubKey = (RSAPublicKey) oop.readObject();
            System.out.println(pubKey);
            oop.close();
            return pubKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  read priKey object
     *  @return  RSAPrivateKey
     */
    private static RSAPrivateKey readPri() {
        try {
            ObjectInputStream oop = new ObjectInputStream(RSAUtils.class.getClassLoader().getResourceAsStream("priKey.txt"));
            RSAPrivateKey priKey = (RSAPrivateKey) oop.readObject();
            System.out.println(priKey);
            oop.close();
            return priKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ASCII码转BCD码
     */
    private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    private static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    private static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    private static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }


}
