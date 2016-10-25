import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class bbst {

	// Global RBTree instance
	static RBTree tree = new RBTree();
	static InputReader in;
	// To store the input initially
	static int input[][];

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		String s = args[0];
		File f = new File(s);
		input(f);
		tree.initilize(input);
		input = null;
		command();
	}

	/**
	 * Takes input from the given file stores in array
	 * 
	 * @param f
	 * @return
	 */
	static int input(File f) {
		int n = 0;
		try {
			in = new InputReader(new FileInputStream(f));
			n = in.nextInt();
			input = new int[n][2];
			int id = 0, m = 0;
			for (int i = 0; i < n; i++) {
				id = in.nextInt();
				m = in.nextInt();
				input[i][0] = id;
				input[i][1] = m;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not Found");
		}
		return n;
	}

	/**
	 * Takes commands from console and segregates type of the command and
	 * respectively calls the command in RBTree class
	 * 
	 * @throws IOException
	 */
	static void command() throws IOException {
		Scanner in = new Scanner(System.in);
		int theId = 0;
		int m = 0;
		int res = 0;

		while (in.hasNext()) {
			String command = in.next();
			switch (command) {
			case "increase": {
				// If command is to increase
				theId = in.nextInt();
				m = in.nextInt();
				// Call increase method for given id:theId and count m
				res = tree.increase(theId, m);
				System.out.println(res);
				break;
			}
			case "reduce": {
				theId = in.nextInt();
				m = in.nextInt();
				// Call reduce method for given theId and count m
				res = tree.reduce(theId, m);
				System.out.println(res);
				break;
			}
			case "count": {
				theId = in.nextInt();
				// Prints the count from theId
				res = tree.count(theId).getCount();
				System.out.println(res);
				break;
			}
			case "inrange": {
				int lrange = in.nextInt();
				int rrange = in.nextInt();
				// Prints the sum of elements which falls between range
				res = tree.inrange(lrange, rrange);
				System.out.println(res);
				break;
			}
			case "next": {
				theId = in.nextInt();
				// Prints the next bigger element
				Event e = tree.next(theId);
				System.out.println(e.getId() + " " + e.getCount());
				break;
			}
			case "previous": {
				theId = in.nextInt();
				// Prints the detail of number just smaller than id
				Event e = tree.previous(theId);
				System.out.println(e.getId() + " " + e.getCount());
				break;
			}
			case "quit": {
				// Quits the program on receiving this messsage
				System.exit(0);
				break;
			}
			default: {
				// Returns on invalid input
				System.out.println("Invalid Input.. Exiting.....");
				System.exit(0);
				break;
			}
			}
		}
		in.close();
	}

	/**
	 * 
	 * To print the inorder of the tree For testing and debugging purpose
	 * 
	 * @param root
	 * @param dots
	 * @param pw
	 * @throws IOException
	 */
	static void printInorderTree(Event root, String dots) throws IOException {
		if (root == null)
			return;
		printInorderTree(root.getLeft(), "." + dots);
		printInorderTree(root.getRight(), "." + dots);
	}

	/**
	 * 
	 * Custom class to fast fetch data from the provided inputstream. Uses
	 * buffer and parses token into int or string
	 * 
	 * @author pratu
	 *
	 */
	static class InputReader {
		public BufferedReader reader;
		public StringTokenizer tokenizer;

		public InputReader(InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream), 32768);
			tokenizer = null;
		}

		/**
		 * Returns int
		 * 
		 * @return
		 */
		public int nextInt() {
			String s = next();
			try {
				int nexti = Integer.parseInt(s);
				return nexti;
			} catch (NumberFormatException nfe) {
				return -1;
			}
		}

		/**
		 * Parses nextline into tokenizer and gives one token at a time returns
		 * string
		 * 
		 * @return
		 */
		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}
	}

}