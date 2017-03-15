package com.example.brian.afinal.View;

/**
 * Created by Brian on 25/02/2017.
 */
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Paint;
import com.example.brian.afinal.enums.TileType;
import android.graphics.Canvas;

public class SnakeViews extends View {
    private Paint mPaint = new Paint();
    private TileType snakeMap[][];

    public SnakeViews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSnakeMap(TileType[][] map){
        this.snakeMap = map;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (snakeMap!=null) {
            float tileSizeX = canvas.getWidth() / snakeMap.length;
            float tileSizeY = canvas.getHeight() / snakeMap[0].length;
            float circleSize = Math.min(tileSizeX, tileSizeY) / 2;


            for (int x = 0; x < snakeMap.length; x++) {
                for (int y = 0; y < snakeMap[x].length; y++) {
                    switch (snakeMap[x][y]) {

                        case Nothing:
                            mPaint.setColor(Color.BLACK);
                            break;
                        case Wall:
                            mPaint.setColor(Color.WHITE);
                            break;
                        case SnakeHead:
                            mPaint.setColor(Color.WHITE);
                            break;
                        case SnakeTail:
                            mPaint.setColor(Color.WHITE);
                            break;
                        case Apple:
                            mPaint.setColor(Color.WHITE);
                            break;
                    }
                    canvas.drawCircle(x * tileSizeX + tileSizeX / 2f + circleSize / 2, y * tileSizeY + tileSizeY /2f + circleSize /2,circleSize,mPaint);
                }
            }
        }
    }
}
