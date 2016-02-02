package util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by Administrator on 2014/11/18.
 * 使用DES加密与解密，
 * 密文使用String存储
 */
public class EncAndDecByDES {
    private static final String STRING2 = "fghaobvdjm,/,.?0--67.25456asfdsty645779*&^@#!~uewedfbm,.789<?^&%#$@#WDaw";
    private byte[] byteMi = null;
    private byte[] byteMing = null;
    private String strMi = "";
    private String strMing = "";
    private static Key key;

    static {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(EncAndDecByDES.class.getClassLoader().getResourceAsStream("Keys.txt"));
            key = (Key) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EncAndDecByDES() {
    }

    /**
     * 根据参数生成KEY
     *
     * @param strKey
     * @void
     */

    private void setKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            _generator.init(new SecureRandom(strKey.getBytes()));
            this.key = _generator.generateKey();
            Key k = _generator.generateKey();
            File file = new File("d:/Keys.txt");
            file.createNewFile();
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(k);
            _generator = null;
        } catch (Exception e) {
            throw new RuntimeException("Error initializing SqlMap class. Cause:" + e);
        }
    }

    //加密String
    public String getEncString(String strMing) {
        try {
            byteMing = strMing.getBytes("UTF8");
            byteMi = this.getEncCode(byteMing);
            strMi = bytes2HexString(byteMi);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause:" + e);
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    //解密String
    public String getDesString(String strMi) {
        try {
            byteMi = hexString2Bytes(strMi);
            byteMing = this.getDesCode(byteMi);
            strMing = new String(byteMing, "UTF8");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause:" + e);
        } finally {
            // base64Decoder = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }


    /**
     * 加密以byte[]明文输入,byte[]密文输出
     *
     * @param byteE
     * @return
     */
    private byte[] getEncCode(byte[] byteE) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteE);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause:" + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     *
     * @param byteD
     * @return
     */
    public byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause:" + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * byte[]转为16进制字符串
     *
     * @param src
     * @String
     */
    public String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
             return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 16进制字符串转为byte[]
     */
    public byte[] hexString2Bytes(String hexString) {

        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
