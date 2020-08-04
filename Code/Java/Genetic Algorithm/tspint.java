
import java.awt.*;

public class tspint extends Frame {
	public
    MenuBar menubar;
    Menu file;
	Menu options;
	Menu help;
    Button start_button;
	Button reset_button;
	Button advance_button;
    Choice crossover_type;
	Choice deletion;
	Choice fitness_type;
    CheckboxGroup checkbox_group;
    Checkbox[] checkboxes;
	TextField crossover_text;
	TextField mutation_text;
	TextField fitness_text;
	TextField elitism;
//*	TitleCanvas title_image;
	cityboard city_display;

    FileDialog file_dialog;
    
    Panel panel1;
	Panel panel2;
    Panel button_panel;
	Panel title_panel;

    // The layout manager for each of the containers.
    GridBagLayout gridbag = new GridBagLayout();
    
    public tspint(String title) {
        super(title);

        // Create the menubar.  Tell the frame about it.
        menubar = new MenuBar();
        this.setMenuBar(menubar);
        // Create the file menu.  Add two items to it.  Add to menubar.
        file = new Menu("File");
        file.add(new MenuItem("Start"));
		file.add(new MenuItem("Next"));
		file.add(new MenuItem("Stop"));
		file.addSeparator();
        file.add(new MenuItem("Quit"));
        menubar.add(file);

		//options = new Menu("Options");
		//options.add(new CheckboxMenuItem("Advance"));
		//menubar.add(options);

        // Create Help menu; add an item; add to menubar
        help = new Menu("Help");
        help.add(new MenuItem("About"));
        menubar.add(help);
        // Display the help menu in a special reserved place.
        menubar.setHelpMenu(help);
        
        title_panel = new Panel();
        title_panel.setLayout(gridbag);
//*		title_image = new TitleCanvas();
//*        constrain(title_panel, title_image, 0, 1, 1, 1);

		city_display = new cityboard();

        // Create pushbuttons
        //okay = new Button("Okay");
        //cancel = new Button("Cancel");
        
        
		crossover_text = new TextField("0.8", 5);
		mutation_text  = new TextField("0.25", 5);
		fitness_text   = new TextField("1.1", 5);
		elitism   = new TextField("20", 5);

        crossover_type = new Choice();
        crossover_type.addItem("Fixed Position");
        crossover_type.addItem("Fixed Position Seq");

		deletion = new Choice();
		deletion.addItem("Delete All");
		deletion.addItem("Steady Delete");

		fitness_type = new Choice();
		fitness_type.addItem("Evaluation");
		fitness_type.addItem("Windowing");
		fitness_type.addItem("Linear Form");        

        start_button   = new Button("    Start    ");
		reset_button   = new Button("   Reset  ");
		advance_button = new Button("Advance ");

        // Create a file selection dialog box
        file_dialog = new FileDialog(this, "Open File", FileDialog.LOAD);
        
        // Create a Panel to contain all the components along the
        // left hand side of the window.  Use a GridBagLayout for it.
        panel1 = new Panel();
        panel1.setLayout(gridbag);
       
        // Use several versions of the constrain() convenience method
        // to add components to the panel and to specify their 
        // GridBagConstraints values.
        constrain(panel1, new Label("Crossover:"), 0, 0, 1, 1);
        constrain(panel1, crossover_text, 0, 1, 1, 1);
        constrain(panel1, new Label("Crossover Type:"), 0, 2, 1, 1, 
              5, 0, 0, 0);
        constrain(panel1, crossover_type, 0, 3, 1, 1);
        constrain(panel1, new Label("Deletion Type:"), 0, 4, 1, 1, 
              10, 0, 0, 0);
        constrain(panel1, deletion, 0, 5, 1, 1);
        constrain(panel1, new Label("Fitness:"), 0, 6, 1, 1, 10, 0, 0, 0);
        constrain(panel1, fitness_text, 0, 7, 1, 1);
        constrain(panel1, new Label("Fitness Type:"), 0, 8, 1, 1, 
              5, 0, 0, 0);
        constrain(panel1, fitness_type, 0, 9, 1, 1);
        constrain(panel1, new Label("Mutation:"), 0, 10, 1, 1, 10, 0, 0, 0);
        constrain(panel1, mutation_text, 0, 11, 1, 1);
        constrain(panel1, new Label("Elitism:"), 0, 12, 1, 1, 10, 0, 0, 0);
        constrain(panel1, elitism, 0, 13, 1, 1);
		//constrain(panel1, start_button, 0, 14, 1, 1, 10, 0, 0, 0);
        //constrain(panel1, advance_button, 0, 15, 1, 1, 3, 0, 0, 0);
		//constrain(panel1, reset_button, 0, 16, 1, 1, 15, 0, 0, 0);

		button_panel = new Panel();
		button_panel.setLayout(gridbag);
		constrain (button_panel, start_button, 0,0,1,1,GridBagConstraints.NONE,
					GridBagConstraints.WEST,0.3,0.0,0,2,0,2);
		constrain (button_panel, advance_button, 1,0,1,1,GridBagConstraints.NONE,
					GridBagConstraints.CENTER,0.3,0.0,0,2,0,2);
		constrain (button_panel, reset_button, 2,0,1,1,GridBagConstraints.NONE,
					GridBagConstraints.EAST,0.3,0.0,0,2,0,2);


		panel2 = new Panel();
        panel2.setLayout(gridbag);
        
		constrain (panel2, city_display, 0,0,1,5,GridBagConstraints.BOTH,
				GridBagConstraints.NORTH,1.0,1.0,0,0,0,0);

		constrain (panel2, button_panel, 0,6,1,1,GridBagConstraints.NONE,
					GridBagConstraints.NORTH,0.3,0.0,0,0,0,0);
		//constrain (panel2, advance_button, 0,6,1,1,GridBagConstraints.NONE,
		//			GridBagConstraints.NORTH,0.3,0.0,0,0,0,0);
		//constrain (panel2, reset_button, 0,6,1,1,GridBagConstraints.NONE,
		//			GridBagConstraints.NORTH,0.3,0.0,0,0,0,0);


        // Finally, use a GridBagLayout to arrange the panels themselves
        this.setLayout(gridbag);
        // And add the panels to the toplevel window
        constrain(this, title_panel, 0, 0, 10, 1, GridBagConstraints.BOTH,
              GridBagConstraints.NORTH, 1.0, 0.0, 5, 0, 0, 0);
        constrain(this, panel1, 0, 1, 1, 1, GridBagConstraints.VERTICAL, 
              GridBagConstraints.NORTHWEST, 0.0, 1.0, 10, 10, 5, 5);
        constrain(this, panel2, 1, 1, 1, 1, GridBagConstraints.NONE,
              GridBagConstraints.NORTH, 1.0, 1.0, 10, 10, 5, 30);
		//constrain(this, button_panel,1,1,1,1,GridBagConstraints.BOTH,
		//	  GridBagConstraints.CENTER, 1.0, 1.0, 10, 10, 5, 30);
    }
    
