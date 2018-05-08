//package first;


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
		this.numJob = numJob;
	}
	
	public Operation(String nameOperation, int machinesNeeded, int[] nameMachine, int[] timeOperation, int numJob) {
		this.nameOperation = nameOperation;		
		this.machinesNeeded = machinesNeeded;
		this.machineTime = new Tuple[machinesNeeded];
		for(int i=0;i<machinesNeeded;i++) {
			this.machineTime[i] = new Tuple(nameMachine[i],timeOperation[i]);
		}
		this.numJob = numJob;
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

}
