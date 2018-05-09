import java.awt.*;
import java.awt.geom.Rectangle2D;

public class shapeItem {
    private Shape shape;
    private Color color;
    private Color startColor;
    boolean noRoom;
    int startY;
    int vel;

    shapeItem(Shape shape, Color color) {
        this.shape = shape;
        this.color = this.startColor = color;
        this.startY = (int) this.shape.getBounds().getY();
    }

    shapeItem(Shape shape){
        this.shape = shape;
        this.color = Color.white;
        this.startY = (int) this.shape.getBounds().getY();
    }

    public Shape getShape() {
        return this.shape;
    }

    public Color getColor() {
        return this.color;
    }

    public double getX() { return this.shape.getBounds().getX(); }

    public double getY() { return this.shape.getBounds().getY(); }

    public double getH() {return this.shape.getBounds().getHeight(); }

    public double getDiff() {
        return Math.abs(this.shape.getBounds().getY() - this.startY);
    }

    public void setRec(double x, double y, double s) {
        this.shape = new Rectangle2D.Double(x, y, s, s);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor() {
        this.color = this.startColor;
    }

    public void setStartColor(Color c) {this.startColor = c; }

    public void setNoRoom() {this.noRoom = true;}

    public void setOpacity(int diff) {
        this.color = new Color(this.startColor.getRed(),
                this.startColor.getGreen(),
                this.startColor.getBlue(),
                this.color.getAlpha() + diff);
    }
}
