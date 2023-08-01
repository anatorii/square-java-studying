import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Square {
    public static void main(String[] args) throws IOException {
        new MainWindow();
    }
}

class MainWindow extends JFrame {

    MainPanel panel;

    public MainWindow () throws IOException {
        super("square");

        panel = new MainPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(800, 600));

        this.setLocation(100, 60);

        this.pack();

        this.setVisible(true);

        this.add(panel);

        panel.addComponentListener(new MainPanelListener());

        Timer timer = new Timer(100, new PaintPanelListener(panel));
        timer.start();
    }
}

class MainPanel extends JPanel {
    BufferedImage image;
    int x, y, dx, dy;
    int squareSide;
    int directionX, directionY;
    boolean init;

    public MainPanel() {
        super();

        init = false;

        try {
            image = ImageIO.read(new File("duck.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initDimensions() {
        x = 0; y = 0;
        squareSide = Math.min(getWidth(), getHeight());
        dx = getWidth() / 40;
        dy = getHeight() / 40;
        directionX = 1; directionY = 0;
        init = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!init) {
            initDimensions();
        }

        if (directionX == 1 && directionY == 0) {
            if ((x + dx * directionX) > (getWidth() - image.getWidth())) {
                directionX = 0;
                directionY = 1;
            }
        } else if (directionX == 0 && directionY == 1) {
            if ((y + dy * directionY) > (getHeight() - image.getHeight())) {
                directionX = -1;
                directionY = 0;
            }
        } else if (directionX == -1 && directionY == 0) {
            if (x + dx * directionX < 0) {
                directionX = 0;
                directionY = -1;
            }
        } else if (directionX == 0 && directionY == -1) {
            if (y + dy * directionY < 0) {
                directionX = 1;
                directionY = 0;
            }
        }

        x += dx * directionX; y += dy * directionY;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, x, y, this);
    }

    public void repaintImage() {
        repaint();
    }
}

class PaintPanelListener implements ActionListener {

    JPanel panel;

    public PaintPanelListener(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((MainPanel) panel).repaintImage();
    }
}

class MainPanelListener extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        ((MainPanel) e.getComponent()).init = false;
    }
}
