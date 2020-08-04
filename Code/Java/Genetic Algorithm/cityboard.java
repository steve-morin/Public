import java.awt.*;


public class cityboard extends Canvas {

	public static final int MAX_CITIES = 100;

	int areax  = 300;
	int areay  = 450;
	int boardx = 300;
	int boardy = 300;

	int generation = 0;
	double distance   = 0.0;

	City city_list[] = new City[MAX_CITIES];
	int num_cities = 0;

	int state = 0;


	public cityboard() {
		this.setBackground (Color.white);
		this.resize(areax, areay);
	}


	public void paint (Graphics g) {
		g.setColor(Color.black);
		g.drawRect(5,5,boardx-10,boardy-10);

		g.drawString ("Number of Cities :", 20,330);
		g.drawString ("Generation # : ", 20, 350);
		g.drawString ("Distance : ", 20, 370);

		g.setColor (Color.red);
		g.drawString (""+num_cities, 150, 330);
		g.drawString (""+generation, 150, 350);
		g.drawString (""+distance, 150, 370);

		g.setColor (Color.blue);
		for (int i=0; i<num_cities; i++)
			g.fillRect(city_list[i].x-2,city_list[i].y-2,5,5);

		// if they've started, make sure we connect the cities
		if (generation > 0) {

			// redraw city connecting lines here...
			g.setColor (Color.red);
			//for (int i=0; i < num_genes; i++)
				//g.drawLine(gene_list[i].coord_x,gene_list[i].coord_y,gene_list[i+1].coord_x,gene_list[i+1].coord_y);
		}

	}

	public void dbr_drawLine(Graphics g, int x1, int y1, int x2, int y2) {
		int x;
		int y;
		int stop_x;
		int new_y;
		int orig_y;
		int i = 0;

		float slope;



		if (x1 < x2) {
			x = x1;
			y = y1;
			orig_y = y;
			stop_x = x2;
			slope = (float)(y1 - y2) / (float)(x2 - x1);
		} else {
			x = x2;
			y = y2;
			orig_y = y;
			stop_x = x1;
			slope = (float)(y2 - y1) / (float)(x1 - x2);
		}

		if (slope > 50) {
			g.drawLine(x1,y1,x2,y2);
			return;
		}

	    // System.out.println("Slope = "+slope + "("+x+","+y+")\n");
	

		//y = 425 - (int) B0.floatValue();
		//for (i=0; (((int) (B0.floatValue() + B1.floatValue() * i) < 355) && (i < 355)); i+=1) {
		//	g.drawLine(x, y, i + 70, 425 - (int) (B0.floatValue() + B1.floatValue() * i));
		//	x = i + 70;
		//	y = 425 - (int) (B0.floatValue() + B1.floatValue() * i);
		while (x < stop_x+1) { 
			new_y = orig_y - (int)((float)i * slope);
			g.drawLine(x,y,x+1,new_y);
			y = new_y;
			x++;
			i++;
		}
	}


    public void update_data(gene[] gene_list, int num_genes) {
		Graphics g = this.getGraphics();

		g.translate(0,0);
		g.setColor(Color.white);
		g.fillRect(6,6,boardx-11,boardy-11);

		g.setColor (Color.blue);
		for (int i=0; i<num_cities; i++)
			g.fillRect(city_list[i].x-2,city_list[i].y-2,5,5);

		g.setColor (Color.white);
		g.fillRect(150,331,175,20);
		g.fillRect(150,351,175,20);
		
		g.setColor (Color.red);
		g.drawString (""+generation, 150, 350);
		g.drawString (""+distance, 150, 370);

		g.setColor (Color.red);
		for (int i=0; i < num_genes; i++) {
			dbr_drawLine(g,gene_list[i].coord_x,gene_list[i].coord_y,gene_list[i+1].coord_x,gene_list[i+1].coord_y);
//			g.drawLine(gene_list[i].coord_x,gene_list[i].coord_y,gene_list[i+1].coord_x,gene_list[i+1].coord_y);
			//System.out.println("("+gene_list[i].coord_x+","+gene_list[i].coord_y+") -> ("+gene_list[i+1].coord_x+","+gene_list[i+1].coord_y+")\n");
		}
	}

	public void reset_board() {

		Graphics g = this.getGraphics();

		g.setColor(Color.white);
		g.fillRect(6,6,boardx-11,boardy-11);

		num_cities = 0;
		generation = 0;
		distance = 0.0;
		state = 0;

		g.setColor (Color.white);
		g.fillRect(150,311,175,20);
		g.fillRect(150,331,175,20);
		g.fillRect(150,351,175,20);
		
		g.setColor (Color.red);
		g.drawString (""+num_cities, 150, 330);
		g.drawString (""+generation, 150, 350);
		g.drawString (""+distance, 150, 370);
	}



	public boolean mouseDown (Event e, int ex, int ey) {

		// first ensure that they are clicking within the city area
		if ((ex < 8) || (ex > boardx-8) || (ey < 8) || (ey > boardy-8) ||
			(num_cities >= MAX_CITIES) || (state != 0))
			return (false);

		Graphics g = this.getGraphics();

		g.setColor(Color.blue);
		g.fillRect(ex-2,ey-2,5,5);

		city_list[num_cities] = new City(ex,ey);
		num_cities++;

		g.setColor (Color.white);
		g.fillRect(150,311,75,20);
		g.setColor (Color.red);
		g.drawString (""+num_cities, 150, 330);

		return (false);
	}
}


class City {

	int x;
	int y;


	public City(int nx, int ny) {
		x = nx;
		y = ny;
	}

}