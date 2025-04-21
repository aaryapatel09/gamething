package com.example;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_videoio.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_videoio.*;

/**
 * Simple hand tracking program that detects vertical hand movement
 */
public class HandTracker {
    // Movement threshold in pixels
    private static final int MOVEMENT_THRESHOLD = 20;
    private static int previousY = -1;

    public static void main(String[] args) {
        // Open the default camera
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Error: Could not open camera");
            return;
        }

        // Set camera resolution
        camera.set(CAP_PROP_FRAME_WIDTH, 640);
        camera.set(CAP_PROP_FRAME_HEIGHT, 480);

        // Create matrices for processing
        Mat frame = new Mat();
        Mat hsv = new Mat();
        Mat mask = new Mat();
        Mat hierarchy = new Mat();

        System.out.println("Starting hand tracking. Press 'q' to quit.");
        System.out.println("Move your hand up and down in front of the camera.");

        while (true) {
            // Read a frame
            if (camera.read(frame)) {
                // Convert to HSV for better color detection
                cvtColor(frame, hsv, COLOR_BGR2HSV);

                // Create a mask for skin color (adjust these values if needed)
                inRange(hsv, new Scalar(0, 48, 80), new Scalar(20, 255, 255), mask);

                // Find contours in the mask
                MatVector contours = new MatVector();
                findContours(mask, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

                // Find the largest contour (likely the hand)
                double maxArea = 0;
                int maxIdx = -1;
                for (int i = 0; i < contours.size(); i++) {
                    double area = contourArea(contours.get(i));
                    if (area > maxArea) {
                        maxArea = area;
                        maxIdx = i;
                    }
                }

                // If we found a hand
                if (maxIdx != -1 && maxArea > 1000) {
                    // Get the center of the hand
                    Moments moments = moments(contours.get(maxIdx));
                    int centerY = (int)(moments.m01() / moments.m00());

                    // Draw a circle at the center
                    circle(frame, new Point((int)(moments.m10() / moments.m00()), centerY), 
                          5, new Scalar(0, 255, 0), -1);

                    // Check for movement
                    if (previousY != -1) {
                        int yDiff = centerY - previousY;
                        if (Math.abs(yDiff) > MOVEMENT_THRESHOLD) {
                            System.out.println(yDiff < 0 ? "Moving Up" : "Moving Down");
                        }
                    }
                    previousY = centerY;
                }

                // Show the video feed
                imshow("Hand Tracking", frame);
                imshow("Mask", mask);

                // Break if 'q' is pressed
                if (waitKey(1) == 'q') {
                    break;
                }
            }
        }

        // Clean up
        camera.release();
        frame.release();
        hsv.release();
        mask.release();
        hierarchy.release();
        destroyAllWindows();
    }
} 