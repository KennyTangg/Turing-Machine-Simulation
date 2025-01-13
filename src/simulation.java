import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.*;

public class simulation extends Application {
    private int cell_width = 80; 
    private int cell_height = 80; 
    private int cell_number = 50; 
    int head_position = 0;
    String current_state = "q0";
    String accept_state = "q_accept";
    String reject_state = "q_reject";
    String blank_symbol = "_";
    String tape = "111000";
    List<String> inputList = new ArrayList<>(Arrays.asList(tape.split("")));

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root,1280,720, Color.LAVENDER);

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

        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
            25.0,150.0,
            55.0,150.0,
            40.0,175.0);
        triangle.setFill(Color.GREY);
        triangle.setStroke(Color.BLACK);

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
        }

        Image icon = new Image("TM_icon.png");

        stage.getIcons().add(icon);
        stage.setTitle("Turing Machine Simulation");
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false);

        root.getChildren().add(text);
        root.getChildren().add(line);
        root.getChildren().add(triangle);
        stage.setScene(scene);
        stage.show();

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

        try{
            while (true){
                String symbol = inputList.get(head_position);

                // new state as key with transition function as value 
                Map<String,List<String>> new_state = transition.get(current_state);
                List<String> transitionList = new_state.get(symbol);

                current_state = transitionList.get(0);
                inputList.set(head_position, transitionList.get(1));
                if (transitionList.get(2).toUpperCase() == "R"){
                    head_position += 1;
                } else if (transitionList.get(2).toUpperCase() == "L"){
                    head_position -= 1;
                } else{
                    System.out.println("Please input either R for right or L for Left ");
                };
                
                if (head_position >= inputList.size()){
                    inputList.add(blank_symbol);
                }

                System.out.println(inputList);
                System.out.println("Head Position : " + head_position);
                System.out.println("Current State : " + current_state);
                if (current_state == accept_state){
                    System.out.println("Input Accepted\n");
                    break;
                }else if (current_state == reject_state){
                    System.out.println("Input Rejected\n");
                    break;
                }
                else{
                    System.out.println("Running...\n");
                    if (head_position == inputList.size() + 1){
                        System.out.println("Your Algorithm cannot Halt...\n");
                        break;
                    }
                }
                // Thread.sleep(300); 
            }
        } catch (NullPointerException e) {
            System.out.println("Input Rejected\n");
        }
    }
}
