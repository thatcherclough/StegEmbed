package com.github.thatcherdev.stegembed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StegEmbed {

	private static String mode;
	private static String image;
	private static String text;
	private static String password;
	final private static String help = "StegEmbed: A stenography program that can embed and extract text in and out of the pixels of an image (1.2.0)"
			+ "\n\nUsage:\n\tjava -jar stegembed.jar   [-h] [-v] [embed -i IMAGE -t TEXTFILE -p PASSWORD]\n\t\t\t\t  [extract -i IMAGE -p PASSWORD]\n"
			+ "Arguments:\n\t-h, --help	Display this message.\n\t-v, --version\tDisplay current version.\n\t-i, --image\tSpecify image to use for embedding/extracting.\n"
			+ "\t-t, --text\tSpecify text file to use for embedding.\n\t-p, --password\tSpecify password to use for encrypting/decrypting when embedding/extracting.";

	/**
	 * Starts StegEmbed based on command line arguments {@link args}.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0)
				throw new Exception();
			for (int k = 0; k < args.length; k++)
				if (args[k].equals("-h") || args[k].equals("--help")) {
					throw new Exception();
				} else if (args[k].equals("-v") || args[k].equals("--version")) {
					System.out.println(help.substring(0, help.indexOf("\n")));
					System.exit(0);
				} else if (args[k].equals("embed") || args[k].equals("extract"))
					mode = args[k];
				else if (args[k].equals("-i") || args[k].equals("--image"))
					image = args[++k];
				else if (args[k].equals("-t") || args[k].equals("--text"))
					text = args[++k];
				else if (args[k].equals("-p") || args[k].equals("--password"))
					password = args[++k];
			if (mode == null || password == null || image == null || !new File(image).exists()
					|| (mode.equals("embed") && (text == null || !new File(text).exists())))
				throw new Exception();
			try {
				if (mode.equals("embed"))
					embed();
				else if (mode.equals("extract"))
					extract();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(help);
			System.exit(0);
		}
	}

	/**
	 * Embeds text into image.
	 * <p>
	 * Encrypts (using password {@link #password}) and compresses the bytes from
	 * text file with name {@link #text}. Gets pixels from image with name
	 * {@link #image} using {@link ImageUtils#getPixels(String)}, and uses
	 * {@link ImageUtils#getRandomPixels(int, int, int, int)} to get pixels to embed
	 * bytes from text into. Embed the bytes from text into pixels of the image and
	 * outputs the new image to {@link output} using
	 * {@link ImageUtils#writePixels(int[][], String)}.
	 *
	 * @throws IOException
	 */
	private static void embed() throws IOException {
		System.out.println("Getting     '" + text + "'");
		byte[] bytes = Files.readAllBytes(Paths.get(text));
		System.out.println("Compressing '" + text + "'");
		byte[] compressed = TextUtils.compress(bytes);
		System.out.println("Encrypting  '" + text + "'");
		byte[] encrypted = TextUtils.xorCrypt(compressed, password.getBytes());
		System.out.println("Getting     '" + image + "'");
		int[][] pixels = ImageUtils.getPixels(image);
		System.out.println("Embedding   '" + text + "'");
		int[][] randomPixels = ImageUtils.getRandomPixels(new BigInteger(password.getBytes("US-ASCII")).intValue(),
				encrypted.length + 2, pixels.length, pixels[0].length);
		pixels[randomPixels[1][0]][randomPixels[0][0]] = Math.abs(password.hashCode() % 10000000);
		pixels[randomPixels[1][1]][randomPixels[0][1]] = encrypted.length;
		for (int k = 0; k < encrypted.length; k++)
			pixels[randomPixels[1][k + 2]][randomPixels[0][k + 2]] = encrypted[k];
		String output = "output" + image.substring(image.lastIndexOf("."));
		System.out.println("Writing     '" + output + "'");
		ImageUtils.writePixels(pixels, output);
		System.out.println("Done.\n\nEmbedded image outputted to '" + output + "'");
	}

	/**
	 * Extracts text from image.
	 * <p>
	 * Gets pixels from image with name {@link #image} using
	 * {@link ImageUtils#getPixels(String)}, and uses
	 * {@link ImageUtils#getRandomPixels(int, int, int, int)} to get pixels to
	 * extract text from. Decrypts (using password {@link #password}) and
	 * decompresses the data from these pixels and writes it to 'output.txt'.
	 *
	 * @throws IOException
	 */
	private static void extract() throws IOException {
		System.out.println("Getting       '" + image + "'");
		int[][] pixels = ImageUtils.getPixels(image);
		int[][] firstTwoPixels = ImageUtils.getRandomPixels(new BigInteger(password.getBytes("US-ASCII")).intValue(), 2,
				pixels.length, pixels[0].length);
		if (Math.abs(password.hashCode() % 10000000) != (int) pixels[firstTwoPixels[1][0]][firstTwoPixels[0][0]])
			throw new IOException("Incorrect password");
		int length = (int) pixels[firstTwoPixels[1][1]][firstTwoPixels[0][1]];
		byte[] bytes = new byte[length];
		int[][] randomPixels = ImageUtils.getRandomPixels(new BigInteger(password.getBytes("US-ASCII")).intValue(),
				length + 2, pixels.length, pixels[0].length);
		System.out.println("Getting       embedded text");
		for (int k = 0; k < length; k++)
			bytes[k] = (byte) pixels[randomPixels[1][k + 2]][randomPixels[0][k + 2]];
		System.out.println("Decrypting    embedded text");
		byte[] decrypted = TextUtils.xorCrypt(bytes, password.getBytes());
		System.out.println("Decompressing embedded text");
		byte[] decompressed = TextUtils.decompress(decrypted);
		System.out.println("Writing       'output.txt'");
		FileOutputStream out = new FileOutputStream(new File("output.txt"));
		out.write(decompressed);
		out.flush();
		out.close();
		System.out.println("Done.\n\nExtracted text outputted to 'output.txt'");
	}
}