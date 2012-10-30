package controllers;

import models.Console_Model;

/**
 * <p>Controls communication between the GUI and the Console_Model. The console
 * is used for displaying interactive information pertaining to the image.
 * 
 * @author Adam Childs
 * @since 0.01
 */
public class Console_Controller
{
	Console_Model model;

	public Console_Controller()
	{
		model = new Console_Model();
	}

	public Console_Model getModel()
	{
		return model;
	}
}