package org.example.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class MatrixClass {

    private static final Random random = new Random();
    private static final Scanner sc = new Scanner(System.in);
    private static final String[] colors = {"\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m"};
    private static final String RESET = "\u001B[0m";
    private static final List<MatrixColumn> matrixColumns = new ArrayList<>();
    private static final List<IllusionEffect> illusions = new ArrayList<>();
    private static final Verifier verifier = new Verifier();
    private static boolean isClosing = false;
    private static float fadeLevel = 1.0f;

    static abstract class IllusionEffect {
        protected float x, y;
        protected float size;
        protected float rotation;
        protected float alpha;
        protected float speed;

        public IllusionEffect() {
            reset();
        }

        protected void reset() {
            x = random.nextFloat() * 800;
            y = random.nextFloat() * 600;
            size = random.nextFloat() * 50 + 30;
            alpha = random.nextFloat() * 0.2f;
            speed = random.nextFloat() * 2 + 0.5f;
            rotation = 0;
        }

        public abstract void update();

        public abstract void draw(Graphics2D g2d);
    }

    static class CircleWave extends IllusionEffect {
        private float wavePhase = 0;

        public void update() {
            rotation += 0.03f;
            wavePhase += 0.05f;
            x = 400 + (float) Math.cos(rotation) * 200;
            y = 300 + (float) Math.sin(rotation) * 200;
            alpha = (float) (0.2f + Math.sin(wavePhase) * 0.1f);
        }

        public void draw(Graphics2D g2d) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * fadeLevel));
            g2d.setColor(new Color(0, 255, 0));

            for (int i = 0; i < 8; i++) {
                float wave = (float) Math.sin(wavePhase + i * 0.5f) * 20;
                g2d.drawOval((int) (x - size / 2 - wave), (int) (y - size / 2 - wave),
                        (int) (size + wave * 2), (int) (size + wave * 2));
            }
        }
    }

    static class MatrixColumn {
        private final List<Character> chars = new ArrayList<>();
        private final int x;
        private int y;
        private final int speed;
        private final int length;
        private boolean isPulled = false;
        private float pullSpeed = 0;

        public MatrixColumn(int x) {
            this.x = x;
            this.y = random.nextInt(600);
            this.speed = random.nextInt(5) + 5;
            this.length = random.nextInt(20) + 10;
            IntStream.range(0, length)
                    .forEach(i -> chars.add((char) (random.nextInt(93) + 33)));
        }

        public void update() {
            if (isClosing) {
                if (!isPulled) {
                    pullSpeed += 0.5f;
                    y += pullSpeed;
                    if (y > 600) {
                        isPulled = true;
                    }
                }
            } else {
                y = (y + speed) % 600;
                if (random.nextInt(10) == 0) {
                    int idx = random.nextInt(chars.size());
                    chars.set(idx, (char) (random.nextInt(93) + 33));
                }
            }
        }

        public void draw(Graphics2D g2d) {
            for (int i = 0; i < chars.size(); i++) {
                float alpha = (1.0f - ((float) i / chars.size())) * fadeLevel;
                g2d.setColor(new Color(0, 255, 0, (int) (alpha * 255)));
                g2d.drawString(String.valueOf(chars.get(i)), x, (y + i * 20) % 600);
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n" + randomColor() + "╔════════════════════╗");
            System.out.println("║ Select a Project  ║");
            System.out.println("╚════════════════════╝" + RESET);
            System.out.println("1. MatrixApp");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    startMatrixApp();
                    break;
                case "2":
                    System.exit(0);
                    break;
                default:
                    System.out.println(randomColor() + "❌ Invalid choice, please try again." + RESET);
            }
        }
    }

    public static void startMatrixApp() {
        JFrame frame = new JFrame("Matrix Rain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        startMatrixRain(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("\n" + randomColor() + "╔══════════════════════════════════════╗");
        System.out.println("║  Welcome to the Digital Dreamscape!  ║");
        System.out.println("╚══════════════════════════════════════╝" + RESET + "\n");

        boolean running = true;

        while (running) {
            System.out.print(randomColor() + "⚡ You: " + RESET);
            String userInput = sc.nextLine().toLowerCase();

            switch (userInput) {
                case "exit" -> {
                    isClosing = true;
                    displayExitAnimation();
                    java.util.Timer fadeTimer = new java.util.Timer();
                    fadeTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            fadeLevel -= 0.05f;
                            if (fadeLevel <= 0) {
                                fadeTimer.cancel();
                                frame.dispose();
                                return;
                            }
                        }
                    }, 500, 100);
                }
                case "fractal" -> drawSierpinskiTriangle(5);
                case "account" -> handleAccountCreation();
                case "validate" -> handleValidation();
                case "help" -> displayHelp();
                default -> displayPsychedelicMessage();
            }
        }
    }

    private static void startMatrixRain(JFrame frame) {
        IntStream.range(0, 80)
                .forEach(i -> matrixColumns.add(new MatrixColumn(i * 10)));

        for (int i = 0; i < 5; i++) {
            illusions.add(new CircleWave());
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                illusions.forEach(illusion -> {
                    illusion.update();
                    illusion.draw(g2d);
                });

                g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
                matrixColumns.forEach(column -> {
                    column.update();
                    column.draw(g2d);
                });
            }
        };
        panel.setPreferredSize(new Dimension(800, 600));
        frame.add(panel);
        new javax.swing.Timer(50, e -> panel.repaint()).start();
    }

    private static void handleAccountCreation() {
        System.out.println(randomColor() + "▶ AI: Let me help you with your account." + RESET);
        System.out.println(randomColor() + "▶ AI: Please enter your desired username:" + RESET);
        System.out.print("You: ");
        verifier.setUsername(sc.nextLine());

        System.out.println(randomColor() + "▶ AI: Now, please enter your email:" + RESET);
        System.out.print("You: ");
        verifier.setEmail(sc.nextLine());

        System.out.println(randomColor() + "▶ AI: Account created in the digital realm:" + RESET);
        System.out.println(rainbow(verifier.toString()));
    }

    private static void handleValidation() {
        System.out.println(randomColor() + "⚡ Initiating quantum validation sequence..." + RESET);
        try {
            verifier.validateApplicant();
            System.out.println(randomColor() + "✨ Validation successful in all dimensions!" + RESET);
        } catch (Exception e) {
            System.out.println(randomColor() + "❌ Reality breach detected: " + e.getMessage() + RESET);
        }
    }

    private static void displayHelp() {
        System.out.println(randomColor() + """
                Available commands:
                - fractal : Generate a Sierpinski triangle
                - account : Create a new digital identity
                - validate : Verify your existence
                - exit : Return to base reality
                """ + RESET);
    }

    private static void drawSierpinskiTriangle(int depth) {
        IntStream.range(0, (int) Math.pow(2, depth))
                .forEach(i -> {
                    String line = " ".repeat((int) Math.pow(2, depth) - i) +
                            IntStream.range(0, i * 2 + 1)
                                    .mapToObj(j -> random.nextBoolean() ? "▲" : "△")
                                    .reduce("", String::concat);
                    System.out.println(randomColor() + line + RESET);
                });
    }

    private static String randomColor() {
        return colors[random.nextInt(colors.length)];
    }

    private static String rainbow(String text) {
        return text.chars()
                .mapToObj(ch -> randomColor() + (char) ch)
                .reduce("", String::concat) + RESET;
    }

    private static void displayPsychedelicMessage() {
        String[] messages = {
                "✨ Traverse the digital cosmos with me! ✨",
                "🌌 Reality is but a quantum fluctuation... 🌌",
                "🎮 Enter the matrix of infinite possibilities! 🎮",
                "🔮 Your consciousness has been expanded! 🔮"
        };
        System.out.println(randomColor() + messages[random.nextInt(messages.length)] + RESET);
    }

    private static void displayExitAnimation() {
        System.out.println("\n" + randomColor() + "╔═════════════════════════════════╗");
        System.out.println("║ Folding spacetime dimensions...   ║");
        System.out.println("║ Reality tunnel closing...         ║");
        System.out.println("║ Consciousness stream terminated   ║");
        System.out.println("╚═════════════════════════════════╝" + RESET);
    }
}
