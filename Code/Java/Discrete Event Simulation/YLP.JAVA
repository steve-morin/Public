/////////////////////////////////////////////////////////////////////////////////
//
//  File: YLP.java
//  
//  Description: This file contains the source for YLP. This is the LP
//               that receives cell events from the processor(p) and 
//               then it destroys those cells(i.e. it sends them to assigned
//               destination).
//
//
////////////////////////////////////////////////////////////////////////////////
import java.lang.Math;
import SimKit.*;
import java.util.*;


class YLP extends SimKit.LP {
       String lpName;
       LP fDestination;                    // the destination LP
       private int fbuffCapacity;          // the buffer capacity assigned to this output port

       // these variables keep track of how many cells of both low and high priority 
       // are lost
       int X1YnumLowLost;
       int X2YnumLowLost;
       int X1YnumHighLost;
       int X2YnumHighLost;
       int X1YnumTrans;
       int X2YnumTrans;

       private double totalTime;                  // the total time cells spent in the system
       private double times[] = new double[2000]; // this holds all times that the cells
                                                  // were in the system

       // buffer declarations, we do not need to know the size beforehand
       Vector HighBuffer = new Vector( fbuffCapacity);
       Vector LowBuffer = new Vector( fbuffCapacity);
       private boolean busy ;                 // keeps track of whether the output port is busy
       private int fNumberCellsTransferred=0; // counts number of cells transferred

       private static final double SERVICE_TIME = 2.74; // time to send out a cell

       // constructor creates an LP with specified name and buffer capacity
       public YLP(String name, int BuffCapacity) {
               lpName = name;
               fbuffCapacity = BuffCapacity;
       }

       // constructor sets the state
       public YLP (boolean b) {
               busy = b;
       }

       // set the destination LP
       public void setDestination(LP  dest) {
               fDestination = dest;
       }

       // initialize the LP for the purposes of the simulation
       public void initialize() {
               busy = false;  
               Simulation.setCurrLP(this);
       }

       // This method processes all incoming events, in the following way:
       // if an event is received from this LP the event is deleted. If 
       // it is from the processor we try to process it immediately, if that is
       // not possible we put it into the buffer until the processor becomes available.
       public void process (Event ev) {

               Cell cellArrived = (Cell) ev;          // cast it as a cell
               if (cellArrived.sourceLP() == this) {  
                       if(cellArrived.source ==1)     // determine source
                          X1YnumTrans++;
                       else
                          X2YnumTrans++;   
                  
                       // record total time
                       totalTime = totalTime+(cellArrived.recvTime() - cellArrived.time_in); 
                       times[fNumberCellsTransferred] = cellArrived.recvTime() - cellArrived.time_in;

                       cellArrived = null;        // the cell is destroyed
                       fNumberCellsTransferred++; // count the number of cells transferred

                       // check the high priority buffer to see if there are any
                       // cells remaining to be processed
                       if (!HighBuffer.isEmpty()){
                           Event newEvent =(Event)HighBuffer.firstElement();
                           double clock = Simulation.currTime();
                           newEvent.send_and_delete(this,clock+SERVICE_TIME);
                           HighBuffer.removeElementAt(0);
                       }
                      // check the low Priority buffer to see if there is 
                      // anything remaining to process
                      else if (!LowBuffer.isEmpty()){
                           Event newEvent =(Event)LowBuffer.firstElement();
                           double clock = Simulation.currTime();
                           newEvent.send_and_delete(this,clock+SERVICE_TIME);
                           LowBuffer.removeElementAt(0);        
                       }
                       else
                          busy = false;  

               }
               else if ( busy == false) {
                       busy = true;
                       Event newEvent =  (Event) cellArrived;
                       // schedule the event to depart
                       newEvent.send_and_delete(this, cellArrived.recvTime() +
                                               SERVICE_TIME);
               }
               else if (HighBuffer.size()+LowBuffer.size() < fbuffCapacity){
                  if(cellArrived.priority == 0){
                     // add the cell to the low priority queue
                     LowBuffer.addElement(cellArrived);
                  } else {
                      // add the cell to the high priority queue
                     HighBuffer.addElement(cellArrived);         
                  }               
               }
               else {   
                  // if it is a low priority cell then we can delete it
                  if(cellArrived.priority == 0){ 
                     if(cellArrived.source == 1)
                           X1YnumLowLost++;
                     else      
                           X2YnumLowLost++;
                     cellArrived = null;
                  }
                  else { 
                     // if the high priority buffer is full then we delete the cell
                     if(HighBuffer.size() == fbuffCapacity){
                     if(cellArrived.source == 1)
                           X1YnumHighLost++;
                     else      
                           X2YnumHighLost++;
                     cellArrived = null;
                     }
                     // otherwise we delete a cell from the low priority queue
                     // and add the high priority cell to the high priority
                     // buffer
                     else {
                     HighBuffer.addElement(cellArrived);          // add to high buffer
                     cellArrived = (Cell)LowBuffer.firstElement();
                     LowBuffer.removeElementAt(0);        // take the cell from low buffer
                     if(cellArrived.source == 1)
                           X1YnumLowLost++;
                     else      
                           X2YnumLowLost++;
                     }
                  }
               }
       }
   // determines if the busy variable is true
   public boolean isBusy()  { return busy; }

