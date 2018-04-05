package first;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;


public class Main {

	private static int jobs = 0;
	private static int machines = 0;
	private static int avgMachine = 0;
	private static Job[] tabJobs;
	
 	public static void main(String[] args) {
 		
		readFile("example1.txt");
	}

	public static void readFile(String file) {
		Scanner sc = null;
		String sentence = null;
            try {
                sc = new Scanner(new File(file));
                
                if (sc.hasNextLine()) {
                    //for (char c : sc.nextLine())
                	sentence = sc.nextLine();
                	jobs = sentence.charAt(0);
                	machines = sentence.charAt(4);
                	avgMachine = sentence.charAt(8);
                    
                    tabJobs = new Job[jobs];
                }
                    
                for(int i = 0;i<jobs;i++) {
                    if(sc.hasNextLine()) {
                    	sentence = sc.nextLine();
                    	tabJobs[i] = new Job(sentence);
                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (sc != null)
                    sc.close();
            }
		
	}
	
	private static void Tutorial1() {
		
			Graph graph = new SingleGraph("Tutorial 1");
			graph.addNode("A" );
			graph.addNode("B" );
			graph.addNode("C" );
			graph.addEdge("AB", "A", "B");
			graph.addEdge("BC", "B", "C");
			graph.addEdge("CA", "C", "A");
			graph.display();
			
		
	}
	
}
