import java.util.*;
import java.lang.Math;

public class gene
{
   private float distance[];       // distance vector
   private int distVecLength; // length of distance vector
   private String name;         // name of gene
   public int coord_x;         // x coordinate
   public int coord_y;         // y coordinate
   private int index;           // gene's index

   // constructor for Gene class
   public gene(String _name, int _coord_x, int _coord_y, int _index,
		int _distVecLength){
     name = _name;
     coord_x = _coord_x ;
     coord_y = _coord_y ;
     index = _index;
     distVecLength = _distVecLength;
     distance = new float[distVecLength];
     for (int i = 0; i < distVecLength; i++)
       distance[i] = (float)-1.0;
   }

  // Calculates Eucledian distance between this gene and
  // _gene and returns the value.
   public void calculateDistanceTo(gene _gene)
   {
     distance[_gene.getIndex()] = (float)
       Math.sqrt(Math.pow((coord_x - _gene.coord_x), 2) + 
       Math.pow((coord_y - _gene.coord_y), 2));
   }

   // Returns this gene's index in original chromosome pool.
   public int getIndex() {return index;}

   // Returns this gene's name.
   public String getName() {return name;}

   // Returns Eucledian distance between this gene and _gene.
   public float distanceTo(gene _gene)
   {
     return distance[_gene.getIndex()];
   }

   // prints information about a gene
   public void printgene(){
     System.out.println("["+index+"]:"+name+" ("+coord_x+","+coord_y+")");
   }


   
}

