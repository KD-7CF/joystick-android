package Joystick;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

public class Joystick implements draw {

    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    public  boolean enabled = true;

    public float x = 0;
    public float y = 0;
    public float angle = 0;


    public Joystick(ImageView imageView, int width, int height) {
        this.imageView = imageView;
        this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circTouch(this.bitmap.getWidth() / 2, this.bitmap.getHeight() / 2, this.bitmap.getWidth() / 8);
    }
    @Override
    public void circTouch(float x, float y, float r) {

        if(!enabled)
            return;
        float limitLine =  this.bitmap.getWidth();
        if(x < r)
            x = r;
        else if ( x > limitLine - r)
            x = limitLine - r;
        if(y < r)
            y = r;
        else if ( y > limitLine - r)
            y = limitLine - r;

        float mx = this.bitmap.getWidth() / 2;
        float my = this.bitmap.getHeight() / 2;
        float dx = x - mx;
        float dy = y - my;

        int a = (int) Math.abs(dx);
        if(a > 255)
            a = 255;
        int margin = 50;
        int pensz1 = 12;
        int pensz2 = 6;

        this.canvas.drawColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(Color.argb(a,200,200,200));
        this.canvas.drawOval(0, my - margin, limitLine, my + margin, this.paint);

        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(pensz1);

        float angle = (float) (-180 * dy / (2 * my));
        if (dy < 0)
            this.paint.setColor(Color.rgb(0,255, 0));
        else
            this.paint.setColor(Color.rgb(255,0, 0));

        this.canvas.drawArc(
                pensz1,
                pensz1,
                limitLine - pensz1,
                limitLine - pensz1,
                -90,
                angle,
                false,
                this.paint
        );

        this.paint.setStrokeWidth(pensz2);
        this.paint.setColor(Color.rgb(255,255,255));
        this.canvas.drawCircle(x, my, r - pensz2, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(Color.rgb(0, 0,  0));
        this.canvas.drawCircle(x, my, r - (pensz2 * 3), this.paint);
        this.imageView.setImageBitmap(this.bitmap);

        this.x = dx;
        this.y = -dy;
        this.angle = angle;
    }
}








