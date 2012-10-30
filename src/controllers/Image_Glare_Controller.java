package controllers;

import views.Image_Glare_View;

/**
 * <p>This class controls communication between the main thread and
 * view of the program.
 * 
 * @author Adam Childs
 * @since 0.01
 */
public class Image_Glare_Controller
{
	
	public Image_Glare_Controller()
	{
		
	}

	public void showView()
	{
		new Image_Glare_View().show();
	}
}