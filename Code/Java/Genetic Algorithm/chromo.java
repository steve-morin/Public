
import java.util.*;
import java.lang.Math;

public class chromo
{

   private  int chromoLength; // chromosome length
   private float myEval; // chromosome evaluation
   private float myModEval; // chromosome modified evaluation (fitness)
   public gene genePool[]; // pointer to original gene pool vector
   public gene myGenePool[]; // private gene pool vector
   private Random randNum = new Random();   
   private static int RAND_MAX = 1000;
   private static int fixedPos = 0;
   private static int fixedPosSeq = 1;

  // Keeping track of used genes during crossover.
  private int isUsed(gene gene, gene usedgene[], int noOfUsedgenes)
  {
    for (int i = 0; i < noOfUsedgenes; i++)
       if (usedgene[i] == gene)
	 return 1;
    return 0;
  }

  // Performs crossover using parent1 and parent2 as templates.
  // This chromosome is assigned the result.
  private void crossover(float crossoverType, chromo parent1, chromo parent2)
  {
   int i, j, k, l;
   int curgene = 0;
   gene usedgene[] = new gene[chromoLength];


   usedgene[0] = (myGenePool[0] = parent1.myGenePool[0]);

   usedgene[chromoLength - 1] = (myGenePool[chromoLength - 1] =
				 parent1.myGenePool[chromoLength - 1]);
   if (crossoverType == fixedPos)
   {
      j = 1;
      for (i = 1; i < (chromoLength - 1); i++)
      {
        if (( Math.abs(randNum.nextInt()) % 2)==1)
        {
          usedgene[j] = (myGenePool[i] = parent1.myGenePool[i]);
          j++;
        }
	   else 
             myGenePool[i] = null;
      }
   } else // crossoverType == fixedPosSeq
     {
        int left, right;
        gene tmpgene;
      
        left = ((Math.abs(randNum.nextInt()) % (chromoLength - 3)) + 1);
        right = ((Math.abs(randNum.nextInt()) % (chromoLength - (2 + left))) + left + 1);
      
        j = 1;
        for (i = 1; i < (chromoLength - 1); i++)
        {
	   if (i >= left && i <= right)
	   {
	      usedgene[j] = (myGenePool[i] = parent1.myGenePool[i]);
	      j++;
	   }
  	   else
  	      myGenePool[i] = null;

        }
     }
   
     curgene = 0;
     for (k = 0; k < chromoLength; k++)
     {
        if (myGenePool[k] == null)
        {
	   for (l = curgene; l < chromoLength; l++)
	   {
	      if ((isUsed(parent2.myGenePool[l], usedgene, j))==0)
	      {
	         myGenePool[k] = parent2.myGenePool[l];
	         curgene = (l + 1);
	         break;
	      }
      	   }
        }
     }

     myEval =(float) -1.0;
  }

  // Performs mutation on this chromosome.
  private void mutate()
  {
     int left, right, i, s;
     gene tmpgene;
   
     left = (( Math.abs(randNum.nextInt()) % (chromoLength - 3)) + 1);
     right = (( Math.abs(randNum.nextInt()) % (chromoLength - (2 + left))) + left + 1);
   
     for (i = left; i <= right; i++)
     {
        tmpgene = myGenePool[i];
        s = left + Math.abs(randNum.nextInt()) % (right - left);
        myGenePool[i] = myGenePool[s];
        myGenePool[s] = tmpgene;
     }
     myEval = (float)-1.0;
  }

  // Modified random generator.
  private int myRand()
  {
     return (Math.abs(randNum.nextInt() % (chromoLength - 2)) + 1);
  } 

  public chromo(int _chromoLength, gene _genePool[])
  {
     chromoLength = _chromoLength;
     genePool = _genePool;
     myGenePool = new gene [chromoLength];
     myEval = (float)-1.0;
     myModEval = (float)-1.0;
//for(int i=0; i <chromoLength;i++){
//  System.out.println("gene"+i);
//  genePool[i].printgene();
//}
  }

  // chromosome initialization.
  public void init()
  {
     int i, j;
   
     for (i = 1; i < (chromoLength - 1); i++)
        myGenePool[i] = null;
     myGenePool[0] = genePool[0];
     myGenePool[chromoLength - 1] = genePool[chromoLength - 1];

     for (i = 1; i < (chromoLength - 1); i++)
     {
        while (myGenePool[(j = myRand())] != null)
  	 continue;
        myGenePool[j] = genePool[i];
     }
  }

  // Calculates (if necessary) and returns chromosome evaluation.
  public float eval()
  {
     if (myEval == -1.0)
     {
        myEval = (float)0.0;

        for (int i = 0; i < (chromoLength - 1); i++){
//System.out.println("["+i+"]"+myGenePool[i]);
	   myEval += myGenePool[i].distanceTo(myGenePool[i + 1]);
        }
     }
     return myEval;
  }

  // Mates this chromosome with partner resulting in two new offsprings, child1 and child2.
  public void mate(float _crossover, float crossoverType,
  		    float _mutation, chromo partner,
		    chromo child1, chromo child2)
  {

     if ( Math.abs(randNum.nextInt()) <= (_crossover * RAND_MAX))
        child1.crossover(crossoverType, this, partner);
     else
        child1 = this;
     if ( Math.abs (randNum.nextInt()) <= (_mutation * RAND_MAX))
        child1.mutate();
   
     if ( Math.abs(randNum.nextInt()) <= (_crossover * RAND_MAX))
        child2.crossover(crossoverType, partner, this);
     else
        child2 = partner;
   
     if ( Math.abs(randNum.nextInt()) <= (_mutation * RAND_MAX))
        child2.mutate();
  }


   // Sets chromosome's modified evaluation (fitness).
   public float setModEval(float _modEval) {return (myModEval = _modEval);}

   // Returns chromosome's modified evaluation (fitness).
   public float getModEval() {return myModEval;}

   // Returns chromosome's evaluation (fitness).
   public float getEval() {return myEval;}

   // Returns chromosome length.
   public int getLength() {return chromoLength;}


  // Prints chromosome information.
  public void printChromo()
  {
     for (int i = 0; i < chromoLength; i++){
        System.out.println("["+i+"]: ");
        myGenePool[i].printgene();
     }
     System.out.println("Distance:   "+myEval);

  }

  // Copy method.
  public void copyIt(chromo original)
  {
//     if (this == original)
//        return this;

     chromoLength = original.chromoLength;
     myGenePool = new gene [chromoLength];
     for (int i = 0; i < chromoLength; i++)
        myGenePool[i] = original.myGenePool[i];
     myEval = original.myEval;
  }
  
}


/*

public:
   
   // Copy constructor.
   chromo& chromo::operator=(chromo& original);


// Copy constructor.
chromo& chromo::operator=(chromo& original)
{
   if (this == &original)
      return *this;
   delete [] myGenePool;
   chromoLength = original.chromoLength;
   myGenePool = new gene *[chromoLength];
   for (int i = 0; i < chromoLength; i++)
      myGenePool[i] = original.myGenePool[i];
   myEval = original.myEval;
}
*/