    public void constrain(Container container, Component component, 
                  int grid_x, int grid_y, int grid_width, int grid_height,
                  int fill, int anchor, double weight_x, double weight_y,
                  int top, int left, int bottom, int right)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = grid_x; c.gridy = grid_y;
        c.gridwidth = grid_width; c.gridheight = grid_height;
        c.fill = fill; c.anchor = anchor;
        c.weightx = weight_x; c.weighty = weight_y;
        if (top+bottom+left+right > 0)
            //c.insets = new Insets(top, left, bottom, right);
		c.insets = new Insets(top, left, bottom, 40);
        
        ((GridBagLayout)container.getLayout()).setConstraints(component, c);
        container.add(component);
    }
    
    public void constrain(Container container, Component component, 
                  int grid_x, int grid_y, int grid_width, int grid_height) {
        constrain(container, component, grid_x, grid_y, 
              grid_width, grid_height, GridBagConstraints.NONE, 
              GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
    }
    
    public void constrain(Container container, Component component, 
                  int grid_x, int grid_y, int grid_width, int grid_height,
                  int top, int left, int bottom, int right) {
        constrain(container, component, grid_x, grid_y, 
              grid_width, grid_height, GridBagConstraints.NONE, 
              GridBagConstraints.NORTHWEST, 
              0.0, 0.0, top, left, bottom, right);
    }
 /*   
    public void main(String[] args) {
        Frame f = new tspint("Evolutionary Algorithm");
  		f.pack();
//        f.show(); // remove this
        f.setLocationRelativeTo(null);
        f.setVisible(true);

    }
	*/
}
