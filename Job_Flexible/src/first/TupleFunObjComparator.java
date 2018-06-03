package first;

import java.util.Comparator;

public class TupleFunObjComparator implements Comparator<TupleFunObj> {
	
	@Override
    public int compare(TupleFunObj a, TupleFunObj b) {
        return a.valeurFunctionObj < b.valeurFunctionObj ? -1 : a.valeurFunctionObj == b.valeurFunctionObj ? 0 : 1;
    }
	
}
