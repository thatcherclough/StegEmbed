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
	private static String help = "StegEmbed: A program that embeds and extracts text in and out of the pixels of an image (1.2.0)\n\nUsage:\n\tjava -jar stegembed.jar   [-h] [-v] [embed -i IMAGE -t TEXTFILE -p PASSWORD]\n"
			+ "\t\t\t\t  [extract -i IMAGE -p PASSWORD]\nArguments:\n\t-h, --help	Display this message.\n\t-v, --version	Display current version.\n\t-t, --text	Specify text file to use for embedding.\n\t-i, --image	Specify image to use for embedding/extracting.\n\t-p, --password	Specify password to use for encrypting/decrypting when embedding/extracting.";

	/*
	 * Embed or extract text with given command line arguments.
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
				if (e.toString().contains("java.lang.NegativeArraySizeException")
						|| e.toString().equals("java.util.zip.ZipException: incorrect header check"))
					error("Incorrect password");
				else
					error(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(help);
			System.exit(0);
		}
	}

	/**
	 * Encrypt, compress, and embed bytes from {@link text} into image {@link image}
	 * by pseudo randomly generating pixels based on {@link password} and outputting
	 * pixels to new image file with name {@link output}.
	 *
	 * @throws IOException
	 */
	private static void embed() throws IOException {
		System.out.println("Getting     '" + text + "'");
		System.out.println("Compressing '" + text + "'");
		byte[] compressed = TextUtils.compress(Files.readAllBytes(Paths.get(text)));
		System.out.println("Encrypting  '" + text + "'");
		byte[] encrypted = TextUtils.xorCrypt(compressed, password.getBytes());
		System.out.println("Getting     '" + image + "'");
		int[][] pixels = ImageUtils.getPixels(image);
		int[][] randomPixels = ImageUtils.getRandomPixels(new BigInteger(password.getBytes("US-ASCII")).intValue(),
				encrypted.length + 1, pixels.length, pixels[0].length);
		pixels[randomPixels[1][0]][randomPixels[0][0]] = encrypted.length + 1;
		System.out.println("Embedding   '" + text + "'");
		for (int k = 0; k < encrypted.length; k++)
			pixels[randomPixels[1][k + 1]][randomPixels[0][k + 1]] = encrypted[k];
		String output = "output" + image.substring(image.lastIndexOf("."));
		System.out.println("Writing     '" + output + "'");
		ImageUtils.writePixels(pixels, output);
		System.out.println("Done.\n\nEmbedded image outputted to '" + output + "'");
	}

	/**
	 * Extract, decrypt, and decompress embedded text from {@link image} using
	 * password {@link password} and output text to 'output.txt'.
	 *
	 * @throws IOException
	 */
	private static void extract() throws IOException {
		System.out.println("Getting       '" + image + "'");
		int[][] pixels = ImageUtils.getPixels(image);
		int[][] firstPixel = ImageUtils.getRandomPixels(new BigInteger(password.getBytes("US-ASCII")).intValue(), 1,
				pixels.length, pixels[0].length);
		int length = (byte) pixels[firstPixel[1][0]][firstPixel[0][0]];
		byte[] bytes = new byte[length - 1];
		int[][] randomPixels = ImageUtils.getRandomPixels(new BigInteger(password.getBytes("US-ASCII")).intValue(),
				length, pixels.length, pixels[0].length);
		System.out.println("Getting       embedded text");
		for (int k = 1; k < length; k++)
			bytes[k - 1] = (byte) pixels[randomPixels[1][k]][randomPixels[0][k]];
		System.out.println("Decrpyting    embedded text");
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

	/**
	 * Display 'An error occurred' followed by {@link errorMessage} and exit.
	 *
	 * @param errorMessage error message
	 */
	private static void error(String errorMessage) {
		System.out.println("\nAn error occurred:\n" + errorMessage);
		System.exit(0);
	}
}
