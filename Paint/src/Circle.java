
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
public class Circle extends DrawnShape {
    private double size;
    
    public Circle(double x, double y, double size, Color color) {
        super(x, y, color);
        this.size = size;
    }
    
    @Override
    public void Draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(getX(), getY(), size, size);
    }
}
