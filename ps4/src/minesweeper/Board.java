/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Representation of board
 */

public class Board {
    
    // TODO: Abstraction function, rep invariant, rep exposure, thread safety
    // AF: Board is represented using 2d array of Cells
	// 
	private final int LENGTH;
	private final int WIDTH;
	private Cell[][] cells;
	public boolean debug = true;
	private int[][] dir = {{-1,-1}, {-1,0}, {-1,1}, {0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}};
	
	
	/**
	 * Empty board constructor
	 * @param length the length (height) of the generated board
	 * @param width the width of the generated board
	 * @return an empty board with no bomb
	 */
	private Board(int length, int width) {
		LENGTH = length;
		WIDTH = width;
		cells = new Cell[length][width];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++) {
				cells[i][j] = new Cell(0);
			}
		}
	}
	
	/**
	 * Generate a board as described in file
	 * @param file input txt file containing number of bombs at each cell
	 * @return a board described by file
	 * @throws IOException 
	 */
	public static Board fromFile(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String[] sizes = br.readLine().split("\\s");
		int length = Integer.valueOf(sizes[1]);
		int width = Integer.valueOf(sizes[0]);
		if (length <= 0 || width <= 0) {
			br.close();
			throw new RuntimeException("Illegal Board Size!\n");
		}
		Board board = new Board(length, width);
    	try {
    		for (int i = 0; i < length; i++) {
    			String line = br.readLine();
    			String[] isBomb = line.split("\\s");
    			for (int j = 0; j < width; j++) {
    				if (isBomb[j].equals("1")) {
    					// there's a bomb
    					board.cells[i][j] = new Cell(-1);
    				} else if (!isBomb[j].equals("0")) {
    					throw new RuntimeException("Improperly Formatted File!");
    				}
    			}
    		}
    	} finally {
    		br.close();
    	}
    	// loop over the board and initialize rest of the cells
		int[][] dir = {{-1,-1}, {-1,0}, {-1,1}, {0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}};
		for (int r = 0; r < length; r++) {
			for (int c = 0; c < width; c++) {
				if (board.cells[r][c].numNeighboringBomb != -1) {
					int numBombs = 0;
					// look at the neighboring cells
					// {rowOffset, colOffset}
					for (int i = 0; i < dir.length; i++) {
						int rowOffset = dir[i][0];
						int colOffset = dir[i][1];
						if (r + rowOffset >= 0 && r + rowOffset < board.LENGTH && c + colOffset >= 0 && c + colOffset < board.WIDTH
							&& board.cells[r + rowOffset][c + colOffset].numNeighboringBomb == -1) {
							numBombs++;
						}
					}
					board.cells[r][c] = new Cell(numBombs);
				}
			}
		}
		return board;
	}
	
	/**
	 * Generate a random board
	 * @param length the length (height) of the generated board
	 * @param width the width of the generated board
	 * @return a board with random number of bombs
	 */
	public static Board randomBoard(int length, int width) {
		Board board = new Board(length, width);
		// randomly assign the bombs with probability 0.25
		Random rand = new Random();
		for (int r = 0; r < length; r++) {
			for (int c = 0; c < width; c++) {
				if (rand.nextDouble() <= 0.25) {
					board.cells[r][c] = new Cell(-1);
				}
			}
		}
		// loop over the board and initialize rest of the cells
		int[][] dir = {{-1,-1}, {-1,0}, {-1,1}, {0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}};
		for (int r = 0; r < length; r++) {
			for (int c = 0; c < width; c++) {
				if (board.cells[r][c].numNeighboringBomb != -1) {
					int numBombs = 0;
					// look at the neighboring cells
					// {rowOffset, colOffset}
					for (int i = 0; i < dir.length; i++) {
						int rowOffset = dir[i][0];
						int colOffset = dir[i][1];
						if (r + rowOffset >= 0 && r + rowOffset < board.LENGTH && c + colOffset >= 0 && c + colOffset < board.WIDTH
							&& board.cells[r + rowOffset][c + colOffset].numNeighboringBomb == -1) {
							numBombs++;
						}
					}
					board.cells[r][c] = new Cell(numBombs);
				}
			}
		}
		return board;
	}
	
	/**
	 * Dig (r, c) cell
	 * @param r row
	 * @param c column
	 * @return false if dig a bomb, true otherwise
	 */
	public synchronized boolean digSquare(int r, int c) {
		if (c < 0 || r < 0 || c >= WIDTH || r >= LENGTH || cells[r][c].digged || cells[r][c].flagged) {
			return true;
		}
		cells[r][c].digged = true;
		if (cells[r][c].isBomb) {
			// update neighboring cells
			int numBombs = 0;
			for (int i = 0; i < dir.length; i++) {
				int rowOffset = dir[i][0];
				int colOffset = dir[i][1];
				if (r + rowOffset >= 0 && r + rowOffset < LENGTH && c + colOffset >= 0 && c + colOffset < WIDTH) {
					Cell cel = cells[r + rowOffset][c + colOffset];
					if (!cel.isBomb) {
						boolean digged = cel.digged;
						boolean flagged = cel.flagged;
						cells[r + rowOffset][c + colOffset] = new Cell(Math.max(cel.numNeighboringBomb - 1, 0));
						cells[r + rowOffset][c + colOffset].digged = digged;
						cells[r + rowOffset][c + colOffset].flagged = flagged;
						// if there's a digged cell whose numNeighboringBomb reduces to 0, expand on it
						if (cells[r + rowOffset][c + colOffset].numNeighboringBomb == 0 && cells[r + rowOffset][c + colOffset].digged) {
							// set this cell.digged to false so that it can be expanded
							cells[r + rowOffset][c + colOffset].digged = false;
							expandDig(r + rowOffset, c + colOffset);
						}
					} else {
						numBombs++;
					}
				}
			}
			// change this cell to normal cell
			cells[r][c] = new Cell(numBombs);
			cells[r][c].digged = true;
			return false;
		}
		if (cells[r][c].numNeighboringBomb == 0) {
			// {rowOffset, colOffset}
			for (int i = 0; i < dir.length; i++) {
				int rowOffset = dir[i][0];
				int colOffset = dir[i][1];
				expandDig(r + rowOffset, c + colOffset);
			}
		}
		return true;
	}
	
	/**
	 * Expand neighboring cells of (c, r)
	 */
	private void expandDig(int r, int c) {
		if (c < 0 || r < 0 || c >= WIDTH || r >= LENGTH 
			|| cells[r][c].digged || cells[r][c].flagged || cells[r][c].isBomb) {
			return;
		}
		cells[r][c].digged = true;
		if (cells[r][c].numNeighboringBomb > 0) {
			return;
		}
		for (int i = 0; i < dir.length; i++) {
			int rowOffset = dir[i][0];
			int colOffset = dir[i][1];
			expandDig(r + rowOffset, c + colOffset);
		}
	}
	
	public synchronized void flagSquare(int r, int c) {
		if (c < 0 || r < 0 || c >= WIDTH || r >= LENGTH 
			|| cells[r][c].digged || cells[r][c].flagged) {
			return;
		}
		cells[r][c].flagged = true;
	}
	
	public synchronized void deflagSquare(int r, int c) {
		if (c < 0 || r < 0 || c >= WIDTH || r >= LENGTH 
			|| cells[r][c].digged || !cells[r][c].flagged) {
			return;
		}
		cells[r][c].flagged = false;
	}
	
	@Override
	public synchronized String toString() {
		StringBuilder bs = new StringBuilder();
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (debug) {
					bs.append(cells[i][j].numNeighboringBomb);
				} else {
					if (cells[i][j].digged) {
						bs.append(cells[i][j].numNeighboringBomb);
					} else if (cells[i][j].flagged) {
						bs.append('F');
					} else if (!cells[i][j].digged) {
						bs.append('-');
					}
				}
				bs.append('\t');
			}
			bs.append('\n');
		}
		return bs.toString();
	}
	
	/*
	 * The following 3 functions are mainly for test purposes
	 */
	public Cell getCell(int r, int c) {
		Cell cel = new Cell(cells[r][c].numNeighboringBomb);
		cel.digged = cells[r][c].digged;
		cel.flagged = cells[r][c].flagged;
		return cel;
	}
	
	public int getLength() {
		return LENGTH;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	/*
	public static void main(String[] args) {
		Board b = Board.randomBoard(5, 5);
		System.out.println(b);	
	}
	*/
}

class Cell {
	public boolean digged;
	public boolean isBomb;
	public boolean flagged;
	public final int numNeighboringBomb;
	
	/**
	 * Create a new cell.
	 * @param numBomb number of neighboring bomb, if <0 then this cell is a bomb, else it's normal.
	 * Note: Always create new Cell object to avoid errors.
	 */
	public Cell(int numBomb) {
		digged = false;
		flagged = false;
		if (numBomb < 0) {
			isBomb = true;
			numNeighboringBomb = -1;
		} else {
			isBomb = false;
			numNeighboringBomb = numBomb;
		}
	}
}
