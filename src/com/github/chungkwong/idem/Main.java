package com.github.chungkwong.idem;
import static com.github.chungkwong.idem.global.Log.LOG;
import com.github.chungkwong.idem.gui.*;
import com.github.chungkwong.idem.parser.*;
import com.github.chungkwong.swingconsole.*;
import java.awt.*;
import java.util.*;
import java.util.logging.Level;
import javax.swing.*;
public final class Main implements Shell{
	String result;
	static String lineSeparator=System.getProperty("line.separator");
	public Main(){
		new ErrorConsole();
	}
	public boolean acceptLine(String line){
		try{
		   //result=new Lex(line).getRemainingTokens().toString()+"\n";
			result=new Parser(line).getRemainingDatums().toString()+"\n";
			return true;
		}catch(Exception ex){
			LOG.log(Level.WARNING,"null",ex);
		}
		return false;
	}
	public String evaluate(){
		return result;
	}
	public java.util.List<String> getHints(String prefix){
		ArrayList<String> lst=new ArrayList<String>();

		return lst;
	}
	public static void main(String[] args)throws Exception{
		JFrame f=new JFrame("Console");
		f.add(new SwingConsole(new Main()),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		//System.out.println(Arrays.stream(new String[]{"Hello","world"}).map(String::toUpperCase).toArray()[0]);
	}
}
