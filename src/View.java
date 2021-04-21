// The View class creates and manages the GUI for the application.
// It doesn't know anything about the game itself, it just displays
// the current state of the Model, and handles user input

// We import lots of JavaFX libraries (we may not use them all, but it
// saves us having to thinkabout them if we add new code)
//import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View 
{ 
	
	// variables for components of the user interface
    public int width;       // width of window
    public int height;      // height of window

    // user interface objects
    public Pane pane;       // basic layout pane
    public Canvas canvas;   // canvas to draw game on
    public Label infoText;  // info at top of screen

    // The other parts of the model-view-controller setup
    public Controller controller;
    public Model model;

    public GameObj   bat;            // The bat
    public GameObj   ball;           // The ball
    public GameObj[] bricks;         // The bricks
    public PersistentGameObj[] healthbricks; //Bricks with health
//    public ArrayList<GameObj> albricks = new ArrayList<GameObj>();
//    public ArrayList<PersistentGameObj> alhealthbricks;
    public int score =  0;     // The score
   
    // constructor method - we get told the width and height of the window
    public View(int w, int h)
    {
        Debug.trace("View::<constructor>");
        width = w;
        height = h;
    }

    // start is called from the Main class, to start the GUI up
    
    public View() {
		
	}

	public void start(Stage window) 
    {
        // breakout is basically one big drawing canvas, and all the objects are
        // drawn on it as rectangles, except for the text at the top - this
        // is a label which sits 'in front of' the canvas.
        // Note that it is important to create control objects (Pane, Label,Canvas etc) 
        // here not in the constructor (or as initialisations to instance variables),
        // to make sure everything is initialised in the right order
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed
        
        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane
        
        // infoText box for the score - a label which we position in front of
        // the canvas (by adding it to the pane after the canvas)
        infoText = new Label("BreakOut: Score = " + score);
        infoText.setTranslateX(50);  // these commands setthe position of the text box
        infoText.setTranslateY(10);  // (measuring from the top left corner)
        pane.getChildren().add(infoText);  // add label to the pane

        // Make a new JavaFX Scene, containing the complete GUI
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file
        // Add an event handler for key presses. By using 'this' (which means 'this 
        // view object itself') we tell JavaFX to call the 'handle' method (below)
        // whenever a key is pressed
        
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
        		
        	public void handle(KeyEvent event) {  //attempting to get the bat to follow the mouse instead of using keys to control direction.
				  // send the event to the controller
				  controller.userKeyInteraction(event);
				 
				}
        		
        	});
        
        scene.setOnMouseMoved(new EventHandler<javafx.scene.input.MouseEvent>() { //this was one of the hardest things I have EVER had to do!
        	
			public void handle(javafx.scene.input.MouseEvent e) {  //attempting to get the bat to follow the mouse instead of using keys to control direction.
				  // send the event to the controller
				  controller.userMouseInteraction(e);
				 
				}

			});
        scene.setCursor(Cursor.NONE);
        
        // put the scene in the window and display it
        window.setScene(scene);
        window.show();
    }
  
    
    // drawing the game image
    public void drawPicture()
    {
        // the game loop is running 'in the background' so we have
        // add the following line to make sure it doesn't change
        // the model in the middle of us updating the image
        synchronized ( model ) 
        {
            // get the 'paint brush' to pdraw on the canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // clear the whole canvas to white
            gc.setFill( Color.WHITE );
            gc.fillRect( 0, 0, width, height );
            
            // draw the bat and ball
            displayGameObj( gc, ball );   // Display the Ball
            displayGameObj( gc, bat  );   // Display the Bat
            
            //draw the bricks
            
            for (GameObj brick : bricks) {
            if (brick.visible) {
            displayGameObj(gc, brick);
           }
            	}
           
            //draws the bricks with health
            
            for (PersistentGameObj healthbrick : healthbricks) {
                if (healthbrick.visible) {
                displayPersistentGameObj(gc, healthbrick);
                }
                	}
            
            //TODO: get this working 
            //draws the arraylist bricks
            
//            for (GameObj brick : albricks) {
//                if (brick.visible) {
//                displayGameObj(gc, brick);
//                }
//                	}

            
            // update the score
            infoText.setText("Score:  " + score);
        }
    }

    // Display a game object - it is just a rectangle on the canvas
    public void displayGameObj( GraphicsContext gc, GameObj go )
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }
    
    public void displayPersistentGameObj( GraphicsContext gc, PersistentGameObj go )
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }

    // This is how the Model talks to the View
    // This method gets called BY THE MODEL, whenever the model changes
    // It has to do whatever is required to update the GUI to show the new game position
    public void update()
    {
        // Get from the model the ball, bat, bricks & score
        ball    = model.getBall();              // Ball
        bricks  = model.getBricks();            // Bricks
        healthbricks = model.getHealthBricks();
        bat     = model.getBat();               // Bat
        score   = model.getScore();             // Score
        //Debug.trace("Update");
        drawPicture();                     // Re draw game
    }

	



}
