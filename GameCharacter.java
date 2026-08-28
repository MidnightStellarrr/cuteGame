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
    
    // Expression counter
    private int expressionIndex = 0;
    private final String[] expressions = {"😊", "😢", "😮", "😠", "😎"};
    
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
            
            // Draw different expressions based on expressionIndex
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            
            switch (expressionIndex) {
                case 0: // Happy 😊
                    g2d.drawArc(15, 25, 20, 15, 0, -180);
                    break;
                case 1: // Sad 😢
                    g2d.drawArc(15, 30, 20, 15, 0, 180);
                    break;
                case 2: // Surprised 😮
                    g2d.drawOval(17, 28, 16, 12);
                    break;
                case 3: // Angry 😠
                    g2d.drawLine(12, 30, 20, 35);
                    g2d.drawLine(30, 30, 38, 35);
                    g2d.drawArc(12, 25, 26, 15, 0, 180);
                    break;
                case 4: // Cool 😎
                    g2d.drawLine(10, 28, 40, 28);
                    g2d.drawArc(15, 25, 20, 15, 0, -180);
                    break;
                default:
                    g2d.drawArc(15, 25, 20, 15, 0, -180);
                    break;
            }
        }
        
        g2d.dispose();
        return image;
    }
    
    public void changeExpression() {
        expressionIndex = (expressionIndex + 1) % expressions.length;
        // Update the character image with new expression
        characterImage = createCharacterImage(false);
        if (!isSitting) {
            characterLabel.setIcon(new ImageIcon(characterImage));
        } else {
            // If sitting, update sitting image too
            sittingImage = createCharacterImage(true);
            characterLabel.setIcon(new ImageIcon(sittingImage));
        }
        System.out.println("Expression: " + expressions[expressionIndex]);
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
            sittingImage = createCharacterImage(true);
            characterLabel.setIcon(new ImageIcon(sittingImage));
            characterLabel.setBounds(x, y, 50, 35);
            updateCharacterPosition();
        }
    }
    
    public void standUp() {
        if (isSitting && !isJumping) {
            isSitting = false;
            y = GROUND_Y;
            characterImage = createCharacterImage(false);
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
            leftPanel.setBackground(new Color(245, 240, 235));
            leftPanel.setPreferredSize(new Dimension(50, 500));
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // ----- RIGHT PANEL (EAST) -----
            JPanel rightPanel = new JPanel();
            rightPanel.setBackground(new Color(245, 240, 235));
            rightPanel.setPreferredSize(new Dimension(50, 500));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // ----- TOP PANEL (NORTH) -----
            JPanel topPanel = new JPanel();
            topPanel.setBackground(new Color(245, 240, 235));
            topPanel.setPreferredSize(new Dimension(500, 50));
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // ----- BOTTOM PANEL (SOUTH) - UNIFIED CROSS + EXPRESSION BUTTON! -----
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(new Color(245, 240, 235));
            bottomPanel.setPreferredSize(new Dimension(500, 200));
            bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            // Create cross panel - holds the unified cross
            JPanel crossPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw the cross background (dark gray)
                    g2d.setColor(new Color(245, 240, 235));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
            };
            crossPanel.setBackground(new Color(245, 240, 235));
            crossPanel.setLayout(new GridBagLayout());
            crossPanel.setPreferredSize(new Dimension(180, 180));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 0); // NO GAPS!
            
            // Create buttons - they will form a unified D-pad cross
            JButton upBtn = new JButton("");
            JButton downBtn = new JButton("");
            JButton leftBtn = new JButton("");
            JButton rightBtn = new JButton("");
            
            // Create the center non-clickable square
            JPanel centerSquare = new JPanel();
            centerSquare.setBackground(new Color(70, 70, 70));
            centerSquare.setPreferredSize(new Dimension(56, 56));
            centerSquare.setBorder(null);
            
            Dimension btnSize = new Dimension(56, 56);
            Font btnFont = new Font("Arial", Font.BOLD, 22);
            Color btnColor = new Color(70, 70, 70);
            
            // Style UP button - remove BOTTOM border
            upBtn.setPreferredSize(btnSize);
            upBtn.setMinimumSize(btnSize);
            upBtn.setMaximumSize(btnSize);
            upBtn.setFont(btnFont);
            upBtn.setBackground(btnColor);
            upBtn.setForeground(Color.WHITE);
            upBtn.setFocusPainted(false);
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
            
            // Add action listeners for cross buttons
            upBtn.addActionListener(e -> game.jump());
            downBtn.addActionListener(e -> game.toggleSit());
            leftBtn.addActionListener(e -> game.moveLeft());
            rightBtn.addActionListener(e -> game.moveRight());
            
            // ----- EXPRESSION BUTTON (Circle button on the right) -----
            // Create a circular button using JButton override
            JButton expressionBtn = new JButton("😊") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Fill circle background
                    g2d.setColor(new Color(60, 60, 60));
                    g2d.fillOval(0, 0, getWidth(), getHeight());
                    // Draw circle border
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                    g2d.dispose();
                    // Draw the text/emoji
                    super.paintComponent(g);
                }
            };
            
            // Configure the expression button
            expressionBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            expressionBtn.setForeground(Color.WHITE);
            expressionBtn.setFocusPainted(false);
            expressionBtn.setContentAreaFilled(false);
            expressionBtn.setBorder(null);
            expressionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            expressionBtn.setPreferredSize(new Dimension(80, 80));
            expressionBtn.setMinimumSize(new Dimension(80, 80));
            expressionBtn.setMaximumSize(new Dimension(80, 80));
            
            // Add expression button action - cycles through expressions
            expressionBtn.addActionListener(e -> {
                game.changeExpression();
                // Update button text to show current expression
                String[] emojis = {"😊", "😢", "😮", "😠", "😎"};
                int currentIndex = game.expressionIndex;
                expressionBtn.setText(emojis[currentIndex]);
            });
            
            // Add panels to bottom panel
            bottomPanel.add(crossPanel);
            
            // Add a spacer panel to push expression button to the right
            JPanel spacer = new JPanel();
            spacer.setBackground(Color.DARK_GRAY);
            spacer.setPreferredSize(new Dimension(90, 80));
            bottomPanel.add(spacer);
            
            bottomPanel.add(expressionBtn);

            // ----- INNER FRAME PANEL (Game Boy style screen border) -----
            // This wraps around the game panel with a dark frame
            JPanel innerFrame = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Draw dark border/frame
                    g2d.setColor(new Color(30, 30, 30));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    
                    // Draw inner border (slightly lighter)
                    g2d.setColor(new Color(50, 50, 50));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 12, 12);
                    
                    // Draw subtle highlight
                    g2d.setColor(new Color(70, 70, 70));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 10, 10);
                }
            };
            innerFrame.setBackground(new Color(20, 20, 20));
            innerFrame.setLayout(new BorderLayout());
            innerFrame.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Add the game panel inside the inner frame
            innerFrame.add(game, BorderLayout.CENTER);

            // ----- ADD ALL PANELS TO FRAME -----
            frame.add(leftPanel, BorderLayout.WEST);
            frame.add(rightPanel, BorderLayout.EAST);
            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(innerFrame, BorderLayout.CENTER);  // ← Inner frame instead of game
            frame.add(bottomPanel, BorderLayout.SOUTH);

            // ----- FRAME SETTINGS -----
            frame.setSize(550, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            // Request focus for keyboard input
            game.requestFocusInWindow();
        });
    }
}