
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
public class Polygon extends DrawnShape {
    private double[] xPoints;
    private double[] yPoints;
    private int numPoints;

    public Polygon(double[] xPoints, double[] yPoints, int numPoints, Color color) {
        super(0, 0, color); // x and y coordinates not relevant for polygon but want to keep all drawings in same array
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.numPoints = numPoints;
    }
    
    @Override
    public void Draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillPolygon(xPoints, yPoints, numPoints);
    }
}
