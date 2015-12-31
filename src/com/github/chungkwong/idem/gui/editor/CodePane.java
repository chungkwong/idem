package  com.github.chungkwong.idem.gui.editor;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.awt.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.text.*;
public final class CodePane extends JPanel{
	JTextPane pane=new JTextPane();
	Highlighter highlighter=new DefaultHighlighter();
	JTextField field=new JTextField();
	public CodePane(){
 		setLayout(new BorderLayout());
 		field.getDocument().addUndoableEditListener((e)->search());
		pane.setHighlighter(highlighter);
		add(field,BorderLayout.NORTH);
 		add(new JScrollPane(pane),BorderLayout.CENTER);
	}
	void search(){
		highlighter.removeAllHighlights();
		String tosearch=pane.getText(),search=field.getText();
		if(search.isEmpty())
			return;
		int i=0;
		try{
			while((i=tosearch.indexOf(search,i))>=0){
				highlighter.addHighlight(i,i+=search.length(),new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
			}
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
	}
	public static void main(String[] args) throws Exception{
		JFrame f=new JFrame("Console");
		f.add(new CodePane(),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
