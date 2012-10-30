package models;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;

/**
 * <p>Deals with image manipulation and presenting the user with image specific
 * information.
 * 
 * @author Adam Childs
 * @since 0.01
 */
public class Image_Model
{
	BufferedImage bImage;
	ImageIcon image;

	public Image_Model(ImageIcon i)
	{
		image = i;
		bImage = convert();
	}

	public Image_Model(BufferedImage i)
	{
		bImage = i;
	}

	public BufferedImage convert()
	{
		return imageIconToBufImage(image);
	}

	/**
	 * <p>Converts an ImageIcon to a BufferedImage.
	 * @param i the ImageIcon to convert
	 * @return
	 */
	private BufferedImage imageIconToBufImage(ImageIcon i)
	{
		Image source = i.getImage();
		int w = source.getWidth(null);
		int h = source.getHeight(null);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)bi.getGraphics();
		g2d.drawImage(source, 0, 0, null);
		g2d.dispose();

		return bi;
	}

	/**
	 * <p>Returns an integer array holding the RGB values at the location
	 * specified by the parameters.
	 * 
	 * @param x x-location of a coordinate plane
	 * @param y y-location of a coordinate plane
	 * @return an integer array containing the RGB value at location (x, y)
	 */
	public int[] getPixel(int x, int y)
	{
		int argb = bImage.getRGB(x, y);

		int rgb[] = new int[] {
		    (argb >> 16) & 0xff, // Red
		    (argb >>  8) & 0xff, // Green
		    (argb      ) & 0xff  // Blue
		};
		
		return rgb;
	}

	/**
	 * <p>Resizes the buffered image supplied as a parameter when the
	 * ImageEdit class was instantiated somewhere else in the program.
	 * 
	 * @param nWidth the new width of the image
	 * @param nHeight the new height of the image
	 * @return
	 */
	public BufferedImage resizeImage(int nWidth, int nHeight)
	{
		// Create new (blank) image of required (scaled) size
		BufferedImage scaledImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

		// Paint scaled version of image to new image
		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(bImage, 0, 0, nWidth, nHeight, null);

		// Clean up
		graphics2D.dispose();
		return scaledImage;
	}

	/**
	 * <p>Converts the image RGB values to grayscale values.
	 * 
	 * @return a BufferedImage converted to grayscale
	 */
	public BufferedImage convertToGrayscale()
	{
		BufferedImage image = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = image.getGraphics();
		g.drawImage(bImage, 0, 0, null);
		g.dispose();
		
		return image;
	}

	/**
	 * <p>Converts the given RGB values to their equivalent Y from YIQ values.
	 * <p><i>Note: Doesn't convert the image, only the values.</i>
	 * 
	 * @param rgb an array of RGB pixel values
	 * @return the equivalent Y from YIQ values of the given RGB values
	 */
	public double rgbToY(int[] rgb)
	{
		return 	(rgb[0] * 0.299) + 
				(rgb[1] * 0.587) + 
				(rgb[2] * 0.114);
	}

	/**
	 * <p>Converts a BufferedImage's pixel values from RGB to Y from YIQ.
	 * 
	 * @return a new BufferedImage, where the pixel values are represented in Y from YIQ format
	 */
	public BufferedImage rgbToY()
	{
		int[] curPixel = new int[3];
		double y = 0.0;

		for (int i = 0; i < bImage.getWidth(); i++)
		{
			for (int j = 0; j < bImage.getHeight(); j++)
			{
				curPixel = getPixel(i, j);

				y = (curPixel[0] * 0.299) + 
					(curPixel[1] * 0.587) + 
					(curPixel[2] * 0.114);

				bImage.setRGB(i, j, (int)y);
			}
		}

		return bImage;
	}

	/**
	 * <p>Converts the RGB values in a specific range to their equivalent Y value from YIQ.
	 * <p><i>Note: Doesn't convert the image, only the values.</i>
	 * 
	 * @param xLow starting point for x
	 * @param xHigh ending point for x
	 * @param yLow starting point for y
	 * @param yHigh ending point for y
	 * @return an ArrayList<Integer> containing the Y value equivalent of each RGB value
	 */
	public ArrayList<Integer> rgbToY(int xLow, int xHigh, int yLow, int yHigh)
	{
		ArrayList<Integer> arr = new ArrayList<Integer>();

		// Convert RGB to Y from YIQ:
		for (int i = xLow; i < xHigh; i++)
		{
			for (int j = yLow; j < yHigh; j++)
			{
				// Change RGB to Y and add it to an ArrayList<Integer>
				int[] temp = getPixel(i, j);
				int val = (int)rgbToY(temp);
				arr.add(val);
			}
		}
		return arr;
	}

	/**
	 * <p>Prints the Y value for 30 pixels to the right and left and x, and 30 pixels
	 * to the north and south of y.
	 * 
	 * @param x x-location of a coordinate plane
	 * @param y y-location of a coordinate plane
	 * @return an ArrayList<Integer> containing intensity values (Y from YIQ) of their corresponding RGB values.
	 */
	public ArrayList<Integer> printHorizontal(int x, int y)
	{
		ArrayList<Integer> arr = new ArrayList<Integer>();

		for (int i = (x - 30); i < (x + 30); i++)
		{
			int[] temp = getPixel(i, y);
			arr.add((int)rgbToY(temp));
		}
		
		return arr;
	}

	/**
	 * <p>Computes the lowest variance in a 60 x 60 matrix, beginning at position (x, y).
	 * 
	 * @param x the starting x-coordinate of the matrix
	 * @param y the starting y-coordinate of the matrix
	 * @return an integer value representing the lowest variance in a glare spot
	 */
	public int getLowestVariance(int x, int y)
	{
		ArrayList<Integer> arr;

		int largest = 0;
		int lowestVarPointer = 0;
//		double lowVariance = 2147000000.0;
//		double highVariance = 0.0;

		// Convert to Grayscale
		convertToGrayscale();

		// Convert RGB to Y from YIQ:
		arr = rgbToY(x - 30, x + 30, y - 30, y + 30);

		// Sort the Y values from lowest to highest:
		Collections.sort(arr);
		System.out.println("Sorted array: " + arr.toString());

		// Find the largest value
		largest = arr.get(arr.size() - 1);

		// Keep a pointer to the current place in the array we're looking at
		int pointer = arr.indexOf(largest);
		lowestVarPointer = pointer;

		// Set pointer to point to the index of the next value lower than the highest:
		if (pointer > 0)
			pointer--;

		// Histrogram (probability of i)
		double[] hist = histogram(arr);

		// Mean pixel value in arr
		double mean = getMean(arr, 0, arr.size() - 1);

		// Total Variance
		double totalVariance = 0.0;
		for (int i = 0; i < (255 - 1); i++)
			totalVariance = totalVariance + (Math.pow((i - mean), 2) * hist[i]);

		System.out.println("TOTAL VARIANCE:\t\t\t" + totalVariance);

		double lowestN = 214700000000.0;
		/*
		 * Compute the lowest variance in the list by repeatedly moving
		 * the pointer to the next lowest value in the list and recomputing
		 * the variance.
		 * 
		 * sum (x - avg)^2 / n
		 */
		while (pointer > 0)
		{
			// If arr[0] and arr[1] are the same, break
			if (arr.get(pointer) == arr.get(pointer + 1))
				break;

			double w0 = 0.0;
			for (int i = 0; i < hist.length; i++)
				w0 = w0 + hist[arr.get(pointer)];
			double w1 = 1 - w0;
			double mi = getMean(arr, 0, pointer);
			double m0 = getMean(arr, pointer, arr.size() - 1);

			double O2B = (w0 * w1) * (mi * m0);
			
			double n = O2B / totalVariance;
			System.out.print(n + " ");
			
			if (n < lowestN)
			{
				lowestN = n;
				lowestVarPointer = pointer;
			}

			// Adjust the pointer to the next lowest number
			int temp = pointer;
			while(pointer > 0 && arr.get(pointer).equals(arr.get(temp)))
				pointer--;
		}

		System.out.println();
		System.out.println("Pointer index:\t\t\t" + lowestVarPointer);
		System.out.println("Change everything above:\t" + arr.get(lowestVarPointer));
		return (int)lowestN;
	}

	/**
	 * <p>Calculates the variance of a given ArrayList<Integer> beginning at index start
	 * and ending on the specified index end.
	 * 
	 * @param arr the array to calculate the variance for
	 * @param start the index to start at in the array
	 * @param end the index to stop at in the array
	 * @return the variance of the given ArrayList<Integer>
	 */
	public double getVariance(ArrayList<Integer> arr, int start, int end)
	{
		double mean = getMean(arr, start, end);

		// Compute the variance of arr from 0 - end
		double sum = 0.0;
		for (int j = start; j <= end; j++)
			sum = sum + Math.pow((arr.get(j) - mean), 2);

		return (sum / end);
	}

	/**
	 * <p>Calculates the mean of the given ArrayList<Integer> beginning at the
	 * specified 'start' index and ending on the specified 'end' index.
	 * 
	 * @param arr an ArrayList<Integer> of integers values
	 * @param start the index of the ArrayList<Integer> to start at
	 * @param end the index of the ArrayList<Integer> to end on
	 * @return the mean of the values in the ArrayList<Integer>
	 */
	public double getMean(ArrayList<Integer> arr, int start, int end)
	{
		double mean = 0.0;
		
		for (int i = start; i <= end; i++)
			mean = mean + arr.get(i);
		
		return mean / end;
	}

	/**
	 * <p>Creates a histogram representation with probability of each value in
	 * the given array.
	 * 
	 * @param arr The ArrayList<Integer> to find the histogram for
	 * @return an int[] array where index i equals the value and the data at
	 * 			index i equals the probability of i's occurrence in the array.
	 */
	public double[] histogram(ArrayList<Integer> arr)
	{
		Collections.sort(arr);
		int max = arr.get(arr.size() - 1);
		double[] hist = new double[max + 1];
		
		for (int i = 0; i < arr.size(); i++)
			hist[arr.get(i)]++;

		for (int i = 0; i < hist.length; i++)
			hist[i] = hist[i] / (arr.size() - 1);

		return hist;
	}

	/**
	 * <p>Calculates the standard deviation of the given ArrayList<Integer> arr from start to end.
	 * 
	 * @param arr The array the calculate the standard deviation for
	 * @param start the index of the ArrayList<Integer> to start at
	 * @param end the index of the ArrayList<Integer> to end on
	 * @return the standard deviation of the values in the ArrayList<Integer>
	 */
	public double getStandardDeviation(ArrayList<Integer> arr, int start, int end)
	{
		return 0.0;
	}

	public int getWidth()
	{
		return image.getIconWidth();
	}

	public int getHeight()
	{
		return image.getIconHeight();
	}

	public Dimension getSize()
	{
		return new Dimension(getWidth(), getHeight());
	}

	public Image getImage()
	{
		return Toolkit.getDefaultToolkit().createImage(bImage.getSource());
	}

	public BufferedImage getBImage()
	{
		return bImage;
	}

	public ImageIcon getIImage()
	{
		return image;
	}

	public void setImage(ImageIcon i)
	{
		image = i;
		bImage = convert();
	}
}