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
	private static ArrayList<Boolean> machinesUsed = new ArrayList<Boolean>();
	private static ArrayList<Operation> listOpToDo = new ArrayList<Operation>();
	private static int tempsTotale = 0;
	private static ArrayList<String> oS = new ArrayList<String>();
	private static ArrayList<String> mA = new ArrayList<String>();
	private static ArrayList<ArrayList<String>> oSPop = new ArrayList<ArrayList<String>>();   //vecteur de population : OS
	private static ArrayList<ArrayList<String>> mAPop = new ArrayList<ArrayList<String>>();	  //vecteur de population : MA
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("test.txt");
		System.out.println("Fichier chargé !");
		
		while(nbJobEnded < jobs)
			testAdam();
		createPopulation();
		System.out.println(oS.toString());
		System.out.println(mA.toString());
		//printTabJobs();
		//tutorial1();
	}
 	
 	public static void createPopulation() {
 		int nbPopulation = 100;
 		int nbTaches = mA.size();
 		int indiceRandom = (int) (Math.random() * nbTaches);
 		
 		ArrayList<String> maTemp = new ArrayList<String>();
 		maTemp = (ArrayList<String>) mA.clone();

 		String randomOs = oS.get(indiceRandom);
 		
 		
 	}
 	
	public static void createListMachines(){
		System.out.println("creation des listes pour chaque machine");  //liste d'operations (initialement vides) pour chaque machines
		for(int i=0;i<machines;i++) {
			machinesUsed.add(false);
		}
		System.out.println(machinesUsed.toString());
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
 	
 	private static void doOp2() {
 		Tuple machineUsed;
 		boolean usedOrNot = false;
 		int tempsParallele = 0, tempsExec = 0;
 		
 		for(int j=0;j<listOpToDo.size();j++) {
 			
 			machineUsed = listOpToDo.get(j).getMachineTime()[0];
 			usedOrNot = machinesUsed.get(machineUsed.nomMachine-1);
 			
 			if(!usedOrNot){
 				machinesUsed.set(machineUsed.nomMachine-1, true);
 				tempsExec = machineUsed.timeOperation;

 				if(tempsExec > tempsParallele) {
 					tempsParallele = tempsExec;
 				}
 				else {
 					
 				}
 				//System.out.println(listOpToDo.toString());
 				refreshMA(listOpToDo.remove(j), machineUsed.nomMachine);
 		 		//System.out.println(mA.toString());
 				j--;
 			}
 			else {
 			}
 				
 		}
 	}
 	
 	private static void doOp() {
 		ArrayList<Integer> listMachinesUsed = new ArrayList<Integer>();
 		Tuple machineUsed;
 		int tempsParallele = 0, tempsExec = 0;
 		
 		for(int j=0;j<listOpToDo.size();j++) {
 			machineUsed = listOpToDo.get(j).getMachineTime()[0];
 			
 			if(!listMachinesUsed.contains(machineUsed.nomMachine)) {
 				
 				listMachinesUsed.add(machineUsed.nomMachine);
 				tempsExec = machineUsed.timeOperation;

 				if(tempsExec > tempsParallele)
 					tempsParallele = tempsExec;
 				//System.out.println(listOpToDo.toString());
 				refreshMA(listOpToDo.remove(j), machineUsed.nomMachine);

 		 		//System.out.println(mA.toString());
 				j--;
 				
 			}
 		}
 		tempsTotale += tempsParallele;
 	}
 	
 	public static void testAdam() {
 		
 		selectOpToDo();
 		//System.out.println("Opération à faire : " + listOpToDo.toString() );
 		doOp();
 		//System.out.println("Temps après opérations : " + tempsTotale);
 		//System.out.println("Opérations restantes : " + listOpToDo.toString());
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
