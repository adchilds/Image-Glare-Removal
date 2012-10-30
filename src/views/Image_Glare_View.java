package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import lib.OSProperties;
import lib.RectangleArea;
import controllers.Image_Controller;

/**
 * <p>Controls the display of the main program.
 * 
 * @author Adam Childs
 * @since 0.01
 */
public class Image_Glare_View implements ActionListener, MouseListener, MouseMotionListener
{
	Container contentPane;
	ImageIcon image; // The image that has been selected by the user
	JFrame f; // The program's visual frame
	JLabel imageLabel;
	JTextArea console;
	JTextField xField, yField; // Display the width and height of the image
	JTextField textField; // Displays the current position of the mouse relative to the image
	OSProperties osp; // Operating System specific information
	Point mouseCoords; // Holds the current (x, y) position of the mouse
	double version = 0.01;
	String lastUpdate = "10/01/2012";

	public Image_Glare_View()
	{
		f = new JFrame();
		mouseCoords = new Point();
		osp = new OSProperties();
	}

	public void show()
	{
		f.setTitle("Remove Image Glare");
		contentPane = f.getContentPane();
		f.setContentPane(contentPane);
		f.setJMenuBar(menuBar());

		/*
		 * Set the Look and Feel to the OS's default L&F
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			System.out.println("L&F class could not be found for this system...");
			System.out.println("Using the default L&F.");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println("L&F unsupported for this system.");
			System.out.println("Using the default L&F.");
		}

		/*
		 * Main JPanel content (only the image)
		 */
		imageLabel = new RectangleArea(); // RectangleArea so that we can draw over the image with rectangles that the user creates
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // centers images in the JLabel
		JScrollPane sp = new JScrollPane(imageLabel);
		sp.setSize(new Dimension(contentPane.getSize()));
		sp.setMinimumSize(new Dimension(contentPane.getSize()));
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		/*
		 * Mouse Listening/Events
		 */
		imageLabel.addMouseListener(this);
		imageLabel.addMouseMotionListener(this);
		imageLabel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

		/*
		 * Add the console to the bottom of the content pane
		 */
		console = consoleProperties();
		JScrollPane csp = new JScrollPane(console);
		csp.setPreferredSize(new Dimension(f.getWidth(), 125));
		csp.setMinimumSize(new Dimension(f.getWidth(), 125));
		csp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		/*
		 * Add JScrollPanes to the splitPane
		 */
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, csp);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(425);

		/*
		 * Add Components to the content pane
		 */
		contentPane.add(toolBar(), BorderLayout.NORTH);
		contentPane.add(splitPane);

