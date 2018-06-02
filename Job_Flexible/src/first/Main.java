package first;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	private static int jobs = 0;   //nb de jobs
	private static int machines = 0;  //nb de machines
	private static int avgMachine = 0;
	private static int cmptOpDone = 0;
	private static Job[] tabJobs;
	private static int nbJobEnded = 0;
	private static ArrayList<Integer> machinesUsed = new ArrayList<Integer>();
	private static ArrayList<Operation> listOpToDo = new ArrayList<Operation>();
	private static int tempsTotale = 0;
	private static ArrayList<Operation> oS = new ArrayList<Operation>();
	private static ArrayList<Tuple> mA = new ArrayList<Tuple>();
	private static ArrayList<ArrayList<Operation>> oSPop = new ArrayList<ArrayList<Operation>>();   //vecteur de population : OS
	private static ArrayList<ArrayList<Tuple>> mAPop = new ArrayList<ArrayList<Tuple>>();	  //vecteur de population : MA
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("test.txt");
		System.out.println("Fichier chargé !");
		
		while(nbJobEnded < jobs)
			testAdam();
		createPopulation();
		System.out.println(oS.toString());
		System.out.println(mA.toString());

		System.out.println("La solution est réalisable ? : "+ checkFaisability());
		System.out.println("ospop:\n"+oSPop.toString());
		System.out.println("osma:\n"+mAPop.toString());


		//System.out.println("La solution est réalisable ? : "+ checkFaisability());
		//tutorial1();
	}
 	
 	/*Combiner 2 solutions (parents) = croisement ( -> enfants) -> intensification
 	 			-> les meilleures solutions-parents donnent les meilleures solutions-enfants
 	  Modifier 1 solution = Mutation : permet de diversifier l’ensemble de solutions*/
 	
 	public static void createPopulation() {
 		int nbPopulation = 100;
 		int indiceRandom = (int) (Math.random() * mA.size())+1;
 		ArrayList<Tuple> maTemp = new ArrayList<Tuple>();
 		maTemp = (ArrayList<Tuple>) mA.clone();
 		Operation opMut = oS.get(indiceRandom);
 		int machineReal = getIndexMa(opMut);
 		if(opMut.getMachinesNeeded() > 1) {
 				
 		}
 		
 		
  	}

 	
 	private static void cross(int os1,int os2) {
 		int indiceCut = (int)Math.random() * oS.size();
 		
 		ArrayList<Operation> sol1 = oSPop.get(os1);
 		ArrayList<Operation> sol2 = oSPop.get(os2);
 		
 		ArrayList<Operation> sol2Cloned = (ArrayList<Operation>) sol2.clone();
 		
 		int i;
 		for(i=indiceCut;i<oS.size();i++) {
 			sol2.set(i, sol1.get(i));
 			sol1.set(i, sol2Cloned.get(i));
 		}
 		
 		System.out.println("crossover down");
 	}
 	
 	public static void selection() {  //par tournoi 2 à 2
 		int nbparents = 0;
 		int ind1 = (int) Math.random() * oSPop.size();
 		int ind2 = (int) Math.random() * oSPop.size();
 		
 		while(ind1 != ind2) { //si ils sont égaux c'est pas bon
 			ind2 = (int) Math.random() * oSPop.size();
 		}
 		
 		ArrayList<Operation> solOs1 = oSPop.get(ind1);
 		ArrayList<Tuple> solMa1 = mAPop.get(ind1);
 		ArrayList<Operation> solOs2 = oSPop.get(ind1);
 		ArrayList<Tuple> solMa2 = mAPop.get(ind1);

 		int f1 = functionObjective(solOs1, solMa1);
 		int f2 = functionObjective(solOs2, solMa2);
 		
 		if(f1<=f2) {
 			oSPop.remove(solOs2);
 			mAPop.remove(solMa2);
 		}
 		else {
 			oSPop.remove(solOs1);
 			mAPop.remove(solMa1);
 		}
 		
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
 		for(int j=0;j<listOpToDo.size();j++) {
 			
 			op = listOpToDo.get(j);
 			machineUsing = op.getMachineTime()[0];
 			if(!machinesUsed.contains(machineUsing.nomMachine)){
 				machinesUsed.add(machineUsing.nomMachine);
 				oS.set(cmptOpDone,op);
 				
 				int indice = getIndexMa(listOpToDo.remove(j));
 				mA.set(indice, machineUsing);
 				j--;
 				cmptOpDone++;
 			}
 			
 		}
 		machinesUsed.clear();
 		/*refreshMUtime(tempsParallele);
 		tempsTotale += tempsParallele;
 		System.out.println("Temps totale: " + tempsTotale);*/
 	}
 	
 	private static int functionObjective(ArrayList<Operation> os,ArrayList<Tuple> ma) {
 		int temps = 0;
 		int tempsExec = 0, tempsParallele = 0;
 		int indiceMachine = 0;
 		Tuple machineUsing;
 		Operation op;
 		
 		createListMachines();
 		
 		for(int i=0;i<os.size();i++) {
 			op = os.get(i);
 			indiceMachine = getIndexMa(op);
 			machineUsing = ma.get(indiceMachine);
 			tempsExec = machineUsing.timeOperation;
 			
 			if(machinesUsed.get(machineUsing.nomMachine) < 1) {
 				
 				machinesUsed.set(machineUsing.nomMachine,tempsExec);
 				if(tempsExec > tempsParallele) {
					tempsParallele = tempsExec;
				}
 				
 			}
 			else {
 				temps += tempsParallele;
 				initListMachines();
 			}
 			
 			tabJobs[op.getNumJob()].increaseCmptOpFinished();
 			/*tempsExec = machinesUsed[machineUsed.nomMachine-1];
				System.out.println(machineUsed.nomMachine-1 + ": " + machineUsed.timeOperation);
				if(tempsExec > tempsParallele) {
					tempsParallele = tempsExec;
				}*/
 			}
 			
 		
 		
 		return temps;
 	}
 	
 	private static int getIndexMa(Operation op) {
 		String name = op.getNameOperation();
 		int numOp = Integer.parseInt(name.substring(1, 2));
 		int numJob = Integer.parseInt(name.substring(3, 4));
 		int indice = 0;
 		
 		for(int i=0;i!=numJob;i++)
 				indice += tabJobs[i].getNbOperations();
 		indice += numOp;
 		return indice;
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

	public static boolean checkFaisability() {	
		
		boolean precedesor = true, goodMachine = false;
		int indice;
		Integer verif[] = new Integer[oS.size()];
		String name;
		Tuple[] tuple;
		Operation op;
 		int numOp;
		
 		for(int v=0;v<verif.length;v++)
 			verif[v] = 0;
 		
		for(int z=0;z<oS.size();z++) {
			goodMachine=false;
			op = oS.get(z);
			name = op.getNameOperation();
			numOp = Integer.parseInt(name.substring(1, 2));
			tuple = op.getMachineTime();
			indice = getIndexMa(op);
			
			for(int j=0;j<tuple.length;j++) {
				if(tuple[j].equals(mA.get(indice)))
					goodMachine = true;
			}
			
			for(int i=0;i<verif.length;i++) {
				if(numOp != 0 && verif[indice-1] != 1)
					precedesor = false;
			}
			if(verif[indice] != 1)
				verif[indice] = 1;
			else
				precedesor = false;
			
		}
		
		return precedesor && goodMachine;
	}
	
	
//TODO: Tant que notre voisinage est meilleur --> on continue d'en rechercher sinon on arrete
	
	private static void createListMachines(){
		System.out.println("creation des listes pour chaque machine");
		for(int i=0;i<machines;i++) 
			machinesUsed.add(0);
	}
	
	private static void initListMachines() {
		for(int i=0;i<machines;i++) 
			machinesUsed.set(i,0);
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
