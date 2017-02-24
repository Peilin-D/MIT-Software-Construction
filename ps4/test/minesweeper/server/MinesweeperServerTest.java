/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Random;

import org.junit.Test;

/**
 * TODO
 */
public class MinesweeperServerTest {
    
	private static final String LOCALHOST = "127.0.0.1";
	private static final int PORT = 4000 + new Random().nextInt(1 << 15);
	private static final int MAX_CONNECTION_ATTEMPTS = 10;
	
	/**
	 * Start a MinesweeperServer in debug mode
	 * @param file input board file
	 * @return thread running the server
	 */
	private static Thread startMinesweeperServer(String filePath) {
		final String[] args = {
				"--debug",
				"--port", Integer.toString(PORT),
				"--file", filePath
		};
		Thread serverThread = new Thread(()->MinesweeperServer.main(args));
		serverThread.start();
		return serverThread;
	}
	
	/**
	 * Connect to MinesweeperServer
	 * @param server thread running server
	 * @return client socket
	 * @throws IOException
	 */
	private static Socket connectToServer(Thread server) throws IOException {
		int attempts = 0;
		while (true) {
			try {
				Socket socket = new Socket(LOCALHOST, PORT);
				socket.setSoTimeout(3000);
				return socket;
			} catch (ConnectException ce) {
				if (!server.isAlive()) {
					throw new IOException("Server thread not running");
				}
				if (++attempts > MAX_CONNECTION_ATTEMPTS) {
					throw new IOException("Exceeded max connection attempts", ce);
				}
				try {
					Thread.sleep(attempts * 10);
				} catch (InterruptedException ie) {
					
				}
			}
		}
	}
	
	@Test(timeout = 10000)
	public void test() throws IOException {
		Thread thread = startMinesweeperServer("test/minesweeper/testFile3.txt");
		Socket socket = connectToServer(thread);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		assertTrue("expected Hello Message", in.readLine().startsWith("Welcome"));
		
		out.println("look");
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        
        out.println("dig 3 1");
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t1\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
        assertEquals("-\t-\t-\t-\t-\t-\t-\t", in.readLine());
		
        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look"); // debug mode is on
        assertEquals("0\t0\t0\t0\t0\t0\t0\t", in.readLine());
        assertEquals("0\t0\t0\t0\t0\t0\t0\t", in.readLine());
        assertEquals("0\t0\t0\t0\t0\t0\t0\t", in.readLine());
        assertEquals("0\t0\t0\t0\t0\t0\t0\t", in.readLine());
        assertEquals("0\t0\t0\t0\t0\t0\t0\t", in.readLine());
        assertEquals("1\t1\t0\t0\t0\t0\t0\t", in.readLine());
        assertEquals("-\t1\t0\t0\t0\t0\t0\t", in.readLine());

        out.println("bye");
        socket.close();
        
	}
	
	
	
	
    
    
}
