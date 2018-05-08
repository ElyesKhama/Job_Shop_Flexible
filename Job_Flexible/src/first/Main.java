//package first;

/*import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*; */
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
	private static ArrayList<ArrayList<Operation>> tabMachines;
	private static int nbJobEnded = 0;
	private static ArrayList<String> solution = new ArrayList<String>();
	private static ArrayList<Integer> operationSelection = new ArrayList<Integer>();
	private static ArrayList<Integer> machineAssignment = new ArrayList<Integer>();
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("example2.txt");
		//tutorial1();
		System.out.println("Fichier chargé !");
		createListMachines();	
		printTabJobs();
		while(nbJobEnded < machines){  //Tant que tous les jobs ne sont pas terminés, on continue
			giveSolution();
			printTabJobs();
		}
		Iterator<String> it;
		it = solution.iterator();
				while(it.hasNext()){   
					System.out.println(it.next());	
				}
	}

//TODO: Pour chaque machine, une liste d'opérations pouvant(devant) s'effectuer dessus

	public static void readFile(String file) {
		Scanner sc = null;
		String sentence = null;
            try {
                sc = new Scanner(new File(file));
                
                if (sc.hasNextLine()) {
                    //for (char c : sc.nextLine())
                	sentence = sc.nextLine();
                	jobs = Integer.parseInt(sentence.substring(0,1));
                	machines = Integer.parseInt(sentence.substring(4,5));
                	avgMachine = Integer.parseInt(sentence.substring(8,9));
                    
                    tabJobs = new Job[jobs];
                }
                    
                for(int i = 0;i<jobs;i++) {
                    if(sc.hasNextLine()) {
                    	sentence = sc.nextLine();
                    	tabJobs[i] = new Job(sentence,i);
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
	
	public static void createListMachines(){
		System.out.println("creation des listes pour chaque machine");  //liste d'operations (initialement vides) pour chaque machines
		tabMachines = new ArrayList<ArrayList<Operation>>();
		int i;
		for(i=0;i<machines;i++){
					ArrayList<Operation> listOperation = new ArrayList<Operation>();
					tabMachines.add(listOperation);
		}
		System.out.println("J'ai crée ma liste de "+machines+" machines ");
		System.out.println(tabMachines.toString());
	}

	public static void selectBestJob(){
		int i,j;
		Iterator<Operation> it,it2;	
		int current;
		int elemRemove,elemToRemove;
		for(i = 0;i<machines;i++){   //pour chaque machine
			if(!tabMachines.get(i).isEmpty()){
				current = 99;
				elemRemove = 0;
				elemToRemove = 0;
				it = tabMachines.get(i).iterator();
				int time = 0;
				while(it.hasNext()){
					Operation itnext = it.next();
					int k;		
					for(k = 0;k<itnext.getMachinesNeeded();k++){   //boucle qui sert a verifier qu'on va bien recuperer le bon temps d'exec de cette machine et pas d'une autre (pour la même opération)
						if(i == itnext.getMachineTime()[k].getNomMachine()-1 ){
										time = itnext.getMachineTime()[k].getTimeOperation();
						}
 					}
					if(time < current){				//pour prendre le plus petit
						current = time;
						elemToRemove = elemRemove;
					}
					elemRemove++;
				}
				int numJob = tabMachines.get(i).get(elemToRemove).getNumJob();
				tabJobs[numJob].updateCompteur();			// Augmenter le compteur du job et mettre a jour la liste des opérations restantes
				int a;				
				for(a=i+1;a<machines;a++){															//si yen a plusieurs jobs pour la meme machine on l'enleve dans celle des ordres
					if (tabMachines.get(a).contains(tabMachines.get(i).get(elemToRemove))){
						tabMachines.get(a).remove(tabMachines.get(i).get(elemToRemove));
					}								
				}   
				System.out.println(tabMachines.get(i).get(elemToRemove));
				tabMachines.get(i).remove(elemToRemove);   //Une opération a été enlevé ici, la rajouter dans la solution
				int numMachine = i+1;
				int numJobDisplay = numJob+1;  //pour l'affichage un ajoute 1 comme on part de 0 ici mais pas dans le fichier
				solution.add("Opération n° "+tabJobs[numJob].getCompteur()+ " du job n° "+ numJobDisplay + " sur la machine "+numMachine);
				System.out.println("Opération n° "+tabJobs[numJob].getCompteur()+ " du job n° "+ numJobDisplay + " sur la machine "+numMachine);
				if(tabJobs[numJob].getListOperations().isEmpty()){
					nbJobEnded++;
					System.out.println("Job terminé ...........");
				}
				System.out.println(tabMachines.toString());
				
			}
		}	
	}
	
	public static void printTabJobs(){
		int i;
		for (i=0;i<jobs;i++){
			System.out.println("JOBS COMPTEUR \n"+ tabJobs[i].getCompteur());
		}
	}

	//TODO: Vérifier qu'on ne crée pas de circuit dans le graphe (avec les machines et contrainte de précédence)  ???
	public static void giveSolution(){
		System.out.println("Solution en cours de traitement.......");
		int i;
		int compteurJob;
		ArrayList<Operation> listOperations;
		Operation operationCurrent;
		int machinesNeededOperationCurrent;
		Tuple[] machineTimeOperationCurrent;  //TODO : modifier "machineTime" --> pas compréhensible
		int timeOperationCurrent;
		for(i=0;i<jobs;i++){		//pour chaque job
			compteurJob = tabJobs[i].getCompteur();						//recuperation du compteur
			//System.out.println("compteur job"+i+" : "+compteurJob);		
			listOperations = tabJobs[i].getListOperations();			//recuperation de la liste op
			System.out.println("liste opération job"+i+" :"+tabJobs[i].getListOperations().toString());
			if(!listOperations.isEmpty()){
				operationCurrent = listOperations.get(0);			//recherche de la compteur-ième operation dans la liste du job
				machinesNeededOperationCurrent = operationCurrent.getMachinesNeeded(); //recup du nombre de machines pour cette operation
				machineTimeOperationCurrent = operationCurrent.getMachineTime();	//recup de sa/ses machines pour l'operation
				int a;			
				for(a=0;a<machinesNeededOperationCurrent;a++){
					System.out.println("la/les machines needed sont :"+ machineTimeOperationCurrent[a].toString());
						int k;
						for(k=0;k<machines+1;k++){
							if(k == machineTimeOperationCurrent[a].getNomMachine()){
								if(!tabMachines.get(k-1).contains(operationCurrent)){
									tabMachines.get(k-1).add(operationCurrent);  //add dans la liste des machines
								}
							}
						}																			//TODO: Recuperer les temps et les comparer pour effectuer la 1ere heuristique
				}
			}
		}
		System.out.println(tabMachines.toString());
		selectBestJob();
		System.out.println("Solution terminé .......");
	}

	
/*	private static void tutorial1() {
		ArrayList<Operation> listOperations;
		Graph graph = new SingleGraph("Problème visualisé");
		graph.setAttribute("ui.label", true);
		for(int i=0;i<tabJobs.length;i++) {
			listOperations = tabJobs[i].getListOperations();
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
	*/
}
