import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class Editor extends WindowAdapter implements ActionListener
{
	Frame f,fr,frc,fne,fsd;
	Label l1,l2,l3;
	Button b1,b2,b3,b4,b5;
	Panel p;
	TextField t1,t2;
	MenuBar mb;
	Menu m1,m2,m3;
	MenuItem nw,opn,sv,svas,ext,fnd,fndr;
	CheckboxMenuItem bld,itlc;
	TextArea t;
	File file;
	FileInputStream fis;
	FileOutputStream fos;
	String fname = "Untitled.txt",path;
	FileDialog fd;
	GridBagConstraints gbc;
	boolean saved = true;
	int index =0,start=-1,end=-1,close = 0;
	
	public Editor()
	{
		f = new Frame();
		f.setSize(1000,600);
		f.setLocation(0,0);
		f.setTitle("Untitled.txt");
		
																						//Setting Menu Bar
		mb = new MenuBar();
		m1 = new Menu("File");
		m2 = new Menu("Edit");
		//m3 = new Menu("Others");
		
		nw = new MenuItem("New");
		opn = new MenuItem("Open");
		sv = new MenuItem("Save");
		svas = new MenuItem("Save As");
		ext = new MenuItem("Exit");
		fnd = new MenuItem("Find");
		fndr = new MenuItem("Find and Replace");
		//nw = new MenuItem("Find and Replace");
		
		nw.addActionListener(this);
		opn.addActionListener(this);
		sv.addActionListener(this);
		svas.addActionListener(this);
		ext.addActionListener(this);
		fnd.addActionListener(this);
		fndr.addActionListener(this);
		
		m1.add(nw);
		m1.add(opn);
		m1.add(sv);
		m1.add(svas);
		m1.addSeparator();
		m1.add(ext);
		
		m2.add(fnd);
		m2.add(fndr);
		
		mb.add(m1);
		mb.add(m2);
		
		f.setMenuBar(mb);
		f.setVisible(true);
		
																						//setting TextArea
		t = new TextArea();
		f.add(t);
		
		f.addWindowListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String str = e.getActionCommand();
		System.out.println(str + " was clicked");
		if(e.getSource() == nw)
		{
			try
			{
				newFile();	
			}
			catch(Exception exc)
			{
				System.out.println("Yes");
			}
		}
		else if(e.getSource() == opn)
		{
			try
			{
				openFile();	
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == sv)
		{
			try
			{
				if(isSaved() || file!=null)
				{
					String s = t.getText();
					writeFile(s);
				}
				else
				{
					saveFile();
				}
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == svas)
		{
			try
			{
				saveFile();
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == ext)
		{
			try
			{
				exit(f);
			}
			catch(Exception exc)
			{
				
			}
		}
		else if(e.getSource() == fnd)
		{
			fr = new Frame();
			fr.setSize(400,150);
			fr.setLocation(500,250);
			fr.setResizable(false);
			fr.setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			
			l1 = new Label("Find Next");
			gbc.gridx = 0;
			gbc.gridy = 0;
			fr.add(l1,gbc);
			
			t1 = new TextField(25);
			gbc.gridx = 1;
			//gbc.gridwidth = GridBagConstraints.REMAINDER;
			fr.add(t1,gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.ipadx = 20;
			//gbc.ipady = 5;
			Insets i = new Insets(0,50,0,0);
			gbc.insets = i;
			b1 = new Button("Find");
			//gbc.anchor = GridBagConstraints.EAST;
			fr.add(b1,gbc);
			b1.addActionListener((e1)->{match();});		//Lambda Expression
			
			gbc.gridx = 1;
			i = new Insets(0,0,0,0);
			gbc.insets = i;
			b2 = new Button("Cancel");
			//gbc.anchor = GridBagConstraints.WEST;
			fr.add(b2,gbc);
			b2.addActionListener((e1)->{fr.setVisible(false);fr.dispose();resetIndex();});			//Lambda Expression
			
			fr.setVisible(true);
			fr.addWindowListener(this);
		}
		else if(e.getSource() == fndr)
		{
			fr = new Frame();
			fr.setSize(400,200);
			fr.setLocation(500,250);
			fr.setResizable(false);
			fr.setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			
			l1 = new Label("Find");
			gbc.gridx = 0;
			gbc.gridy = 0;
			fr.add(l1,gbc);
			
			t1 = new TextField(25);
			gbc.gridx = 1;
			//gbc.gridwidth = GridBagConstraints.REMAINDER;
			fr.add(t1,gbc);
			
			l2 = new Label("Replace With ");
			gbc.gridx = 0;
			gbc.gridy = 1;
			fr.add(l2,gbc);
			
			t2 = new TextField(25);
			gbc.gridx = 1;
			//gbc.gridwidth = GridBagConstraints.REMAINDER;
			fr.add(t2,gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.ipadx = 20;
			//gbc.ipady = 5;
			Insets i = new Insets(0,50,0,0);
			gbc.insets = i;
			b1 = new Button("  Replace  ");
			//gbc.anchor = GridBagConstraints.EAST;
			fr.add(b1,gbc);
			b1.addActionListener((e1)->{replaceString();});												//Lambda Expression
			
			gbc.gridx = 1;
			i = new Insets(0,0,0,0);
			gbc.insets = i;
			b2 = new Button("Replace All");
			//gbc.anchor = GridBagConstraints.WEST;
			fr.add(b2,gbc);
			b2.addActionListener((e1)->{replaceAllString();});											//Lambda Expression

			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.ipadx = 20;
			//gbc.ipady = 5;
			i = new Insets(0,50,0,0);
			gbc.insets = i;
			b3 = new Button(" Find Next ");
			//gbc.anchor = GridBagConstraints.EAST;
			fr.add(b3,gbc);
			b3.addActionListener((e1)->{match();});													//Lambda Expression
			
			gbc.gridx = 1;
			i = new Insets(0,0,0,0);
			gbc.insets = i;
			b4 = new Button("   Cancel   ");
			//gbc.anchor = GridBagConstraints.WEST;
			fr.add(b4,gbc);
			b4.addActionListener((e1)->{fr.setVisible(false);fr.dispose();resetIndex();});			//Lambda Expression
			
			fr.setVisible(true);
			fr.addWindowListener(this);
		}
	}
	
	String readFile(File f)throws IOException 
	{
		FileInputStream fis = new FileInputStream(f);
		byte[] data = new byte[(int) f.length()];
		fis.read(data);
		fis.close();
		return(new String(data, "UTF-8"));
				
		//byte[] encoded = Files.readAllBytes(Paths.get(path));
		//return new String(encoded,"UTF-8");
	}
	
	void writeFile(String s)throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(s.getBytes());
		fos.close();
	}
	
	boolean isSaved()throws Exception
	{
		if(file == null && t.getText().equals(""))
			return true;
		else if(file == null && !(t.getText().equals("")))
			return false;
		String s1 = t.getText();
		String s2 = readFile(file);
		System.out.println("*"+s1+"*");
		System.out.println("#"+s2+"#");
		if(s1.equals(s2))
			return true;
		else
			return false;
	}

																					//Creating new File
	void newFile()throws Exception
	{
		//System.out.println(fname);
		if(!isSaved())
		{
			//saveDialogue();
			fsd = new Frame();
			fsd.setLocation(500,250);
			fsd.setSize(500,200);
			fsd.setVisible(true);
			fsd.setResizable(false);
			fsd.setLayout(new BorderLayout());
			fsd.addWindowListener(this);
			
			l1 = new Label("Do you want to save changes to "+fname+" ?");
			fsd.add(l1,"Center");
			
			p = new Panel();
			fsd.add(p,"South");
			p.setLayout(new FlowLayout());
			
			b1 = new Button("  Save  ");
			b2 = new Button("Don't Save");
			b3 = new Button(" Cancel ");
			p.add(b1);
			b1.addActionListener((e1)->{try
										{
											saveFileOption();
											if(path != null && fname != null)
											{
												t.setText("");
												f.setTitle("Untitled.txt");
												path = null;
												fname = null;
												file = null;
											}
											fsd.setVisible(false);
											fsd.dispose();
										}
										catch(Exception exc)
										{
											
										}});
			p.add(b2);
			b2.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										t.setText("");
										f.setTitle("Untitled.txt");
										file = null;
										path = null;
										fname = null;
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			p.add(b3);
			b3.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			/*
			if(file == null)
			{
				saveFile();
			}
			else
				writeFile(t.getText());	
			*/
		}
		else
		{
			t.setText("");
			f.setTitle("Untitled.txt");
			file = null;
			path = null;
			fname = null;
		}
		/*
		fd = new FileDialog(f,"Select File",FileDialog.LOAD);
		fd.setVisible(true);
		fname = fd.getFile();
		path = fd.getDirectory();
		System.out.println("File Name : "+fname+" Path : "+path);
		if(fname!=null && path!=null)
		{
			file = new File(path,fname);
			file.createNewFile();
			f.setTitle(file.getName());
			t.setText(readFile(file));
		}*/
	}
	
																					//Opening a File
	void openFile()throws Exception
	{
		
		if(!isSaved())
		{
			//saveDialogue();
			fsd = new Frame();
			fsd.setLocation(500,250);
			fsd.setSize(500,200);
			fsd.setVisible(true);
			fsd.setResizable(false);
			fsd.setLayout(new BorderLayout());
			fsd.addWindowListener(this);
			
			l1 = new Label("Do you want to save changes to "+fname+" ?");
			fsd.add(l1,"Center");
			
			p = new Panel();
			fsd.add(p,"South");
			p.setLayout(new FlowLayout());
			
			b1 = new Button("  Save  ");
			b2 = new Button("Don't Save");
			b3 = new Button(" Cancel ");
			p.add(b1);
			b1.addActionListener((e1)->{try
										{
											saveFileOption();
											fd = new FileDialog(f,"Open File",FileDialog.LOAD);
											fd.setVisible(true);
											fname = fd.getFile();
											path = fd.getDirectory();
											System.out.println("File Name : "+fname+" Path : "+path);
											if(fname!=null && path!=null)
											{
												file = new File(path,fname);
												if(file.exists())
												{
													System.out.println(readFile(file));
													t.setText(readFile(file));
													f.setTitle(file.getName());
												}
												else
												{
													//System.out.println("File not exists");
													fileNotExists();
													path = null;
													fname = null;
													file = null;
												}
											}
											fsd.setVisible(false);
											fsd.dispose();
										}
										catch(Exception exc)
										{
											
										}});
			p.add(b2);
			b2.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										fd = new FileDialog(f,"Open File",FileDialog.LOAD);
										fd.setVisible(true);
										fname = fd.getFile();
										path = fd.getDirectory();
										System.out.println("File Name : "+fname+" Path : "+path);
										if(fname!=null && path!=null)
										{
											file = new File(path,fname);
											if(file.exists())
											{
												try
												{
													//System.out.println(readFile(file));
													t.setText(readFile(file));
												}
												catch(Exception exc)
												{
													
												}
												f.setTitle(file.getName());
											}
											else
											{
												//System.out.println("File not exists");
												fileNotExists();
												path = null;
												fname = null;
												file = null;
											}
										}
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			p.add(b3);
			b3.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			/*
			if(file == null)
			{
				saveFile();
			}
			else
				writeFile(t.getText());	
			*/
		}
		else
		{
			fd = new FileDialog(f,"Open File",FileDialog.LOAD);
			fd.setVisible(true);
			fname = fd.getFile();
			path = fd.getDirectory();
			System.out.println("File Name : "+fname+" Path : "+path);
			if(fname!=null && path!=null)
			{
				file = new File(path,fname);
				if(file.exists())
				{
					System.out.println(readFile(file));
					t.setText(readFile(file));
					f.setTitle(file.getName());
				}
				else
				{
					//System.out.println("File not exists");
					fileNotExists();
					path = null;
					fname = null;
					file = null;
				}
			}
		}
	}
	
																					//Saving a File to a directory
	void saveFile()throws Exception
	{
		//String s = readFile(file);
		fd = new FileDialog(f,"Save As",FileDialog.SAVE);
		fd.setVisible(true);
		fname = fd.getFile();
		path = fd.getDirectory();
		if(fname!=null && path!=null)
		{
			file = new File(path,fname);
			System.out.println("File Name : "+fname+" Path : "+path);
			file.createNewFile();
			writeFile(t.getText());
			f.setTitle(file.getName());
		}
	}
	
	void saveFileOption() throws Exception
	{
		if(!isSaved())
		{
			if(file == null)
			{
				saveFile();
			}
			else
				writeFile(t.getText());
		}
	}
	
	public void windowClosing(WindowEvent e)
	{
		
		Window w = e.getWindow();
		boolean x = false;;
		try
		{
			x = !isSaved();
		}
		catch(Exception exc)
		{
			
		}
		if(x)
		{
			//saveDialogue();
			fsd = new Frame();
			fsd.setLocation(500,250);
			fsd.setSize(500,200);
			fsd.setVisible(true);
			fsd.setResizable(false);
			fsd.setLayout(new BorderLayout());
			fsd.addWindowListener(this);
			
			l1 = new Label("Do you want to save changes to "+fname+" ?");
			fsd.add(l1,"Center");
			
			p = new Panel();
			fsd.add(p,"South");
			p.setLayout(new FlowLayout());
			
			b1 = new Button("  Save  ");
			b2 = new Button("Don't Save");
			b3 = new Button(" Cancel ");
			p.add(b1);
			b1.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										try
										{
											saveFile();
										}
										catch(Exception exc)
										{
											
										}
										fsd.setVisible(false);
										fsd.dispose();
										w.setVisible(false);
										w.dispose();
										if(e.getSource()==f)
											System.exit(1);
									}
								});
			p.add(b2);
			b2.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										fsd.setVisible(false);
										fsd.dispose();
										w.setVisible(false);
										w.dispose();
										if(e.getSource()==f)
											System.exit(1);
									}
								});
			p.add(b3);
			b3.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			/*
			if(file == null)
			{
				saveFile();
			}
			else
				writeFile(t.getText());	
			*/
		}
		else
		{
			w.setVisible(false);
			w.dispose();
			if(e.getSource()==f)
				System.exit(1);
		}
	}
	
	void exit(Frame f1)throws Exception
	{
		if(!isSaved())
		{
			//saveDialogue();
			fsd = new Frame();
			fsd.setLocation(500,250);
			fsd.setSize(500,200);
			fsd.setVisible(true);
			fsd.setResizable(false);
			fsd.setLayout(new BorderLayout());
			fsd.addWindowListener(this);
			
			l1 = new Label("Do you want to save changes to "+fname+" ?");
			fsd.add(l1,"Center");
			
			p = new Panel();
			fsd.add(p,"South");
			p.setLayout(new FlowLayout());
			
			b1 = new Button("  Save  ");
			b2 = new Button("Don't Save");
			b3 = new Button(" Cancel ");
			p.add(b1);
			b1.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										try
										{
											saveFile();
										}
										catch(Exception exc)
										{
											
										}
										f1.setVisible(false);
										f1.dispose();
										if(f1==f)
											System.exit(1);
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			p.add(b2);
			b2.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										f1.setVisible(false);
										f1.dispose();
										if(f1==f)
											System.exit(1);
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			p.add(b3);
			b3.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e)
									{
										fsd.setVisible(false);
										fsd.dispose();
									}
								});
			/*
			if(file == null)
			{
				saveFile();
			}
			else
				writeFile(t.getText());	
			*/
		}
		else
		{
			f.setVisible(false);
			f.dispose();
			if(f==this.f)
				System.exit(1);
		}
	}
	
	void resetIndex()
	{
		index = 0;
	}
	
	void match()
	{
			String s = t1.getText();
			String text = t.getText();
			text = text.replace("\n","\n\r");
			int x = 0;
			System.out.println(text);
			
			Pattern pat = Pattern.compile(s);
			Matcher m = pat.matcher(text);
			if(!m.find())
				index = -1;
			while(index != -1 && (m.start()<index))
			{
				System.out.println("111");
				if(m.find())
					System.out.println(m.start()+" "+m.end());
				else
				{
					index = -1;
				}
			}
			if(index != -1)
			{				
				System.out.println("222");
				for(int i = 0;i<m.start();i++)
				{
					if(text.charAt(i)=='\n')
						x++;
				}
				t.requestFocus();
				//t.select(0,2);
				t.select(m.start()-2*x,m.end()-2*x);
				start = m.start();
				end = m.end();
				System.out.println(start+" "+end);
				index = m.start()+1;
			}
			else if(frc == null)
			{
				System.out.println("333");
				frc = new Frame("Stop");
				frc.setSize(200,200);
				frc.setVisible(true);
				//start = -1;
				//end = -1;
				frc.setLayout(new GridBagLayout());
				l3 = new Label("No Match Found !!");
				frc.add(l3);
				b5 = new Button("Close");
				frc.add(b5);
				b5.addActionListener((e1)->{frc.setVisible(false);frc.dispose();});			//Lambda Expression	
			}
	}
	
	void replaceString()
	{
		if((start != -1 && end !=-1) && index!=-1)
		{
			String s = t2.getText();
			String text = t.getText();
			text = text.replace("\n","\n\r");
			StringBuffer s1 = new StringBuffer(text);
			t.setText((s1.replace(start,end,s)).toString());
			match();
		}
		else if(start == -1 && end == -1)
		{
			match();
		}
		else if(index == -1)
		{
			System.out.println("333");
			frc = new Frame("Stop");
			frc.setSize(200,200);
			frc.setVisible(true);
			start = -1;
			end = -1;
			frc.setLayout(new GridBagLayout());
			l3 = new Label("No Match Found !!");
			frc.add(l3);
			b5 = new Button("Close");
			frc.add(b5);
			b5.addActionListener((e1)->{frc.setVisible(false);frc.dispose();});			//Lambda Expression	
		}
	}
	
	void replaceAllString()
	{
		String pat = t1.getText();
		String rpl = t2.getText();
		String text = t.getText();
		text = text.replace(pat,rpl);
		t.setText(text);
	}
	/*
	void saveDialogue()
	{
		fsd = new Frame();
		fsd.setLocation(500,250);
		fsd.setSize(500,200);
		fsd.setVisible(true);
		fsd.setResizable(false);
		fsd.setLayout(new BorderLayout());
		
		l1 = new Label("Do you want to save changes to "+fname+" ?");
		fsd.add(l1,"Center");
		
		p = new Panel();
		fsd.add(p,"South");
		p.setLayout(new FlowLayout());
		
		b1 = new Button("  Save  ");
		b2 = new Button("Don't Save");
		b3 = new Button(" Cancel ");
		p.add(b1);
		b1.addActionListener((e1)->{try
									{
										saveFileOption();
										t.setText("");
										fsd.setVisible(false);
										fsd.dispose();
									}
									catch(Exception exc)
									{
										
									}});
		p.add(b2);
		b2.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									t.setText("");
									file = null;
									path = null;
									fname = null;
									fsd.setVisible(false);
									fsd.dispose();
								}
							});
		p.add(b3);
		b3.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									close = 1;
									fsd.setVisible(false);
									fsd.dispose();
								}
							});
	}*/
	
	void fileNotExists()
	{
		fne = new Frame();
		fne.setLocation(500,250);
		fne.setSize(250,150);
		fne.setResizable(false);
		fne.setVisible(true);
		fne.setVisible(true);
		fne.setLayout(new FlowLayout(FlowLayout.CENTER,100,20));
		l1 = new Label("File Not Found");
		fne.add(l1);
		b1 = new Button("  OK  ");
		b1.setLocation(75,350);
		fne.add(b1);
		b1.addActionListener((e1)->{fne.setVisible(false);fne.dispose();});
		fne.addWindowListener(this);
	}
	
	public static void main(String args[])
	{
		Editor e = new Editor();
	}
}