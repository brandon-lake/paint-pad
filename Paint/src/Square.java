
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Brandon Lake
 */
public class Square extends DrawnShape {
    private double width;
    private double height;
    
    public Square(double x, double y, double width, double height, Color color) {
        super(x, y, color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void Draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillRect(getX(), getY(), width, height);
    }
}
