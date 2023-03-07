import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import static java.lang.Math.round;

public class GUI {

    public static void changeTitle(int x){
        if (x == 0){
            f.setTitle("Chinese Checkers");
        }else{
            f.setTitle("Computer Thinking");
        }
    }
    public static void endOption(int x){
        if (x == 0){
            JOptionPane.showMessageDialog(f, "End Game\nPlayer Won");
        }else{
            JOptionPane.showMessageDialog(f, "End Game\nComputer Won");
        }
    }
    public static int inputInit() {

        JRadioButton button1 = new JRadioButton("1");
        button1.setMnemonic(KeyEvent.VK_1);
        button1.setActionCommand("1");
        JRadioButton button2 = new JRadioButton("3");
        button2.setMnemonic(KeyEvent.VK_3);
        button2.setActionCommand("3");
        JRadioButton button3 = new JRadioButton("5");
        button3.setMnemonic(KeyEvent.VK_5);
        button3.setActionCommand("5");
        ButtonGroup group = new ButtonGroup();
        group.add(button1);
        group.add(button2);
        group.add(button3);

        button1.setBounds(40,10,50,50);
        button2.setBounds(130,10,50,50);
        button3.setBounds(220,10,50,50);

        int input;
        int result = JOptionPane.showOptionDialog(f,new Object[] {button1,button2,button3},"Input Depth",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
        if (result == JOptionPane.OK_OPTION){
            if (button1.isSelected()) {
                input = 1;
            } else if (button2.isSelected()) {
                input = 3;
            }else if(button3.isSelected()){
                input =5;
            }
            else {
                input = -1;
            }
        }else{
            input = -1;
        }
        return input;
    }

    public static void initialize(Board board) {

        f = new JFrame();
        f.setBounds(0, 0, 484, 570);
        f.setTitle("Chinese Checkers");
        ImageIcon icon = new ImageIcon("images/board.png");

        background = new JLabel(icon);
        background.setBounds(0, 0, 487, 570);
        background.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                System.out.printf("%s %n", me.getPoint());
            }
        });

        currBoard = new Board(board.nodes);
        lastBoard = new Board(board.nodes);
        showBoard(board);

        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    static JLabel background;
    public static JFrame f;
    public static ArrayList<Marbel> selected = new ArrayList<>();

    public static Board currBoard, lastBoard;

    public static void inValid() {
        showBoard(lastBoard);
        currBoard = new Board(lastBoard.nodes);
        JOptionPane.showMessageDialog(f, "Please Make A Valid Move");
    }

    public static void showBoard(Board board) {
        f.getContentPane().removeAll();
        for (Node n : board.nodes) {
            Marbel mx = new Marbel(n);
            f.add(mx);
        }
        f.add(background);
        SwingUtilities.updateComponentTreeUI(f);
    }

    public static void setCurrBoard(Board board) {
        lastBoard = currBoard;
        currBoard = new Board(board.nodes);
    }

    public static boolean moveMade = false;
    public static boolean takeMove = false;

    public static Board getUserBoard() {
        takeMove = true;
        while (!moveMade) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        moveMade = false;
        return currBoard;
    }

    public static void swap() {
        Marbel m2 = selected.get(0);
        Marbel m1 = selected.get(1);
        lastBoard = currBoard;
        currBoard = currBoard.swap(m1.n, m2.n);
        showBoard(currBoard);
        selected.clear();
        moveMade = true;
    }

    static class Marbel extends JLabel {
        Node n;
        Marbel s = this;

        Marbel(Node n) {
            super(new ImageIcon(new ImageIcon(n.getC().iconPath).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)));
            double r = 16.5;
            this.n = n;
            int wh = 33;
            double x;
            double y;
            double offsetx = 17.6;
            double offsety = 31;

            y = 513 - (n.getID()[0] - 1) * offsety;
            x = 447 - (n.getID()[1] + n.getID()[2] - 6) * offsetx;
            this.setBounds((int) round(x - r), (int) round(y - r), wh, wh);
            this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    if (takeMove) {
                        selected.add(s);
                        if (selected.size() == 2) {
                            swap();
                            takeMove = false;
                        }
                    }
                }
            });
        }
    }

}
