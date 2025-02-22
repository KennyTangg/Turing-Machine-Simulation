// importing libraries
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.*;

public class Simulation extends Application {
    // Attributes for Simulation class
    private final int cell_width = 80;
    private final String accept_state = "q_accept";
    private final String reject_state = "q_reject";
    private final String blank_symbol = "_";
    private final String tape = "111000";
    private final List<String> inputList = new ArrayList<>(Arrays.asList(tape.split("")));
    private int head_position = 0;
    private String current_state = "q0";
    private Polygon triangle;

    public static void main(String[] args){
        // Launch JavaFX Application
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        // Create a Group object to add UI components
        Group root = new Group();
        Scene scene = new Scene(root,1280,720, Color.LAVENDER);
        List<Text> inputTapeNodes = new ArrayList<>();
        Map<String, Map<String,List<String>>> transition = new HashMap<>();

        /* Create a Turing Machine transition with these rules
        Map < "What the current state is ", Map < "the input symbol the tape head read at the current state",
        List < "the next state the current state will go to", "the tape will write this symbol",
        "Move to left or right using 'L' or 'R' " > >
        */

        // Transition Rules for state q0
        Map<String,List<String>> q0 = new HashMap<>();
        q0.put("0", Arrays.asList("q_reject","0","R"));
        q0.put("1", Arrays.asList("q1","X","R"));
        q0.put("Y", Arrays.asList("q3","Y","R"));
        q0.put("_", Arrays.asList("q3","_","L"));

        // Transition Rules for state q1
        Map<String,List<String>> q1 = new HashMap<>();
        q1.put("0", Arrays.asList("q2","Y","L"));
        q1.put("1", Arrays.asList("q1","1","R"));
        q1.put("Y", Arrays.asList("q1","Y","R"));

        // Transition Rules for state q2
        Map<String,List<String>> q2 = new HashMap<>();
        q2.put("1", Arrays.asList("q2","1","L"));
        q2.put("Y", Arrays.asList("q2","Y","L"));
        q2.put("X", Arrays.asList("q0","X","R"));

        // Transition Rules for state q3
        Map<String,List<String>> q3 = new HashMap<>();
        q3.put("Y", Arrays.asList("q3","Y","R"));
        q3.put("_", Arrays.asList("q_accept","_","L"));

        // Add Transition Rules to transition map
        transition.put("q0", q0);
        transition.put("q1", q1);
        transition.put("q2", q2);
        transition.put("q3", q3);

        // Create a Text Object for Title
        Text text = new Text();
        text.setText("Turing Machine Simulation");
        text.setX(340);
        text.setY(65);
        text.setFont(Font.font("Poppins",FontWeight.BOLD, 50));
        text.setFill(Color.GRAY);

        // Create Line Object to give a horizontal line below title
        Line line = new Line();
        line.setStartX(100);
        line.setStartY(100);
        line.setEndX(1180);
        line.setEndY(100);
        line.setStrokeWidth(2);
        line.setStroke(Color.GREY);

        // Create a triangle using the Polygon Object to represent tape head
        triangle = new Polygon();
        triangle.getPoints().setAll(
                25.0, 150.0,
                55.0, 150.0,
                40.0, 175.0);
        triangle.setFill(Color.GREY);
        triangle.setStroke(Color.BLACK);
        triangle.setTranslateX(0);

        // Create a Rectangle Object to create a button
        Rectangle button = new Rectangle();
        button.setX(550);
        button.setY(550);
        button.setHeight(75);
        button.setWidth(175);
        button.setStroke(Color.BLACK);
        button.setStrokeWidth(3);
        button.setFill(Color.LAVENDERBLUSH);
        button.setOpacity(0.5);

        // Create a Text Object to give the rectangle a text "START"
        Text begin = new Text();
        begin.setText("START");
        begin.setX(575);
        begin.setY(600);
        begin.setFont(Font.font("Poppins",FontWeight.BOLD, 40));
        begin.setFill(Color.GRAY);

        // Event when button(rectangle) clicked the method runTuringMachine will run
        button.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Left mouse button clicked on Start button !");
                button.setFill(Color.LIGHTGREEN);
                runTuringMachine(transition, root, inputTapeNodes, begin, button);
            }
        });

        // Event when text inside button(rectangle) clicked the method runTuringMachine will run
        begin.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Left mouse button clicked on Start button !");
                button.setFill(Color.LIGHTGREEN);
                runTuringMachine(transition, root, inputTapeNodes, begin, button);
            }
        });

        // Turing Machine should have an infinite amount of tape. Using for loop to create 50 cells ( to show illusion of infinite cells ).
        int cell_number = 50;
        for (int i = 0; i < cell_number; i++) {
            int cell_height = 80;
            Rectangle rect = new Rectangle(cell_width * i, 200, cell_width, cell_height);
            rect.setFill(Color.WHITE);
            rect.setStroke(Color.BLACK);
            root.getChildren().add(rect);
        }

        // Using for loop to take each of the symbol inside the input list to place in the tape
        for (int i = 0; i < inputList.size(); i++) {
            String symbol = inputList.get(i);
            Text input_tape = new Text(cell_width * i + 30, 250, symbol);
            input_tape.setFont(Font.font("Arial",35));
            root.getChildren().add(input_tape);
            inputTapeNodes.add(input_tape);
        }

        // Set the screen title and dimensions
        stage.setTitle("Turing Machine Simulation");
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);

        // Add UI components (Objects) to the root (Group object)
        root.getChildren().add(text);
        root.getChildren().add(line);
        root.getChildren().add(triangle);
        root.getChildren().add(button);
        root.getChildren().add(begin);

        // Set the scene and display it on the stage
        stage.setScene(scene);
        stage.show();
    }

    public void runTuringMachine(Map<String, Map<String, List<String>>> transition, Group root, List<Text> inputTapeNodes, Text begin, Rectangle button) {
        // Remove existing tape nodes and clear the list
        root.getChildren().removeAll(inputTapeNodes);
        root.getChildren().remove(begin);
        root.getChildren().remove(button);
        inputTapeNodes.clear();

        // Create a PauseTransition for the delay
        PauseTransition pause = new PauseTransition(Duration.seconds(0.4)); // 0.4 second / 400ms  delay
        pause.setOnFinished(event -> {
            try {
                if (current_state.equals(accept_state)) {
                    System.out.println("Input Accepted\n");
                    return;
                } else if (current_state.equals(reject_state)) {
                    System.out.println("Input Rejected\n");
                    return;
                }

                String symbol = inputList.get(head_position);
                // Retrieve the transition for the current state and symbol
                Map<String, List<String>> new_state = transition.get(current_state);
                List<String> transitionList = new_state.get(symbol);

                // Update current state and tape symbol
                current_state = transitionList.get(0);
                inputList.set(head_position, transitionList.get(1));
                if (transitionList.get(2).equalsIgnoreCase("R")) {
                    head_position++;
                } else if (transitionList.get(2).equalsIgnoreCase("L")) {
                    head_position--;
                } else {
                    System.out.println("Please input either R for right or L for left ");
                }

                if (head_position >= inputList.size()) {
                    inputList.add(blank_symbol);
                }

                // Clear existing tape nodes from the UI and the list
                root.getChildren().removeAll(inputTapeNodes);
                inputTapeNodes.clear();

                // Update UI and change input in the tape
                for (int i = 0; i < inputList.size(); i++) {
                    String newSymbol = inputList.get(i);
                    Text input_tape = new Text(cell_width * i + 30, 250, newSymbol);
                    input_tape.setFont(Font.font("Arial", 35));
                    root.getChildren().add(input_tape);
                    inputTapeNodes.add(input_tape);
                }

                // Update tape head (Triangle) position following the tape hade position
                triangle.setTranslateX(head_position * cell_width);

                // Continue the loop if not halted
                if (!current_state.equals(accept_state) && !current_state.equals(reject_state)) {
                    pause.playFromStart();
                }
            } catch (NullPointerException e) {
                System.out.println("Input Rejected\n");
            }
        });

        pause.play();
    }
}
