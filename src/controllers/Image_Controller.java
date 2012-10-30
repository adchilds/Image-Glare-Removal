package controllers;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import models.Image_Model;

/**
 * <p>Controls communication between the main program and the image. The Image_Controller
 * class facilitates image manipulations.
 * 
 * @author Adam Childs
 * @since 0.01
 */
public class Image_Controller
{
	Image_Model model;

	public Image_Controller(ImageIcon image)
	{
		model = new Image_Model(image);
	}

	public Image_Controller(BufferedImage image)
	{
		model = new Image_Model(image);
	}

	public Image_Model getModel()
	{
		return model;
	}
}