package com.glen.gobang;

import java.util.HashMap;
import java.util.Map;

public class Ai {
    public static Map<String, Integer> map = new HashMap<String, Integer>();

    //存储权重的数组
    public static int[][] weight = new int[Model.COLUMN][Model.ROW];

    //得到AI落子点
    public static int[] AI_Play() {
        return theBestCoordinate();
    }

    //对于单个权重数组最佳落子点
    private static int[] theBestCoordinate() {
        weight_Calculator();
        int max_weight = 0;
        int[] coordinate = new int[2];
        for (int i = 0; i < Model.COLUMN; ++i) {
            for (int j = 0; j < Model.ROW; ++j) {

                if (weight[i][j] > max_weight) {
                    coordinate[0] = i;
                    coordinate[1] = j;
                    max_weight = weight[i][j];
                }
            }
        }

        return coordinate;
    }

    //统计权重
    private static void weight_Calculator() {
        //确定执子颜色
        boolean aiColor = Model.getCurrentPlayer();
        boolean peoColor = !aiColor;

        for (int i = 0; i < Model.COLUMN; ++i) {
            for (int j = 0; j < Model.ROW; ++j) {
                //将之前的权重信息清除
                weight[i][j] = 0;
                //如果该点没有棋子，则计算权重，如果有，该位置权重为0

                if (!Model.PIECES[i][j].isFallen()) {
                    StringBuffer codeAI, codePeo;
                    //水平方向
                    codeAI = code1(i,j, aiColor);

                    codePeo = code1(i, j,peoColor);
                    Integer value1 = map.get(codeAI.toString());
                    Integer value2 = map.get(codePeo.toString());

                    if (value1 != null)
                        weight[i][j] += value1;
                    if (value2 != null)
                        weight[i][j] += value2;

                    //竖直方向
                    codeAI = code2(i,j, aiColor);

                    codePeo = code2(i, j,peoColor);

                    value1 = map.get(codeAI.toString());
                    value2 = map.get(codePeo.toString());
                    if (value1 != null)
                        weight[i][j] += value1;
                    if (value2 != null)
                        weight[i][j] += value2;

                    //斜1
                    codeAI = code3(i,j, aiColor);

                    codePeo = code3(i, j,peoColor);


                    value1 = map.get(codeAI.toString());
                    value2 = map.get(codePeo.toString());
                    if (value1 != null)
                        weight[i][j] += value1;
                    if (value2 != null)
                        weight[i][j] += value2;

                    //斜2
                    codeAI = code4(i,j, aiColor);

                    codePeo = code4(i, j,peoColor);

                    value1 = map.get(codeAI.toString());
                    value2 = map.get(codePeo.toString());
                    if (value1 != null)
                        weight[i][j] += value1;
                    if (value2 != null)
                        weight[i][j] += value2;
                }
                else weight[i][j] = -1;
            }
        }

    }

    //0代表无子，1代表同色子，2代表异色子
    //水平方向棋子排列
    private static StringBuffer code1(int x,int y, boolean color) {

        StringBuffer str = new StringBuffer("222202222");
        for (int k = -4; k <= 4; k++) {
            if (x + k >= 0 && x + k < Model.COLUMN) {
                if (!Model.PIECES[x+ k][y].isFallen()) {
                    str.setCharAt(4 + k, (char) 48);
                } else if (Model.PIECES[x+ k][y].getColor()==color) {
                    str.setCharAt(4 + k, (char) 49);
                }
            }
            else continue;
        }
        return str;
    }

    //竖直方向棋子排列
    private static StringBuffer code2(int x,int y, boolean color) {
        StringBuffer str = new StringBuffer("222202222");
        for (int k = -4; k <= 4; k++) {
            if (y + k  >= 0 && y + k  < Model.ROW ) {
                if (!Model.PIECES[x][y+k].isFallen()) {
                    str.setCharAt(4 + k, (char) 48);
                } else if (Model.PIECES[x][y+k].getColor()==color) {
                    str.setCharAt(4 + k, (char) 49);
                }
            }
            else continue;
        }
        return str;
    }

    //斜1方向棋子排列
    private static StringBuffer code3(int x,int y, boolean color) {
        StringBuffer str = new StringBuffer("222202222");
        for (int k = -4; k <= 4; k++) {
            if (x + k  >= 0 && y + k >= 0 && x + k < Model.COLUMN && y + k  < Model.ROW) {
                if (!Model.PIECES[x+k][y+k].isFallen()) {
                    str.setCharAt(4 + k, (char) 48);
                } else if (Model.PIECES[x+k][y+k].getColor()==color) {
                    str.setCharAt(4 + k, (char) 49);
                }
            } else
                continue;
        }
        return str;
    }

    //斜2方向棋子排列
    private static StringBuffer code4(int x,int y, boolean color) {
        StringBuffer str = new StringBuffer("222202222");
        for (int k = -4; k <= 4; k++) {
            if (x + k  >= 0 && y - k >= 0 && x + k < Model.COLUMN && y - k  < Model.ROW) {
                if (!Model.PIECES[x+k][y-k].isFallen()) {
                    str.setCharAt(4 + k, (char) 48);
                } else if (Model.PIECES[x+k][y-k].getColor()==color) {
                    str.setCharAt(4 + k, (char) 49);
                }
            } else
                continue;
        }
        return str;
    }

