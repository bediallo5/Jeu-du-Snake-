import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private static final int BOX = 25;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final ArrayList<Point> snake = new ArrayList<>();
    private String direction = "RIGHT";
    private Point food;
    private int score = 0;

    private final Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        snake.add(new Point(9 * BOX, 10 * BOX));
        spawnFood();

        timer = new Timer(100, this);
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(WIDTH / BOX) * BOX;
            y = rand.nextInt(HEIGHT / BOX) * BOX;
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessin du serpent
        for (int i = 0; i < snake.size(); i++) {
            if (i == 0) g.setColor(Color.GREEN);
            else g.setColor(new Color(0, 150, 0));
            g.fillRect(snake.get(i).x, snake.get(i).y, BOX, BOX);
        }

        // Dessin de la nourriture
        g.setColor(Color.RED);
        g.fillRect(food.x, food.y, BOX, BOX);

        // Affichage du score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        checkCollision();
        repaint();
    }

    private void move() {
        Point head = snake.get(0);
        int x = head.x;
        int y = head.y;

        // Switch classique pour la direction
        switch (direction) {
            case "LEFT":
                x -= BOX;
                break;
            case "UP":
                y -= BOX;
                break;
            case "RIGHT":
                x += BOX;
                break;
            case "DOWN":
                y += BOX;
                break;
        }

        Point newHead = new Point(x, y);

        if (newHead.equals(food)) {
            score++;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }

        snake.add(0, newHead);
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Collision avec les murs
        if (head.x < 0 || head.y < 0 || head.x >= WIDTH || head.y >= HEIGHT) {
            gameOver();
        }

        // Collision avec le corps
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                break;
            }
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) direction = "LEFT";
        else if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) direction = "UP";
        else if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) direction = "RIGHT";
        else if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) direction = "DOWN";
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public void startGame() {
        score = 0;
        direction = "RIGHT";
        snake.clear();
        snake.add(new Point(9 * BOX, 10 * BOX));
        spawnFood();
        requestFocusInWindow(); // Assure le focus clavier
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton startButton = new JButton("Démarrer");
        JButton quitButton = new JButton("Quitter");

        startButton.setFocusable(false);
        quitButton.setFocusable(false);

        startButton.addActionListener(e -> gamePanel.startGame());
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        frame.setLayout(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Démarrage automatique
        gamePanel.startGame();
    }
}

