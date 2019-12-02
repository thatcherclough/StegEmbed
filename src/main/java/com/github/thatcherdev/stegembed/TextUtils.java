package com.github.thatcherdev.stegembed;

import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

public class TextUtils {

	/**
	 * Compresses {@link uncompressed}.
	 *
	 * @param uncompressed byte array to compress
	 * @return {@link uncompressed} compressed
	 * @throws IOException
	 */
	public static byte[] compress(byte[] uncompressed) throws IOException {
		byte[] ret = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DeflaterOutputStream compressor = new DeflaterOutputStream(out);
		compressor.write(uncompressed);
		compressor.flush();
		compressor.close();
		ret = out.toByteArray();
		out.close();
		return ret;
	}

	/**
	 * Uncompress {@link compressed}.
	 *
	 * @param compressed byte array to decompress
	 * @return {@link compressed} decompressed
	 * @throws IOException
	 */
	public static byte[] decompress(byte[] compressed) throws IOException {
		byte[] ret = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InflaterInputStream decompressor = new InflaterInputStream(new ByteArrayInputStream(compressed));
		int b;
		while ((b = decompressor.read()) != -1)
			out.write(b);
		out.flush();
		decompressor.close();
		ret = out.toByteArray();
		out.close();
		return ret;
	}

	/**
	 * Uses XOR algorithm to encrypt/decrypt {@link input} with key {@link key}.
	 *
	 * @param input byte array to encrypt/decrypt
	 * @param key   key to use to encrypt/decrypt
	 * @return {@link input} encrypted/decrypted
	 */
	public static byte[] xorCrypt(byte[] input, byte[] key) {
		byte[] output = new byte[input.length];
		int current = 0;
		for (int k = 0; k < input.length; ++k) {
			output[k] = (byte) (input[k] ^ key[current++]);
			if (current >= key.length)
				current = 0;
		}
		return output;
	}
}