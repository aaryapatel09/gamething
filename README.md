# Hand Tracking Program

This program tracks your hand movements using your computer's camera. When you move your hand up or down, the program will display "Moving Up" or "Moving Down" messages.

## What You Need

1. A computer with a camera
2. Java (version 11 or newer)

## How to Run the Program

### Option 1: Using Java directly

1. Download the program files
2. Open a terminal/command prompt
3. Navigate to the program folder
4. Run the program with:
   ```
   java -cp src/main/java com.example.HandTracker
   ```

### Option 2: Using Eclipse

1. Download and install Eclipse from: https://www.eclipse.org/downloads/
2. Open Eclipse
3. Click File -> Import -> Existing Projects into Workspace
4. Select the folder where you downloaded this program
5. Click Finish
6. Find the project in the left sidebar
7. Right-click on the project
8. Click Run As -> Java Application
9. Select "HandTracker" when asked

## Using the Program

1. A window will open showing your camera feed (or a simulation if a camera isn't available)
2. Move your hand up and down in front of the camera
3. The program will show "Moving Up" or "Moving Down" on the screen
4. Press the 'q' key to stop the program

## Tips

- Make sure you're in a well-lit room
- Keep your hand clearly visible to the camera
- If the program doesn't detect your hand, try moving it closer to the camera 