package com.github.chungkwong.idem.gui.widget;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import static com.github.chungkwong.idem.global.UILanguageManager.*;
import java.util.logging.*;
public final class FontChooser extends JPanel implements Chooser<Font>,ListCellRenderer<Font>{
	JComboBox<Font> sysfonts;
	int style=Font.PLAIN;
	float size=12;
	JSpinner fontSize=new JSpinner(new SpinnerNumberModel(size,0.5,256.0,1));
	JCheckBox bold=new JCheckBox(getDefaultTranslation("BOLD")),italic=new JCheckBox(getDefaultTranslation("ITALIC"));
	JButton addFont=new JButton("+");
	JFileChooser fileChooser=new JFileChooser();
	Color itemBackground=Color.CYAN;
	JTextField previewText=new JTextField("ABCD abcd 1234 +-*/ ()[]");
	public FontChooser(){
		this(Font.decode(null));
	}
	public FontChooser(Font init){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		size=init.getSize2D();
		fontSize.setValue(size);
		fontSize.addChangeListener((e)->updateFontSize());
		sysfonts=new JComboBox<Font>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
		addAndSetFont(init);
		sysfonts.addItemListener((e)->updatePreview());//previewText.setFont(((Font)e.getItem()).deriveFont(size)));
		add(sysfonts);
		updatePreview();
		sysfonts.setRenderer(this);
		add(previewText);
		add(fontSize);
		bold.setSelected(init.isBold());
		italic.setSelected(init.isItalic());
		bold.addChangeListener((e)->updateStyle());
		italic.addChangeListener((e)->updateStyle());
		add(bold);
		add(italic);
		addFont.addActionListener((e)->addFont());
		add(addFont);
	}
	public void addFont(){
		if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			addFont(fileChooser.getSelectedFile());
	}
	public void addFont(File file){
		try{
			Font font=Font.createFont(file.getName().endsWith(".ttf")||file.getName().endsWith(".otf")?Font.TRUETYPE_FONT:Font.TYPE1_FONT,file);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			sysfonts.addItem(font);
			sysfonts.setSelectedItem(font);
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
	}
	private void addAndSetFont(Font font){
		DefaultComboBoxModel model=(DefaultComboBoxModel)sysfonts.getModel();
		String name=font.getName();
		int n=model.getSize(),i=0;
		for(;i<n;i++){
			int cmp=((Font)model.getElementAt(i)).getName().compareTo(name);
			if(cmp>0)
				break;
		}
		model.insertElementAt(font,i);
		sysfonts.setSelectedIndex(i);
	}
	/*
	public void setFontSize(float size){
		this.size=size;
		fontSize.setValue(size);
	}
	public void setBold(boolean bold){
		bold.setSelected(true);
	}
	public void setItalic(boolean bold){
		bold.setSelected(true);
	}
	*/
	private void updateFontSize(){
		size=((Number)fontSize.getValue()).floatValue();
		updatePreview();
	}
	private void updateStyle(){
		style=0;
		if(bold.isSelected())
			style+=Font.BOLD;
		if(italic.isSelected())
			style+=Font.ITALIC;
		updatePreview();
	}
	private void updatePreview(){
		previewText.setFont(getValue());
	}
	public JComponent getComponent(){
		return this;
	}
	public Font getValue(){
		return ((Font)sysfonts.getSelectedItem()).deriveFont(style,size);
	}
	public Component 	getListCellRendererComponent(JList<? extends Font> list,Font value,int index,boolean isSelected,boolean cellHasFocus){
		JLabel entry=new JLabel(value.getName());
		if(isSelected){
			entry.setOpaque(true);
			entry.setBackground(itemBackground);
		}
		return entry;
	}
	public static void main(String[] args) throws Exception{
		JFrame f=new JFrame("Console");
		f.add(new FontChooser(),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
   }
}
