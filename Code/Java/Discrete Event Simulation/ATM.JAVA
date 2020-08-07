/////////////////////////////////////////////////////////////////////////////////
//
//  File: ATM.java
//  
//  Description: This file contains the source for the ATM simulation.
//
//
////////////////////////////////////////////////////////////////////////////////

import SimKit.*;

class ATM extends SimKit.Simulation {

       double fEndTime;
       int fBuffCapacity;
       double fInterArrive;
       double fProb;

       public ATM(Arguments args) {

               super(args);
               // get the end time
               String val = args.retrieve("EndTime");
               if ( val != null) {
                       fEndTime = (Double.valueOf(val)).doubleValue();
		      
               }
               else {
                       System.out.println("EndTime not set");
                       System.out.println("Command line input format:");
                       System.exit(-1);
               }
               // get the interarrival time
               val = args.retrieve("InterArrive");
               if ( val != null) {
                       fInterArrive = (Double.valueOf(val)).doubleValue();
               }
               else 
                       fInterArrive = 3.0; 

               // get the prob
               val = args.retrieve("Prob");
               if ( val != null) {
                       fProb = (Double.valueOf(val)).doubleValue();
               }
               else 
                       fProb = 0.5; 

               // get the buffer capacity
               val = args.retrieve("BuffCapacity");
               if ( val != null) {
                       fBuffCapacity = (Integer.valueOf(val)).intValue();
               }
               else 
                       fBuffCapacity = 100; 

               System.out.println("EndTime: "+fEndTime+" BuffCapacity: "+ fBuffCapacity+" Prob: "+fProb+" InterArrive: "+fInterArrive);
       }

       // This is the main method for the ATM simulation. This
       // method starts and runs the simulation.
       public static void main(String[] argv) {
               SimKit.Arguments args = new SimKit.Arguments(argv);
               new ATM(args).run();
       }

       // This method initializes the simulation by declaring instances
       // of the LPs that are needed and then linking those LPs together.
       public void initialize() {
               // set the end time for the simulation
               Simulation.setEndTime(fEndTime);

               // declare instantiations x1 and x2 of type XLP
               XLP x1 = new XLP("x1" );
               XLP x2 = new XLP("x2", fInterArrive, fProb);

               // declare instantiations p of type PLP              
               PLP p  = new PLP("p");

               // declare instantiations y1 and y2 of type YLP
               YLP y1 = new YLP("y1", fBuffCapacity);
               YLP y2 = new YLP("y2", fBuffCapacity);

               // set the destinations for each of the LPs
               x1.setDestination(p);
               x2.setDestination(p);
               y1.setDestination(y1);
               y2.setDestination(y2);
               p.setDest1and2(y1, y2);


       }

       // This method prints out final statistics for the simulation.
       public void terminate() {
               System.out.println("Simulation ended");
               System.out.println("System clock:" +Simulation.currTime());
               Simulation.dbgPrint("Simulation terminated!!!");

       }
}
////////////////////////////END OF ATM.JAVA//////////////////////////////////




