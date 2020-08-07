//////////////////////////////////////////////////////////////////////////
//
//  XLP.java
//  
//  Description: This file contains the source code for XLP. The XLP is
//               is the logical process that generates cell events as 
//               inputs to a model of ATM switch.
//
//
/////////////////////////////////////////////////////////////////////////

import SimKit.*;
import java.util.*;

public class XLP extends SimKit.LP {

       private int count = 0;    
       String lpName;
       private LP destLP;
       private EXP myEXP;
       public   int source;// make this private and create accessor method for it
       private double myProbY1;
       private RandomNumber myRandom = new RandomNumber();
       private static final double PROB_HIGH = 0.80;
       private static final int DEST_Y1 = 1;
       private static final int DEST_Y2 = 2;
       private static final int HIGH = 1;
       private static final int LOW  = 0;

       // This XLP constructor requires three arguments to create an instance of XLP;
       //  1) the name of the object(i.e. "X1")
       //  2) the mean interarrival time in micro seconds(this is used to generate the 
       //     exponentially distributed interarrival times)
       //  3) the probability that the destnation is Y1
       // An alternate constructor is coded below.
       public XLP(String name, double meanInterarrival, double probY1) {
               lpName   = name;
               myEXP    = new EXP(meanInterarrival); 
               myProbY1 = probY1;
               if(name == "x1")
                  source = 1;
               else
                  source = 2;
       }

       // This constructor is called when only the name has been provided as an
       // argument. By default, it sets the mean interarrival time to 3 microseconds
       // and the probability that it's destination is Y1 to 0.60 . Essentially, it 
       // will create the X1 specified in the assignment specifications.
       public XLP(String name) {
               lpName  = name;       // set the lpName
               myEXP   = new EXP(3); // create an instance the random EXP class using 3 msecs
               myProbY1 = 0.60;      // set ProbY1 to 0.60
               source = 1;
       }

       // This method generates the cell's destination based on the probability that the
       // cell will go to destination Y1.
       private int cellDestination() {
               if(myRandom.nextDouble()<myProbY1)
                  return(DEST_Y1);               // destination is Y1
               else
                  return(DEST_Y2);               // destination is Y2
       }


       // This method generates a random exponentially distributes interarrival time 
       // using the EXP class.
       private double interarrivalTime() {
          double a = myEXP.nextEXPDouble();
          if (a< 2.74)
             return(2.74);
          else
             return a;     
       }

       // This method generates a random number between 0 and 1,
       // If that number is less than PROB_HIGH then it returns a priority level of
       // HIGH(integer value 1), otherwise the priority level is LOW(integer value 0)
       private int priorityLevel() {
               if(myRandom.nextDouble()<PROB_HIGH)
                  return(HIGH); 
               else
                  return(LOW);   
       }

       // This method is called to initialize XLP. Pretty lame comment. 
       public void initialize() {
               // set current LP
               Simulation.setCurrLP(this);

               // schedule the first event arrival
               double randNum = interarrivalTime();

               // create a new internal event
               Event ev = new Event();
               ev.send_and_delete(this, randNum); // schedule the first event
       }

       // Set the destination LP. In this case it will usually be the processor.
       public void setDestination(LP dest) {
               destLP = dest;
       }

       public void process (Event ev) {
          if (ev.sourceLP() == this){
               double clock = ev.recvTime();        // get the recvTime of the event
               double randNum = interarrivalTime(); // get random interarrival time
               int dest = cellDestination();        // determine the destination
               int cellPriority = priorityLevel();  // get random priority level
               Cell cellToBeSent = new Cell(cellPriority, dest, this.source);
               cellToBeSent.time_in = clock;
               cellToBeSent.send_and_delete(destLP,clock);
               count++;
               Event newEvent = new Event();
               newEvent.send_and_delete(this, clock + randNum);
               }

//	       String line = Simulation.currTime() + "\t";
//	       line += ((Teller)destLP).numberCustomerIntoBank()+"\t";
//	       line += Simulation.FELToString();
//	       System.out.println(line);
       }
}

////////////////////////////////END OF XLP.JAVA//////////////////////////////////////


