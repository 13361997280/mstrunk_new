package util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    private MD5Utils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);
    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            LOGGER.error("MD5初始化异常:不支持MD5加密技术..{}", nsaex.getMessage());
        }
    }

    /**
     * 生成字符数组的md5校验值
     *
     * @param bytes
     * @return
     */
    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }


    /**
     * 生成字符串的md5校验值
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }

            return bufferToHex(messagedigest.digest());
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != fis) {
                fis.close();
            }
        }

    }


    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
    
    public static String md5Str(String salt, Object... args) {
		StringBuffer argsJoin = new StringBuffer("");
		for(Object arg:args){
			if(null!=arg){
				argsJoin.append(arg.toString());
			}
		}
		return getMD5String(argsJoin.toString() + salt);
	}
    public static void main(String[] args){
        System.out.println(md5Str("1111","productType=签到&userId=1&timestamp=1511168560335"));
        System.out.println(md5Str("657y9b7faa6a50a4abb5e00b5117ca45","companyId=101&productId=10101&productType=签到&userId=423456&timestamp=1511150073123"));
        System.out.println(MD5Utils.md5Str("1111","productType=签到&userId=1&timestamp=1511168560335"));
    }



}

