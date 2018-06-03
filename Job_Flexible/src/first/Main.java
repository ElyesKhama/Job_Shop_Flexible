package first;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
	
	private static int cmptNotMuteOP=0,cmptNotMuteMA=0;
	private static int jobs = 0;   //nb de jobs
	private static int machines = 0;  //nb de machines
	private static int avgMachine = 0;
	private static int cmptOpDone = 0;
	private static Job[] tabJobs;
	private static int nbJobEnded = 0;
	private static ArrayList<Integer> machinesUsed = new ArrayList<Integer>();
	private static ArrayList<Operation> listOpToDo = new ArrayList<Operation>();
	private static ArrayList<Operation> oS = new ArrayList<Operation>();
	private static ArrayList<Tuple> mA = new ArrayList<Tuple>();
	private static ArrayList<ArrayList<Operation>> oSPop = new ArrayList<ArrayList<Operation>>();   //vecteur de population : OS
	private static ArrayList<ArrayList<Tuple>> mAPop = new ArrayList<ArrayList<Tuple>>();	  //vecteur de population : MA
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("mt10c1.txt");
		System.out.println("Fichier chargé !");
		
		initSolution();
		
		for(int i=0;i<10;i++) {
		
			createPop();
			System.out.println(i+"taille os/ma before select:"+mAPop.size());
			selection();
			System.out.println(i+"taille os/ma after select:"+mAPop.size());
			crossOver();
			System.out.println(i+"taille os/ma after cross:"+mAPop.size());
		}

		for(int i=0;i<mAPop.size();i++) {
			System.out.print(objFunction(oSPop.get(i),mAPop.get(i))+" > ");
		}
		
		int indexFinalSolution = giveFinalSolution();
		System.out.println("indexfinal:"+indexFinalSolution);
		System.out.println("LA SOLUTION FINALE EST :"+ oSPop.get(indexFinalSolution) +"\n" + mAPop.get(indexFinalSolution));
		System.out.println("LE TEMPS FINAL EST DE : "+ objFunction(oSPop.get(indexFinalSolution),mAPop.get(indexFinalSolution)));
		//tutorial1();
	}
 	
 	/*Combiner 2 solutions (parents) = croisement ( -> enfants) -> intensification
 	 			-> les meilleures solutions-parents donnent les meilleures solutions-enfants
 	  Modifier 1 solution = Mutation : permet de diversifier l’ensemble de solutions*/
 	
 	private static void mutationMachines(ArrayList<Operation> oS, ArrayList<Tuple> mA	) {
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
 		
 		iRandMachine = (int) (Math.random()* opMut.getMachinesNeeded());
 			
 		mATmp.set(getIndexMa(opMut), opMut.getMachineTime()[iRandMachine]);
 		
 		if(!mAPop.contains(mATmp) && checkFaisability(oS,mATmp)) {
 			mAPop.add(mATmp);
 			oSPop.add(oS);
 	 	//	System.out.println(mATmp.toString());
 		}
 		else
 			cmptNotMuteMA++;
  	
 		
 	//	System.out.println(oSPop.toString()+ ", Machine n*"+iRandMachine + ", opération n*"+iRandOp);
 	//	System.out.println(mAPop.toString());
  	}
 	
 	private static void createPop(){
		long currentTime = System.currentTimeMillis();
		long finalTime = currentTime + 1000;
 		while(currentTime < finalTime) {
 			for(int i=0;i<oSPop.size();i++) {
 				while(cmptNotMuteMA< 20) {
 					mutationMachines(oSPop.get(i),mAPop.get(i));
 				}
 				cmptNotMuteMA = 0;
 			}
 			
 			for(int i=0;i<oSPop.size();i++) {
 				while(cmptNotMuteOP< 20) {
 					mutationOperation(oSPop.get(i),mAPop.get(i));
 				}
 				cmptNotMuteOP = 0;
 			}
 	 			//mutationMachines(oSPop.get(oSPop.size()-1),mAPop.get(mAPop.size()-1));
 	 			//mutationOperation(oSPop.get(oSPop.size()-1),mAPop.get(mAPop.size()-1)); //mutation operation sur solution initiale
 			currentTime = System.currentTimeMillis();
 		}
 	}
 	
 	private static void mutationOperation(ArrayList<Operation> oS, ArrayList<Tuple> mA) {
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
 				if(!oSPop.contains(oSTmp) && checkFaisability(oSTmp,mA)) {
	 				oSPop.add(oSTmp);
	 				mAPop.add(mA);
	 			//	System.out.println("jai rajouté un os : "+ oSTmp.toString());
 				}
 				else
 					cmptNotMuteOP++;
 			}
  			else {
 	 			cmptNotMuteOP++;
 				iToSwap++;
 	 		}
 		}
 				
 	}
 	

 	
 	private static void crossOver() {
 		ArrayList<Operation> sol2OS, sol2OSCloned, sol1OS ;
 		ArrayList<Tuple> sol2MA, sol2MACloned, sol1MA ;
 		int indiceCut;
 		
 		for(int i=0;i<oSPop.size()-1;i+=2) {
	 		indiceCut = (int)Math.random() * oS.size();
		 	sol1OS = oSPop.get(i);
		 	sol1MA = mAPop.get(i);
		 	sol2OS = oSPop.get(i+1);
		 	sol2MA = mAPop.get(i+1);
		 	sol2OSCloned = new ArrayList<Operation>(sol2OS);
		 	sol2MACloned = new ArrayList<Tuple>(sol2MA);
		 	
		 	for(int j=indiceCut;j<oS.size();j++) {
		 		sol2OS.set(j, sol1OS.get(j));
		 		sol1OS.set(j, sol2OSCloned.get(j));
		 		sol2MA.set(j, sol2MA.get(j));
		 		sol1MA.set(j, sol2MACloned.get(j));
		 	}
		 	if(!mAPop.contains(sol1MA) && checkFaisability(sol2OS,sol2MA)) {
		 		oSPop.set(i+1, sol2OS);
		 		mAPop.set(i+1, sol2MA);
		 	}else {
		 		oSPop.remove(i);
		 		mAPop.remove(i);
		 	}
		 	if(!mAPop.contains(sol1MA) && checkFaisability(sol1OS,sol1MA)) {
			 	oSPop.set(i, sol1OS);
			 	mAPop.set(i, sol1MA);
	 		}
		 	else {
		 		oSPop.remove(i);
		 		mAPop.remove(i);
		 	}
 		}
	 		
	 		System.out.println("crossover down");
	 		
 	}
 	
 	public static void selection() {
 		ArrayList<Tuple> mANull = new ArrayList<Tuple>();
 		mANull.add(new Tuple(-1,-1));
 		int taille = oSPop.size();
 		TupleFunObj[] tab = new TupleFunObj[taille];
 		for(int i=0;i<oSPop.size();i++)
 			tab[i] = new TupleFunObj(objFunction(oSPop.get(i),mAPop.get(i)),i);
 		Arrays.sort(tab, Collections.reverseOrder(new TupleFunObjComparator()));
 		for(int i=0;i<(tab.length/2);i++)
	 		mAPop.set(tab[i].getplace(),mANull);
 		for(int i=0;i<oSPop.size();i++) {
			if(mAPop.get(i).size() == 1) {
				mAPop.remove(i);
				oSPop.remove(i);
				i--;
			}
		}
 	}
 	
 	public static void initSolution() {
 		while(nbJobEnded < jobs) {
 			selectOpToDo();
 			//System.out.println("Opération à faire : " + listOpToDo.toString() );
 			doOp();
 			//System.out.println("Opérations restantes : " + listOpToDo.toString());
 		}
 		doOp();
		oSPop.add(oS);
		mAPop.add(mA);

		System.out.println("os init" + oS.toString());
		System.out.println("ma init" + mA.toString());
 	}
 	
 	private static void selectOpToDo() {
 		Operation operation = null;
 		
 		for(int i=0;i<tabJobs.length;i++) {
 			operation = tabJobs[i].getOperation();
 			if(operation != null) {
	 			if(!containsAJobOp(i)) {
	 				listOpToDo.add(operation);
	 				tabJobs[i].popOperation();
	 				if(tabJobs[i].getOperation() == null){
	 					nbJobEnded++;
	 				}
	 			}
 			}
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
 			System.out.print(op+", ");
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
 			if(tabDates[numJob] >= tabMachines[ma.get(indexMa).getNomMachine()-1]) {
 				tabDates[numJob] += opTime;
 				tabMachines[ma.get(indexMa).getNomMachine()-1] = tabDates[numJob];
 			}
 			else {
 				tabMachines[ma.get(indexMa).getNomMachine()-1] += opTime;
 				tabDates[numJob] = tabMachines[ma.get(indexMa).getNomMachine()-1];
 			}
 		}
	 	Arrays.sort(tabDates);
	 	time = tabDates[jobs-1];
 		return time ;
 	}
 	
 	private static int getIndexMa(Operation op) {
 		String name = op.getNameOperation();
 		int numOp = Integer.parseInt(name.substring(1, 2));
 		int numJob = Integer.parseInt(name.substring(3, 4));
 		int indice = 0;
 		
 		for(int i=0;i!=numJob;i++)
 				indice += tabJobs[i].getOperationsTotales().size();
 		indice += numOp;
 		return indice;
 	}
 	
 	/*private static void refreshMUtime(int time) {
 		for(int i=0;i<machinesUsed.length;i++) {
 			machinesUsed[i] -= time;
 		}
 	}*/


	public static void readFile(String file) {
		Scanner sc = null;
		String sentence = null;
		String[] splited;
            try {
                sc = new Scanner(new File(file));
                
                if (sc.hasNextLine()) {
                	sentence = sc.nextLine();
                	splited = sentence.split("\\s+");
                	jobs = Integer.parseInt(splited[0]);
                	machines = Integer.parseInt(splited[1]);
                	avgMachine = Integer.parseInt(splited[2]);
                    
                    tabJobs = new Job[jobs];

             		createListMachines();
                }
                    
                for(int i = 0;i<jobs;i++) {
                	
                    if(sc.hasNextLine()) {
                    	sentence = sc.nextLine();
                    	tabJobs[i] = new Job(sentence,i);
                    	for(int j=0;j<tabJobs[i].getOperationsTotales().size();j++) {
                    		oS.add(null);
                    		mA.add(null);
                    	}
                    }
                }
            } catch (FileNotFoundException e) {
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
	
	private static int giveFinalSolution() {
		int[] tab = new int[mAPop.size()];
		int indice = 0;
		int mini = 1000;
		for(int i=0;i<mAPop.size();i++) {
			tab[i] = objFunction(oSPop.get(i),mAPop.get(i));
		}
		for(int i=0;i<tab.length;i++) {
			if(tab[i] < mini) {
				mini = tab[i];
				indice = i;
			}
		}
		
		return indice;
	}
	
	
	private static void createListMachines(){
		System.out.println("creation des listes pour chaque machine");
		machinesUsed.clear();
		for(int i=0;i<machines;i++) 
			machinesUsed.add(0);
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
