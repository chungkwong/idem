package com.github.chungkwong.idem;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
public class SumViewer extends JPanel implements UndoableEditListener{
	JTextField input1=new JTextField("0"),input2=new JTextField("0");
	JLabel output=new JLabel();
	public SumViewer(){
		super(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.weightx=1.0;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(input1,c);
		JLabel plus=new JLabel("+");
		plus.setHorizontalAlignment(SwingConstants.CENTER);
		add(plus,c);
		c.gridwidth=GridBagConstraints.REMAINDER;
		add(input2,c);
		c.gridwidth=3;
		output.setHorizontalAlignment(SwingConstants.CENTER);
		add(output,c);
		input1.getDocument().addUndoableEditListener(this);
		input2.getDocument().addUndoableEditListener(this);
	}
	@Override
	public void undoableEditHappened(UndoableEditEvent e){
		try{
			output.setText("="+(Integer.parseInt(input1.getText())+Integer.parseInt(input2.getText())));
		}catch(NumberFormatException ex){
			JOptionPane.showMessageDialog(this,"无效数字格式");
		}
	}
	public static void main(String[] args){
		JFrame f=new JFrame("Console");
		f.add(new SumViewer(),BorderLayout.CENTER);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}