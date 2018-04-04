package first;

public class Operation {

	private int machinesNeeded = 0;
	private Tuple[] machineTime= null;
	public Operation() {
		
	}
	
	public Operation(int machinesNeeded, int nameMachine, int timeOperation) {
		this.machinesNeeded = machinesNeeded;
		this.machineTime[0] = new Tuple(nameMachine,timeOperation);
	}
	
	public Operation(int machinesNeeded, int[] nameMachine, int[] timeOperation) {
		this.machinesNeeded = machinesNeeded;
		for(int i=0;i<machinesNeeded;i++) {
			this.machineTime[i] = new Tuple(nameMachine[i],timeOperation[i]);
		}
	}
	
	public String toString() { 
		String ret = "";
		ret += "Nombre de machines dont on a besoin : " + machinesNeeded + machineTime.toString();
		return ret;
	}

}
