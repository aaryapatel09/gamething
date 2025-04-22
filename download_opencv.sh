#!/bin/bash

# Create directories
mkdir -p lib
mkdir -p native

# Define version
OPENCV_VERSION=4.8.0

# Download OpenCV jar from Maven Central
echo "Downloading OpenCV $OPENCV_VERSION jar..."
curl -L "https://repo1.maven.org/maven2/org/openpnp/opencv/$OPENCV_VERSION-0/opencv-$OPENCV_VERSION-0.jar" -o "lib/opencv-$OPENCV_VERSION.jar"

# Download native libraries
echo "Downloading OpenCV native libraries..."
if [[ "$OSTYPE" == "darwin"* ]]; then
  # macOS
  curl -L "https://github.com/openpnp/opencv/releases/download/$OPENCV_VERSION-0/opencv-$OPENCV_VERSION-0-macosx-x86_64.jar" -o "native/opencv-$OPENCV_VERSION-macos.jar"
  cd native
  jar xf "opencv-$OPENCV_VERSION-macos.jar"
  rm "opencv-$OPENCV_VERSION-macos.jar"
  cd ..
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
  # Linux
  curl -L "https://github.com/openpnp/opencv/releases/download/$OPENCV_VERSION-0/opencv-$OPENCV_VERSION-0-linux-x86_64.jar" -o "native/opencv-$OPENCV_VERSION-linux.jar"
  cd native
  jar xf "opencv-$OPENCV_VERSION-linux.jar"
  rm "opencv-$OPENCV_VERSION-linux.jar"
  cd ..
elif [[ "$OSTYPE" == "msys"* || "$OSTYPE" == "cygwin"* || "$OSTYPE" == "win32" ]]; then
  # Windows
  curl -L "https://github.com/openpnp/opencv/releases/download/$OPENCV_VERSION-0/opencv-$OPENCV_VERSION-0-windows-x86_64.jar" -o "native/opencv-$OPENCV_VERSION-windows.jar"
  cd native
  jar xf "opencv-$OPENCV_VERSION-windows.jar"
  rm "opencv-$OPENCV_VERSION-windows.jar"
  cd ..
else
  echo "Unsupported operating system: $OSTYPE"
  exit 1
fi

echo "OpenCV $OPENCV_VERSION has been downloaded successfully" 