   // returns the number of cells transferred
   public int numberCellsTransferred() { return fNumberCellsTransferred; }

   // This method prints out simulation statistics. 
   public void terminate() {
       System.out.println("*******************************************************************************************************");
       System.out.println("\nResults for LP: "+lpName);
       System.out.println("-------------------------------------------------------------------------------------------------------");
       System.out.println("                           "+"\t\t"+"x1"+lpName+"\t\t"+"x2"+lpName+"\t\t"+"Aggregate");
       System.out.println("-------------------------------------------------------------------------------------------------------");
       System.out.println("# Cells Transferred       |"+"\t\t"+X1YnumTrans+"\t\t"+X2YnumTrans+"\t\t"+(X1YnumTrans+X2YnumTrans));
       System.out.println("# Low Priority Cells Lost |"+"\t\t"+X1YnumLowLost+"\t\t"+X2YnumLowLost+"\t\t"+(X1YnumLowLost+X2YnumLowLost));
       System.out.println("#High Priority Cells Lost |"+"\t\t"+X1YnumHighLost+"\t\t"+X2YnumHighLost+"\t\t"+(X1YnumHighLost+X2YnumHighLost));
       System.out.println("-------------------------------------------------------------------------------------------------------");
       double prob1 =  ((double)X1YnumLowLost/(double)X1YnumTrans);
       double prob2 =  ((double)X1YnumHighLost/(double)X1YnumTrans);
       double prob3 =  ((double)X2YnumLowLost/(double)X1YnumTrans);
       double prob4 =  ((double)X2YnumHighLost/(double)X1YnumTrans);
       double prob5 =  (prob1+prob3)/2;
       double prob6 =  (prob2+prob4)/2;
       System.out.println("Prob. of Low  Cell Loss(%)|"+"\t\t"+prob1+"\t\t"+prob3+"\t\t"+prob5);
       System.out.println("Prob. of High Cell Loss(%)|"+"\t\t"+prob2+"\t\t"+prob4+"\t\t"+prob6);
       System.out.println("-------------------------------------------------------------------------------------------------------");
       System.out.println("Total time:             "+totalTime);
       System.out.println("# of Cells Transferred: "+(X1YnumTrans+X2YnumTrans+X1YnumLowLost+X1YnumHighLost+X2YnumLowLost+X2YnumHighLost));
       System.out.println("Mean Time in System: "+ (totalTime/((double)fNumberCellsTransferred)));
       double stddev = StdDev(totalTime/((double)(fNumberCellsTransferred)), fNumberCellsTransferred);
       System.out.println("Standard Deviation : "+stddev);
       System.out.println("*******************************************************************************************************");
   }

   // This method calculates the standard deviation from the mean interarrival times.
   public double StdDev(double mean, int num) {
      double total = 0;      
      double result = 0;   

      for(int i=0; i<num;i++){
         if((times[i]-mean)>0)
            result = (times[i]-mean);
  
         else
            result = (mean - times[i]);
         total = total + result;
      }      
      return(total/num);
   }

}

///////////////////////////////END OF YLP.JAVA///////////////////////////////////////




