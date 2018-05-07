import java.util.ArrayList;

public class Job {
	private int numJob;
	private int compteurOp = 0;
	private String sentence = null;
	private int nbOperations = 0;
	private ArrayList<Operation> listOperations = new ArrayList<Operation>();
	private int compteur = 0;
	
	public Job(String sentence, int numJob) {
		this.numJob = numJob;
		this.sentence = sentence;
		for(int i=0;i<10;i++) {
		}
		initJob();
	}
	
	public void initJob() {
		
		this.nbOperations = Character.getNumericValue(this.sentence.charAt(0));
		for(int i=4;i<this.sentence.length();i+=12) {
			int machinesNeeded = Character.getNumericValue(this.sentence.charAt(i));
			if(machinesNeeded == 1) {
				int nameMachine = Character.getNumericValue(this.sentence.charAt(i+4));
				int timeOperation = Character.getNumericValue(this.sentence.charAt(i+8));
				String name = "o"+Integer.toString(compteurOp)+"-"+Integer.toString(numJob);
				compteurOp++;
				listOperations.add( new Operation(name,machinesNeeded,nameMachine,timeOperation) );
			}
			else {
				int[] nameMachine = new int[machinesNeeded];
				int[] timeOperation = new int[machinesNeeded];

				for(int j=1;j<=machinesNeeded;j++) {
					// Attention 
					nameMachine[j-1] = Character.getNumericValue(this.sentence.charAt(i+((j*4))));
					timeOperation[j-1] = Character.getNumericValue(this.sentence.charAt(i+(j*8)));
				}
				i += ((machinesNeeded*4));
				String name = "o"+Integer.toString(compteurOp);
				compteurOp++;
				listOperations.add( new Operation(name,machinesNeeded,nameMachine,timeOperation));
			}
		}
		System.out.println(this.listOperations.toString());
		
	}
	
	public String toString() { 
		String ret = "";
	    for (Operation ope : listOperations) {
	    	ret += ope.toString();
	    }
	    return ret;
	}

	public int getCompteur(){
		return compteur;
	}

	public ArrayList<Operation> getListOperations(){
		return listOperations;	
	}
	
}
