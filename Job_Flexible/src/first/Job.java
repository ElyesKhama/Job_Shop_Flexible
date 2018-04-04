package first;

import java.util.ArrayList;

public class Job {
	
	private String sentence = null;
	private int nbOperations = 0;
	private ArrayList<Operation> listOperations = new ArrayList<Operation>();
	
	public Job(String sentence) {
		this.sentence = sentence;
		initJob();
	}
	
	public void initJob() {
		this.nbOperations = Character.getNumericValue(this.sentence.charAt(0));
		for(int i=4;i<this.sentence.length();i+=12) {
			
			int machinesNeeded = Character.getNumericValue(this.sentence.charAt(i));
			if(machinesNeeded == 1) {
				int nameMachine = Character.getNumericValue(this.sentence.charAt(i+4));
				int timeOperation = Character.getNumericValue(this.sentence.charAt(i+8));
				listOperations.add( new Operation(machinesNeeded,nameMachine,timeOperation) );
			}
			else {
				
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
	
}
