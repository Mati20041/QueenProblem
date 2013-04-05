package com.lds.mati.hetmansproblem;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.math.plot.Plot2DPanel;

import com.lds.mati.CSP.engine.CSPSolver;
import com.lds.mati.CSP.engine.Coinstraint;
import com.lds.mati.CSP.engine.ConstraintsProblem;
import com.lds.mati.hetmansproblem.GUI.GUI;
import com.lds.mati.hetmansproblem.HetmanProblem.HetmansProblemFactory;

public class Main {
	/**
	 * @param args
	 */
	static int maxHetmans = 35;
	static int hetmans = 8;
	static boolean backtracking = true;

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("benchmark")) {
			if (args.length > 1) {
				try {
					maxHetmans = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			runBenchmark();
		} else {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						GUI frame = new GUI();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void findAllSolutions(int hetmans, boolean backtracing) {
		HetmansProblemFactory factory = HetmansProblemFactory.getFactory();
		Coinstraint<Integer>[] coinstraints = factory.getCoinstraints(hetmans);
		List<Integer>[] domains = factory.getDomain(hetmans);
		ConstraintsProblem<Integer> problem = new ConstraintsProblem<Integer>(
				coinstraints, domains);
		CSPSolver<Integer> solver = new CSPSolver<>();
		solver.backTracking(problem);
		solver.reset();
		solver.forwardChecking(problem);
		solver.reset();
		int i = 0;
		long start, end;
		if (backtracing) {
			start = System.currentTimeMillis();
			while (solver.forwardChecking(problem) != null) {
				++i;
			}
			end = System.currentTimeMillis() - start;
		} else {
			start = System.currentTimeMillis();
			while (solver.forwardChecking(problem) != null) {
				++i;
			}
			end = System.currentTimeMillis() - start;
		}

		System.out
				.printf("%s\nLiczba hetmanów %d\nLiczba rozwi¹zañ %d\nCzas dzia³ania %d\nLiczba iteracji %d\nLiczba powrotów %d\n",
						backtracking ? "Backtracking" : "Forward Checking",
						hetmans, i, end, solver.getIterations(),
						solver.getBacks());
	}

	public static void runBenchmark() {
		ArrayList<Double> timesBackward = new ArrayList<>();
		ArrayList<Double> timesForward = new ArrayList<>();
		long start, end;
		for (int i = 4; i < maxHetmans; ++i) {
			start = System.currentTimeMillis();
			findAllSolutions(i, true);
			end = System.currentTimeMillis() - start;
			timesBackward.add((double) end);
			start = System.currentTimeMillis();
			findAllSolutions(i, false);
			end = System.currentTimeMillis() - start;
			timesForward.add((double) end);
		}
		double backward[] = new double[timesBackward.size()];
		double forward[] = new double[timesBackward.size()];
		double x[] = new double[timesBackward.size()];
		for (int i = 0; i < backward.length; ++i) {
			backward[i] = timesBackward.get(i);
			forward[i] = timesForward.get(i);
			x[i] = i + 4;
		}
		JFrame window = new JFrame("Backward Tracking");
		window.setSize(600, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Plot2DPanel p = new Plot2DPanel();
		p.addLegend("SOUTH");
		p.addLinePlot("Backward Tracking", x, backward);
		p.setAxisLabel(0, "Liczba Hetmanów");
		p.setAxisLabel(1, "Czas(ms)");
		p.setVisible(true);
		window.setContentPane(p);
		window.setVisible(true);

		JFrame window2 = new JFrame("Forward Tracking");
		window2.setSize(600, 600);
		window2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new Plot2DPanel();
		p.addLegend("SOUTH");
		p.addLinePlot("Forward Checking", x, forward);
		p.setAxisLabel(0, "Liczba Hetmanów");
		p.setAxisLabel(1, "Czas(ms)");
		p.setVisible(true);
		window2.setContentPane(p);
		window2.setVisible(true);

		JFrame window3 = new JFrame("Both Algorithm");
		window3.setSize(600, 600);
		window3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new Plot2DPanel();
		p.addLegend("SOUTH");
		p.addLinePlot("Forward Checking", x, forward);
		p.addLinePlot("Backward Tracking", x, backward);
		p.setAxisLabel(0, "Liczba Hetmanów");
		p.setAxisLabel(1, "Czas(ms)");
		p.setVisible(true);
		window3.setContentPane(p);
		window3.setVisible(true);
	}
}