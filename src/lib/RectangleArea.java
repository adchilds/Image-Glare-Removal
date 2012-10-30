package lib;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * RectangleArea.java
 * 
 * <p>The RectangleArea class extends the JLabel class to add drawing functionality to images within JLabels.
 * This class allows the user to drag a rectangle box over their image, which will in turn be used to retrieve
 * the exact coordinates and pixel values within the rectangle.
 * 
 * @author Adam Childs
 * @since 0.01
 */
public class RectangleArea extends JLabel implements ActionListener, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private int previousX, previousY;

	private Graphics buffer;
	private Image image;
	private Icon icon;
	private Rectangle cRect;

	public RectangleArea()
	{
	    super();
	    addMouseListener(this);
	    addMouseMotionListener(this);      
	}

	public void actionPerformed(ActionEvent e)
	{
		repaintoffscreen();
	}

	/*
	 * Double buffering to avoid flickering
	 */
	public void repaintoffscreen()
	{
		icon = this.getIcon();
		if (icon != null)
		{
			BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.createGraphics();
			icon.paintIcon(null, g, 0,0); // Paint Icon to the BufferedImage
			image = bi;
			buffer = image.getGraphics();

			Dimension d = getSize();
			if(cRect != null)
			{
				Rectangle box = getDrawableRect(cRect, d);
				buffer.drawRect(box.x, box.y, box.width - 1, box.height - 1);
				repaint();
			}
		}
	}

	private Rectangle getDrawableRect(Rectangle originalRect, Dimension drawingArea)
	{
		int x = originalRect.x;
		int y = originalRect.y;
		int width = originalRect.width;
		int height = originalRect.height;
		
		//Make sure rectangle width and height are positive.
		if (width < 0)
		{
			width = 0 - width;
			x -= width + 1;
			if (x < 0)
			{
				width += x;
				x = 0;
	        }
		}
		if (height < 0)
		{
			height = 0 - height;
			y -= height + 1;
			if (y < 0)
			{
				height += y;
				y = 0;
			}
		}

		//The rectangle shouldn't extend past the drawing area.
		if ((x + width) > drawingArea.width)
			width = drawingArea.width - x;
		if ((y + height) > drawingArea.height)
			height = drawingArea.height - y;

		return new Rectangle(x, y, width, height);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		previousX = e.getX();
		previousY = e.getY();
		cRect = new Rectangle(previousX, previousY, 0, 0);
		repaintoffscreen();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		previousX = e.getX();
		previousY = e.getY();
		cRect.setSize(previousX - cRect.x, previousY - cRect.y);
		repaintoffscreen();    
		repaint();
	}

	public void mouseReleased(MouseEvent e)
	{
		previousX = e.getX();
		previousY = e.getY();
		cRect.setSize(previousX - cRect.x, previousY - cRect.y);
		repaintoffscreen();  
		repaint();
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	public void paint(Graphics g)
	{
		g.drawImage(image, 0, 0, this);
	}
}