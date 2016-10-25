import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class test {
	
	static RBTree tree = new RBTree();
	static int input[][];

	public static void main(String[] args) throws IOException {
		input();
		File f = new File("src\\command_1000.txt");
		if (!f.exists()) {
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f);
		for(int i=0;i<input.length;i++) {
			fw.write("reduce "+input[i][0]+" "+100000+"\n");
		}
		fw.close();
	}
	
	public static void RandomizeArray(int[][] array){
		Random rgen = new Random();  // Random number generator			
		for (int i=0; i<array.length; i++) {
		    int randomPosition = rgen.nextInt(array.length);
		    int temp = array[i][0];
		    array[i][0] = array[randomPosition][0];
		    array[randomPosition][0] = temp;
		}
	}
	
	static int input() {
		String s = //args[0];
				"E:/study/ufl/ADS/project/ads/src/test_100.txt";
				//"C:/Users/pratu/Downloads/test_100000000.txt";
		File f = new File(s);
		int n=0;
		try {
			Scanner in = new Scanner(new FileInputStream(f));
			n= in.nextInt();
			input = new int[n][2];
			int id=0,m=0;
			for(int i=0;i<n;i++) {
				id=in.nextInt();
				m=in.nextInt();
				input[i][0]=id;
				input[i][1]=m;
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return n;
	}
	

}