    static {
        //落子连五
        int weight5 = 100000;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++)
                    for (int k = 0; k < 3; k++) {
                        //"11110****"
                        StringBuffer str1 = new StringBuffer("11110");
                        str1.append(i).append(j).append(g).append(k);
                        setmap(str1, weight5);
                        //"*11101***"
                        StringBuffer str2 = new StringBuffer(((Integer) i).toString());
                        str2.append("11101").append(j).append(g).append(k);
                        setmap(str2, weight5);
                        //"**11011**"
                        StringBuffer str3 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                        str3.append("11011").append(j).append(g).append(k);
                        setmap(str3, weight5);
                    }
        //落子活四
        int weight4_alive = 10000;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++) {
                    //"011100***"
                    StringBuffer str1 = new StringBuffer("011100");
                    str1.append(i).append(j).append(g);
                    setmap(str1, weight4_alive);
                    //"*011010**"
                    StringBuffer str2 = new StringBuffer(((Integer) i).toString());
                    str2.append("011010").append(j).append(g);
                    setmap(str2, weight4_alive);
                }
        //落子冲四
        int weight4_sleep = 1000;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++) {
                    //"211100***"
                    StringBuffer str1 = new StringBuffer("211100");
                    str1.append(i).append(j).append(g);
                    setmap(str1, weight4_sleep);
                    //"*211010**"
                    StringBuffer str2 = new StringBuffer(((Integer) i).toString());
                    str2.append("211010").append(j).append(g);
                    setmap(str2, weight4_sleep);
                    //"**210110*"
                    StringBuffer str3 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                    str3.append("210110").append(g);
                    setmap(str3, weight4_sleep);

                    for (int k = 0; k < 3; k++) {
                        //"**11001**"
                        StringBuffer str4 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                        str4.append("11001").append(g).append(k);
                        setmap(str4, weight4_sleep);
                        //"*11001***"
                        StringBuffer str5 = new StringBuffer(((Integer) i).toString());
                        str5.append("11001").append(j).append(g).append(k);
                        setmap(str5, weight4_sleep);
                    }

                }

        //落子活三
        int weight3_alive = 700;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++)
                    for (int k = 0; k < 3; k++) {
                        //"*01100***"
                        StringBuffer str1 = new StringBuffer(((Integer) i).toString());
                        str1.append("01100").append(j).append(g).append(k);
                        setmap(str1, weight3_alive);
                        //"**01010**"
                        StringBuffer str2 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                        str2.append("01010").append(g).append(k);
                        setmap(str2, weight3_alive);
                    }


        //落子眠三//待补
        int weight3_sleep = 100;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++) {
                    //"*210010**"
                    StringBuffer str4 = new StringBuffer(((Integer) i).toString());
                    str4.append("210010").append(j).append(g);
                    setmap(str4, weight3_sleep);
                    //"**210010*"
                    StringBuffer str5 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                    str5.append("210010").append(g);
                    setmap(str5, weight3_sleep);
                    for (int k = 0; k < 3; k++) {
                        //"*21100***"
                        StringBuffer str1 = new StringBuffer(((Integer) i).toString());
                        str1.append("21100").append(j).append(g).append(k);
                        setmap(str1, weight3_sleep);
                        //"**21010**"
                        StringBuffer str2 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                        str2.append("21010").append(g).append(k);
                        setmap(str2, weight3_sleep);
                        //"***20110*"
                        StringBuffer str3 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString() + ((Integer) g).toString());
                        str3.append("20110").append(k);
                        setmap(str3, weight3_sleep);
                    }
                }
        //落子活二
        int weight2_alive = 20;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++)
                    for (int k = 0; k < 3; k++)
                        for (int h = 0; h < 3; h++) {
                            //"**0100***"
                            StringBuffer str1 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                            str1.append("0100").append(g).append(k).append(h);
                            setmap(str1, weight2_alive);
                        }
        //落子眠二
        int weight2_sleep = 5;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int g = 0; g < 3; g++)
                    for (int k = 0; k < 3; k++)
                        for (int h = 0; h < 3; h++) {
                            //"**2100***"
                            StringBuffer str1 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString());
                            str1.append("2100").append(g).append(k).append(h);

                            setmap(str1, weight2_sleep);
                            //"***2010**"
                            StringBuffer str2 = new StringBuffer(((Integer) i).toString() + ((Integer) j).toString() + ((Integer) g).toString());
                            str2.append("2010").append(k).append(h);
                            setmap(str2, weight2_sleep);
                        }

    }

    public static void setmap(StringBuffer str, int weight) {
        if (!map.containsKey(str.toString()))
            map.put(str.toString(), weight);
        str.reverse();
        if (!map.containsKey(str.toString()))
            map.put(str.toString(), weight);
    }
}
