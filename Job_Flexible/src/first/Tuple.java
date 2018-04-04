package first;

public class Tuple { 
	  public final int nomMachine; 
	  public final int timeOperation; 
	  
	  public Tuple(int x, int y) { 
	    this.nomMachine = x; 
	    this.timeOperation = y; 
	  } 
	  
	  public String toString() {
		  String ret = "";
		  
		  ret += "Numéro machine : " + this.nomMachine + ", temps d'exécution : " + this.timeOperation;
		  
		  return ret;
	  }
} 