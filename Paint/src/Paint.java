

import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Method to allow a user to draw on a blank canvas with a variety of colors or
 * brush sizes and shapes Also gives the user the option to clear the canvas, or
 * use the eraser brush
 *
 * @author Brandon Lake
 */
public class Paint extends Application {

    // Arrays to hold all the drawn objects
    private ArrayList<ArrayList<DrawnShape>> shapes = new ArrayList<>();
    private ArrayList<DrawnShape> list;

    private int index = 0;   // keeps track of the users location in the array of drawings when undoing and redoing things

    private int size;       // size of the shape to be drawn
    private Color color;    // color of the shape to be drawn

    // arbitrary values to ensure that lines are not drawn between shapes unless the mouse has been dragged
    private double previousX = 123456;
    private double previousY = 123456;

    private GraphicsContext gc;     // the graphics context for canvas 1  
    private GraphicsContext gc2;    // the graphics context for canvas 2

    private Canvas canvas = new Canvas(900, 600);   // the canvas to be drawn on
    private Canvas canvas2 = new Canvas(900, 600);  // second canvas to deal with moving shapes/animations
    
    private Label title = new Label("Press the 'Draw it!' button or draw your design on the canvas"); // the title label

    // components to deal with location inputs
    private Label location = new Label("Location:");
    private TextField locationX = new TextField("50");
    private TextField locationY = new TextField("50");

    // components to deal with size inputs
    private Label sizeLabel = new Label("Size:");
    private TextField sizeInput = new TextField("25");

    // components to deal with color input
    private Label color1 = new Label("Color:");
    private TextField colorR = new TextField("255");
    private TextField colorG = new TextField("0");
    private TextField colorB = new TextField("0");

    // components to deal with string color inputs
    private Label color2 = new Label("Color:");
    private TextField colorString = new TextField("");

    // buttons to undo and redo previous actions on the canvas
    private Button undo = new Button("");
    private Button redo = new Button("");

    // lets the user pick a shape to draw with
    private ComboBox shape = new ComboBox();

    // buttons to draw or clear canvas
    private Button drawMe = new Button("Draw it!");
    private Button clear = new Button("Clear");

    // the error field
    private Label errorField = new Label("");

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        gc = canvas.getGraphicsContext2D();
        gc2 = canvas2.getGraphicsContext2D();
        Scene scene = new Scene(root, 900, 720); // set the window size here
        stage.setTitle("Sketchpad!"); // set the window title here
        stage.setScene(scene);
        // TODO: Add your GUI-building code here

        // 1. Create the model
        // 2. Create the GUI components
        Button up = new Button();           // adds 5 to size
        Button down = new Button();         // subtracts 5 from size

        Label eraser = new Label("**Set color to white\n  for eraser mode**");     // displays to the user how to use eraser

        // 3. Add components to the root
        root.getChildren().addAll(canvas, canvas2, title, location, locationX, locationY, sizeLabel, sizeInput, up, down,
                eraser, color1, colorR, colorG, colorB, color2, colorString, drawMe, clear, errorField, shape, undo, redo);

        // 4. Configure the components (colors, fonts, size, location)
        gc.setLineCap(StrokeLineCap.ROUND);
        gc2.setLineCap(StrokeLineCap.ROUND);

        title.setFont(new Font("Times New Roman", 20));
        title.setPrefWidth(900);
        title.setMinHeight(30);
        title.setMaxHeight(30);
        title.setStyle("-fx-alignment: center; -fx-background-color: yellow; -fx-underline: true");

