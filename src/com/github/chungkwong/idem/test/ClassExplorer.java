package com.github.chungkwong.idem.test;
import static com.github.chungkwong.idem.global.Log.LOG;
import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
public final class ClassExplorer extends JPanel implements TreeSelectionListener,ChangeListener{
	static final char PATH_SEPARATOR=System.getProperty("file.separator").charAt(0);
	final JTree classChooser=new JTree(getPathRoot());
	//final Vector<String> classList=getClasses();
	//final JList<String> classChooser=new JList<String>(classList);
	final Vector<Constructor> constructorList=new Vector<Constructor>();
	final JList<Constructor> constructorChooser=new JList<Constructor>(constructorList);
	final Vector<Method> methodList=new Vector<Method>();
	final JList<Method> methodChooser=new JList<Method>(methodList);
	final Vector<Field> fieldList=new Vector<Field>();
	final JList<Field> fieldChooser=new JList<Field>(fieldList);
	final JLabel classInfo=new JLabel();
	final JCheckBox showDeclared=new JCheckBox("Show all",false);
	public ClassExplorer(){
		setLayout(new BorderLayout());
		showDeclared.addChangeListener(this);
		classChooser.addTreeSelectionListener(this);
		JSplitPane vsplit=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		vsplit.setTopComponent(new JSplitPane(JSplitPane.VERTICAL_SPLIT,false,new JScrollPane(constructorChooser),new JScrollPane(methodChooser)));
		vsplit.setBottomComponent(new JScrollPane(fieldChooser));
		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false,new JScrollPane(classChooser),vsplit),BorderLayout.CENTER);
		add(classInfo,BorderLayout.SOUTH);
		add(showDeclared,BorderLayout.NORTH);
	}
	void updateClass(){
		methodList.clear();
		fieldList.clear();
		constructorList.clear();
		classInfo.setText("");
		if(((TreeNode)classChooser.getSelectionPath().getLastPathComponent()).isLeaf())
			try{
				Object[] path=classChooser.getSelectionPath().getPath();
				String cp=path[1].toString();
				for(int i=2;i<path.length;i++){
					cp+="."+path[i].toString();
				}
				Class cls=Class.forName(cp);
				classInfo.setText(getClassDescription(cls));
				boolean showAll=showDeclared.isSelected();
				constructorList.addAll(Arrays.asList(showAll?cls.getDeclaredConstructors():cls.getConstructors()));
				methodList.addAll(Arrays.asList(showAll?cls.getDeclaredMethods():cls.getMethods()));
				fieldList.addAll(Arrays.asList(showAll?cls.getDeclaredFields():cls.getFields()));
			}catch(Exception ex){
				LOG.log(Level.WARNING,null,ex);
			}
		methodChooser.setListData(methodList);
		fieldChooser.setListData(fieldList);
		constructorChooser.setListData(constructorList);
	}
	public void valueChanged(TreeSelectionEvent e){
		updateClass();
	}
	public void stateChanged(ChangeEvent e){
		updateClass();
	}
	static String getClassDescription(Class cls){
		String description=cls.toString();
		Class<?> supClass=cls.getSuperclass();
		if(supClass!=null)
			description+=" extends "+supClass.getName();
		Class<?>[] faces=cls.getInterfaces();
		if(faces.length>0){
			description+=" implements "+faces[0].getName();
			for(int i=1;i<faces.length;i++)
				description+=","+faces[i].getName();
		}
		return description;
	}
	static DefaultMutableTreeNode getPathRoot(){
		return getPathRoot(System.getProperty("sun.boot.class.path").split(System.getProperty("path.separator")));
	}
	static DefaultMutableTreeNode getPathRoot(String[] roots){
		DefaultMutableTreeNode root=new DefaultMutableTreeNode();
		for(String rt:roots)
			try{
				File file=new File(rt);
				if(file.exists()){
					if(rt.endsWith(".jar")){
						Enumeration<JarEntry> entries=new JarFile(rt).entries();
						while(entries.hasMoreElements()){
							JarEntry entry=entries.nextElement();
							String path=entry.getName();
							if(path.endsWith(".class")){
								addNode(root,path.replace(PATH_SEPARATOR,'.').substring(0,path.length()-6));
							}
						}
					}else if(file.isDirectory()){
						for(String name:file.list())
							if(name.endsWith(".class"))
								addNode(root,name.substring(0,name.length()-6));
					}
				}
			}catch(Exception ex){
				LOG.log(Level.WARNING,null,ex);
			}
		return root;
	}
	private static void addNode(DefaultMutableTreeNode parent,String path){
		addNode(parent,path.split("\\."),0);
	}
	private static void addNode(DefaultMutableTreeNode parent,String[] path,int lv){
		if(lv>=path.length)
			return;
		int low=0;
		DefaultMutableTreeNode child=null;
		for(int high=parent.getChildCount()-1;low<=high;){
			int mid=(low+high)/2;
			DefaultMutableTreeNode c=(DefaultMutableTreeNode)parent.getChildAt(mid);
			int cmp=path[lv].compareTo((String)c.getUserObject());
			if(cmp==0){
				child=c;
				break;
			}else if(cmp<0){
				high=mid-1;
			}else{
				low=mid+1;
			}
		}
		if(child==null){
			child=new DefaultMutableTreeNode(path[lv]);
			parent.insert(child,low);
		}
		addNode(child,path,lv+1);
	}
	/*static Vector<String> getClasses(){
		return getClasses(System.getProperty("sun.boot.class.path").split(System.getProperty("path.separator")));
	}
	static Vector<String> getClasses(String[] roots){
		Vector<String> vector=new Vector<String>();
		for(String root:roots)
			try{
				File file=new File(root);
				if(file.exists()){
					if(root.endsWith(".jar")){
						Enumeration<JarEntry> entries=new JarFile(root).entries();
						while(entries.hasMoreElements()){
							JarEntry entry=entries.nextElement();
							String path=entry.getName();
							if(path.endsWith(".class")){
								vector.add(path.replace(PATH_SEPARATOR,'.').substring(0,path.length()-6));
							}
						}
					}else if(file.isDirectory()){
						for(String name:file.list())
							if(name.endsWith(".class"))
								vector.add(name.substring(0,name.length()-6));
					}
				}
			}catch(Exception ex){
				LOG.log(Level.WARNING,null,ex);
			}
		return vector;
	}*/
	public static void main(String[] args) throws Exception{
		JFrame f=new JFrame("ClassExplorer");
		f.add(new ClassExplorer(),BorderLayout.CENTER);//the use of SwingConsole can be so simple
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
