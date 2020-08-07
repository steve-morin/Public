import SimKit.*;


public class Cell extends Object  {
	
	public static int numCells = 0;
	private int cellNum;
	private int finPort;
	private int foutPort;
        private int fpriority;
	
	public boolean wasSent = false;
	public int inPort()  { return (finPort); }
	public int outPort() { return (foutPort); }
	public int priority() { return (fpriority); }

	
	public Cell(int ip, int op, int pr)  {	
		numCells++;  
		cellNum  = numCells;
		finPort  = ip;
		foutPort = op;
                fpriority = pr;

	}
	
	public String toString()  {
		String str = "Cell"+cellNum+"(iop="+finPort+foutPort
			+ fpriority+")";
		return (str);
	}

}  