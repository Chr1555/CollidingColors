package board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import socket.io.player;

/**
 *
 * @author Yohanes
 */
public class Game extends JFrame {
    public player pemain;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        super("---COLLIDING COLORS---");
        pemain = new player("http://localhost:3000", this);
        pemain.create();
        initGame();
    }

    public void initGame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 610);
        setLocation(400, 5);
        setLayout(null);
        setResizable(false);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 900, 610);
        panel.setBackground(Color.DARK_GRAY);

        papan = new JPanel();
        papan.setLayout(null);
        papan.setBounds(10, 10, 555, 555);
        papan.setBackground(Color.BLACK);

        pnl = new JPanel[100];
        int x1 = 5, y1 = 5,temp=0,temp2=0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                pnl[temp2] = new JPanel();
                pnl[temp2].setBackground(Color.white);
                pnl[temp2].setBounds(x1, y1, 50, 50);
                x1 += 55;
                papan.add(pnl[temp2]);
                temp2++;
            }
            x1 = 5; y1 += 55;
        }

        for (int k = 0; k < 10; k++) {
            for (int l = 0; l < 10; l++) {
                int temp3 = temp;
                pnl[temp3].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent arg0) {
                        pemain.choose(temp3);
                    }
                });
                temp++;
            }
        }

        //PANEL SCORE
        score = new JPanel();
        score.setLayout(null);
        score.setBounds(575, 360, 150, 200);
        score.setBackground(Color.BLACK);
        
        JLabel title = new JLabel();
        title.setForeground(Color.WHITE);
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        title.setText("SCORE:");
        title.setBounds(10, 10, 100, 20);
        score.add(title);
        
        lS = new JPanel[4];
        lS2 = new JLabel[4];
        rS = new JLabel[4];
        int hasil=0, x = 10, y = 40;
        for (int i = 0; i < 4; i++) {
            
            lS2[i] = new JLabel();
            lS2[i].setForeground(Color.RED);
            lS2[i].setText("Player " + (i + 1));
            
            lS[i] = new JPanel();
            lS[i].setBackground(Color.yellow);
            lS[i].setBounds(x, y, 60, 30);
            
            rS[i] = new JLabel();
            rS[i].setForeground(Color.WHITE);
            rS[i].setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            rS[i].setText("0");
            rS[i].setBounds(x+70, y-1, 50, 30);
            
            score.add(rS[i]);
            y += 40;
            lS[i].add(lS2[i]);
            score.add(lS[i]);
        }
        ////////////////////////////////////////////////////////////////
        
        //PANEL TURN
        turn = new JPanel();
        turn.setLayout(null);
        turn.setBounds(740, 360, 140, 200);
        turn.setBackground(Color.BLACK);
        
        JLabel txtTurn1 = new JLabel();
        txtTurn1.setForeground(Color.WHITE);
        txtTurn1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 17));
        txtTurn1.setText("YOUR COLOR");
        txtTurn1.setBounds(20, 10, 100, 20);
        turn.add(txtTurn1);
        
        pnlTurn1 = new JPanel();
        pnlTurn1.setBounds(20, 40, 100, 50);
        pnlTurn1.setBackground(Color.BLUE);
        turn.add(pnlTurn1);
        
        JLabel txtTurn2 = new JLabel();
        txtTurn2.setForeground(Color.WHITE);
        txtTurn2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        txtTurn2.setText("TURN NOW");
        txtTurn2.setBounds(20, 110, 100, 20);
        turn.add(txtTurn2);
        
        pnlTurn2 = new JPanel();
        pnlTurn2.setBounds(20, 140, 100, 50);
        pnlTurn2.setBackground(Color.BLUE);
        turn.add(pnlTurn2);
        ///////////////////////////////////////////////////////////////////
        tA = new JTextArea();
        tA.setBounds(575, 10, 305, 300);
        tA.setBackground(Color.WHITE);
        tA.setForeground(Color.BLACK);
        tA.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        panel.add(tA);
        
        tF = new JTextField();
        tF.setBounds(575, 320, 230, 30);
        tA.setBackground(Color.WHITE);
        tA.setForeground(Color.BLACK);
        tF.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        panel.add(tF);
        
        send = new JButton();
        send.setBounds(815,320,65,30);
        send.setBackground(Color.YELLOW);
        send.setForeground(Color.RED);
        send.setText("Send");
        panel.add(send);
        
        send.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                send();
            }
        });
        
        panel.add(score);
        panel.add(turn);
        panel.add(papan);
        setVisible(true);
        add(panel);
    }
    
    public void send(){
        String message = tF.getText();
        pemain.send(message);
    }
    
    public JOptionPane jo;
    
    public JButton send;
    public static JTextArea tA;
    public JTextField tF;
    public JPanel panel;
    public JPanel lS[];
    public JLabel lS2[];
    public JLabel rS[];
    
    public JPanel score;
    public JPanel turn;
    public JPanel pnlTurn1;
    public JPanel pnlTurn2;
    public JPanel symbol;
    public JPanel papan;
    public JPanel pnl[];
}