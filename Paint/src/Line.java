
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
public class Line extends DrawnShape {
    private double endX;
    private double endY;
    private double size;
    
    public Line(double x, double y, double size, double endX, double endY, Color color) {
        super(x, y, color);
        this.endX = endX;
        this.endY = endY;
        this.size = size;
    }
    
    @Override
    public void Draw(GraphicsContext gc) {
        gc.setLineWidth(size);
        gc.setStroke(getColor());
        gc.strokeLine(getX(), getY(), endX, endY);
    }
}
