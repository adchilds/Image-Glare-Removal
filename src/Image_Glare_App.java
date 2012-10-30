import javax.swing.SwingUtilities;

import controllers.Image_Glare_Controller;

/**
 * <p>This class instantiates the program which creates and displays a new user interface
 * on the screen.
 * 
 * @author Adam Childs
 * @since 0.01
 */
class Image_Glare_App
{

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					new Image_Glare_Controller().showView();
				}
			}
		);
	}
}