package com.qianwang.util.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

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

		DcPasswordEncoder encoder=new DcPasswordEncoder();
		System.out.println(encoder.encodePassword("123456", "admin"));

	}
}
