package com.github.chungkwong.idem.gui.widget;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.colorchooser.*;
import static com.github.chungkwong.idem.global.UILanguageManager.*;
import java.util.logging.*;

public final class ColorChooser extends JColorChooser implements Chooser<Color>{
	//JColorChooser chooser=new JColorChooser();
	public ColorChooser(){
		addChooserPanel(new ColorPicker(this));
	}
	public ColorChooser(Color color){
		this();
		setColor(color);
	}
	public void showDialog(){
		JColorChooser.createDialog(null,getDefaultTranslation("PICK_COLOR"),false,this,null,null).setVisible(true);
	}
	public JComponent getComponent(){
		return this;
	}
	@Override
	public Color getValue(){
		return getColor();
	}
	public static void main(String[] args) throws Exception{
		JFrame f=new JFrame("Console");
		f.add(new ColorChooser(),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		new ColorChooser().showDialog();
	}
}
class ColorPicker extends AbstractColorChooserPanel{
	JLabel screenshot=new JLabel();
	Robot robot;
	ColorChooser chooser;
	ColorPicker(ColorChooser chooser){
		this.chooser=chooser;
	}
	protected void buildChooser(){
		try{
			robot=new Robot();
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
		setLayout(new BorderLayout());
		Box tool=Box.createHorizontalBox();
		tool.add(new JLabel(getDefaultTranslation("PICK_COLOR_BELOW")));
		JButton refresh=new JButton(getDefaultTranslation("CAPTURE"));
		//captureScreen();
		refresh.addActionListener((e)->captureScreen());
		tool.add(refresh);
		JButton load=new JButton(getDefaultTranslation("IMAGE"));
		load.addActionListener((e)->readImage());
		tool.add(load);
		add(tool,BorderLayout.NORTH);
		screenshot.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		screenshot.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				getColorSelectionModel().setSelectedColor(robot.getPixelColor(e.getXOnScreen(),e.getYOnScreen()));
			}
		});
		add(new JScrollPane(screenshot),BorderLayout.CENTER);
	}
	void captureScreen(){
		try{
			loadImage(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
	}
	void readImage(){
		JFileChooser jfc=new JFileChooser();
		if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			readImage(jfc.getSelectedFile());
	}
	void readImage(File file){
		try{
			loadImage(ImageIO.read(file));
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
	}
	void loadImage(Image image){
		screenshot.setIcon(new ImageIcon(image));
		//chooser.repaint();
	}
	public Dimension getPreferredSize(){
		return new Dimension(chooser.getWidth()/2,chooser.getHeight()/2);//XXX
	}
	public String getDisplayName(){
		return getDefaultTranslation("PICK_COLOR_FROM_SCREEN");
	}
	public Icon getLargeDisplayIcon(){
		return null;
	}
	public Icon getSmallDisplayIcon(){
		return null;
	}
	public void updateChooser(){

	}
}