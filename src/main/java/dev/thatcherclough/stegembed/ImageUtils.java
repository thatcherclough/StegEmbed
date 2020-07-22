package dev.thatcherclough.stegembed;

import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

	/**
	 * Gets matrix of pixels from image with name {@link file}.
	 *
	 * @param file name of image
	 * @return matrix of pixels from image with name {@link file}
	 * @throws IOException
	 */
	public static int[][] getPixels(String file) throws IOException {
		BufferedImage image = ImageIO.read(new File(file));
		int height = image.getHeight();
		int width = image.getWidth();
		int[][] pixels = new int[height][width];
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++)
				pixels[row][col] = image.getRGB(col, row) & 0xFFFFFF;
		return pixels;
	}

	/**
	 * Writes image with name {@link file} containing pixels {@link pixels}.
	 *
	 * @param pixels pixels of image to write
	 * @param file   name of image to write
	 * @throws IOException
	 */
	public static void writePixels(int[][] pixels, String file) throws IOException {
		BufferedImage image = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < pixels.length; row++)
			for (int col = 0; col < pixels[0].length; col++)
				image.setRGB(col, row, pixels[row][col]);
		ImageIO.write(image, file.substring(file.indexOf(".") + 1), new File(file));
	}

	/**
	 * Uses pseudo random number generation to get pixels for embedding.
	 * <p>
	 * Gets {@link numOfPixels} pixels from an image with height {@link height} and
	 * width {@link width}. Uses seed {@link seed} for pseudo random number
	 * generation of these pixels.
	 *
	 * @param seed        seed for pseudo random number generation
	 * @param numOfPixels number of random pixels to generate
	 * @param height      height of image to get pixels of
	 * @param width       width of image to get pixels of
	 * @return pixels to use for embedding
	 * @throws IOException
	 */
	public static int[][] getRandomPixels(int seed, int numOfPixels, int height, int width) throws IOException {
		int[][] pixels = new int[2][numOfPixels];
		Random random = new Random(seed);
		for (int k = 0; k < numOfPixels; k++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			if (pixelChosen(pixels, x, y)) {
				if (k != numOfPixels - 1)
					k--;
				continue;
			} else {
				pixels[0][k] = x;
				pixels[1][k] = y;
			}
		}
		return pixels;
	}

	/**
	 * Checks if matrix {@link pixels} already contains coordinates with x value
	 * {@link x} and y value {@link y}.
	 *
	 * @param pixels matrix of pixels to check
	 * @param x      x value
	 * @param y      y value
	 * @return if matrix {@link pixels} already contains coordinates with x value
	 *         {@link x} and y value {@link y}
	 */
	private static boolean pixelChosen(int[][] pixels, int x, int y) {
		for (int k = 0; k < pixels[0].length; k++)
			if (pixels[0][k] == x && pixels[1][k] == y)
				return true;
		return false;
	}
}