        canvas.relocate(0, 30);
        canvas2.relocate(0, 30);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 900, 600);

        location.relocate(0, 633);
        location.setFont(new Font("Times New Roman", 20));
        location.setStyle("-fx-background-color: lightgreen");

        locationX.relocate(80, 633);
        locationX.setPrefWidth(50);
        locationY.relocate(130, 633);
        locationY.setPrefWidth(50);

        sizeLabel.relocate(187, 633);
        sizeLabel.setFont(new Font("Times New Roman", 20));
        sizeLabel.setStyle("-fx-background-color: lightgreen");
        sizeInput.relocate(231, 633);
        sizeInput.setPrefWidth(50);

        Image upArrow = new Image("up.jpeg");
        up.setGraphic(new ImageView(upArrow));
        up.setPrefWidth(27);
        up.relocate(281, 630);
        up.setStyle("-fx-padding: 0;");

        Image downArrow = new Image("down.jpeg");
        down.setGraphic(new ImageView(downArrow));
        down.setPrefWidth(27);
        down.relocate(281, 645);
        down.setStyle("-fx-padding: 0;");

        Image undoArrow = new Image("undo.jpeg");
        undo.setGraphic(new ImageView(undoArrow));
        undo.relocate(520, 634);
        undo.setStyle("-fx-padding: 3");
        
        Image redoArrow = new Image("redo.jpeg");
        redo.setGraphic(new ImageView(redoArrow));
        redo.relocate(852, 634);
        redo.setStyle("-fx-padding: 3");

        color1.relocate(314, 633);
        color1.setFont(new Font("Times New Roman", 20));
        color1.setStyle("-fx-background-color: lightgreen");
        colorR.relocate(368, 633);
        colorR.setPrefWidth(50);
        colorG.relocate(418, 633);
        colorG.setPrefWidth(50);
        colorB.relocate(468, 633);
        colorB.setPrefWidth(50);

        eraser.relocate(200, 661);
        eraser.setLineSpacing(-3);

        color2.relocate(314, 665);
        color2.setFont(new Font("Times New Roman", 20));
        color2.setStyle("-fx-background-color: lightgreen");
        colorString.relocate(368, 665);
        colorString.setPrefWidth(150);

        shape.getItems().addAll("Circle", "Square", "Triangle", "Line Drawer!");
        shape.setValue("Circle");
        shape.relocate(80, 665);
        shape.setPrefWidth(100);

        drawMe.relocate(569, 630);
        drawMe.setPrefWidth(280);
        drawMe.setPrefHeight(55);
        drawMe.setFont(new Font("Times New Roman", 22));

        clear.relocate(520, 687);
        clear.setPrefHeight(30);
        clear.setPrefWidth(378);
        clear.setFont(new Font("Times New Roman", 18));

        errorField.relocate(0, 694);
        errorField.setMinWidth(518);
        errorField.setStyle("-fx-background-color: lightblue; -fx-alignment: center");
        errorField.setFont(new Font("Arial", 20));

        // 5. Add Event Handlers and do final setup
        drawMe.setOnAction(this::buttonPressed);
        clear.setOnAction(this::clearCanvas);
        up.setOnAction(this::addSize);
        down.setOnAction(this::minusSize);
        undo.setOnAction(this::undoButton);
        redo.setOnAction(this::redoButton);
        canvas2.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mouse);
        canvas2.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouse);
        canvas2.addEventHandler(MouseEvent.MOUSE_RELEASED, this::resetLine);
        // 6. Show the stage
        stage.show();
    }

    /**
     * Method to allow the user to 'redo' actions they have previously undone
     *
     * @param ae ActionEvent
     */
    private void redoButton(ActionEvent ae) {
        if (index < shapes.size()) {
            index++;
            shapes.get(index - 1).forEach(d -> {
                d.Draw(gc);
            });
        }
    }

    /**
     * Method to allow the user to undo previous actions
     *
     * @param ae ActionEvent
     */
    private void undoButton(ActionEvent ae) {
        if (index > 0) {
            index--;

            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            for (int i = 0; i < index; i++) {
                shapes.get(i).forEach(d -> {
                    d.Draw(gc);
                });
            }
        }
    }
    
    /**
     * Method which deletes drawn shapes ahead of your current location in the array if you start drawing after hitting undo
     */
    private void overwrite() {
        if(shapes.size() > index) {
            for (int i = shapes.size() - 1; i >= index; i--) {
                shapes.remove(i);
            }
        }
    }

    /**
     * Method to clear the canvas
     *
     * @param ae ActionEvent on button press
     */
    private void clearCanvas(ActionEvent ae) {
        overwrite();    // checks your location in the array, and drops everything in front of you once you start drawing
        list = new ArrayList<>();
        Square s = new Square(0, 0, canvas.getWidth(), canvas.getHeight(), Color.WHITE);
        s.Draw(gc);
        
        list.add(s);
        shapes.add(list);
        index++;
    }

    /**
     * Method to add 5 to size
     *
     * @param ae ActionEvent on button press
     */
    private void addSize(ActionEvent ae) {
        int tempSize = Integer.parseInt(sizeInput.getText());
        tempSize += 5;
        sizeInput.setText("" + tempSize);
    }

    /**
     * Method to subtract 5 from size
     *
     * @param ae ActionEvent on button press
     */
    private void minusSize(ActionEvent ae) {
        int tempSize = Integer.parseInt(sizeInput.getText());
        if (tempSize <= 5) {
            errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
            errorField.setText("Size cannot go below 1");
        } else {
            tempSize -= 5;
            sizeInput.setText("" + tempSize);
        }
    }

    /**
     * Method to draw a single shape
     *
     * @param ae ActionEvent on button press
     */
    private void buttonPressed(ActionEvent ae) {
        errorField.setStyle("-fx-text-fill: green; -fx-background-color: lightblue; -fx-alignment: center");
        errorField.setText("No errors");
        int x;      // x location as decided by the users input
        int y;      // y location as decided by the users input

        // X coordinate try-catch
        try {
            x = Integer.parseInt(locationX.getText().trim());
            if (x < 0 || x > 900) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("X value must be between 0-900");
                return;
            }
        } catch (NumberFormatException e) {
            errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
            errorField.setText("Please enter a valid number for X location");
            return;
        }

        // Y coordinate try-catch
        try {
            y = Integer.parseInt(locationY.getText().trim());
            if (y < 0 || y > 600) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Y value must be between 0-600");
                return;
            }
        } catch (NumberFormatException e) {
            errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
            errorField.setText("Please enter a valid number for Y location");
            return;
        }

        if (errorTrap()) { 
            list = new ArrayList<>();
            overwrite();    // checks your location in the array, and drops everything in front of you once you start drawing
            // draws a different shape based on which shape the user chose
            if (shape.getValue().equals("Line Drawer!")) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Line Drawer option requires you to draw your own line");
            } else if (shape.getValue().equals("Circle")) {
                Circle c = new Circle(x - size / 2, y - size / 2, size, color);
                c.Draw(gc);
                list.add(c);
            } else if (shape.getValue().equals("Square")) {
                Square s = new Square(x - size / 2, y - size / 2, size, size, color);
                s.Draw(gc);
                list.add(s);
            } else if (shape.getValue().equals("Triangle")) {
                double[] triangleX = {x, x + size / 2, x - size / 2};
                double[] triangleY = {y - size / 2, y + size / 2, y + size / 2};
                Polygon p = new Polygon(triangleX, triangleY, 3, color);
                p.Draw(gc);
                list.add(p);
            }
            shapes.add(list);
            index++;
        }
    }

    /**
     * Method to draw the chosen shape on mouse click and on mouse drag
     *
     * @param me MouseEvent
     */
    private void mouse(MouseEvent me) {
        errorField.setStyle("-fx-text-fill: green; -fx-background-color: lightblue; -fx-alignment: center");
        errorField.setText("No errors");

        double currentX = me.getX();
        double currentY = me.getY();

        if (previousX == 123456 && previousY == 123456) {
            list = new ArrayList<>();
        }

        if (errorTrap()) {
            overwrite();    // checks your location in the array, and drops everything in front of you once you start drawing
            // check if color is white for eraser mode
            if (colorR.getText().equals("255") && colorG.getText().equals("255") && colorB.getText().equals("255")) {
                errorField.setText("--ERASER MODE--");
            }
            // draws a different shape based on which shape the user chose
            if (shape.getValue().equals("Line Drawer!")) {
                gc2.setStroke(color);
                gc2.setLineWidth(size);
                gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
                if (previousX == 123456 && previousY == 123456) {
                    Circle c = new Circle(currentX - size / 2, currentY - size / 2, size, color);
                    c.Draw(gc);
                    list.add(c);

                    previousX = me.getX();
                    previousY = me.getY();
                } else {
                    gc2.strokeLine(previousX, previousY, me.getX(), me.getY());
                }
            } else {
                gc2.setLineWidth(size / 12);
                gc2.setStroke(Color.BLACK);
                if (shape.getValue().equals("Circle")) {
                    Circle c = new Circle(currentX - size / 2, currentY - size / 2, size, color);
                    c.Draw(gc);
                    list.add(c);

                    gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
                    gc2.strokeOval(me.getX() - size / 2, me.getY() - size / 2, size, size);
                    if (previousX != 123456 && previousY != 123456 && size > 0) {
                        Line line = new Line(previousX, previousY, size, currentX, currentY, color);
                        line.Draw(gc);
                        list.add(line);
                    }
                } else if (shape.getValue().equals("Square")) {
                    Square s = new Square(currentX - size / 2, currentY - size / 2, size, size, color);
                    s.Draw(gc);
                    list.add(s);
                    
                    gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
                    gc2.strokeRect(me.getX() - size / 2, me.getY() - size / 2, size, size);
                    if (previousX != 123456 && previousY != 123456 && size > 0) {
                        // create 2 polygons which will form an X shape through each square
                        // growth = bottom left corner to top right corner
                        double[] growthX = {previousX - size / 2, previousX + size / 2, currentX + size / 2, currentX - size / 2};
                        double[] growthY = {previousY + size / 2, previousY - size / 2, currentY - size / 2, currentY + size / 2};
                        Polygon p1 = new Polygon(growthX, growthY, 4, color);
                        p1.Draw(gc);
                        list.add(p1);
                        // decay = top left corner to bottom right corner
                        double[] decayX = {previousX - size / 2, previousX + size / 2, currentX + size / 2, currentX - size / 2};
                        double[] decayY = {previousY - size / 2, previousY + size / 2, currentY + size / 2, currentY - size / 2};
                        Polygon p2 = new Polygon(decayX, decayY, 4, color);
                        p2.Draw(gc);
                        list.add(p2);
                    }
                } else if (shape.getValue().equals("Triangle")) {
                    double[] triangleX = {currentX, currentX + size / 2, currentX - size / 2};
                    double[] triangleY = {currentY - size / 2, currentY + size / 2, currentY + size / 2};
                    Polygon p = new Polygon(triangleX, triangleY, 3, color);
                    p.Draw(gc);
                    list.add(p);

                    gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
                    gc2.strokePolygon(triangleX, triangleY, 3);
                    if (previousX != 123456 && previousY != 123456 && size > 0) {
                        // create 2 polygons which will form an upside down T shape through each triangle
                        // vert = top point of triangle to middle of the base
                        double[] vertX = {previousX, previousX, currentX, currentX};
                        double[] vertY = {previousY - size / 2, previousY + size / 2, currentY + size / 2, currentY - size / 2};
                        Polygon p2 = new Polygon(vertX, vertY, 4, color);
                        p2.Draw(gc);
                        list.add(p2);
                        // base = bottom left point to bottom right point
                        double[] baseX = {previousX - size / 2, previousX + size / 2, currentX + size / 2, currentX - size / 2};
                        double[] baseY = {previousY + size / 2, previousY + size / 2, currentY + size / 2, currentY + size / 2};
                        Polygon p3 = new Polygon(baseX, baseY, 4, color);
                        p3.Draw(gc);
                        list.add(p3);
                    }
                }
                previousX = currentX;
                previousY = currentY;
            }
        }
    }

    /**
     * Method to run on mouse release - resets previous mouse coordinates to
     * random arbitrary value, such that no line will be drawn between two
     * clicks, the mouse must be dragged to draw a line
     *
     * @param me MouseEvent
     */
    private void resetLine(MouseEvent me) {
        if (shape.getValue().equals("Line Drawer!") && errorTrap()) {
            Line line = new Line(previousX, previousY, size, me.getX(), me.getY(), color);
            line.Draw(gc);
            list.add(line);
        }
        previousX = 123456;
        previousY = 123456;
        shapes.add(list);
        index++;
        gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
    }

    /**
     * Method to ensure all user inputs won't crash the code
     *
     * @return boolean Returns true if the inputs are error free, false if there
     * are errors
     */
    private boolean errorTrap() {
        int red, green, blue;

        // size try-catch
        try {
            size = Integer.parseInt(sizeInput.getText().trim());
            if (size <= 0) {
                size = 0;
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Please enter value for radius greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
            errorField.setText("Please input a valid number for radius");
            return false;
        }

        // checks RGB values if no color string input
        if (colorString.getText().isEmpty()) {
            color1.setStyle("-fx-background-color: coral");
            color2.setStyle("-fx-background-color: lightgreen");
            // red try-catch
            if (colorR.getText().isEmpty()) {
                colorR.setText("0");
            }
            try {
                red = Integer.parseInt(colorR.getText().trim());
                if (red < 0 || red > 255) {
                    errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                    errorField.setText("Red value must be between 0-255");
                    return false;
                }
            } catch (NumberFormatException e) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Enter a valid number for red");
                return false;
            }

            // green try-catch
            if (colorG.getText().isEmpty()) {
                colorG.setText("0");
            }
            try {
                green = Integer.parseInt(colorG.getText().trim());
                if (green < 0 || green > 255) {
                    errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                    errorField.setText("Green value must be between 0-255");
                    return false;
                }
            } catch (NumberFormatException e) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Enter a valid number for green");
                return false;
            }

            // blue try-catch
            if (colorB.getText().isEmpty()) {
                colorB.setText("0");
            }
            try {
                blue = Integer.parseInt(colorB.getText().trim());
                if (blue < 0 || blue > 255) {
                    errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                    errorField.setText("Blue value must be between 0-255");
                    return false;
                }
            } catch (NumberFormatException e) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Enter a valid number for blue");
                return false;
            }
            color = Color.rgb(red, green, blue);
        } else {
            color1.setStyle("-fx-background-color: lightgreen");
            color2.setStyle("-fx-background-color: coral");
            // color string input error trapping
            try {
                color = Color.valueOf(colorString.getText());
                colorR.setText("" + (int) (color.getRed() * 255));
                colorG.setText("" + (int) (color.getGreen() * 255));
                colorB.setText("" + (int) (color.getBlue() * 255));
            } catch (IllegalArgumentException e) {
                errorField.setStyle("-fx-text-fill: red; -fx-background-color: lightblue; -fx-alignment: center");
                errorField.setText("Please select a supported color name");
                return false;
            }
        }
        return true;
    }

    /**
     * Make no changes here.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
