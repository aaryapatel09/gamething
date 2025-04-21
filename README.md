# Simple Hand Tracking with OpenCV

This is a simple Java program that tracks hand movement using OpenCV. It captures video from your webcam, detects hand movement, and prints "Moving Up" or "Moving Down" when significant vertical movement is detected.

## Prerequisites

1. Java 11 or higher
2. Maven
3. Eclipse IDE with M2E (Maven Integration) plugin

## Setup in Eclipse

1. Open Eclipse
2. Go to File -> Import -> Maven -> Existing Maven Projects
3. Select the root directory of this project
4. Click Finish
5. Wait for Maven to download dependencies

## Running the Program

### In Eclipse:
1. Right-click on the project
2. Select Run As -> Java Application
3. Select `com.example.HandTracker` as the main class

### From Command Line:
```bash
mvn clean package
mvn exec:java
```

## Usage

1. The program will open two windows:
   - "Hand Tracking": Shows the video feed with hand tracking
   - "Mask": Shows the skin color detection mask

2. Move your hand up and down in front of the camera
3. The program will print "Moving Up" or "Moving Down" when significant movement is detected
4. Press 'q' to quit the program

## Notes

- The skin color detection parameters might need adjustment based on your lighting conditions
- Make sure your hand is well-lit and clearly visible to the camera
- The movement threshold is set to 20 pixels by default 