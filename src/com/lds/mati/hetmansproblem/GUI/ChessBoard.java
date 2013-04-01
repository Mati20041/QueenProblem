package com.lds.mati.hetmansproblem.GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ChessBoard extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3273074361820239448L;
	private Integer[] positions;

	public void setPositions(Integer[] result) {
		positions =  result;

		repaint();
	}

	public Integer[] getPositions() {
		if (positions != null)
			return (Integer[]) positions.clone();
		else
			return null;
	}
	
	public void paintImage(int width, int height, Graphics g){
		g.fillRect(0, 0, width, height);
		if (positions != null) {
			double boxWidth = 1. * width / (1. * positions.length);
			double boxHeight = 1. * height / (1. * positions.length);
			int bwpx = (int) boxWidth;
			int bhpx = (int) boxHeight;
			for (int i = 0; i < positions.length; ++i) {
				for (int j = 0; j < positions.length; ++j) {
					if ((i + j) % 2 == 0)
						g.setColor(Color.white);
					else
						g.setColor(Color.gray);
					int x = (int) (i * boxWidth);
					int y = (int) (j * boxHeight);
					g.fillRect(x, y, bwpx, bhpx);
					if (positions[i] != null && positions[i] == j) {
						g.setColor(Color.black);
						g.fillOval(x, y, bwpx, bhpx);
					}
				}
			}
		}
	}
		

	@Override
	public void paint(Graphics g) {
		paintImage(getWidth(), getHeight(), g);
	}

}
