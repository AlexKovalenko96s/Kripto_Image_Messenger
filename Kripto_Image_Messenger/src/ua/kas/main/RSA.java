package ua.kas.main;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
	private BigInteger n, d, e;

	private int bitlen = 256;

	public RSA(BigInteger newn, BigInteger newe) {
		n = newn;
		e = newe;
	}

	public RSA(BigInteger newd, BigInteger newn, BigInteger newe) {
		d = newd;
		n = newn;
		e = newe;
	}

	public RSA(int bits) {
		bitlen = bits;
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = new BigInteger("3");
		while (m.gcd(e).intValue() > 1) {
			e = e.add(new BigInteger("2"));
		}
		d = e.modInverse(m);
	}

	public synchronized BigInteger encrypt(BigInteger message) {
		return message.modPow(e, n);
	}

	public synchronized BigInteger decrypt(BigInteger message) {
		return message.modPow(d, n);
	}

	public synchronized void generateKeys() {
		SecureRandom r = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, r);
		BigInteger q = new BigInteger(bitlen / 2, 100, r);
		n = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = new BigInteger("3");
		while (m.gcd(e).intValue() > 1) {
			e = e.add(new BigInteger("2"));
		}
		d = e.modInverse(m);
	}

	private String dGet() {
		return d.toString();
	}

	private String nGet() {
		return n.toString();
	}

	public static String encryptText(String text) {
		RSA rsa = new RSA(256);
		BigInteger plaintext = new BigInteger(text.getBytes());
		BigInteger ciphertext = rsa.encrypt(plaintext);

		String all = "";
		int textSize = ciphertext.toString().length();
		int d = rsa.dGet().length();
		int n = rsa.nGet().length();

		if (textSize > 99)
			all += textSize;
		else if (textSize < 100 && textSize > 9)
			all += "0" + textSize;
		else if (textSize < 10)
			all += "00" + textSize;

		if (d > 99)
			all += d;
		else if (d < 100 && d > 9)
			all += "0" + d;
		else if (d < 10)
			all += "00" + d;

		if (n > 99)
			all += n;
		else if (n < 100 && n > 9)
			all += "0" + n;
		else if (n < 10)
			all += "00" + n;
		System.out.println(ciphertext.toString());
		System.out.println(rsa.dGet());
		System.out.println(rsa.nGet());
		return all + ciphertext.toString() + rsa.dGet() + rsa.nGet();
	}

	public static String decryptText(String dInput, String nInput, String ciphertext) {
		RSA rsa = new RSA(new BigInteger(dInput), new BigInteger(nInput), new BigInteger("69"));
		BigInteger plaintext = rsa.decrypt(new BigInteger(ciphertext));
		String text = new String(plaintext.toByteArray());
		return text;
	}
}