		f.setSize(700, 700);
		f.setMinimumSize(new Dimension(525, 700));
		f.setLocationRelativeTo(null); // Centers the frame on the user's screen
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	private JToolBar toolBar()
	{
		JToolBar toolbar = new JToolBar();
		toolbar.setLayout(new BorderLayout());
		JButton button;
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));

		button = new JButton(new ImageIcon(getClass().getResource("../images/color_wheel.png")));
		button.setToolTipText("Get Pixel Colors");
		button.setFocusPainted(false);
		button.setActionCommand("pixeldata");
		button.addActionListener(this);
		p.add(button);

		button = new JButton(new ImageIcon(getClass().getResource("../images/resize.gif")));
		button.setToolTipText("Resize Image");
		button.setFocusPainted(false);
		button.setActionCommand("resizebutton");
		button.addActionListener(this);
		p.add(button);

		JPanel panel = new JPanel(new BorderLayout());
		JPanel northPanel, southPanel;
		JLabel label;

		/*
		 * North Panel
		 */
		northPanel = new JPanel();
		label = new JLabel("Width:", JLabel.RIGHT);
		label.setPreferredSize(new Dimension(50, 25));
		label.setToolTipText("New Image Width");
		xField = new JTextField();
		xField.setPreferredSize(new Dimension(50, 25));
		xField.setEnabled(true);
		xField.setText("" + p.getWidth());

		northPanel.add(label);
		northPanel.add(xField);

		/*
		 * South Panel
		 */
		southPanel = new JPanel();
		label = new JLabel("Height:", JLabel.RIGHT);
		label.setPreferredSize(new Dimension(50, 25));
		label.setToolTipText("New Image Height");
		yField = new JTextField();
		yField.setPreferredSize(new Dimension(50, 25));
		yField.setEnabled(true);
		yField.setText("" + p.getHeight());

		southPanel.add(label);
		southPanel.add(yField);

		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(southPanel, BorderLayout.SOUTH);
		p.add(panel);

		button = new JButton("Remove Glare");
		button.setToolTipText("Remove Image Glare");
		button.setFocusPainted(false);
		button.setActionCommand("glare");
		button.addActionListener(this);
		p.add(button);

		toolbar.add(p);

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(125, 25));
		textField.setEditable(false);
		textField.setForeground(Color.RED);
		textField.setText("Off Screen!");
		toolbar.add(textField, BorderLayout.EAST);

		toolbar.setRollover(true);
		toolbar.setFloatable(false);
		toolbar.setSize(f.getWidth(), 15);
		toolbar.setVisible(true);

		return toolbar;
	}

	private JMenuBar menuBar()
	{
		JMenuBar m = new JMenuBar();
		JMenu fileMenu, editMenu, imageMenu, viewMenu, helpMenu;
			fileMenu = new JMenu( "File" );
			editMenu = new JMenu( "Edit" );
			imageMenu = new JMenu( "Image" );
			viewMenu = new JMenu( "View" );
			helpMenu = new JMenu( "Help" );
		JMenuItem open, save, exit, rmvGlare, pixelData, resizeImage, grayscale, help, about;
			open = new JMenuItem( "Open" );
			save = new JMenuItem( "Save" );
			exit = new JMenuItem( "Exit" );
//			undo = new JMenuItem( "Undo" );
			rmvGlare = new JMenuItem( "Remove Glare" );
			pixelData = new JMenuItem( "Pixel Colors" );
			resizeImage = new JMenuItem( "Resize" );
			grayscale = new JMenuItem( "Convert to Grayscale" );
			help = new JMenuItem( "Help" );
			about = new JMenuItem( "About" );
		JCheckBoxMenuItem viewConsole;
			viewConsole = new JCheckBoxMenuItem( "Console" );
			viewConsole.setSelected(true);

		// Add JMenuItems to their respective JMenus
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.addSeparator();
		fileMenu.add(exit);

//		editMenu.add(undo);

		imageMenu.add(rmvGlare);
		imageMenu.add(pixelData);
		imageMenu.add(resizeImage);
		imageMenu.add(grayscale);

		viewMenu.add(viewConsole);

		helpMenu.add(help);
		helpMenu.add(about);

		// Add the JMenus to the JMenuBar
		m.add(fileMenu);
		m.add(editMenu);
		m.add(imageMenu);
		m.add(viewMenu);
		m.add(helpMenu);

		// Instantiate ActionListening Events for JMenuItems
		open.setActionCommand("open");
		open.addActionListener(this);
		save.setActionCommand("save");
		save.addActionListener(this);
		exit.setActionCommand("exit");
		exit.addActionListener(this);
//		undo.setActionCommand("undo");
//		undo.addActionListener(this);
		rmvGlare.setActionCommand("glare");
		rmvGlare.addActionListener(this);
		pixelData.setActionCommand("pixeldata");
		pixelData.addActionListener(this);
		resizeImage.setActionCommand("resizemenu");
		resizeImage.addActionListener(this);
		grayscale.setActionCommand("grayscale");
		grayscale.addActionListener(this);
		viewConsole.setActionCommand("viewConsole");
		viewConsole.addActionListener(this);
		help.setActionCommand("help");
		help.addActionListener(this);
		about.setActionCommand("about");
		about.addActionListener(this);

		// Mnemonics
		fileMenu.setMnemonic('F');
		editMenu.setMnemonic('E');
		helpMenu.setMnemonic('H');
				
		// Accelerators
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
				
		return m;
	}

	private JTextArea consoleProperties()
	{
		JTextArea temp = new JTextArea();
		temp.setEditable(false);
		temp.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		return temp;
	}

	/**
	 * Add text from a specified file to the specified JTextArea
	 * @param t JTextArea to add the content to
	 * @param f Text file name
	 */
	private void addContent(JTextArea t, String f)
	{
		String line;
		try {
			InputStream iStream = getClass().getResourceAsStream(f);
			InputStreamReader isr = new InputStreamReader(iStream);
			BufferedReader reader = new BufferedReader(isr);
			
			while ((line = reader.readLine()) != null)
			{
				t.append(line);
				t.append("\n");
			}
			iStream.close();
			isr.close();
			reader.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * <p>Determines whether or not the clicked coordinate is out of the set image's bounds.
	 * 
	 * @param x the clicked x-location
	 * @param y the clicked y-location
	 * @return a boolean representing if the user has clicked outside the bounds of the set image.
	 */
	private boolean clickOutOfBounds(int x, int y)
	{
		return (x > image.getIconWidth()) || (y > image.getIconHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("exit"))
		{
			if (image != null)
			{
				// Prompt the user to save their progress
			}
			System.exit(0);
		}
		else if (e.getActionCommand().equals("open"))
		{
			JFileChooser open;
			if (osp.isMac())
				open = new JFileChooser(osp.getSeparator() + "Users" + osp.getSeparator() + osp.getUser() + osp.getSeparator() + "Pictures");
			else if (osp.isWindows())
				open = new JFileChooser("C:" + osp.getSeparator() + "Users" + osp.getSeparator() + osp.getUser() + osp.getSeparator() + "Pictures");
			else
				open = new JFileChooser();

			int returnVal = open.showOpenDialog(f);

			File file;
			if (returnVal == JFileChooser.APPROVE_OPTION)
				file = open.getSelectedFile();
			else // If the user hits the cancel button
				return;
			
			BufferedImage bi = null;
			ImageIcon im = null;
			try {
				bi = ImageIO.read(file);
			} catch (IOException e1) {}
			
			if (bi instanceof BufferedImage)
			{
				im = new ImageIcon(bi);
				imageLabel.setIcon(im);
			} else {
				System.out.println("Selected file is not an image!");
				return;
			}
			((RectangleArea) imageLabel).repaintoffscreen();

			Image_Controller controller = new Image_Controller(im);
			image = im;
			xField.setText("" + controller.getModel().getWidth());
			yField.setText("" + controller.getModel().getHeight());
		}
		else if (e.getActionCommand().equals("resizebutton")) // User clicks button on toolbar
		{
			if (image != null)
			{
				Image_Controller controller = new Image_Controller(image);
				controller.getModel().convert();
				image = new ImageIcon(controller.getModel().resizeImage(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText())));
				imageLabel.setIcon(image);
				imageLabel.validate();
				((RectangleArea) imageLabel).repaintoffscreen();
//				previousImage.add(im);
			} else
				JOptionPane.showMessageDialog(f, "You need to set the image first.",
						"No Image", JOptionPane.ERROR_MESSAGE);
		}
		else if (e.getActionCommand().equals("resizemenu"))
		{
			if (image != null)
			{
				String s = (String)JOptionPane.showInputDialog(f,
	                    "Enter a new image proportion\n"
	                    + "(e.g. 1/3 or 1/2 or 5/3)",
	                    "Resize Image",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    "1/2");

				if (s == null) // Cancel button pressed
					return;

				int indexOfDiv = s.indexOf('/');
				try
				{
					String s1 = s.substring(0, indexOfDiv);
					String s2 = s.substring(indexOfDiv + 1, s.length());
					double proportion = Double.parseDouble(s1) / Double.parseDouble(s2);
					double x = Integer.parseInt(xField.getText()) * proportion;
					double y = Integer.parseInt(yField.getText()) * proportion;

					Image_Controller controller = new Image_Controller(image);
					controller.getModel().convert();
					image = new ImageIcon(controller.getModel().resizeImage((int)x, (int)y));
					imageLabel.setIcon(image);
					imageLabel.validate();
					((RectangleArea) imageLabel).repaintoffscreen();
//					previousImage.add(image);
					xField.setText("" + (int)x);
					yField.setText("" + (int)y);
				} catch (StringIndexOutOfBoundsException ex) { // User entered: #
					JOptionPane.showMessageDialog(f, "Wrong format! Please use the format: 1/2, 1/3, 5/2, etc...",
							"Wrong format", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException ex) { // User entered: #/
					JOptionPane.showMessageDialog(f, "Wrong format! Please use the format: 1/2, 1/3, 5/2, etc...",
							"Wrong format", JOptionPane.ERROR_MESSAGE);
				}
			} else
				JOptionPane.showMessageDialog(f, "You need to set the image first.",
						"No Image", JOptionPane.ERROR_MESSAGE);
		}
		else if (e.getActionCommand().equals("grayscale"))
		{
			if (image != null)
			{
				Image_Controller controller = new Image_Controller(image);
				controller.getModel().convert();
				image = new ImageIcon(controller.getModel().convertToGrayscale());
				imageLabel.setIcon(image);
				imageLabel.validate();
				((RectangleArea) imageLabel).repaintoffscreen();
//				previousImage.add(im);
			} else
				JOptionPane.showMessageDialog(f, "You need to set the image first.",
						"No Image", JOptionPane.ERROR_MESSAGE);
		}
		else if (e.getActionCommand().equals("help"))
		{
			// Open a general information window
			JTextArea textArea = new JTextArea();
			addContent(textArea, "../HELP");
			textArea.setEditable(false);
			textArea.setCaretPosition(java.awt.Frame.NORMAL);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
			JScrollPane sc = new JScrollPane(textArea);
			sc.setPreferredSize(new Dimension(540, 250)); // Width, Height
			sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JOptionPane.showMessageDialog(f, sc, "Help", JOptionPane.QUESTION_MESSAGE);
			return;
		}
		else if (e.getActionCommand().equals("about"))
		{
			DecimalFormat fmt = new DecimalFormat("#0.00");
			JOptionPane.showMessageDialog(f, "Current version: v" + fmt.format(version) + " (" + lastUpdate + ")\n" +
											"Created by Adam Childs" + "\n" +
											"adchilds@eckerd.edu",
											"Image Glare Removal v" + fmt.format(version),
											JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		Point clickPoint = e.getPoint();
		
		if (e.getButton() == MouseEvent.BUTTON1) // Left mouse button
		{
			if (image != null)
			{
				if (!clickOutOfBounds(clickPoint.x, clickPoint.y))
				{
					Image_Controller image_controller = new Image_Controller(image);
					int variance = image_controller.getModel().getLowestVariance(clickPoint.x, clickPoint.y);
					
					System.out.println("Lowest variance:\t\t" + variance);
				} else {
					System.out.println("Click out of bounds");
				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON2) // Mouse scroll wheel
		{
			
		}
		else if (e.getButton() == MouseEvent.BUTTON3) // Right mouse button
		{
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		/*
		 * Updates the mouse coordinates textfield if the mouse re-enters the program area
		 */
		mouseCoords = e.getPoint();
		textField.setText("X: [" + (int)mouseCoords.getX() + "] Y: [" + (int)mouseCoords.getY() + "]");
		textField.setForeground(Color.BLACK);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		/*
		 * Updates the mouse coordinates textfield if the mouse leaves the program area
		 */
		textField.setForeground(Color.RED);
		textField.setText("Off Screen!");
		mouseCoords = null;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		/*
		 * Updates mouse coordinates for display in the JFrame toolbar
		 */
		mouseCoords = e.getPoint();

		if (image != null)
		{
			if (mouseCoords.getX() > image.getIconWidth() || mouseCoords.getY() > image.getIconHeight())
			{
				textField.setForeground(Color.RED);
				textField.setText("Out of bounds!");
			} else {
				textField.setForeground(Color.BLACK);
				textField.setText("X: [" + (int)mouseCoords.getX() + "] Y: [" + (int)mouseCoords.getY() + "]");
			}
		} else {
			textField.setForeground(Color.BLACK);
			textField.setText("X: [" + (int)mouseCoords.getX() + "] Y: [" + (int)mouseCoords.getY() + "]");
		}
	}

}