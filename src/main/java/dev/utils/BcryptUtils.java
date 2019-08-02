package dev.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptUtils {

	public static String encrypt(String plainPassword) {
		String pwHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
		return pwHash;
	}

	public static boolean checkPw(String plainPwd, String hashPwd) {
		if (BCrypt.checkpw(plainPwd, hashPwd)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		System.out.println(encrypt("admin"));
	}

}
