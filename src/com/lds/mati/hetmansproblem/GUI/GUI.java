package com.lds.mati.hetmansproblem.GUI;

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


import com.lds.mati.CSP.engine.CSPSolver;
import com.lds.mati.CSP.engine.Coinstraint;
import com.lds.mati.CSP.engine.ConstraintsProblem;
import com.lds.mati.CSP.engine.Hook;
import com.lds.mati.hetmansproblem.HetmanProblem.HetmansProblemFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Rectangle;

public class GUI extends JFrame implements Hook<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4380088251305554494L;
	private JPanel contentPane;
	private JTextField textField;
	private ChessBoard chess;
	private int boardSize;
	private JButton btnNewButton;
	private JCheckBox chckbxPodgld;
	/**
	 * @wbp.nonvisual location=456,429
	 */
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnNewRadioButton;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmZapiszWynik;
	private JLabel lblInfo;
	private JScrollPane scrollPane;
	private JTextArea textArea;


	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("Problem Hetmanów");
		initGui();
		
	}

	private void initGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 781, 467);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("Plik");
		menuBar.add(mnNewMenu);
		
		mntmZapiszWynik = new JMenuItem("Zapisz wynik");
		mntmZapiszWynik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveResult();
			}
		});
		mnNewMenu.add(mntmZapiszWynik);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		chckbxPodgld = new JCheckBox("Podgl\u0105d");
		panel.add(chckbxPodgld);
		
		JLabel lblNewLabel = new JLabel("Liczba hetmanów");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setText("8");
		panel.add(textField);
		textField.setColumns(10);
		
		btnNewButton = new JButton("Rozmieœæ");
		
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		rdbtnNewRadioButton = new JRadioButton("backtracking");
		rdbtnNewRadioButton.setSelected(true);
		panel_1.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("forwardchecking");
		panel_1.add(rdbtnNewRadioButton_1);
		buttonGroup.add(rdbtnNewRadioButton);
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		lblInfo = new JLabel("Info");
		panel.add(lblInfo);
		lblInfo.setBounds(new Rectangle(0, 0, 300, 200));
		lblInfo.setSize(new Dimension(200, 300));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(200, 50));
		panel.add(scrollPane);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed();
			}
		});
		chess = new ChessBoard();
		panel_2.add(chess);
	}


	protected void saveResult() {
		Integer[] positions = chess.getPositions();
		if(positions!=null){
			try {
				BufferedImage im = new BufferedImage(positions.length*20,positions.length*20,BufferedImage.TYPE_INT_RGB);
				chess.paintImage(im.getWidth(), im.getHeight(), im.getGraphics());
				ImageIO.write(im, "PNG", new File("result.png"));
			} catch (IOException|OutOfMemoryError e) {
				JOptionPane.showMessageDialog(this, "Brak pamiêci na zapis obrazka! Kontynuujê...");
			}

			try {
				PrintWriter wyj = new PrintWriter(new File("result.txt"));
				for(int i = 0 ; i < positions.length ; ++i){
					wyj.println(i+" "+positions[i]);
				}
				wyj.close();
				JOptionPane.showMessageDialog(this, "Zapisano!");
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "B³¹d zapisu wyniku!");
			}
		}
		
	}

	private void buttonPressed() {
		boardSize = 0;
		try {
			boardSize = Integer.parseInt(textField.getText());
			if(boardSize<1)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Wprowadzona wartoœæ jest niepoprawna!");
			return;
		}
		btnNewButton.setEnabled(false);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Integer[] result = runScript(rdbtnNewRadioButton.isSelected(), chckbxPodgld.isSelected(), boardSize);
				if(result!=null)
					chess.setPositions(result);
				else
					JOptionPane.showMessageDialog(GUI.this, "Brak rozwi¹zania!");
				btnNewButton.setEnabled(true);
			}
		}).start();
	}

	private Integer[] runScript(boolean backtracking,boolean view,int boardSize) {
		HetmansProblemFactory factory = HetmansProblemFactory.getFactory();
		Coinstraint<Integer>[] coinstraints = factory.getCoinstraints(boardSize);
		List<Integer>[] domains = factory.getDomain(boardSize);
		
		ConstraintsProblem<Integer> problem = new ConstraintsProblem<Integer>(coinstraints, domains);
		CSPSolver<Integer> solver = new CSPSolver<>();
		long currentTime = System.currentTimeMillis();
		Integer[] result;
		if(backtracking)
			if(view)
				result =  solver.backTracking(problem,this);
			else
				result =  solver.backTracking(problem);
		else 
			if(view)
				result =  solver.forwardChecking(problem,this);
			else
				result =  solver.forwardChecking(problem);
		long runTime =  System.currentTimeMillis()-currentTime;
		System.out.println("Czas dzia³ania "+runTime+" ms");
		textArea.append("Czas dzia³ania "+runTime+" ms\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		return result;
	}

	@Override
	public void partialResult(Integer[] result) {
		chess.setPositions(result);
	}

}
