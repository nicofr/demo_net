package main.view;

import java.awt.*;


import javax.swing.JFrame;
import javax.swing.JPanel;

import main.data.Member;
import main.data.Net; 

public class View extends JFrame {
	
	/**
	 * radius of circle that represents a member
	 */
	static final int rad = 7;
	
	private static final long serialVersionUID = -3118717791647109905L;
	
	Net net;
	
	public View(Net net, int x, int y) {
		super();
		this.net = net;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(x, y);
		Painter p = new Painter();
		p.setSize(x, y);
		getContentPane().add(p);
		setVisible(true);
	}
	
	/**
	 * repaints the window and its contents 
	 */
	public void print() {
		repaint();
	}
	

class Painter extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		 g2.setColor(new Color(0, 0, 0));
		 for (int i = 0; i < net.size(); i++) {
			 Member m = net.getMember(i);
			 if(m.isTarget) {
				 g2.fillOval(m.position.x-rad*2, m.position.y-rad*2, rad*2, rad*2);
			 } else {
				 if (m.info.isPresent()) {
					 g2.fillOval(m.position.x-rad, m.position.y-rad, rad, rad);
				 } else
					 g2.drawOval(m.position.x-rad, m.position.y-rad, rad, rad);
				 
				 m.connection.ifPresent(con -> {
				 g2.drawLine(m.position.x, m.position.y, con.position.x, con.position.y);
			 });
			
			}
		 } 
	}
		
}
}

