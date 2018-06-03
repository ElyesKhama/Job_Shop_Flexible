package first;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		//System.out.println("faisable:" + checkFaisability(oS,mA)+", time: "+functionObjective(oS,mA) );
		System.out.println(oS.toString());
		System.out.println(mA.toString());
		System.out.println(" le temps final est de : "+objFunction(oS,mA));
		while(oSPop.size() < 7) {
			mutationMachines();
		}
		
		int i;
		for(i=0;i<mAPop.size();i++) {
			System.out.println(" le temps final est de : "+objFunction(oS,mAPop.get(i)));
		}

		ArrayList<Operation> oSTmp = (ArrayList<Operation>) oS.clone();
		do{
				oSTmp = mutationOperation();
		}while(oSTmp.equals(oS) || !checkFaisability(oSTmp,mA));
		
		System.out.println(oSTmp.toString()+checkFaisability(oSTmp,mA));
		System.out.println(mA.toString());
		//System.out.println("time: "+functionObjective(oSTmp,mA));
		System.out.println(" le temps final est de : "+objFunction(oSTmp,mA));

		//System.out.println("Temps de réalisation :" + functionObjective(oS,mA));
		//System.out.println("La solution est réalisable ? : "+ checkFaisability());
		//System.out.println("ospop:\n"+oSPop.toString());
		//System.out.println("osma:\n"+mAPop.toString());


		//System.out.println("La solution est réalisable ? : "+ checkFaisability());
		//tutorial1();
	}
 	
 	/*Combiner 2 solutions (parents) = croisement ( -> enfants) -> intensification
 	 			-> les meilleures solutions-parents donnent les meilleures solutions-enfants
 	  Modifier 1 solution = Mutation : permet de diversifier l’ensemble de solutions*/
 	
 	private static void mutationMachines() {
 		int iRandMachine=0;
 		int iRandOp = 0;
 		Operation opMut = null;
 		int opMachines = 1;
 		ArrayList<Tuple> mATmp = (ArrayList<Tuple>) mA.clone();
 		
 		
 		while(opMachines == 1) {
 			iRandOp = (int) (Math.random() * mA.size());
 			opMut = oS.get(iRandOp);
 			opMachines = opMut.getMachinesNeeded();
 		}
 		
 		while(iRandMachine == 0)
 			iRandMachine = (int) (Math.random()* opMut.getMachinesNeeded());
 			
 		mATmp.set(getIndexMa(opMut), opMut.getMachineTime()[iRandMachine]);
 		
 		if(!mAPop.contains(mATmp) && checkFaisability(oS,mATmp)) {
 			mAPop.add(mATmp);
 			oSPop.add(oS);
 		}
 		
 	//	System.out.println(oSPop.toString()+ ", Machine n*"+iRandMachine + ", opération n*"+iRandOp);
 	//	System.out.println(mAPop.toString());
  	}
 	
 	private static ArrayList<Operation> mutationOperation() {
 		int iToSwap = 0;
 		int iRandOp = (int) (Math.random()* oS.size());
 		Operation op,op2;
 		int indexMA = getIndexMa(oS.get(iRandOp));
 		int machineToSwap = mA.get(indexMA).nomMachine;
 		int indexMATest;
 		int machineTest;
 		ArrayList<Operation> oSTmp = (ArrayList<Operation>) oS.clone();
 		boolean stop = false;
 		
 		while(iToSwap<oS.size() && !stop) {
 			indexMATest = getIndexMa(oS.get(iToSwap));
 			machineTest = mA.get(indexMATest).nomMachine;
 			op = oS.get(iRandOp);
 			op2 = oS.get(iToSwap);
 			
 			if( indexMATest != indexMA && machineTest == machineToSwap && op.getNumJob() != op2.getNumJob()) {
 				op = oS.get(iRandOp);
 				oSTmp.set(iRandOp, oS.get(iToSwap));
 				oSTmp.set(iToSwap, op);
 				stop = true;
 				return oSTmp;
 			}
 			else
 				iToSwap++;
 		}
 		return null;
 				
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

 		System.out.println(listOpToDo.toString());
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
 	
 	public static int objFunction(ArrayList<Operation> os,ArrayList<Tuple> ma) {
 		int time = 0;
 		int[] tabDates = new int[jobs];
 		int[] tabMachines = new int[machines];
 		int i;
 		for(i=0;i<jobs;i++) {
 			tabDates[i] = 0;
 			tabMachines[i] = 0;
 		}
 		Operation opToDo;
 		int opTime;
 		int numJob;
 		for(i=0;i<os.size();i++) {
 			opToDo = os.get(i);
 			numJob = Integer.parseInt(opToDo.getNameOperation().substring(3, 4));
 			int indexMa = getIndexMa(opToDo);
 			opTime = opToDo.getTimeMachine(ma.get(indexMa).getNomMachine());  //todo : getIndexMA avec ma passé en parametre et pas tjrs mA global.
 		//	System.out.println("optime : "+opTime);
 			if(tabDates[numJob] >= tabMachines[ma.get(indexMa).getNomMachine()-1]) {
 				tabDates[numJob] += opTime;
 				tabMachines[ma.get(indexMa).getNomMachine()-1] = tabDates[numJob];
 			}
 			else {
 				tabMachines[ma.get(indexMa).getNomMachine()-1] += opTime;
 				tabDates[numJob] = tabMachines[ma.get(indexMa).getNomMachine()-1];
 			}
 	//		printTab(tabMachines);
 	// 		printTab(tabDates);
 		}
	 	Arrays.sort(tabDates);
	 	time = tabDates[jobs-1];
 		return time ;
 	}
 	
 	private static void printTab(int[] tab) {
 		int i;
 		for(i=0;i<tab.length;i++) {
 			System.out.print(tab[i]+",");
 		}
 		System.out.println("\n");
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

	public static boolean checkFaisability(ArrayList<Operation>os,ArrayList<Tuple> ma) {	
		
		boolean precedesor = true, goodMachine = false;
		int indice;
		Integer verif[] = new Integer[os.size()];
		String name;
		Tuple[] tuple;
		Operation op;
 		int numOp;
		
 		for(int v=0;v<verif.length;v++)
 			verif[v] = 0;
 		
		for(int z=0;z<os.size();z++) {
			goodMachine=false;
			op = os.get(z);
			name = op.getNameOperation();
			numOp = Integer.parseInt(name.substring(1, 2));
			tuple = op.getMachineTime();
			indice = getIndexMa(op);
			
			for(int j=0;j<tuple.length;j++) {
				if(tuple[j].equals(ma.get(indice)))
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
		machinesUsed.clear();
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
