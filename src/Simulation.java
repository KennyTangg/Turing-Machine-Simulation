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

public class simulation extends Application {
    private final int cell_width = 80;
    private int cell_height = 80;
    private int cell_number = 50;
    int head_position = 0;
    String current_state = "q0";
    String accept_state = "q_accept";
    String reject_state = "q_reject";
    String blank_symbol = "_";
    String tape = "111000";
    List<String> inputList = new ArrayList<>(Arrays.asList(tape.split("")));

    // Make the triangle a field so it can be updated later.
    private Polygon triangle;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root,1280,720, Color.LAVENDER);
        List<Text> inputTapeNodes = new ArrayList<>();
        Map<String, Map<String,List<String>>> transition = new HashMap<>();

        Map<String,List<String>> q0 = new HashMap<>();
        q0.put("0", Arrays.asList("q_reject","0","R"));
        q0.put("1", Arrays.asList("q1","X","R"));
        q0.put("Y", Arrays.asList("q3","Y","R"));
        q0.put("_", Arrays.asList("q3","_","L"));

        Map<String,List<String>> q1 = new HashMap<>();
        q1.put("0", Arrays.asList("q2","Y","L"));
        q1.put("1", Arrays.asList("q1","1","R"));
        q1.put("Y", Arrays.asList("q1","Y","R"));

        Map<String,List<String>> q2 = new HashMap<>();
        q2.put("1", Arrays.asList("q2","1","L"));
        q2.put("Y", Arrays.asList("q2","Y","L"));
        q2.put("X", Arrays.asList("q0","X","R"));

        Map<String,List<String>> q3 = new HashMap<>();
        q3.put("Y", Arrays.asList("q3","Y","R"));
        q3.put("_", Arrays.asList("q_accept","_","L"));

        transition.put("q0", q0);
        transition.put("q1", q1);
        transition.put("q2", q2);
        transition.put("q3", q3);

        Text text = new Text();
        text.setText("Turing Machine Simulation");
        text.setX(340);
        text.setY(65);
        text.setFont(Font.font("Poppins",FontWeight.BOLD, 50));
        text.setFill(Color.GRAY);

        Line line = new Line();
        line.setStartX(100);
        line.setStartY(100);
        line.setEndX(1180);
        line.setEndY(100);
        line.setStrokeWidth(2);
        line.setStroke(Color.GREY);

        // Initialize the triangle (tape head) and add it to the scene
        triangle = new Polygon();
        triangle.getPoints().setAll(
                25.0, 150.0,
                55.0, 150.0,
                40.0, 175.0);
        triangle.setFill(Color.GREY);
        triangle.setStroke(Color.BLACK);
        triangle.setTranslateX(0);

        Rectangle button = new Rectangle();
        button.setX(550);
        button.setY(550);
        button.setHeight(75);
        button.setWidth(175);
        button.setStroke(Color.BLACK);
        button.setStrokeWidth(3);
        button.setFill(Color.LAVENDERBLUSH);
        button.setOpacity(0.5);

        Text begin = new Text();
        begin.setText("START");
        begin.setX(575);
        begin.setY(600);
        begin.setFont(Font.font("Poppins",FontWeight.BOLD, 40));
        begin.setFill(Color.GRAY);

        button.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Left mouse button clicked on Start button !");
                button.setFill(Color.LIGHTGREEN);
                runTuringMachine(transition, root, inputTapeNodes, begin, button);
            }
        });

        begin.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Left mouse button clicked on Start button !");
                button.setFill(Color.LIGHTGREEN);
                runTuringMachine(transition, root, inputTapeNodes, begin, button);
            }
        });

        for (int i = 0; i < cell_number; i++) {
            Rectangle rect = new Rectangle(cell_width * i, 200, cell_width, cell_height);
            rect.setFill(Color.WHITE);
            rect.setStroke(Color.BLACK);
            root.getChildren().add(rect);
        }

        for (int i = 0; i < inputList.size(); i++) {
            String symbol = inputList.get(i);
            Text input_tape = new Text(cell_width * i + 30, 250, symbol);
            input_tape.setFont(Font.font("Arial",35));
            root.getChildren().add(input_tape);
            inputTapeNodes.add(input_tape);
        }

        stage.setTitle("Turing Machine Simulation");
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);

        root.getChildren().add(text);
        root.getChildren().add(line);
        root.getChildren().add(triangle);
        root.getChildren().add(button);
        root.getChildren().add(begin);
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

                // Update the UI with the new tape content
                for (int i = 0; i < inputList.size(); i++) {
                    String newSymbol = inputList.get(i);
                    Text input_tape = new Text(cell_width * i + 30, 250, newSymbol);
                    input_tape.setFont(Font.font("Arial", 35));
                    root.getChildren().add(input_tape);
                    inputTapeNodes.add(input_tape);
                }

                triangle.setTranslateX(head_position * cell_width);

                // Continue the loop if not halted
                if (!current_state.equals(accept_state) && !current_state.equals(reject_state)) {
                    pause.playFromStart();
                }
            } catch (NullPointerException e) {
                System.out.println("Input Rejected\n");
            }
        });

        // Start the first iteration
        pause.play();
    }
}
