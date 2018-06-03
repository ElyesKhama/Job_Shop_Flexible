package first;


public class TupleFunObj { 
	public final int valeurFunctionObj; 
	public final int place; 

	public TupleFunObj(int x, int y) { 
		this.valeurFunctionObj = x; 
		this.place = y;
	} 
 

	public int getvaleurFunctionObj(){
		return valeurFunctionObj;	
	}
	
	public int getplace(){
		return place;
	}
	
	public String toString() {
		  String ret = "";
		  
		  ret +=  this.valeurFunctionObj + "|" + this.place;
		  
		  return ret;
		}
}