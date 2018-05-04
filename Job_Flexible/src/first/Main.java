//import org.graphstream.graph.*;
//import org.graphstream.graph.implementations.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

	private static int jobs = 0;   //nb de jobs
	private static int machines = 0;  //nb de machines
	private static int avgMachine = 0;
	private static Job[] tabJobs;
	private static ArrayList<ArrayList<Operation>> tabMachines;
	
 	public static void main(String[] args) {
		System.out.println("Chargement du fichier.................");
		readFile("example1.txt");
		System.out.println("Fichier chargé !");
		giveSolution();
		createListMachines();
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
	}

	//TODO: Vérifier qu'on ne crée pas de circuit dans le graphe (avec les machines et contrainte de précédence)
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
			System.out.println("compteur job"+i+" : "+compteurJob);		
			listOperations = tabJobs[i].getListOperations();			//recuperation de la liste op
			System.out.println("liste opération job"+i+" :"+tabJobs[i].getListOperations().toString());
			operationCurrent = listOperations.get(compteurJob);			//recherche de la compteur-ième operation dans la liste du job
			machinesNeededOperationCurrent = operationCurrent.getMachinesNeeded(); //recup du nombre de machines pour cette operation
			machineTimeOperationCurrent = operationCurrent.getMachineTime();	//recup de sa/ses machines pour l'operation
			int a;			
			for(a=0;a<machinesNeededOperationCurrent;a++){
				//tabMachines.get(i).add(machineTimeOperationCurrent[a].getNomMachine());   //TODO: A continuer ici : add dans la liste des machines
			}
			System.out.println(tabJobs[i].toString());
			
		}	
	}

	
/*	private static void Tutorial1() {
		
			Graph graph = new SingleGraph("Tutorial 1");
			graph.addNode("A" );
			graph.addNode("B" );
			graph.addNode("C" );
			graph.addEdge("AB", "A", "B");
			graph.addEdge("BC", "B", "C");
			graph.addEdge("CA", "C", "A");
			graph.display();
			
		
	}  */
	
}
