/* ---------------------------------------------
 *  Author:      Daniel Krastev
 *  Written:     06/18/2017
 * 
 *  Compilation: javac Percolation.java
 *  Execution:   java Percolation
 * 
 *  Course:      Algorithms and Data Structures (Coursera)
 *  Assignment:  #1
 * 
 *  We model a percolation system using an n-by-n grid of sites. Each site is 
 *  either open or blocked. A full site is an open site that can be connected 
 *  to an open site in the top row via a chain of neighboring 
 *  (left, right, up, down) open sites. We say the system percolates if there
 *  is a full site in the bottom row. In other words, a system percolates if 
 *  we fill all open sites connected to the top row and that process fills 
 *  some open site on the bottom row.
 */ 

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF wqf;
    private boolean[][] grid;
    private int gridSize;
    private int openSites;
    private int topSite; // virtual top site
    private int bottomSite; // virtual bottom site
    
    public Percolation(int n) // create a n-by-n grid, with all sites blocked 
    {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n must be greater than 0"); }
        
        grid = new boolean[n][n]; // initialize n*n boolean grid with default values "false"
        wqf = new WeightedQuickUnionUF(n*n + 2);
        
        gridSize = n;
        topSite = 0; // location of virtual top site
        bottomSite = (n*n) + 1;  // location of virtual bottom site
    }
    
    public void open(int row, int col) // open site (row, col) if it is not open already
    {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new IndexOutOfBoundsException();
        if (isOpen(row, col)) return;
        
            // open the site
            grid[row - 1][col - 1] = true;
            openSites++;
            
            // if site is in top or bottom row, connect to virtual top and bottom sites
            if (row == 1) { 
                wqf.union(to2D(row, col), topSite); 
            }
            if (row == gridSize) { 
                wqf.union(to2D(row, col), bottomSite);  
            }
            
            // connect site to neighboring sites
            if (row > 1 && isOpen(row - 1, col)) // left 
            { 
                wqf.union(to2D(row, col), to2D(row - 1, col));  
            }
            if (row < gridSize && isOpen(row + 1, col)) // right
            { 
                wqf.union(to2D(row, col), to2D(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) // up
            { 
                wqf.union(to2D(row, col), to2D(row, col - 1)); 
            }
            if (col < gridSize && isOpen(row, col + 1)) // down 
            { 
                wqf.union(to2D(row, col), to2D(row, col + 1));
            }  
    }
    
    public boolean isOpen(int row, int col) // is site (row, col) open?
    {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new IndexOutOfBoundsException();
        return grid[row - 1][col - 1];
    }
    
    public boolean isFull(int row, int col) // is site (row, col) full?
    {   
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new IndexOutOfBoundsException(); 
        else return wqf.connected(to2D(row, col), topSite); 
    }
    
    public int numberOfOpenSites() // number of open sites
    {
        return openSites;    
    }
        
    public boolean percolates() // does the system percolate?
    {
        return wqf.connected(topSite, bottomSite);
    }
    
    private int to2D(int row, int col) // converts (i, j) to 1D coordinate
    {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new IndexOutOfBoundsException();
        return (row - 1) * gridSize + col;
    }
        
    public static void main(String[] args) // test client (optional)
    { }
}



