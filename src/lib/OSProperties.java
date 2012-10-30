package lib;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * <p>The OSProperties class houses methods specific to System variables. These methods enable
 * programmers to find out, for example, if the user of their program is using Windows, Mac, or
 * Linux. Other information may also be obtained, such as getting the current User's name, screen
 * resolution, etc.
 * 
 * @author Adam Childs
 * @since 0.01
 * @version 1.00
 */
public class OSProperties
{

	/**
	 * <p>Returns true or false depending on whether or not the user's computer is running
	 * the Windows operating system.
	 */
	public boolean isWindows()
	{
	    return getOS().contains("win"); 
	}

	/**
	 * <p>Returns true or false depending on whether or not the user's computer is running
	 * the Mac operating system.
	 */
	public boolean isMac()
	{
	    return getOS().contains("mac"); 
	}

	/**
	 * <p>Returns true or false depending on whether or not the user's computer is running
	 * a derivative of the Unix operating system except for Mac OSX.
	 */
	public boolean isUnix()
	{
	    return getOS().contains("nix") || getOS().contains("nux");
	}

	/**
	 * <p>Returns the operating system's name.
	 * @return the OS's name as a String
	 */
	public String getOS()
	{
		return System.getProperty("os.name").toLowerCase();
	}

	/**
	 * <p>Returns the current user's name.
	 * @return the user's name as a String
	 */
	public String getUser()
	{
		return System.getProperty("user.name");
	}

	/**
	 * <p>Gets the user's operating system specific file separator symbol.
	 * 
	 * @return A String, in the form of: "/", "\\", etc., depending on the
	 * user's operating system.
	 */
	public String getSeparator()
	{
		return System.getProperty("file.separator");
	}

	/**
	 * <p>Returns the resolution of the user's display.
	 * @return the resolution of the user's display, as a new Dimension
	 */
	public Dimension getResolution()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * <p>Returns whether the user's computer's resolution is > 1400
	 * @return true or false
	 */
	public boolean isHighResolution()
	{
		return getResolution().width > 1400;
	}

	/**
	 * <p>Returns whether the user's computer's resolution is > 1249 and < 1400
	 * @return true or false
	 */
	public boolean isMidResolution()
	{
		if (getResolution().width > 1249 && getResolution().width < 1400)
			return true;
		return false;
	}

	/**
	 * <p>Returns whether the user's computer's resolution > 999 and < 1250
	 * @return true or false
	 */
	public boolean isLowMidResolution()
	{
		if (getResolution().width > 999 && getResolution().width < 1250)
			return true;
		return false;
	}

	/**
	 * <p>Returns whether the user's computer's resolution is < 1000
	 * @return true or false
	 */
	public boolean isLowResolution()
	{
		if (getResolution().width < 1000)
			return true;
		return false;
	}
}