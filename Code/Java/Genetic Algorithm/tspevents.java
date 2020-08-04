import java.awt.*;
import java.awt.event.*;

public class tspevents extends tspint {

	public  final int boardx = 250;
	public  final int boardy = 100;
	public  final int sizex = 400;
	public  final int sizey = 400;
	public  int popSize = 100,
		              numGens = 200,
					  crossoverType = 1,
					  deletionType = 1,
					  fitnessType = 1,
					  elitismValue = 20,
					  seed = 0;
	public  float crossover =(float) .89, mutation =(float) .25,
                 fitnessParam = (float)1.1;
	public int MAX_CITIES = 100;
	public int chromoLength;
	
	gene[] genePool = new gene[MAX_CITIES];
	pop myPop;
	pop myNewPop;

	int k;

	public tspevents(String title) {
		super (title);
		
			      addMouseListener(new MouseAdapter() { 
          public void mousePressed(MouseEvent me) { 
            System.out.println(me); 
          } 
        }); 

		}

	public boolean handleEvent (Event e) {

		switch (e.id) {

		case Event.WINDOW_DESTROY:
			this.dispose();
			System.exit(0);
			break;

		case Event.ACTION_EVENT:
			// handle the pull down menus first
			if (e.target instanceof MenuItem) {
				if (((String)e.arg).equals("About")) {
                  	return (false);
				} 
				else if (((String)e.arg).equals("Quit")) {
					this.dispose();
					System.exit (0);
				}

			} else if (e.target == start_button) {

				if (start_button.getLabel().equals("    Start    ")) {
					if (city_display.num_cities > 1) {
						start_button.setLabel("     Stop     ");
						advance_button.setEnabled(false);
						reset_button.setEnabled(false);
						city_display.state = 1;
						init_solve();
						continuous_solve(e);		
					}
				} else {
					start_button.setLabel("    Start    ");
					advance_button.setEnabled(true);
					reset_button.setEnabled(true);
					city_display.state = 2;
				}
			} else if (e.target == advance_button) {
				if (city_display.num_cities > 1) {
					if (city_display.state == 0)
						init_solve();
					city_display.state = 1;		
					start_solve();
					city_display.distance = myPop.bestChromo().getEval();
					city_display.generation++;
					city_display.update_data(myPop.bestChromo().myGenePool,chromoLength-1);
				}
			} else if (e.target == reset_button) {

				city_display.reset_board();
			}

			break;


		case Event.MOUSE_DOWN:
			/*
			crossover.setText(""+e.x);
			mutation.setText(""+e.y);
			if ((e.x > boardx) && (e.x < boardx+sizex) && (e.y > boardy) && (e.y < boardy+sizey)) {
				this.getGraphics().setColor(Color.black);
				this.getGraphics().drawRect(e.x,e.y,5,5);
			}
			*/

			break;
		}

		return (super.handleEvent(e));
	}

	public void init_solve() {
		int i, j;

		// Get the info from the input box action
		crossoverType = crossover_type.getSelectedIndex();
		deletionType  = deletion.getSelectedIndex();
		fitnessType = fitness_type.getSelectedIndex();

		crossover = new Double(crossover_text.getText()).floatValue();
		mutation = new Double(mutation_text.getText()).floatValue();
		elitismValue = new Double(elitism.getText()).intValue();


		chromoLength = city_display.num_cities+1;

		for (i=0; i < chromoLength-1; i++)
			genePool[i] = new gene("", city_display.city_list[i].x, city_display.city_list[i].y, i, chromoLength);

		genePool[city_display.num_cities] = new gene("", city_display.city_list[0].x, city_display.city_list[0].y, city_display.num_cities, chromoLength);


		for (i = 0; i < chromoLength; i++)
			for (j = 0; j < chromoLength; j++)
				genePool[i].calculateDistanceTo(genePool[j]);

		myPop = new pop(popSize, chromoLength, genePool);

	}


	public void start_solve() {

		//******************* Main Evolution Loop **********************
		myPop.fitness(fitnessType, fitnessParam);
		System.out.println(k+" "+ myPop.fitnessAverage()+" "+myPop.bestFitness());
		myNewPop = myPop.newPop(fitnessType, fitnessParam, crossover,
								crossoverType, mutation, deletionType,
								elitismValue);

		if (deletionType == 0) {
			myPop = null;
			myPop = myNewPop; 
		}

		// print out the best chromosome
		myPop.bestChromo().printChromo();
	}


	public void continuous_solve(Event e) {
		int i;

		//******************* Main Evolution Loop **********************
		for (i=0; i<1000; i++) {
			//this.currentThread.sleep(1000);
			if (city_display.state == 1) {
				myPop.fitness(fitnessType, fitnessParam);
				myNewPop = myPop.newPop(fitnessType, fitnessParam, crossover,
										crossoverType, mutation, deletionType,
										elitismValue);

				if (deletionType == 0) {
					myPop = null;
					myPop = myNewPop; 
				}

				city_display.distance = myPop.bestChromo().getEval();
				city_display.generation++;
				city_display.update_data(myPop.bestChromo().myGenePool,chromoLength-1);

			} else if (city_display.state == 0) {
				return;
			}
		}
	}

	public static void main(String[] args) {
		Frame f = new tspevents("Evolutionary Algorithm Example");
		

		
		f.pack();
//		f.show();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

	}
}