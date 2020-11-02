package com.github.xt449.noise;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public class NoiseAttempt1 {

	private static final int IMG_SIZE = 100; // this only controls the resolution of the image

	private static final double[][] sourcesWrapEdge = new double[IMG_SIZE + 1][IMG_SIZE + 1];

	public static float getNoise(double x, double y) {
//		int ix = (int) map(x, 0, 1, 0, sourcesWrapEdge[0].length - 1);
//		int iy = (int) map(y, 0, 1, 0, sourcesWrapEdge[0].length - 1);
		int ix = (int) lerp(0, sourcesWrapEdge[0].length - 1, (float) x);
		int iy = (int) lerp(0, sourcesWrapEdge[0].length - 1, (float) y);

//		int c0 = sourcesWrapEdge[ix][iy];
//		int c1 = sourcesWrapEdge[ix+1][iy];
//		int c2 = sourcesWrapEdge[ix][iy+1];
//		int c3 = sourcesWrapEdge[ix+1][iy+1];

//		float c0 = sourcesWrapEdge[ix][iy];
//		float c1 = sourcesWrapEdge[ix+1][iy];
//		float c2 = sourcesWrapEdge[ix][iy+1];
//		float c3 = sourcesWrapEdge[ix+1][iy+1];

		double c0 = sourcesWrapEdge[ix][iy];
		double c1 = sourcesWrapEdge[ix + 1][iy];
		double c2 = sourcesWrapEdge[ix][iy + 1];
		double c3 = sourcesWrapEdge[ix + 1][iy + 1];

		float dx = (float) (x - Math.floor(x));
		float dy = (float) (y - Math.floor(y));

		float x0 = (float) lerp(c0, c1, dx);
		float x1 = (float) lerp(c2, c3, dx);

		float y0 = (float) lerp(c0, c2, dy);
		float y1 = (float) lerp(c1, c3, dy);

//		double x0 = map(dx, 0, 1, c0, c1);
//		double x1 = map(dx, 0, 1, c2, c3);
//
//		double y0 = map(dy, 0, 1, c0, c2);
//		double y1 = map(dy, 0, 1, c1, c3);

		float average = (x0 + x1 + y0 + y1) / 4;

		return average;
	}

	public static float lerp(float min, float max, float value) {
		return ((1 - value) * min) + (value * max);
	}

	public static double lerp(double min, double max, double value) {
		return ((1 - value) * min) + (value * max);
	}

	public static double map(double value, double minIn, double maxIn, double minOut, double maxOut) {
		return (((value - minIn) / (maxIn - minIn)) * (maxOut - minOut)) + minOut;
	}

	public static void main(String[] args) throws IOException {
		for(int y = 0; y < sourcesWrapEdge.length; y++) {
			for(int x = 0; x < sourcesWrapEdge.length; x++) {
				sourcesWrapEdge[x][y] = Math.random();
			}
		}

		final BufferedImage image = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_3BYTE_BGR);

		for(int y = 0; y < IMG_SIZE; y++) {
			for(int x = 0; x < IMG_SIZE; x++) {
				image.setRGB(x, y, Color.getHSBColor(getNoise(x / ((float) IMG_SIZE), y / ((float) IMG_SIZE)), 1.0F, 1.0F).getRGB());
			}
		}

		ImageIO.write(image, "png", new File("attempt1.png"));
	}
}
