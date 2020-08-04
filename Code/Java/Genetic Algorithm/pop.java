
import java.util.*;

public class pop
{

   private gene genePool[];  // pointer to original gene pool vector
   private int chromoLength; // length of chromosomes in the population
   private float eval;       // sum of all chromosome evaluation values
   private int popSize;      // number of chromosomes in the population
   public chromo myChromoPool[]; // Chromosome vector

   private static int deleteAll = 0;
   private static int evaluation = 0;
   private static int windowing = 1;

   public pop(int _popSize, int _chromoLength, gene _genePool[])
   {
     chromoLength = _chromoLength;
     genePool = _genePool;
     popSize = _popSize;
     eval = 0;
     myChromoPool = new chromo[popSize];
     for (int i = 0; i < popSize; i++)
     {
        myChromoPool[i] = new chromo(chromoLength, genePool);
        myChromoPool[i].init();
     }
   }

   // Population initialization.
   public void init()
   {
     eval = 0;
     for (int i = 0; i < popSize; i++)
        myChromoPool[i].init();
   }

   // Returns a suitable parent using Roulette Wheel Selection.
   public chromo getParent()
   {
     int i, slump;

     Random randNum = new Random();

     slump = Math.abs(randNum.nextInt()) % (int)eval;
     i = 0;
     while ((i < popSize) && (myChromoPool[i].getModEval() < slump))
        i++;
     return myChromoPool[i];
   }

  // Sorts chromosome pool in fitness order.
  private void quickSort(int start, int finish)
  {
     int left, right;
     chromo tmpChromo;
     float startVal;
   
     left = start;
     right = finish;

//System.out.println("pop size"+popSize);
//for(int g=0; g<popSize ;g++){
//System.out.println("chromo number"+g);
//  myChromoPool[g].printChromo();
//}

     startVal = myChromoPool[(start + finish) / 2].eval();
     do
     {
        while (myChromoPool[left].eval() < startVal)
  	   left++;
        while (myChromoPool[right].eval() > startVal)
	   right--;
        if (left <= right)
        {
	   tmpChromo = myChromoPool[left];
	   myChromoPool[left] = myChromoPool[right];
	   myChromoPool[right] = tmpChromo;
	   left++;
	   right--;
        }
     }
     while (left < right);
     if (start < right)
        quickSort(start, right);
     if (left < finish)
        quickSort(left, finish); 

//System.out.println("Done quicksort.");
  }

  // Returns the average chromosome fitness of the population.
  public float fitnessAverage()
  {
     float sum = 0;
     for (int i = 0; i < popSize; i++)
        sum += myChromoPool[i].getEval();
     return (float)(sum / popSize);
  }

   // Returns the best chromosome in the population.
   public chromo bestChromo() {return myChromoPool[0];}

   // Returns the fitness of the best chromosome in the population.
   public float bestFitness() {return myChromoPool[0].getEval();}

   // Returns the modified fitness of the best cromosome in the population.
   public float bestFitnessMod() {return myChromoPool[0].getModEval();}

   // Returns the average modified chromosome fitness of the population.
   public float fitnessAverageMod() {return (eval / popSize);}


  // Returns the modified fitness of the best cromosome in the population.
  public float fitness(int fitnessType, float fitnessParam)
  {
     if (eval == 0)
     {
        int i;
        float tmpEval, minEval, maxEval;
      
        quickSort(0, popSize - 1);
      
        minEval = myChromoPool[0].eval();
        maxEval = myChromoPool[popSize - 1].eval();
        eval = 0;
 
        if (fitnessType == evaluation)
   	   for (i = 0; i < popSize; i++)
	      eval = myChromoPool[i].setModEval(eval + maxEval + minEval -
	  				       myChromoPool[i].eval());
        else if (fitnessType == windowing)
        {
	   float guardedEval;
	 
//	   minEval;
	   for (i = 0; i < popSize; i++)
	   {
	      if ((guardedEval = (maxEval - myChromoPool[i].eval()))
	  	  < fitnessParam)
	         guardedEval = (float)fitnessParam;
	      eval = myChromoPool[i].setModEval(eval + guardedEval);
	   }
        }
      
        else // fitnessType == linearNorm
        {
//  	   float tmpEval;
	 
	   eval = 0;
	   tmpEval = 100;
	   for (i = 0; i < popSize; i++)
	   {
	      if (tmpEval <= 0)
	      {
                 int j;
	         for ( j = i; j < popSize; j++)
		    myChromoPool[j].setModEval(eval);
	         i = j; // break outer loop
	      }
	      else
	      {
	         eval = myChromoPool[i].setModEval(eval + tmpEval);
	         tmpEval -= fitnessParam;
	      }
	   }
        }
     }

     return eval;
  }


  // Creates a new population using this population as a parent pool
  // and returns a pointer to the new population.
  // If deletionType == deleteAll, the user of this class has to
  // delete this population (the new one is really a _new_ one).
  // If deletionType == steadyDelete, the old population is altered
  // and thus, requires no further action by the user.
  public pop newPop(int fitnessType, float fitnessParam,
  		   float crossover, int crossoverType, float mutation,
		   int deletionType, int elitismValue)
  {
     pop nPop;
     if (deletionType == deleteAll)
     {
        int i;
      
        nPop = new pop(popSize, chromoLength, genePool);
//        nPop = new pop();
        for (i = 0; (i < elitismValue) && (i < popSize); i++)
	   nPop.myChromoPool[i].copyIt( myChromoPool[i]);  // copying elite
      
        for (i = elitismValue; i < popSize; i += 2)
	   if (i == (popSize - 1))
	   {
	      chromo tmpChild = new chromo(chromoLength, genePool);
	      getParent().mate(crossover, crossoverType, mutation, 
	  		       getParent(), (nPop.myChromoPool[i]),
			       tmpChild);
	   }
	   else
	      getParent().mate(crossover, crossoverType, mutation,
			       getParent(), (nPop.myChromoPool[i]),
			       (nPop.myChromoPool[i + 1]));   
     }
     else // deletionType == steadyDelete
     {
        int i;      

        chromo tmp1 = new chromo(chromoLength, genePool);
        chromo tmp2 = new chromo(chromoLength, genePool);
        tmp1.init();
        tmp2.init();

        for (i = 0; i < popSize; i += 2)
        {
	   getParent().mate(crossover, crossoverType, mutation,
	  		    getParent(), tmp1, tmp2);

	   if (i != (popSize - 1))
	      myChromoPool[popSize - 2].copyIt( tmp1);
	   myChromoPool[popSize - 1].copyIt( tmp2);
	 
	   eval = 0;
	   fitness(fitnessType, fitnessParam); // resort

        }
        nPop = this;
     }
   
     return nPop;
  }

  // Prints population information to stream.
  public void printPop()
  {
    System.out.println("population size: "+popSize);
    System.out.println("chromosome length: "+chromoLength);
    System.out.println("total evaluation: "+eval);
    System.out.println("chromosome 0: ");

    for (int i = 0; i < 1; i++)
       myChromoPool[i].printChromo();

  }
   
}



