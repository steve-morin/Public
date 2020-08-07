//////////////////////////////////////////////////////////////////////////////
//
//  PLP.java
//
//  Description: This file contains the source code for PLP. The PLP is
//               is the logical process that simulates the routing swith in 
//               our model of an ATM switch.
//
//
////////////////////////////////////////////////////////////////////////////

import SimKit.*;
import java.util.*;

class PLP extends SimKit.LP {
       String lpName;
       LP fDestPlayer;
       LP Dest1;
       LP Dest2;
       Vector InputQueue = new Vector(100);  
       private boolean busy ; 
       public int fNumberX1Y1 = 0;
       public int fNumberX1Y2 = 0;
       public int fNumberX2Y1 = 0;
       public int fNumberX2Y2 = 0;

       private static final double SERVICE_TIME = 1.2;

       // constructors
       public PLP (String name) {
               lpName = name;
       }
      
       public PLP (boolean b) {
               busy = b;
       }

       // This method is used to set the LP destinations for the processor.
       // In our case the destination are Y1 and Y2.
       public void setDest1and2(LP  dest1, LP dest2) {
               Dest1 = dest1;
               Dest2 = dest2;
       }

       public void setDestination(LP  dest) {
               fDestPlayer = dest;
       }

       // This method initializes the PLP setting it up for the
       // simulation.
       public void initialize() {
               busy = false;
               Simulation.setCurrLP(this);
       }

       public void process (Event ev) {

               Cell cellArrived = (Cell) ev;
               if (ev.sourceLP() == this) {  
                       if (FromX1(cellArrived)){
                          if(FromY1(cellArrived)){ 
                             fNumberX1Y1++;
                             this.setDestination(Dest1);
                             Event newEvent = (Event)cellArrived;
                             newEvent.send_and_delete(Dest1, ev.recvTime());

                          }
                          else {
                             fNumberX1Y2++;
                             this.setDestination(Dest2);
                             Event newEvent = (Event)cellArrived;
                             newEvent.send_and_delete(Dest2, ev.recvTime());

                          }
                       } else{
                          if(FromY1(cellArrived)){ 
                             fNumberX2Y1++;
                             this.setDestination(Dest1);
                             Event newEvent = (Event)cellArrived;
                             newEvent.send_and_delete(Dest1, ev.recvTime());

                          }
                          else{
                              fNumberX2Y2++;
                              this.setDestination(Dest2);
                              Event newEvent = (Event)cellArrived;
                              newEvent.send_and_delete(Dest2, ev.recvTime());

                          }
                       }


                      
                       if (!InputQueue.isEmpty()){

                           Event newEvent =(Event)InputQueue.firstElement();
                           double clock = Simulation.currTime();
                           newEvent.send_and_delete(this,clock+SERVICE_TIME); // here we addeing the service time
                           InputQueue.removeElementAt(0);
                       }
                      else
                        busy = false;  
               }
               else if ( busy == false) {
                       busy = true;
                       Event newEvent = (Event)cellArrived;

                       // schedule the customer to leave
                       newEvent.send_and_delete(this, ev.recvTime() +
                                               SERVICE_TIME);
               }
               else {  //if (InputQueue.isEmpty()){  
                       InputQueue.addElement(cellArrived);

               }
       }

   public boolean isBusy()  { return busy; }

   public int numbercellstransferred() { return (fNumberX1Y1+fNumberX1Y2+fNumberX2Y1+fNumberX2Y1); }

   // determine where the cell came from
   private boolean FromX1(Cell cellArrived){
      if (cellArrived.source == 1)
         return(true);
      else
         return(false);
   }

   // determine where the cell is going to
   private boolean FromY1(Cell cellArrived){
      if (cellArrived.destination == 1)
         return(true);
      else
         return(false);
   }

   // output the simulation results
   public void terminate() {

   }

}

///////////////////////////////END OF PLP.JAVA///////////////////////////////
