
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class ClientGreeter implements ActionListener {

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new ClientGreeter();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JButton button = new JButton("Send Request");
	JLabel label = new JLabel();
	JTextField field = new JTextField("index.html",20);
	Socket server;
	BufferedReader in;
	String serverName = "localhost";
	int port = 8080;
	PrintWriter out;

	public ClientGreeter() {
		frame.add(panel);
		label = new JLabel();
		label.setPreferredSize(new Dimension(800, 800));
		label.setVerticalTextPosition(JLabel.TOP);
		label.setVerticalAlignment(JLabel.TOP);
		panel.add(label);
		panel.add(field);
		panel.add(button);
		button.addActionListener(this);
		frame.setSize(200, 200);
		frame.setVisible(true);
		frame.pack();
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			server = new Socket(serverName, port);
			out = new PrintWriter(server.getOutputStream(), true);
			out.println(field.getText());
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						String response = "";
						try {
							while (in.ready())
								response += in.readLine();
							if (response != "") {
								label.setText(response);
								server.close();
								break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		} catch (Exception e2){
			e2.printStackTrace();
		}
	}

}