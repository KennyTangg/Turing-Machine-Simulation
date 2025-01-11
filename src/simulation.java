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

public class simulation extends Application {
    private int cell_width = 80; 
    private int cell_height = 80; 
    private int cell_number = 50; 
    private String tape = "111000";

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

        for (int i = 0; i < tape.length(); i++) {
            String symbol = String.valueOf(tape.charAt(i));
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
    }
}
