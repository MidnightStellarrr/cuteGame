import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameCharacter extends JPanel implements ActionListener {
    private int x = 170, y = 200;
    private int velocityY = 0;
    private boolean isJumping = false;
    private boolean isSitting = false;
    private Timer timer;
    private Image characterImage;
    private Image sittingImage;
    private JLabel characterLabel;
    
    // Different states for the character
    private final int GROUND_Y = 210;
    private final int SIT_Y = 210;
    
    public GameCharacter() {
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(500, 600));
        
        // Create character images
        characterImage = createCharacterImage(false);
        sittingImage = createCharacterImage(true);
        characterLabel = new JLabel(new ImageIcon(characterImage));
        characterLabel.setBounds(x, y, 50, 50);
        add(characterLabel);
        
        // Timer for animation loop
        timer = new Timer(16, this);
        timer.start();
        
        // Add key listener for controls
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRight();
                        break;
                    case KeyEvent.VK_UP:
                        jump();
                        break;
                    case KeyEvent.VK_DOWN:
                        toggleSit();
                        break;
                    case KeyEvent.VK_SPACE:
                        jump();
                        break;
                }
            }
        });
    }
    
    private Image createCharacterImage(boolean sitting) {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (sitting) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(0, 15, 50, 35);
            
            g2d.setColor(Color.WHITE);
            g2d.fillOval(10, 25, 10, 10);
            g2d.fillOval(30, 25, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.fillOval(13, 28, 5, 5);
            g2d.fillOval(33, 28, 5, 5);
            
            g2d.setColor(Color.BLACK);
            g2d.drawLine(15, 40, 35, 40);
        } else {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(0, 0, 50, 50);
            
            g2d.setColor(Color.WHITE);
            g2d.fillOval(10, 15, 10, 10);
            g2d.fillOval(30, 15, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.fillOval(13, 18, 5, 5);
            g2d.fillOval(33, 18, 5, 5);
            
            g2d.setColor(Color.BLACK);
            g2d.drawArc(15, 25, 20, 15, 0, -180);
        }
        
        g2d.dispose();
        return image;
    }
    
    public void moveLeft() {
        if (x > 0) {
            x -= 20;
            updateCharacterPosition();
        }
    }
    
    public void moveRight() {
        if (x < getWidth() - 50) {
            x += 20;
            updateCharacterPosition();
        }
    }
    
    public void jump() {
        if (!isJumping && !isSitting) {
            isJumping = true;
            velocityY = -15;
        }
    }
    
    public void toggleSit() {
        if (!isJumping) {
            if (isSitting) {
                standUp();
            } else {
                sit();
            }
        }
    }
    
    public void sit() {
        if (!isJumping && !isSitting) {
            isSitting = true;
            y = SIT_Y;
            characterLabel.setIcon(new ImageIcon(sittingImage));
            characterLabel.setBounds(x, y, 50, 35);
            updateCharacterPosition();
        }
    }
    
    public void standUp() {
        if (isSitting && !isJumping) {
            isSitting = false;
            y = GROUND_Y;
            characterLabel.setIcon(new ImageIcon(characterImage));
            characterLabel.setBounds(x, y, 50, 50);
            updateCharacterPosition();
        }
    }
    
    private void updateCharacterPosition() {
        characterLabel.setLocation(x, y);
        characterLabel.repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            velocityY += 1;
            y += velocityY;
            
            if (y >= GROUND_Y) {
                y = GROUND_Y;
                isJumping = false;
                velocityY = 0;
            }
            
            updateCharacterPosition();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Character Demo");
            
            // Use BorderLayout for the main frame
            frame.setLayout(new BorderLayout());
            
            // Create the game panel
            GameCharacter game = new GameCharacter();
            
            // ----- LEFT PANEL (WEST) -----
            JPanel leftPanel = new JPanel();
            leftPanel.setBackground(Color.DARK_GRAY);
            leftPanel.setPreferredSize(new Dimension(50, 500));
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // ----- RIGHT PANEL (EAST) -----
            JPanel rightPanel = new JPanel();
            rightPanel.setBackground(Color.DARK_GRAY);
            rightPanel.setPreferredSize(new Dimension(50, 500));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // ----- TOP PANEL (NORTH) -----
            JPanel topPanel = new JPanel();
            topPanel.setBackground(Color.DARK_GRAY);
            topPanel.setPreferredSize(new Dimension(500, 50));
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // ----- BOTTOM PANEL (SOUTH) - UNIFIED CROSS! -----
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.DARK_GRAY);
            bottomPanel.setPreferredSize(new Dimension(500, 200));
            bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 1));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            
            // Create cross panel - holds the unified cross
            JPanel crossPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw the cross background (dark gray)
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
            };
            crossPanel.setBackground(Color.DARK_GRAY);
            crossPanel.setLayout(new GridBagLayout());
            crossPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            crossPanel.setPreferredSize(new Dimension(180, 180));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 0); // NO GAPS!
            
            // Create buttons - they will form a unified D-pad cross
            JButton upBtn = new JButton("▲");
            JButton downBtn = new JButton("▼");
            JButton leftBtn = new JButton("◄");
            JButton rightBtn = new JButton("►");
            
            // Create the center non-clickable square
            JPanel centerSquare = new JPanel();
            centerSquare.setBackground(new Color(70, 70, 70));
            centerSquare.setPreferredSize(new Dimension(56, 56));
            centerSquare.setBorder(null);
            
            Dimension btnSize = new Dimension(56, 56);
            Font btnFont = new Font("Arial", Font.BOLD, 22);
            Color btnColor = new Color(70, 70, 70);
            Color borderColor = Color.WHITE;
            
            // Style UP button - remove BOTTOM border
            upBtn.setPreferredSize(btnSize);
            upBtn.setMinimumSize(btnSize);
            upBtn.setMaximumSize(btnSize);
            upBtn.setFont(btnFont);
            upBtn.setBackground(btnColor);
            upBtn.setForeground(Color.WHITE);
            upBtn.setFocusPainted(false);
            upBtn.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, borderColor)); // No bottom border
            upBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            upBtn.setOpaque(true);
            upBtn.setContentAreaFilled(true);
            
            // Style DOWN button - remove TOP border
            downBtn.setPreferredSize(btnSize);
            downBtn.setMinimumSize(btnSize);
            downBtn.setMaximumSize(btnSize);
            downBtn.setFont(btnFont);
            downBtn.setBackground(btnColor);
            downBtn.setForeground(Color.WHITE);
            downBtn.setFocusPainted(false);
            downBtn.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, borderColor)); // No top border
            downBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            downBtn.setOpaque(true);
            downBtn.setContentAreaFilled(true);
            
            // Style LEFT button - remove RIGHT border
            leftBtn.setPreferredSize(btnSize);
            leftBtn.setMinimumSize(btnSize);
            leftBtn.setMaximumSize(btnSize);
            leftBtn.setFont(btnFont);
            leftBtn.setBackground(btnColor);
            leftBtn.setForeground(Color.WHITE);
            leftBtn.setFocusPainted(false);
            leftBtn.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 0, borderColor)); // No right border
            leftBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            leftBtn.setOpaque(true);
            leftBtn.setContentAreaFilled(true);
            
            // Style RIGHT button - remove LEFT border
            rightBtn.setPreferredSize(btnSize);
            rightBtn.setMinimumSize(btnSize);
            rightBtn.setMaximumSize(btnSize);
            rightBtn.setFont(btnFont);
            rightBtn.setBackground(btnColor);
            rightBtn.setForeground(Color.WHITE);
            rightBtn.setFocusPainted(false);
            rightBtn.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, borderColor)); // No left border
            rightBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rightBtn.setOpaque(true);
            rightBtn.setContentAreaFilled(true);
            
            // Position buttons in cross pattern with center square
            // UP button (top center)
            gbc.gridx = 1;
            gbc.gridy = 0;
            crossPanel.add(upBtn, gbc);
            
            // LEFT button (middle left)
            gbc.gridx = 0;
            gbc.gridy = 1;
            crossPanel.add(leftBtn, gbc);
            
            // CENTER SQUARE (middle center - not clickable)
            gbc.gridx = 1;
            gbc.gridy = 1;
            crossPanel.add(centerSquare, gbc);
            
            // RIGHT button (middle right)
            gbc.gridx = 2;
            gbc.gridy = 1;
            crossPanel.add(rightBtn, gbc);
            
            // DOWN button (bottom center)
            gbc.gridx = 1;
            gbc.gridy = 2;
            crossPanel.add(downBtn, gbc);
            
            // Add action listeners
            upBtn.addActionListener(e -> game.jump());
            downBtn.addActionListener(e -> game.toggleSit());
            leftBtn.addActionListener(e -> game.moveLeft());
            rightBtn.addActionListener(e -> game.moveRight());
            
            // Add cross panel to bottom panel
            bottomPanel.add(crossPanel);

            // ----- ADD ALL PANELS TO FRAME -----
            frame.add(leftPanel, BorderLayout.WEST);
            frame.add(rightPanel, BorderLayout.EAST);
            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(game, BorderLayout.CENTER);
            frame.add(bottomPanel, BorderLayout.SOUTH);

            // ----- FRAME SETTINGS -----
            frame.setSize(500, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            // Request focus for keyboard input
            game.requestFocusInWindow();
        });
    }
}