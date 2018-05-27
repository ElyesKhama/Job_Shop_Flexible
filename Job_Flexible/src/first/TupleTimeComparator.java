package first;

import java.util.Comparator;

public class TupleTimeComparator implements Comparator<Tuple> {
	
	@Override
    public int compare(Tuple a, Tuple b) {
        return a.timeOperation < b.timeOperation ? -1 : a.timeOperation == b.timeOperation ? 0 : 1;
    }
	
}
