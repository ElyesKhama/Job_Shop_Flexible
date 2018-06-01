package first;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	
	private static int jobs = 0;   //nb de jobs
	private static int machines = 0;  //nb de machines
	private static int avgMachine = 0;
	private static int cmptOpDone = 0;
	private static Job[] tabJobs;
	private static int nbJobEnded = 0;
	private static ArrayList<Tuple> machinesUsed;
	private static ArrayList<Operation> listOpToDo = new ArrayList<Operation>();
	private static int tempsTotale = 0;
	private static ArrayList<Operation> oS = new ArrayList<Operation>();
	private static ArrayList<Tuple> mA = new ArrayList<Tuple>();
	private static ArrayList<ArrayList<String>> oSPop = new ArrayList<ArrayList<String>>();   //vecteur de population : OS
	private static ArrayList<ArrayList<String>> mAPop = new ArrayList<ArrayList<String>>();	  //vecteur de population : MA
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("test.txt");
		System.out.println("Fichier chargé !");
		
		while(nbJobEnded < jobs)
			testAdam();
		//createPopulation();
		System.out.println(oS.toString());
		System.out.println(mA.toString());
		//tutorial1();
	}
 	
 	/*Combiner 2 solutions (parents) = croisement ( -> enfants) -> intensification
 	 			-> les meilleures solutions-parents donnent les meilleures solutions-enfants
 	  Modifier 1 solution = Mutation : permet de diversifier l’ensemble de solutions*/
 	
 	public static void createPopulation() {
 		int nbPopulation = 100;
 		int indiceRandom = (int) (Math.random() * mA.size())+1;
 		ArrayList<String> maTemp = new ArrayList<String>();
 		maTemp = (ArrayList<String>) mA.clone();

 		cmptOpDone=0;
 		Operation op =  getOperationToMute(indiceRandom, 0);
 		
 		System.out.println(indiceRandom + "qui donne : " + op.getNameOperation());
 	}
 	
 	private static Operation getOperationToMute(int indiceRandom, int job) {
 		System.out.println("Début : indice => " + indiceRandom + ", job : " + (job+1) );
 		int nbOperations = tabJobs[job].getNbOperations();
 		Operation op;
 		if(tabJobs.length < job || indiceRandom > oS.size())
 			op = null;
 	    else if (indiceRandom <= nbOperations) 
 			op = tabJobs[job].getOperationsTotales().get(indiceRandom-1);
 		else
 			op = getOperationToMute(indiceRandom-nbOperations,++job);
 		return op;
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
 		Tuple machineUsing;
 		Operation op;
 		//int timeLeft;
 		for(int j=0;j<listOpToDo.size();j++) {
 			
 			op = listOpToDo.get(j);
 			machineUsing = op.getMachineTime()[0];
 			
 			if(!machinesUsed.contains(machineUsing)){
 				machinesUsed.add(machineUsing);
 				//tempsExec = machinesUsed[machineUsed.nomMachine-1];
 				//System.out.println(machineUsed.nomMachine-1 + ": " + machineUsed.timeOperation);
 				/*if(tempsExec > tempsParallele) {
 					tempsParallele = tempsExec;
 				}*/
 				oS.set(cmptOpDone,op);
 				refreshMA(listOpToDo.remove(j), machineUsing);
 		 		//System.out.println(mA.toString());
 				j--;
 				cmptOpDone++;
 			}
 			
 		}
 		machinesUsed.clear();
 		/*refreshMUtime(tempsParallele);
 		tempsTotale += tempsParallele;
 		System.out.println("Temps totale: " + tempsTotale);*/
 	}
 	
 	private static void functionObjective() {
 		int timeLeft;
 		int tempsExec = 0, tempsParallele = 0;
 		Operation op;
 		for(int i=0;i<oS.size();i++) {
 			op = oS.get(i);
 			
 			//tempsExec = machinesUsed[machineUsed.nomMachine-1];
				//System.out.println(machineUsed.nomMachine-1 + ": " + machineUsed.timeOperation);
				/*if(tempsExec > tempsParallele) {
					tempsParallele = tempsExec;
				}*/
 		}
 		
 	}
 	
 	private static void refreshMA(Operation op, Tuple machine) {
 		String name = op.getNameOperation();
 		String numOp = name.substring(1, 2);
 		String numJob = String.valueOf(Integer.parseInt(name.substring(3, 4))+1);
 		int i = 0, position;
 		Operation test;
 		boolean stop = false;
 		
 		while(i<mA.size() && !stop) {
 			System.out.println(oS.toString()+ " a gegez " + i);
 			test = oS.get(i);
 			if (test != null && test.getNumJob() == Integer.parseInt(numJob)) {
 				position = Integer.parseInt(numOp)+i;
 				//System.out.println(String.valueOf(machine));
 				mA.set(position,machine);
 				stop = true;
 			}
 			i++;
 			
 		}
 		
 	}
 	
 	/*private static void refreshMUtime(int time) {
 		for(int i=0;i<machinesUsed.length;i++) {
 			machinesUsed[i] -= time;
 		}
 	}*/
 
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
                    		oS.add(null);
                    		mA.add(null);
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
		machinesUsed = new ArrayList<Tuple>();  //liste d'operations (initialement vides) pour chaque machines
		for(int i=0;i<machines;i++) 
			machinesUsed.add(new Tuple(0,0));
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
