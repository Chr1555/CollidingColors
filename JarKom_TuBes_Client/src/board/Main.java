package board;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author yohanes
 */
public class Main extends JFrame{
    public static void main(String[] args) {
        new Main();
    }
    public Main() {
        super("---COLLIDING COLORS---");
        initMain();
    }

    public void initMain() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 428);
        setLocation(400, 30);
        setLayout(null);
        setResizable(false);
        
        pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBackground(Color.black);
        pnl.setBounds(0, 0, 400, 400);
        
        
        home = new JLabel();
        home.setLayout(null);
        home.setBounds(0,0,400,400);
        home.setIcon(new ImageIcon(resize("src/image/background.png")));
          
        lbl = new JLabel();
        lbl.setText("Start Game");
        lbl.setLayout(null);
        lbl.setBounds(125, 180, 250, 250);
        lbl.setFont(new Font("Eras Demi ITC", Font.PLAIN, 27));
        lbl.setForeground(Color.BLACK);
        lbl.setVisible(true);
        pnl.add(lbl);
        pnl.add(home);
        add(pnl);
        setVisible(true);
        
        lbl.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent arg0) {
                new Game();
                dispose();                
            }
            
        });
        
    }
    public static Image resize(String url) {

        Image dimg = null;
        try {
                BufferedImage img = ImageIO.read(new File(url));
                dimg = img.getScaledInstance(394, 400, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
                ex.printStackTrace(System.err);
        }

        return dimg;
    }
    private JPanel pnl;
    private JLabel home;
    private JLabel lbl;
    private JLabel title;
}
