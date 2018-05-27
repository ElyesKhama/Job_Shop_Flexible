package first;

import java.util.Comparator;

public class OperationComparator implements Comparator<Operation> {

	@Override
    public int compare(Operation a, Operation b) {
        return a.getMachineTime()[0].timeOperation < b.getMachineTime()[0].timeOperation ? -1 : a.getMachineTime()[0].timeOperation == b.getMachineTime()[0].timeOperation ? 0 : 1;
    }
	
}