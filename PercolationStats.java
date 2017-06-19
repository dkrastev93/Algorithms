/* ---------------------------------------------
 * Author:      Daniel Krastev
 * Written:     06/18/2017
 * 
 * Compilation: javac Percolation.java
 * Execution:   java Percolation
 * 
 * Course:      Algorithms and Data Structures (Coursera)
 * Assignment:  #1
 * 
 * Performs Monte Carlo simulation to estimate the percolation threshold.
 */ 

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int numExperiments;
    private double[] percolationThreshold;
    private int n;
    private int openSites;
    
    public PercolationStats(int n, int trials) // perform trials independent experiments on an n-by-n grid
    {
        this.n = n;
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be positive");
        }
        numExperiments = 0;
        percolationThreshold = new double[trials];
        while (numExperiments < trials) {
            performExperiment();
            numExperiments++;
        }
    }
    public double mean() // sample mean of percolation threshold
    {
        return StdStats.mean(percolationThreshold);
    }
    public double stddev() // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(percolationThreshold);
    }
    public double confidenceLo() // low endpoint of 95% confidence interval
    {
        return mean() - (1.96*stddev()/Math.sqrt(percolationThreshold.length));
    }
    public double confidenceHi() // high endpoint of 95% confidence interval
    {
        return mean() + (1.96*stddev()/Math.sqrt(percolationThreshold.length));
    }
    private void performExperiment() // perform one trial experiment
    {
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            int x = StdRandom.uniform(1, n + 1);
            int y = StdRandom.uniform(1, n + 1);
            if (!p.isOpen(x, y)) {
                p.open(x, y);
            }
        }   
        openSites = p.numberOfOpenSites();
        percolationThreshold[numExperiments] = (double) openSites / (n*n);
    }
        
    public static void main(String[] args) // test client
    {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, t);

        String confidenceInterval = "[" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]";
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = " + confidenceInterval);
    }
}