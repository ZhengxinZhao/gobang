package com.glen.gobang;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ui extends JFrame {

    private final int X = 35;
    private final int Y = 60; //棋盘起绘点坐标
    private final int GRID_SIZE = 50; //棋盘网格大小
    private final int PIECE_SIZE = 46; //棋子直径
    private String battleType = "人人对战";


    public static void main(String[] args) {
        new Ui().initUi();
    }
    public void paint(Graphics g) {
        super.paint(g);
        // 创建缓冲区对象
        Image image = createImage(GRID_SIZE * (Model.COLUMN - 1) + 12, GRID_SIZE * (Model.ROW - 1) + 12);
        Graphics imageGraphics = image.getGraphics();
        imageGraphics.setColor(new Color(55,156,178));

        imageGraphics.fillRect(0,0,image.getWidth(null),image.getHeight(null));
        imageGraphics.setColor(Color.BLACK);
        int X=6,Y = 6;
        //绘棋盘
        for (int i = 0; i < Model.ROW; i++) {
            //画横线
            imageGraphics.drawLine(X, Y + GRID_SIZE * i, X + GRID_SIZE * (Model.COLUMN - 1), Y + GRID_SIZE * i);

        }
        for (int i = 0; i < Model.COLUMN; i++)
            //画竖线
            imageGraphics.drawLine(X + GRID_SIZE * i, Y, X + GRID_SIZE * i, Y + GRID_SIZE * (Model.ROW - 1));
        //棋盘边框
        imageGraphics.setColor(Color.black);
        ((Graphics2D) imageGraphics).setStroke(new BasicStroke(4));
        imageGraphics.drawLine(X - 5, Y - 5, X + GRID_SIZE * (Model.COLUMN - 1) + 5, Y - 5);
        imageGraphics.drawLine(X - 5, Y - 5, X - 5, Y + GRID_SIZE * (Model.ROW - 1) + 5);
        imageGraphics.drawLine(X + GRID_SIZE * (Model.COLUMN - 1) + 5, Y - 5, X + GRID_SIZE * (Model.COLUMN - 1) + 5, Y + GRID_SIZE * (Model.ROW - 1) + 5);
        imageGraphics.drawLine(X - 5, Y + GRID_SIZE * (Model.ROW - 1) + 5, X + GRID_SIZE * (Model.COLUMN - 1) + 5, Y + GRID_SIZE * (Model.ROW - 1) + 5);
        //重绘棋子
        for (int i = 0; i < Model.COLUMN; i++) {
            for(int j = 0;j<Model.ROW;++j){
                if (Model.PIECES[i][j].isFallen()) {
                    Model.PIECES[i][j].draw(imageGraphics, PIECE_SIZE, X+i*GRID_SIZE-PIECE_SIZE/2, Y+j*GRID_SIZE-PIECE_SIZE/2);
                }
            }

        }
        //当前落子标识
        Piece piece = Model.peek();
        if(piece!=null) {
            int x = piece.getX() * GRID_SIZE + X - PIECE_SIZE / 2;
            int y = piece.getY() * GRID_SIZE + Y - PIECE_SIZE / 2;
            imageGraphics.setColor(Color.orange);
            ((Graphics2D) imageGraphics).setStroke(new BasicStroke(3));
            ;
            imageGraphics.drawLine(x, y, x, y + PIECE_SIZE / 4);
            imageGraphics.drawLine(x, y, x + PIECE_SIZE / 4, y);
            imageGraphics.drawLine(x, y + PIECE_SIZE, x, y + 3 * PIECE_SIZE / 4);
            imageGraphics.drawLine(x, y + PIECE_SIZE, x + PIECE_SIZE / 4, y + PIECE_SIZE);
            imageGraphics.drawLine(x + 3 * PIECE_SIZE / 4, y, x + PIECE_SIZE, y);
            imageGraphics.drawLine(x + PIECE_SIZE, y, x + PIECE_SIZE, y + PIECE_SIZE / 4);
            imageGraphics.drawLine(x + 3 * PIECE_SIZE / 4, y + PIECE_SIZE, x + PIECE_SIZE, y + PIECE_SIZE);
            imageGraphics.drawLine(x + PIECE_SIZE, y + 3 * PIECE_SIZE / 4, x + PIECE_SIZE, y + PIECE_SIZE);
        }
        //将缓冲图绘制到界面上
        g.drawImage(image,this.X,this.Y,null);

    }



    public void initUi() {
        //基础界面
        this.setTitle("五子棋");
        this.setSize(1100, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setLayout(null);

        Font ButtonFont = new Font("楷体", 1, 25);

        JButton jb1 = new JButton("新游戏");
        jb1.setFont(ButtonFont);
        jb1.setBounds(860, 100, 140, 50);
        this.add(jb1);

        JButton jb2 = new JButton("悔棋");
        jb2.setFont(ButtonFont);
        jb2.setBounds(860, 200, 140, 50);
        this.add(jb2);

        JComboBox jComboBox = new JComboBox(new String[]{"人机对战","人人对战"});
        jComboBox.setBounds(860,300,140,50);
        jComboBox.setSelectedItem(battleType);
        jComboBox.setFont(ButtonFont);
        this.add(jComboBox);

        ImageIcon icon = new ImageIcon("res/background.jpg");
        JLabel jl1 = new JLabel(icon);
        jl1.setBounds(0, 0, 1100, 775);
        this.add(jl1);

        this.setVisible(true);


        jComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    battleType = e.getItem().toString();
                }
            }
        });

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("悔棋")) {
                    Model.undo();
                    if(battleType.equals("人机对战")){
                        Model.undo();
                    }
                    Ui.this.repaint();
                }
                else if(e.getActionCommand().equals("新游戏")){
                    Model.newGame();
                    Ui.this.repaint();
                }
            }
        };
        jb1.addActionListener(actionListener);
        jb2.addActionListener(actionListener);

        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX(), y = e.getY(), x1 = 0, y1 = 0;
                //预备落子点的行列
                int r = 0, c = 0;
                if (!(x >= X && x <= X + GRID_SIZE * (Model.COLUMN - 1) && y >= Y && y <= Y + GRID_SIZE * (Model.ROW - 1)))
                    return;

                //找到离点击点最近的交叉点
                c = (x - X) / GRID_SIZE;
                int temp = (x - X) % GRID_SIZE;
                if (temp > GRID_SIZE / 3 && temp < GRID_SIZE * 2 / 3) return;
                if (temp >= GRID_SIZE * 2 / 3) ++c;

                r = (y - Y) / GRID_SIZE;
                temp = (y - Y) % GRID_SIZE;
                if (temp > GRID_SIZE / 3 && temp < GRID_SIZE * 2 / 3) return;
                if (temp >= GRID_SIZE * 2 / 3) ++r;

                //落子
                Model.downPiece(c,r);

                Ui.this.repaint();
                //落子后，判断输赢
                if (Model.weatherWin(c, r)) {

                    String str = Model.getCurrentPlayer() ? "黑棋胜利！": "白棋胜利！" ;
                    JOptionPane.showMessageDialog(null, str);
                    return;
                }

                //是否人机对战
                if (battleType.equals("人机对战")) {
                    int[] coordinate = Ai.AI_Play();

                    c = coordinate[0];
                    r = coordinate[1];
                    //落子
                    Model.downPiece(c,r);

                    Ui.this.repaint();
                    //落子后，判断输赢
                    if (Model.weatherWin(c, r)) {

                        String str = Model.getCurrentPlayer() ? "黑棋胜利！": "白棋胜利！";
                        JOptionPane.showMessageDialog(null, str);
                        return;
                    }


                }


            }

        };
        this.addMouseListener(mouseListener);



        //选择对战方式提示框
        JFrame choice_tip = new JFrame();
        choice_tip.setSize(400, 250);
        choice_tip.setLocationRelativeTo(null);
        choice_tip.setResizable(false);
        choice_tip.setLayout(null);

        Font font = new Font("楷体", 1, 18);
        JButton pvp = new JButton("人人对战");
        pvp.setFont(font);
        pvp.setBounds(30, 50, 150, 60);
        choice_tip.add(pvp);

        JButton pvc = new JButton("人机对战");
        pvc.setFont(font);
        pvc.setBounds(220, 50, 150, 60);
        choice_tip.add(pvc);

//        pvp.addActionListener(cal);
//        pvc.addActionListener(cal);
//        cal.choice_tip = choice_tip;
    }
}
