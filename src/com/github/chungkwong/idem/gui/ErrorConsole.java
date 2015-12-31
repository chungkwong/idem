package com.github.chungkwong.idem.gui;
import static com.github.chungkwong.idem.global.Log.LOG;
import com.github.chungkwong.idem.global.*;
import static com.github.chungkwong.idem.global.UILanguageManager.getDefaultTranslation;
import java.awt.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
/**
 * GUI component showing exception messages
 */
public final class ErrorConsole{
	JFrame dia;
	JTextArea area=new JTextArea();
	private static final int DEFAULT_WIDTH=800,DEFAULT_HEIGHT=600;
	/**
	 * Construct a ErrorReporter
	 *
	 * @param title the title of the message dialog
	 */
	public ErrorConsole(){
		dia=new JFrame(getDefaultTranslation("ERR_MSG"));
		area.setEditable(false);
		dia.add(new JScrollPane(area),BorderLayout.CENTER);
		dia.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		//dia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new StdErrHandler();
		new LogHandler(LOG);
	}
	/**
	 * Write message
	 *
	 * @param msg the message
	 * @param alert show it now or not
	 */
	public void display(String msg,boolean alert){
		area.append(msg);
		area.setCaretPosition(area.getText().length());
		if(alert){
			show();
		}
	}
	/**
	 * Set the console visible
	 */
	public void show(){
		dia.setVisible(true);
		dia.requestFocus();
	}
	public static void main(String[] args){
		new ErrorConsole().show();
		/*log.config("Hello world");
		log.throwing("A","B",new RuntimeException("Hello"));
		log.warning("Bye");
		log.severe("Bad");
		log.info("Isod");
		log.fine("Fine");*/
	}
	class StdErrHandler extends PrintStream{
		boolean redirect=false;
		PrintStream stderr;
		public StdErrHandler(){
			super(System.err);
			stderr=System.err;
			System.setErr(this);
		}
		public boolean isRedirect(){
			return redirect;
		}
		public void setRedirect(boolean redirect){
			this.redirect=redirect;
		}
		/**
		 * Show message in the dialog
		 *
		 * @param buf the message
		 * @param off the offset of the message
		 * @param len the length of the message
		 */
		public void write(byte[] buf,int off,int len){
			if(redirect&&stderr!=null)
				stderr.write(buf,off,len);
			display(new String(buf,off,len),true);
		}
	}
	class LogHandler extends Handler{
		public LogHandler(Logger log){
			log.addHandler(this);
		}
		Formatter fmt=new NativeLogFormatter(); 
		@Override
		public void publish(LogRecord record){
			display(fmt.format(record),true);
		}
		@Override
		public void flush(){

		}
		@Override
		public void close() throws SecurityException{

		}
	}
}
