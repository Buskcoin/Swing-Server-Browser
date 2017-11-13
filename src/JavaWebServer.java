import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.JOptionPane;

public class JavaWebServer {

	private static final int NUMBER_OF_THREADS = 100;
	private static final Executor THREAD_POOL = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(8080);

		// waits for a connection
		while (true) {
			final Socket connection = socket.accept();
			Runnable task = new Runnable() {
				@Override
				public void run() {
					HandleRequest(connection);
				}
			};
			THREAD_POOL.execute(task);
		}

	}

	private static void HandleRequest(Socket s) {
		BufferedReader in;
		PrintWriter out;
		String request = "";
		String responseFile;

		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			request = in.readLine();
			out = new PrintWriter(s.getOutputStream(), true);
			out.print(new String(Files.readAllBytes(Paths.get(request))).substring(15));
			out.flush();
			out.close();
			s.close();
		} catch (IOException e) {
			System.out.println("Failed respond to client request: " + e.getMessage());
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

}