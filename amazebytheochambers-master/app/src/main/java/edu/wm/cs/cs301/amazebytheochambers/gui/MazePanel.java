package edu.wm.cs.cs301.amazebytheochambers.gui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.graphics.RectF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.graphics.Path;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import static androidx.core.content.ContextCompat.startActivity;

public class MazePanel extends View implements Panel {
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;

    public MazePanel(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        //we keep track of the paint color
        paint = new Paint();
        canvas = new Canvas(bitmap);
    }

    public MazePanel(Context context, AttributeSet app) {
        super(context, app);
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        //we keep track of the paint
        paint = new Paint();
        canvas = new Canvas(bitmap);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        //we want it centered
        c.drawBitmap(bitmap, 160, 0, null);
        //Log.v("drawing","drawing"); for debugging
    }

    /**
     * Commits all accumulated drawings to the UI.
     * Substitute for MazePanel.update method.
     * Went back to commit for clarity.
     */
    @Override
    public void commit() {
        // we cause Android environment to call OnDraw by a call to this method invalidate
        invalidate();
    }

    //added to set color of paint using Android color class
    public void setColor(int rgb) {
        //color of instance we update to said color
        this.paint.setColor(rgb);
    }

    /**
     * Returns the RGB value for the current color setting.
     * @return integer RGB value
     */
    @Override
    public int getColor() {
        return this.paint.getColor();
    }

    @Override
    public boolean isOperational() {
        return true;
    } //boolean return, have not used this method really

    /**
     * Draws two solid rectangles to provide a background.
     * Note that this also erases any previous drawings.
     * The color setting adjusts to the distance to the exit to
     * provide an additional clue for the user.
     * Colors transition from black to gold and from grey to green.
     * Substitute for FirstPersonView.drawBackground method.
     * @param percentToExit gives the distance to exit
     */
    @Override
    public void addBackground(float percentToExit) {
        this.setColor(Color.WHITE); //ceiling color
        this.addFilledRectangle(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT/2);
        this.setColor(Color.DKGRAY); //floor is dark gray
        this.addFilledRectangle(0, Constants.VIEW_HEIGHT/2, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT/2);
    }

    /**
     * Adds a filled rectangle.
     * The rectangle is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis.
     * Substitute for Graphics.fillRect() method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(x,y,x+width,y+height),paint); //we draw the rect in relation to the coordinates given
    }

    /**
     * Adds a filled polygon.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.fillPolygon() method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    //found on stackoverflow https://stackoverflow.com/questions/2047573/how-to-draw-filled-polygon
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Path poly_path = new Path();
        paint.setStyle(Paint.Style.FILL);
        int len = nPoints;
        poly_path.moveTo(xPoints[0], yPoints[0]);
        for (int n = 1; n < len; n++) {
            poly_path.lineTo(xPoints[n], yPoints[n]);
        }
        poly_path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(poly_path, paint);
    }

    //so far no need for this
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    /**
     * Adds a line.
     * A line is described by {@code (x,y)} coordinates for its
     * starting point and its end point.
     * Substitute for Graphics.drawLine method
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX,startY,endX,endY,paint);
    }

    /**
     * Adds a filled oval.
     * The oval is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis. An oval is
     * described like a rectangle.
     * Substitute for Graphics.fillOval method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        // new rect to call draw Oval on
        canvas.drawOval(new RectF(x,y,width,height),paint); // RectF for rectangle bounds of the oval
    }

    /**
     * Adds the outline of a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at startAngle and extends for arcAngle degrees,
     * using the current color. Angles are interpreted such that 0 degrees
     * is at the 3 o'clock position. A positive value indicates a counter-clockwise
     * rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is
     * (x, y) and whose size is specified by the width and height arguments.
     * The resulting arc covers an area width + 1 pixels wide
     * by height + 1 pixels tall.
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the start
     * and end of the arc segment will be skewed farther along the longer
     * axis of the bounds.
     * Substitute for Graphics.drawArc method
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        // empty now
    }

    /**
     * Adds a string at the given position.
     * Substitute for CompassRose.drawMarker method
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str) {
    //empty for now
    }

}
