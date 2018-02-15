package com.qianwang.util.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户密码加密
 * @author zou
 *
 */
public class DcPasswordEncoder extends Md5PasswordEncoder {

	/**
	 *加密转换数
	 */
	private static final short TRANSFORM = 1;

	/**
	 * 覆盖加密算法 新增转换数
	 * @param rawPass 明文密码
	 * @salt 散列值
	 * @return 返回加密后的密码
	 */
	@Override
	public String encodePassword(String rawPass, Object salt)
			throws DataAccessException {
		if (rawPass == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : rawPass.toCharArray()) {
			sb.append(c + TRANSFORM);
		}
		return super.encodePassword(sb.toString(), salt);
	}

	/**
	 * 验证密码是否有效
	 * @encPass 用户存储的加密后的密码
	 * @param rawPass 用户输入的明文密码
	 * @salt 散列值
	 * @return 验证密码是否有效
	 */
	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		if (encPass == null) {
			return false;
		}
		return encPass.equals(encodePassword(rawPass, salt));
	}
	public static void main(String[] args) {

		String aaa= "123456";
		///String aaa= "111111";


		DcPasswordEncoder encoder=new DcPasswordEncoder();
		System.out.println(encoder.encodePassword(aaa, "songjie"));
		System.out.println(encoder.isPasswordValid("514c8dfb9b39a46877eb834bb9e3176e",aaa,"admin"));
		System.out.println(new DcPasswordEncoder().encodePassword("admin"+"@#@"+aaa,"admin"));
		//System.out.println(new DcPasswordEncoder().demergePasswordAndSalt("1a0fcece388049ba2cbfcf507a8d4efc"+"admin")[2]);
		System.out.println(encoder.isPasswordValid("1a0fcece388049ba2cbfcf507a8d4efc","admin"+"@#@"+"514c8dfb9b39a46877eb834bb9e3176e","admin"));
		System.out.println(encoder.isPasswordValid("1a0fcece388049ba2cbfcf507a8d4efc","admin"+"@#@"+"514c8dfb9b39a46877eb834bb9e3176e","admin"));

		try {
			System.out.println(encoder.encryption(aaa));
		} catch (Exception e) {
			e.printStackTrace();
		}

//		String value =  "!@#$%^&*()_";
//
//
//
//		value = value.replaceAll("\"","&quot");
//		value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
//		value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
//		value = value.replaceAll("'", "& #39;");
//		value = value.replaceAll("eval\\((.*)\\)", "");
//		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//		value = value.replaceAll("script", "");
//
//
//		System.out.println(value.trim());
	}


	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

}
