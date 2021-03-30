package com.glen.gobang;

import java.awt.*;

public class Piece {
    private int x=0,y=0;//坐标
    private boolean color= false;//黑：false   白：true
    private boolean fallen = false; //棋子是否落下
    public int getX() {
        return x;
    }

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isFallen() {
        return fallen;
    }

    public void setFallen(boolean fallen) {
        this.fallen = fallen;
    }

    public void draw(Graphics g,int pieceSize,int x,int y) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color realColor = this.color?new Color(240,240,240):Color.BLACK;
        g.setColor(realColor);
        g.fillOval(x,y,pieceSize,pieceSize);

    }





}
