package util;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

import redis.RedisConst;
import redis.RedisUtil;

/**
 * This program generates a AES key, retrieves its raw bytes, and then
 * reinstantiates a AES key from the key bytes. The reinstantiated key is used
 * to initialize a AES cipher for encryption and decryption.
 */

public class TokenUtils {

	private static TokenUtils ourInstance = new TokenUtils();

	private static final String AES = "AES";

	private static final String CRYPT_KEY = "YUUAtestYUUAtest";

	public static TokenUtils getInstance() {
		return ourInstance;
	}

	private TokenUtils() {
	}

	/**
	 * 加密
	 * 
	 * @param encryptStr
	 * @return
	 */
	public static byte[] encrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey);// 设置密钥和加密形式
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param decryptStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);// 设置加密Key
		cipher.init(Cipher.DECRYPT_MODE, securekey);// 设置密钥和解密形式
		return cipher.doFinal(src);
	}

	/**
	 * 二行制转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data) {
		try {
			return new String(decrypt(hex2byte(data.getBytes()), CRYPT_KEY));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String data) {
		try {
			return byte2hex(encrypt(data.getBytes(), CRYPT_KEY));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * token 生产策略
	 * 系统时间+随机函数0~100+用户id+过期时间 通过AES对称加密
	 * @param content
	 * @return
	 */
	public String generateToken(String userId, Long expiredTime) {
		Random rand = new Random();
		int randNum = rand.nextInt(100);
		TokenData data = new TokenData();
		data.setEpr(expiredTime);
		data.setSt(System.currentTimeMillis());
		data.setR(randNum);
		data.setuId(userId);
		String jsonString = JSON.toJSONString(data);
		return encrypt(jsonString);
	}
	
	/**
	 * token 解析
	 * 返回值
	 *   成功：token有效返回userId
	 *   失败：如果token失效返回epired 
	 *   异常：异常token
	 * @param content
	 * @return
	 */
	public SimpleResult decodeToken(String token) {
		String dcryptToken = decrypt(token);
		try {
			TokenData data = JSON.parseObject(dcryptToken, TokenData.class);
			if (data == null) {
				return SimpleResult.failureResult("error token");
			} 
			Long maxExpiredTime = data.getSt() + (data.getEpr() * 1000 * 60 * 60);
			if (System.currentTimeMillis() > maxExpiredTime) {
				return SimpleResult.failureResult("expired");
			} else {
				// 获取redis token
				String redisKey = RedisConst.TENGRONG_MS_TOKEN.key;
		        redisKey = String.format(redisKey, data.getuId());
		        RedisUtil redisUtil = new RedisUtil();
				if (token.equals(redisUtil.get(redisKey))) {
					return SimpleResult.successResult(data.getuId());
				} else {
					return SimpleResult.failureResult("expired");
				}
			}
		} catch (Exception e) {
			return SimpleResult.failureResult("token error");
		}      
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		TokenUtils utils = new TokenUtils();
		String token = utils.generateToken("asfawef", 1212L);
		System.out.println(token);
		System.out.println(utils.decrypt(token));
		System.out.println(utils.decodeToken(token));
	}

}
