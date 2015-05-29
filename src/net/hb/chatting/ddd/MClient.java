package net.hb.chatting.ddd;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.List;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MClient extends JFrame implements ActionListener, Runnable {
	JPanel jp3 = new JPanel();
	BorderLayout border = new BorderLayout();
	JPanel jp4 = new JPanel(border);
	JPanel jp6 = new JPanel(new BorderLayout());
	JPanel jp9 = new JPanel();
	JPanel jp10 = new JPanel(new BorderLayout());
	JPanel jp11 = new JPanel();
	JPanel jp12 = new JPanel();
	JTextField tf_name = new JTextField(10);
	JTextField tf_msg = new JTextField(30);
	Vector<JTextArea> v = new Vector<JTextArea>();
	JFrame jf = new JFrame("Messenger");
	JTabbedPane jtp = new JTabbedPane();
	TextArea ta_out = new TextArea(25, 1);
	JButton jb_cl = new JButton(new ImageIcon("C:/Mtest/my3/Chatting_DDD/file.gif"));
	JButton jb_send = new JButton(new ImageIcon("C:/Mtest/my3/Chatting_DDD/send.gif"));
	JButton jb_join = new JButton("Join");
	List list = new List(23);
	Map<String, Boolean> NameList = new HashMap<String, Boolean>();
	Boolean isCreatedTab = false;
	JMenuBar mb = new JMenuBar();
	JMenu mfile = new JMenu("File");
	JMenuItem mopen = new JMenuItem("opne file");
	JMenuItem msave = new JMenuItem("save file");
	JMenuItem mexit = new JMenuItem("exit");
	int num = 1, count = 0, hh, mm, ss;
	Calendar now;
	String time;
	OutputStream out;
	BufferedReader in;
	Socket soc;
	

	public MClient() {
		initializeWindow();
		layoutCom();
	}// end

	public void layoutCom() {
		v.add(new JTextArea());
		this.jtp.addTab("All", jp11.add((JTextArea) v.get(0)));
		v.get(0).setBackground(new Color(178, 204, 255));

		jtp.setPreferredSize(new Dimension(100, 425));
		
		jp12.add(tf_name);
		jp12.add(jb_join);
		jp3.add(jb_cl); // 지우개
		jp3.add(tf_msg);
		jp3.add(jb_send); // 보내기
		
		jp4.add("Center", jtp);
		border.setHgap(15);
		jp4.add("West",list);
		
		jp6.add("East", jp3);	
		jp6.add("West",jp12);
		
		jf.setJMenuBar(mb);
		jf.getContentPane().add("South", jp6);
		jf.getContentPane().add("North", jp4);
		jf.setSize(700, 640);
		jf.setResizable(false);
		mb.add(mfile);
		mfile.add(mopen);
		mfile.add(msave);
		mfile.addSeparator();
		mfile.add(mexit);
		list.setBackground(new Color(255, 167, 167));
		jf.pack();
		jf.setLocationRelativeTo(null);
		jb_send.setToolTipText("메세지 보내기");
		jb_cl.setToolTipText("파일 전송");
	}// layout end

	public void initializeWindow() {
		for (int i = 0; i < 25; i++) { // initialize map
			NameList.put("Friend" + i, this.isCreatedTab);
		}
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent ae) {
				String nick = list.getSelectedItem().toString();
				if (ae.getClickCount() == 2) {
					if (!(NameList.get(nick))) { // New Create						
						v.add(new JTextArea());
						jtp.addTab(nick, new JPanel().add(v.get(num)));
						v.get(num).setBackground(new Color(178, 204, 255));
						num++;
						isCreatedTab = true;
						NameList.put(nick, isCreatedTab);
						jtp.setSelectedIndex(jtp.indexOfTab(nick));
					} else if (nick.equals(jtp.getTitleAt(jtp.indexOfTab(nick)))) {
						jtp.setSelectedIndex(jtp.indexOfTab(nick));
					}
				}
			}
		};

		jp3.setBackground(Color.white);
		jp4.setBackground(Color.white);
		jp6.setBackground(Color.white);
		jp9.setBackground(Color.white);
		jp10.setBackground(Color.white);
		ta_out.setBackground(Color.white);
		ta_out.setFont(new Font("굴림체", Font.BOLD, 16));
		list.setFont(new Font("굴림체", Font.BOLD, 16));
		jf.setResizable(true);
		ta_out.setEditable(true);
		jf.setVisible(true);
		mexit.addActionListener(this);
		msave.addActionListener(this);
		mopen.addActionListener(this);
		jb_send.addActionListener(this);
		tf_msg.addActionListener(this);
		tf_name.addActionListener(this);
		ta_out.setForeground(Color.white);
		jb_cl.addActionListener(this);
		list.addMouseListener(mouseListener);
	}// end

	public void exit() {
		try {
			out.write(("/q" + tf_name.getText() + "\n").getBytes());
			System.out.println("보냄 : " + tf_name.getText());
			ta_out.append("****" + tf_name.getText() + "님이 퇴장하셨습니다****\r\n");
			in.close();
			out.close();
			soc.close();
			// setList();
		} catch (Exception ex) {
			System.out.println("보내기 오류 " + ex.getMessage());
		}
		ta_out.setEnabled(false);
		tf_msg.setEnabled(false);
		tf_name.setEnabled(true);
		tf_name.setText("");
		tf_name.requestFocus();
		System.exit(1);
	}// exit end

	public void fileSave() {
		FileDialog fd = new FileDialog(this, "save", FileDialog.SAVE);
		fd.setVisible(true);
		String dir = fd.getDirectory();
		System.out.println("fd.getDirectory(): " + dir); // after selecting
																											// file,show this line
		String file = fd.getFile();
		if (dir == null || file == null)
			return;
		File f = new File(dir + file);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.println(ta_out.getText());
			pw.close();
			ta_out.append("대화내용이 저장되었습니다\r\n");
		} catch (Exception e) {
		}
	}// fileSave end

	public void fileOpen() {
		FileDialog fd = new FileDialog(this, "open", FileDialog.LOAD);
		fd.setVisible(true);
		String dir = fd.getDirectory();
		String file = fd.getFile();
		if (dir == null || file == null)
			return;
		try {
			FileReader fr = new FileReader(dir + file);
			BufferedReader br = new BufferedReader(fr);
			while (true) {
				String data = "";
				data = br.readLine();
				if (data == null)
					break;
				out.write((tf_name.getText() + data + "\n").getBytes());
			}
		} catch (Exception e) {
		}
	}// fileOpen end

	public void setList() { // list 다시 초기화하는 함수
		try {
			for (int i = 0; i < 20; i++) {
				if (list.getItem(i) != "") {
					list.remove(i);
					list.add("", i);
					count--;
				}
			}
		} catch (Exception e) {
		}
	}// end

	public void run() {

		while (true) {
			now = Calendar.getInstance();
			hh = now.get(now.HOUR_OF_DAY);
			mm = now.get(now.MINUTE);
			ss = now.get(now.SECOND);
			time = hh + ":" + mm + ":" + ss; // 서버로 내 메시지를 보내고, 서버에서 다른 클라이언트들의 메시지를 가져옴
			try {
				String msg = in.readLine();
				System.out.println("서버로부터 읽음 : " + msg);

				if (msg == null)
					return;
				if (msg.charAt(0) == '/') {
					if (msg.charAt(1) == 'c') { 
						list.replaceItem(msg.substring(2), count); 																																															
						count++;
						ta_out.append("****" + msg.substring(2) + "님이 입장하셨습니다****\r\n");																																																																																																								
						tf_name.setEnabled(false); // 대화명 label 비활성
					}
					else if (msg.charAt(1) == 'q') { // 나가기버튼 누르거나 /q를 입력하면 서버에 q가 입력이 돼.													
						String str = msg.substring(2);
						ta_out.append("****" + str + "님이 퇴장하셨습니다****\r\n");
						for (int i = 0; i < list.getItemCount(); i++) { // list목록에서 퇴장하는
																														// 대화명찾아 돌려서 remove
							if (str.equals(list.getItem(i))) {
								list.remove(i);
								count--;
								break;
							}
						}
						// return; 얘 때문에 누군가가 퇴장을 하면 내 메시지가 서버로만 가고 클라이언트로 안 뿌려줌...
					} else if (msg.charAt(1) == 'n') { // 대화명변경
						String oldname = msg.substring(2, msg.indexOf('-'));
						String newname = msg.substring(msg.indexOf('-') + 1);
						ta_out.append("*" + oldname + "님의 이름이 " + newname
								+ "으로 변경되었습니다.*\r\n");
						for (int j = 0; j < count; j++) {
							if (oldname.equals(list.getItem(j))) {
								list.replaceItem(newname, j);
								break;
							}
						}
					}
				} else
					ta_out.append("[" + time + "] " + msg + "\r\n");
			} catch (Exception e) {
				ta_out.append(e.getMessage());
			}
		}
	
	}// run end

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mexit) {
			try {
				out.write(("/q" + tf_name.getText() + "\n").getBytes());
				ta_out.append("****" + tf_name.getText() + "님이 퇴장하셨습니다****\r\n");
				in.close();
				out.close();
				soc.close();
				setList();
			} catch (Exception ex) {
				System.out.println("보내기 오류 " + ex.getMessage());
			}
			ta_out.setEnabled(false);
			tf_msg.setEnabled(false);
			tf_name.setEnabled(true);
			tf_name.setText("");
			tf_name.requestFocus();
		} else if (e.getSource() == msave) {
			fileSave();
		} else if (e.getSource() == mopen) {
			fileOpen();
		} else if (e.getSource() == jb_send || e.getSource() == tf_msg) {
			sendProcess();
		}
		else if (e.getSource() == tf_name) { connect(); }	
		else if (e.getSource() == jb_cl) {
			ta_out.setText("");
		}
	}// actionPerformed end
	
	public void sendProcess() {
		String str = tf_msg.getText();
		try {
			if (str.charAt(0) == 'q') { 
				out.write(("/q" + tf_name.getText() + "\n").getBytes());
																																
				System.out.println("서버로 보냄 >> " + tf_name.getText() + " \n");
				in.close();
				out.close();
				soc.close();
				System.exit(0);
			}
			int i = jtp.getSelectedIndex();
			v.get(i).append(tf_msg.getText() + "\n");
			tf_msg.setText("");
			tf_msg.requestFocus();
		} catch (Exception e) {
			ta_out.append(e.getMessage());
		}
	}// end

	public void connect() { // 대화명을 서버로 보내서 접속자에 등록.
		ta_out.setEnabled(true);
		tf_msg.setEnabled(true);
		try {
			soc = new Socket("203.236.209.120", 5555); // 소켓지정
			in = new BufferedReader(new InputStreamReader(soc.getInputStream())); 																																																																							
			out = soc.getOutputStream(); // 서버로 보냄
			out.write((tf_name.getText() + "\n").getBytes()); // 접속자 대화명을 바이트로해서 서버에 보냄																							
			System.out.println("서버로 보냄 >> " + tf_name.getText());
			tf_name.setEnabled(false);
			new Thread(this).start(); // 다른 클라이언트들의 메시지를 서버로부터 가져옴
			tf_msg.requestFocus();
		} catch (Exception e) {
			ta_out.append(e.getMessage());
		} // 소켓에서 발생한 예외오류메시지를 출력하는듯...
	}// end
	
	public static void main(String[] args) {
		MClient mc = new MClient();
		Thread tr = new Thread(mc);
	}// main end

}// class END
