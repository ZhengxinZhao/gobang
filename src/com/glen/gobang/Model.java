package com.glen.gobang;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Model {

    public static final int ROW = 15, COLUMN = 15;
    //存储棋子信息的数组
    public static final Piece[][] PIECES = new Piece[COLUMN][ROW];
    //落子顺序记录
    private static final Stack<Piece> KIFU = new Stack<>();
    //当前棋手
    private static boolean currentPlayer = false;//黑：false   白：true

    static {
        for (int i = 0; i < COLUMN; ++i) {
            for (int j = 0; j < ROW; ++j)
                Model.PIECES[i][j] = new Piece(i, j);
        }
    }
    public static boolean getCurrentPlayer(){
        return currentPlayer;
    }


    //开启新游戏
    public static void newGame(){
        for (int i = 0; i < COLUMN; ++i) {
            for (int j = 0; j < ROW; ++j)
                Model.PIECES[i][j].setFallen(false);
        }

        currentPlayer = false;

        KIFU.clear();
    }
    //落子
    public static void downPiece(int x, int y) {
        Piece piece = PIECES[x][y];
       // System.out.println("x:"+x+"y:"+y);
        if (piece.isFallen())
            return;
        piece.setColor(currentPlayer);
        currentPlayer = !currentPlayer;
        piece.setFallen(true);
        KIFU.push(piece);
    }

    //悔棋
    public static void undo() {
        if (KIFU.isEmpty()) {
            JOptionPane.showMessageDialog(null, "未落子！", "提示消息", JOptionPane.WARNING_MESSAGE);
            return;
        }
        currentPlayer = !currentPlayer;
        KIFU.pop().setFallen(false);

    }

    //查看当前落子
    public static Piece peek() {
        if(KIFU.empty()) return null;
        return KIFU.peek();
    }

    //是否五子连珠
    public static boolean weatherWin(int x, int y) {
        //竖直方向
        int count = 1;
        for (int k = 1; k <= 4; k++) {
            if (x - k >= 0)
                if (PIECES[x - k][y].isFallen() &&
                        PIECES[x - k][y].getColor() == PIECES[x][y].getColor()) {
                    count++;
                } else break;
            else break;
        }
        for (int k = 1; k <= 4; k++) {

            if (x + k < COLUMN) {
                if (PIECES[x + k][y].isFallen() &&
                        PIECES[x + k][y].getColor() == PIECES[x][y].getColor())
                    count++;
                else break;
            } else break;

        }
        if (count >= 5)
            return true;
        //水平方向
        count = 1;
        for (int k = 1; k <= 4; k++) {
            if (y - k >= 0)
                if (PIECES[x][y - k].isFallen() &&
                        PIECES[x][y - k].getColor() == PIECES[x][y].getColor()) {
                    count++;
                } else break;
            else break;
        }
        for (int k = 1; k <= 4; k++) {

            if (y + k < ROW) {
                if (PIECES[x][y + k].isFallen() &&
                        PIECES[x][y + k].getColor() == PIECES[x][y].getColor())
                    count++;
                else break;

            } else break;
        }
        if (count >= 5)
            return true;
        //斜向1
        count = 1;
        for (int k = 1; k <= 4; k++) {

            if (x - k >= 0 && y - k >= 0) {
                if (PIECES[x - k][y - k].isFallen() &&
                        PIECES[x - k][y - k].getColor() == PIECES[x][y].getColor())
                    count++;
                else break;
            } else break;
        }
        for (int k = 1; k <= 4; k++) {

            if (x + k < COLUMN && y + k < ROW) {
                if (PIECES[x + k][y + k].isFallen() &&
                        PIECES[x + k][y + k].getColor() == PIECES[x][y].getColor())
                    count++;
                else break;
            } else break;
        }
        if (count >= 5)
            return true;
        //斜向2
        count = 1;
        for (int k = 1; k <= 4; k++) {

            if (x - k >= 0 && y + k < ROW) {
                if (PIECES[x - k][y + k].isFallen() &&
                        PIECES[x - k][y + k].getColor() == PIECES[x][y].getColor())
                    count++;
                else break;
            } else break;
        }
        for (int k = 1; k <= 4; k++) {

            if (x + k < COLUMN && y - k >= 0) {
                if (PIECES[x + k][y - k].isFallen() &&
                        PIECES[x + k][y - k].getColor() == PIECES[x][y].getColor())
                    count++;
                else break;
            } else break;
        }
        if (count >= 5)
            return true;
        return false;
    }

}
