package first;

import java.util.Arrays;

public class Operation {
	private String nameOperation;
	private int machinesNeeded = 0;
	private Tuple[] machineTime= null;
	private int numJob;	//correspond au job dans laquel est l'operation

	public Operation() {
		
	}
	
	public Operation(String nameOperation, int machinesNeeded, int nameMachine, int timeOperation, int numJob) {
		this.nameOperation = nameOperation;
		this.machinesNeeded = machinesNeeded;
		this.machineTime = new Tuple[machinesNeeded];
		this.machineTime[0] = new Tuple(nameMachine,timeOperation);	
		Arrays.sort(this.machineTime, new TupleTimeComparator());
		//machineTimeToString();
		this.numJob = numJob;
	}
	
	public Operation(String nameOperation, int machinesNeeded, int[] nameMachine, int[] timeOperation, int numJob) {
		this.nameOperation = nameOperation;		
		this.machinesNeeded = machinesNeeded;
		this.machineTime = new Tuple[machinesNeeded];
		for(int i=0;i<machinesNeeded;i++) {
			this.machineTime[i] = new Tuple(nameMachine[i],timeOperation[i]);
		}
		Arrays.sort(this.machineTime, new TupleTimeComparator());
		//machineTimeToString();
		this.numJob = numJob;
	}
	
	public void machineTimeToString() {
		String toDisplay = this.nameOperation + "ok: ";
		for(int i=0;i<this.machineTime.length;i++) {
			toDisplay += this.machineTime[i].toString() ;
		}
		System.out.println(toDisplay);
	}
	
	/*public String toString() {
		String ret = "";
		ret += "Nombre de machines dont on a besoin : " + machinesNeeded;
		for(int i = 0;i<machineTime.length;i++)
			 ret += machineTime[i].toString();
		return ret; 
		//return Integer.toString(machinesNeeded);
	}  */
	
	public String toString() { 
		return nameOperation;
	}

	public Tuple[] getMachineTime(){
		return machineTime;	
	}

	public int getMachinesNeeded(){
		return machinesNeeded;	
	}
	
	public String getNameOperation() {
		return this.nameOperation;
	}

	public int getNumJob(){
		return numJob;
	}
	
	public boolean equals(Operation op) {
		boolean ret = false;
		if(this.nameOperation == op.nameOperation)
			ret = true;
		return ret;
	}
	
	public int getTimeMachine(int nbmachine) {
		int i;
		for(i=0;i<machinesNeeded;i++) {
			if(machineTime[i].getNomMachine() == nbmachine) {
				return machineTime[i].getTimeOperation();
			}
		}
		return 0;
	}

}
