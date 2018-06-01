package first;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
	
	private static int jobs = 0;   //nb de jobs
	private static int machines = 0;  //nb de machines
	private static int avgMachine = 0;
	private static Job[] tabJobs;
	private static int nbJobEnded = 0;
	private static Integer[] machinesUsed;
	private static ArrayList<Operation> listOpToDo = new ArrayList<Operation>();
	private static int tempsTotale = 0;
	private static ArrayList<String> oS = new ArrayList<String>();
	private static ArrayList<String> mA = new ArrayList<String>();
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("test.txt");
		System.out.println("Fichier chargé !");
		
		while(nbJobEnded < jobs)
			testAdam();
		System.out.println(oS.toString());
		System.out.println(mA.toString());
		//printTabJobs();
		//tutorial1();
	}
 	
 	public static void testAdam() {
 		
 		selectOpToDo();
 		//System.out.println("Opération à faire : " + listOpToDo.toString() );
 		doOp();
 		//System.out.println("Opérations restantes : " + listOpToDo.toString());
 	}
 	
 	private static void selectOpToDo() {
 		Operation operation = null;
 		
 		for(int i=0;i<tabJobs.length;i++) {
 			operation = tabJobs[i].getOperation();
 			if(operation != null) {
	 			if(!containsAJobOp(i)) {
	 				listOpToDo.add(operation);
	 				tabJobs[i].popOperation();
	 			}
 			}
 			else nbJobEnded++;
 		}
 		listOpToDo.sort(new OperationComparator());
 	}
 	
 	private static boolean containsAJobOp(int numJob) {
 		boolean contain = false;
 		for(int j=0;j<listOpToDo.size();j++) {
 			if(listOpToDo.get(j).getNumJob() == numJob)
 				contain = true;
 		}
 		return contain;
 	}
 	
 	private static void doOp() {
 		Tuple machineUsed;
 		int timeLeft;
 		int tempsParallele = 0, tempsExec = 0;
 		
 		for(int j=0;j<listOpToDo.size();j++) {
 			
 			machineUsed = listOpToDo.get(j).getMachineTime()[0];
 			timeLeft = machinesUsed[machineUsed.nomMachine-1];
 			
 			if(timeLeft < 1){
 				machinesUsed[machineUsed.nomMachine-1] += machineUsed.timeOperation;
 				tempsExec = machinesUsed[machineUsed.nomMachine-1];
 				System.out.println(machineUsed.nomMachine-1 + ": " + machineUsed.timeOperation);
 				if(tempsExec > tempsParallele) {
 					tempsParallele = tempsExec;
 				}
 				
 				refreshMA(listOpToDo.remove(j), machineUsed.nomMachine);
 		 		//System.out.println(mA.toString());
 				j--;
 			}
 			
 		}
 		refreshMUtime(tempsParallele);
 		tempsTotale += tempsParallele;
 		System.out.println("Temps totale: " + tempsTotale);
 	}
 	
 	private static void refreshMA(Operation op, int machine) {
 		String name = op.getNameOperation();
 		String numOp = name.substring(1, 2);
 		String numJob = String.valueOf(Integer.parseInt(name.substring(3, 4))+1);
 		int i = 0, position;
 		boolean stop = false;
 		
 		while(i<mA.size() && !stop) {
 			
 			if (oS.get(i).equals(numJob)) {
 				position = Integer.parseInt(numOp)+i;
 				mA.remove(position);
 				//System.out.println(String.valueOf(machine));
 				mA.add(position,String.valueOf(machine));
 				stop = true;
 			}
 			i++;
 			
 		}
 		
 	}
 	
 	private static void refreshMUtime(int time) {
 		for(int i=0;i<machinesUsed.length;i++) {
 			machinesUsed[i] -= time;
 		}
 	}
 
//TODO: Pour chaque machine, une liste d'opérations pouvant(devant) s'effectuer dessus

	public static void readFile(String file) {
		Scanner sc = null;
		String sentence = null;
            try {
                sc = new Scanner(new File(file));
                
                if (sc.hasNextLine()) {
                	sentence = sc.nextLine();
                	jobs = Integer.parseInt(sentence.substring(0,1));
                	machines = Integer.parseInt(sentence.substring(4,5));
                	avgMachine = Integer.parseInt(sentence.substring(8,9));
                    
                    tabJobs = new Job[jobs];
                    createListMachines();
                }
                    
                for(int i = 0;i<jobs;i++) {
                	
                    if(sc.hasNextLine()) {
                    	sentence = sc.nextLine();
                    	tabJobs[i] = new Job(sentence,i);
                    	for(int j=0;j<tabJobs[i].getNbOperations();j++) {
                    		oS.add(Integer.toString(i+1));
                    		mA.add("?");
                    	}
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

//TODO: Tant que notre voisinage est meilleur --> on continue d'en rechercher sinon on arrete
	
	public static void createListMachines(){
		System.out.println("creation des listes pour chaque machine");
		machinesUsed = new Integer[machines];  //liste d'operations (initialement vides) pour chaque machines
		for(int i=0;i<machinesUsed.length;i++) 
			machinesUsed[i] = 0;
	}
	
	private static void tutorial1() {
		ArrayList<Operation> listOperations;
		Graph graph = new SingleGraph("Problème visualisé");
		graph.setAttribute("ui.label", true);
		for(int i=0;i<tabJobs.length;i++) {
			listOperations = tabJobs[i].getOperationsTotales();
			Node n = graph.addNode(listOperations.get(0).getNameOperation());
			n.setAttribute("ui.label",listOperations.get(0).getNameOperation());
			for(int j=1;j<listOperations.size();j++) {
				
				n = graph.addNode(listOperations.get(j).getNameOperation());
				n.setAttribute("ui.label",listOperations.get(j).getNameOperation());
				graph.addEdge(Integer.toString(i)+Integer.toString(j),listOperations.get(j-1).getNameOperation() ,listOperations.get(j).getNameOperation());
			}
			
		}

		graph.display();
		
	}  
	
}
