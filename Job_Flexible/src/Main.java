import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		readFile("Example1");
	}

	public static void readFile(String file) {
		byte[] buf = new byte[8];
		BufferedInputStream fis = null;
		
		try {
			
			fis = new BufferedInputStream(new FileInputStream(new File(file)));
			
			while(fis.read(buf) != -1 ) {
				for (byte bit : buf) {
		               System.out.print("\t"+(char) bit);
		         }
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}
