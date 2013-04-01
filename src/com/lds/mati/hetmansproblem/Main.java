package com.lds.mati.hetmansproblem;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

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
	static int backwardMax = 30;
	static int forwardMax = 300;

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("benchmark")) {
			runBenchmark();
		} else {
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

	public static void runBenchmark() {
		ArrayList<Double> timesBackward = new ArrayList<>();
		ArrayList<Double> timesForward = new ArrayList<>();
		long start, end;
		for (int i = 4; i < backwardMax; ++i) {
			HetmansProblemFactory factory = HetmansProblemFactory.getFactory();
			Coinstraint<Integer>[] coinstraints = factory.getCoinstraints(i);
			List<Integer>[] domains = factory.getDomain(i);
			ConstraintsProblem<Integer> problem = new ConstraintsProblem<Integer>(
					coinstraints, domains);
			CSPSolver<Integer> solver = new CSPSolver<>();
			start = System.currentTimeMillis();
			solver.backTracking(problem);
			end = System.currentTimeMillis() - start;
			timesBackward.add((double) end);
			problem = new ConstraintsProblem<Integer>(coinstraints, domains);
			start = System.currentTimeMillis();
			solver.forwardChecking(problem);
			end = System.currentTimeMillis() - start;
			timesForward.add((double) end);
			System.out.println(i);
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

		for (int i = backwardMax; i < forwardMax; ++i) {
			HetmansProblemFactory factory = HetmansProblemFactory.getFactory();
			Coinstraint<Integer>[] coinstraints = factory.getCoinstraints(i);
			List<Integer>[] domains = factory.getDomain(i);
			ConstraintsProblem<Integer> problem = new ConstraintsProblem<Integer>(
					coinstraints, domains);
			CSPSolver<Integer> solver = new CSPSolver<>();
			start = System.currentTimeMillis();
			solver.forwardChecking(problem);
			end = System.currentTimeMillis() - start;
			timesForward.add((double) end);
			System.out.println(i);
		}

		forward = new double[timesForward.size()];
		x = new double[timesForward.size()];
		for (int i = 0; i < forward.length; ++i) {
			forward[i] = timesForward.get(i);
			x[i] = i + 4;
		}

		JFrame window4 = new JFrame("Forward Tracking 2");
		window4.setSize(600, 600);
		window4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p = new Plot2DPanel();
		p.addLegend("SOUTH");
		p.addLinePlot("Forward Checking", x, forward);
		p.setAxisLabel(0, "Liczba Hetmanów");
		p.setAxisLabel(1, "Czas(ms)");
		p.setVisible(true);
		window4.setContentPane(p);
		window4.setVisible(true);

	}
}