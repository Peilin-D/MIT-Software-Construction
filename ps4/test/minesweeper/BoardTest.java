/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * TODO: Description
 */
public class BoardTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testFromFile1() throws IOException {
    	File file = new File("test/minesweeper/testFile1.txt");
    	Board board = Board.fromFile(file);
    	// (0, 0)
    	assert board.getCell(0, 0).numNeighboringBomb == 1;
    	assert board.getCell(0, 0).digged == false;
    	assert board.getCell(0, 0).flagged == false;
    	assert board.getCell(0, 0).isBomb == false;
    	// (0, 1)
    	assert board.getCell(0, 1).numNeighboringBomb == 1;
    	assert board.getCell(0, 1).digged == false;
    	assert board.getCell(0, 1).flagged == false;
    	assert board.getCell(0, 1).isBomb == false;
    	// (1, 0)
    	assert board.getCell(1, 0).numNeighboringBomb == -1;
    	assert board.getCell(1, 0).digged == false;
    	assert board.getCell(1, 0).flagged == false;
    	assert board.getCell(1, 0).isBomb == true;
    	// (1, 0)
    	assert board.getCell(0, 1).numNeighboringBomb == 1;
    	assert board.getCell(0, 1).digged == false;
    	assert board.getCell(0, 1).flagged == false;
    	assert board.getCell(0, 1).isBomb == false;
    }
    
    @Test
    public void testFromFile2() throws IOException {
    	File file = new File("test/minesweeper/testFile2.txt");
    	Board board = Board.fromFile(file);
    	// (0, 3)
    	assert board.getCell(0, 3).numNeighboringBomb == 0;
    	assert board.getCell(0, 3).digged == false;
    	assert board.getCell(0, 3).flagged == false;
    	assert board.getCell(0, 3).isBomb == false;
    	// (2, 2)
    	assert board.getCell(2, 2).numNeighboringBomb == 3;
    	assert board.getCell(2, 2).digged == false;
    	assert board.getCell(2, 2).flagged == false;
    	assert board.getCell(2, 2).isBomb == false;
    	
    }
    
    @Test
    public void testDig() throws IOException {
    	Board board = Board.fromFile(new File("test/minesweeper/testFile2.txt"));
    	assert board.digSquare(0, 3) == true;
    	assert board.getCell(0, 3).digged == true;
    	assert board.getCell(0, 2).digged == true;
    	assert board.getCell(1, 3).digged == true;
    	assert board.getCell(1, 2).digged == true;
    	
    	assert board.digSquare(0, 0) == false;
    	assert board.getCell(0, 0).isBomb == false;
    	assert board.getCell(0, 0).digged == true;
    	assert board.getCell(0, 0).numNeighboringBomb == 1;
    }
    
    @Test
    public void testFlagDeflag() throws IOException {
    	Board board = Board.fromFile(new File("test/minesweeper/testFile1.txt"));
    	board.flagSquare(1, 0);
    	assert board.getCell(1, 0).flagged == true;
    	board.digSquare(1, 0);
    	assert board.getCell(1, 0).isBomb == true;
    	assert board.getCell(1, 0).digged == false;
    	board.deflagSquare(1, 0);
    	assert board.getCell(1, 0).flagged == false;
    	assert board.digSquare(1, 0) == false;
    	assert board.getCell(1, 0).digged == true;
    }
    
    @Test
    public void testToString() throws IOException {
    	Board board = Board.fromFile(new File("test/minesweeper/testFile2.txt"));
    	board.debug = true;
    	String rep1 = "-1\t2\t1\t0\t\n" + "2\t-1\t2\t1\t\n" + "1\t2\t3\t-1\t\n" + "0\t1\t-1\t2\t\n";
    	assert rep1.equals(board.toString());
    	board.debug = false;
    	StringBuilder rep2 = new StringBuilder();
    	for (int i = 0; i < 4; i++) {
    		for (int j = 0; j < 4; j++) {
    			rep2.append("-\t");
    		}
    		rep2.append('\n');
    	}
    	assert rep2.toString().equals(board.toString());
    	board.digSquare(0, 3);
    	rep2.setCharAt(4, '1');
    	rep2.setCharAt(6, '0');
    	rep2.setCharAt(13, '2');
    	rep2.setCharAt(15, '1');
    	assert rep2.toString().equals(board.toString());
    	board.flagSquare(1, 1);
    	rep2.setCharAt(11, 'F');
    	board.digSquare(1, 1);
    	assert rep2.toString().equals(board.toString());
    	board.deflagSquare(1, 1);
    	rep2.setCharAt(11, '-');
    	assert rep2.toString().equals(board.toString());
    	board.digSquare(1, 1);
    	rep2.setCharAt(11, '1');
    	rep2.setCharAt(13, '1');
    	rep2.setCharAt(4, '0');
    	rep2.setCharAt(2, '1');
    	assert rep2.toString().equals(board.toString());
    }
}
