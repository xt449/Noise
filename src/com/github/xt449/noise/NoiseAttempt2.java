package com.github.xt449.noise;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author Jonathan Talcott (xt449/BinaryBanana)
 */
public class NoiseAttempt2 {

	private static final int SIZE = 1001; // this number must be at least 1 greater than the divisor for the values passed into getNoise ; recommend this is also more than IMG_SIZE
	private static final int IMG_SIZE = 1000; // this only controls the resolution of the image

	private static final Random random = new Random();
	private static final Point2D.Float[][] vectors = new Point2D.Float[SIZE][SIZE];

	private static void generateVectors() {
		for(int y = 0; y < vectors.length; y++) {
			for(int x = 0; x < vectors.length; x++) {
//				vectors[x][y] = new Point2D.Float(
//						// (-1.0,1.0)
//						(float) Math.tanh(random.nextFloat() * Math.PI),
//						(float) Math.tanh(random.nextFloat() * Math.PI)
//				);

//				vectors[x][y] = new Point2D.Float(
//						// (-1.0,1.0)
//						(random.nextBoolean() ? -random.nextFloat() : random.nextFloat()) / 2,
//						(random.nextBoolean() ? -random.nextFloat() : random.nextFloat()) / 2
//				);

				vectors[x][y] = new Point2D.Float((float) (random.nextGaussian() % 1) / 2, (float) (random.nextGaussian() % 1) / 2);

//				vectors[x][y] = new Point2D.Float(
//						// (-1.0,1.0)
//						dot(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat()),
//						dot(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat())
//				);
			}
		}
	}

	public static float getNoise(float x, float y) {
		// [0.0,xMax-1]
		float xf = (float) Math.floor(x);
		// [0.0,yMax-1]
		float yf = (float) Math.floor(y);

		// [0,xMax]
		int xi = (int) xf;
		// [0,yMax]
		int yi = (int) yf;

		// [0.0,1.0)
		float xd = x - xf;
		// [0.0,1.0)
		float yd = y - yf;

		//              .     (-1.0,1.0)      .   [0.0,1.0)   [0.0,1.0)
		// (-2.0,2.0)   .                     .     .    .     .    .
		float dp0 = dot(vectors[xi + 0][yi + 0], xd - 0, yd - 0);
		//              .     (-1.0,1.0)      .   [-1.0,0.0)  [0.0,1.0)
		// (-2.0,2.0)   .                     .     .    .     .    .
		float dp1 = dot(vectors[xi + 1][yi + 0], xd - 1, yd - 0);
		//              .     (-1.0,1.0)      .   [0.0,1.0)   [-1.0,0.0)
		// (-2.0,2.0)   .                     .     .    .     .    .
		float dp2 = dot(vectors[xi + 0][yi + 1], xd - 0, yd - 1);
		//              .     (-1.0,1.0)      .   [-1.0,0.0)  [-1.0,0.0)
		// (-2.0,2.0)   .                     .     .    .     .    .
		float dp3 = dot(vectors[xi + 1][yi + 1], xd - 1, yd - 1);

		// [0.0,1.0)
		float xu = xd * xd * (3 - 2 * xd);
		// [0.0,1.0)
		float yu = yd * yd * (3 - 2 * yd);

		// (-1.0,1.0)
		float l0 = lerp(dp0, dp1, xu);
		// (-1.0,1.0)
		float l1 = lerp(dp2, dp3, xu);
		// (-1.0,1.0)
		float result = lerp(l0, l1, yu);

		return result;
	}

	public static float lerp(float min, float max, float value) {
		return ((1 - value) * min) + (value * max);
	}

	public static float map(float value, float minIn, float maxIn, float minOut, float maxOut) {
		return (((value - minIn) / (maxIn - minIn)) * (maxOut - minOut)) + minOut;
	}

	public static float dot(Point2D.Float vector1, Point2D.Float vector2) {
		return (vector1.x * vector2.y) - (vector1.y * vector2.x);
	}

	public static float dot(Point2D.Float vector1, float x, float y) {
		return (vector1.x * y) - (vector1.y * x);
	}

	public static float dot(float x0, float y0, float x1, float y1) {
		return (x0 * y1) - (y0 * x1);
	}

	public static void main(String[] args) throws IOException {
		generateVectors();

		final BufferedImage image = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_3BYTE_BGR);

		for(int y = 0; y < IMG_SIZE; y++) {
			for(int x = 0; x < IMG_SIZE; x++) {
				image.setRGB(x, y, Color.getHSBColor(2 * getNoise(x / ((float) IMG_SIZE / 7), y / ((float) IMG_SIZE / 7)), 1.0F, 1.0F).getRGB());
			}
		}

		ImageIO.write(image, "png", new File("attempt2.png"));
	}
}
