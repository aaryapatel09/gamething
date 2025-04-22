package com.example;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple hand tracking program that uses native commands to access the camera
 * and basic color tracking for hand detection.
 */
public class HandTracker {
    // Movement threshold in pixels
    private static final int MOVEMENT_THRESHOLD = 20;
    private static int previousY = -1;
    
    // Window size
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    
    // Flag to control when to stop
    private static boolean running = true;
    
    // Mock implementation for demo purposes
    static class Point {
        int x, y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    // Simple display window
    static class CameraPanel extends JPanel {
        private BufferedImage cameraImage;
        private Point handCenter;
        
        public CameraPanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            cameraImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            handCenter = null;
        }
        
        public void updateImage(BufferedImage image) {
            if (image != null) {
                this.cameraImage = image;
                repaint();
            }
        }
        
        public void updateHandPosition(Point center) {
            this.handCenter = center;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(cameraImage, 0, 0, null);
            
            if (handCenter != null) {
                g.setColor(Color.GREEN);
                g.fillOval(handCenter.x - 5, handCenter.y - 5, 10, 10);
            }
        }
    }
    
    // Simple skin color detector
    private static boolean isSkinColor(int r, int g, int b) {
        // Simple skin color detection - can be improved
        return (r > 95 && g > 40 && b > 20 && 
                Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b)) > 15 && 
                Math.abs(r - g) > 15 && r > g && r > b);
    }
    
    // Find skin-colored pixels
    private static List<Point> findSkinPixels(BufferedImage image) {
        List<Point> skinPixels = new ArrayList<>();
        
        // Sample pixels (for performance)
        for (int y = 0; y < image.getHeight(); y += 4) {
            for (int x = 0; x < image.getWidth(); x += 4) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                if (isSkinColor(r, g, b)) {
                    skinPixels.add(new Point(x, y));
                }
            }
        }
        
        return skinPixels;
    }
    
    // Find the center of skin pixels
    private static Point findHandCenter(List<Point> skinPixels) {
        if (skinPixels.size() < 10) {
            return null; // Not enough pixels to detect a hand
        }
        
        int sumX = 0, sumY = 0;
        for (Point p : skinPixels) {
            sumX += p.x;
            sumY += p.y;
        }
        
        return new Point(sumX / skinPixels.size(), sumY / skinPixels.size());
    }
    
    // Generate a mock camera image for demo purposes
    private static BufferedImage generateMockImage(int frameCount) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Moving "hand" (simulated)
        int handY = HEIGHT / 2 + (int)(Math.sin(frameCount / 20.0) * 100);
        g.setColor(new Color(220, 170, 150)); // Skin color
        g.fillOval(WIDTH / 2 - 40, handY - 40, 80, 80);
        
        g.dispose();
        return image;
    }
    
    public static void main(String[] args) {
        // Create the display window
        JFrame frame = new JFrame("Hand Tracking");
        CameraPanel cameraPanel = new CameraPanel();
        frame.add(cameraPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // Add key listener to exit when 'q' is pressed
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'q') {
                    running = false;
                    frame.dispose();
                }
            }
        });
        
        frame.setVisible(true);
        
        // Check if webcam is accessible
        boolean hasWebcam = false;
        try {
            // Check if we can find a camera device
            File devDir = new File("/dev");
            if (devDir.exists()) {
                File[] files = devDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().startsWith("video")) {
                            hasWebcam = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not detect webcam: " + e.getMessage());
        }
        
        if (!hasWebcam) {
            System.out.println("No webcam detected or not accessible. Running in simulation mode.");
        }
        
        // Main processing loop
        new Thread(() -> {
            int frameCount = 0;
            while (running) {
                // In a real implementation, this would capture from camera
                // For this demo, we'll use a simulated image
                BufferedImage capturedFrame = generateMockImage(frameCount++);
                
                // Find skin-colored pixels
                List<Point> skinPixels = findSkinPixels(capturedFrame);
                
                // Find hand center
                Point handCenter = findHandCenter(skinPixels);
                
                // Update UI with a copy of the current frame and hand position
                final BufferedImage displayFrame = capturedFrame;
                final Point displayHandCenter = handCenter;
                
                SwingUtilities.invokeLater(() -> {
                    cameraPanel.updateImage(displayFrame);
                    if (displayHandCenter != null) {
                        cameraPanel.updateHandPosition(displayHandCenter);
                        
                        // Check for vertical movement
                        if (previousY != -1) {
                            int yDiff = displayHandCenter.y - previousY;
                            if (Math.abs(yDiff) > MOVEMENT_THRESHOLD) {
                                System.out.println(yDiff < 0 ? "Moving Up" : "Moving Down");
                            }
                        }
                        previousY = displayHandCenter.y;
                    }
                });
                
                // Sleep to control frame rate
                try {
                    Thread.sleep(33); // ~30 fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        System.out.println("Hand tracking started. Move your hand up and down.");
        System.out.println("Press 'q' to quit.");
    }
} 