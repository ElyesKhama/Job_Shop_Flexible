package first;
import java.util.ArrayList;

public class Job {
	private int numJob;
	private String sentence = null;
	private int nbOperations = 0;
	private int cmptOpFinished = 0;
	private ArrayList<Operation> operationsRestantes = new ArrayList<Operation>();   
	private ArrayList<Operation> operationsTotales = new ArrayList<Operation>();
	
	public Job(String sentence, int numJob) {
		this.numJob = numJob;
		this.sentence = sentence;
		for(int i=0;i<10;i++) {
		}
		initJob();
	}
	
	public int testDizaine(int dizaine, int unite) {
		int ret = dizaine;
		if(unite != -1)
			ret = dizaine*10 + unite;
		return ret;
	}
	
	public void initJob() {
		int compteurOp = 0;
		this.nbOperations = testDizaine(Character.getNumericValue(this.sentence.charAt(0)),Character.getNumericValue(this.sentence.charAt(1)));

		for(int i=4;i<this.sentence.length();i+=12) {
			int machinesNeeded = Character.getNumericValue(this.sentence.charAt(i));
			//System.out.println("MachinesNeeded : "+ machinesNeeded);
			if(machinesNeeded == 1) {
				int nameMachine = testDizaine(Character.getNumericValue(this.sentence.charAt(i+4)),Character.getNumericValue(this.sentence.charAt(i+5)));
				//System.out.println("NameMachine : "+ nameMachine);
				int timeOperation = testDizaine(Character.getNumericValue(this.sentence.charAt(i+8)),Character.getNumericValue(this.sentence.charAt(i+9)));
				//System.out.println("timeOperation : "+ timeOperation);
				String name = "o"+Integer.toString(compteurOp)+"-"+Integer.toString(numJob);
				compteurOp++;
				operationsRestantes.add( new Operation(name,machinesNeeded,nameMachine,timeOperation,numJob) );
				operationsTotales.add( new Operation(name,machinesNeeded,nameMachine,timeOperation,numJob) );
			}
			else {
				int[] nameMachine = new int[machinesNeeded];
				int[] timeOperation = new int[machinesNeeded];
				int f = 0;
				for(int j=0;j<machinesNeeded;j++) {
					// Attention
					f+=4;
					nameMachine[j] = testDizaine(Character.getNumericValue(this.sentence.charAt(i+f)),Character.getNumericValue(this.sentence.charAt(i+f+1)));
					//System.out.print("NameMachine : "+ nameMachine[j]);
					f+=4;
					timeOperation[j] = testDizaine(Character.getNumericValue(this.sentence.charAt(i+f)),Character.getNumericValue(this.sentence.charAt(i+f+1)));
					//System.out.println(", timeOperation : "+ timeOperation[j]);
				}
				i += (machinesNeeded-1)*8;
				String name = "o"+Integer.toString(compteurOp)+"-"+Integer.toString(numJob);
				compteurOp++;
				operationsRestantes.add( new Operation(name,machinesNeeded,nameMachine,timeOperation,numJob));
				operationsTotales.add( new Operation(name,machinesNeeded,nameMachine,timeOperation,numJob) );
			}
		}
		System.out.println(this.operationsRestantes.toString());
		
	}
	
	public String toString() { 
		String ret = "";
	    for (Operation ope : operationsRestantes) {
	    	ret += ope.toString();
	    }
	    return ret;
	}

	public int getNbOperations() {
		return this.nbOperations;
	}

	public ArrayList<Operation> getOperationsRestantes(){
		return this.operationsRestantes;	
	}
	
	public ArrayList<Operation> getOperationsTotales(){
		return this.operationsTotales;
	}

	public Operation getOperation() {
		Operation operation;
		if(!operationsRestantes.isEmpty())
			operation = operationsRestantes.get(0);
		else
			operation = null;
		return operation;
	}
	
	public void popOperation() {
		operationsRestantes.remove(0);
	}
	
	
}
