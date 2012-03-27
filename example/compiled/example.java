import processing.core.*; 
import processing.xml.*; 

import java.io.*; 
import processing.opengl.*; 
import jp.nyatla.nyar4psg.*; 
import codeanticode.gsvideo.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class example extends PApplet {

// Augmented Reality Dynamic Example by Amnon Owed (21/12/11)
// Processing 1.5.1 + NyARToolkit 1.1.6 + GSVideo 1.0

 // for the loadPatternFilenames() function
 // for OPENGL rendering
 // the NyARToolkit Processing library
 // the GSVideo library

// a central location is used for the camera_para.dat and pattern files, so you don't have to copy them to each individual sketch
// Make sure to change both the camPara and the patternPath String to where the files are on YOUR computer
// the full path to the camera_para.dat file
String camPara = "/Users/Steve/Documents/Processing/libraries/nyar4psg/data/camera_para.dat";
// the full path to the .patt pattern files
String patternPath = "/Users/Steve/Documents/Processing/libraries/nyar4psg/patternMaker/examples/ARToolKit_Patterns";
// the dimensions at which the AR will take place. with the current library 1280x720 is about the highest possible resolution.
int arWidth = 640;
int arHeight = 360;
// the number of pattern markers (from the complete list of .patt files) that will be detected, here the first 10 from the list.
int numMarkers = 10;

// the resolution at which the mountains will be displayed
int resX = 60;
int resY = 60;
// this is a 2 dimensional float array that all the displayed mountains use during their update-to-draw routine
float[][] val = new float[resX][resY];

GSCapture cam;
MultiMarker nya;
float[] scaler = new float[numMarkers];
float[] noiseScale = new float[numMarkers];
float[] mountainHeight = new float[numMarkers];
float[] mountainGrowth = new float[numMarkers];

public void setup() {
  size(1280, 720, OPENGL); // the sketch will resize correctly, so for example setting it to 1920 x 1080 will work as well
  cam = new GSCapture(this, 1280, 720); // initializing the webcam capture at a specific resolution (correct/possible settings depends on YOUR webcam)
  cam.start(); // start capturing
  noStroke(); // turn off stroke for the rest of this sketch :-)
  // create a new MultiMarker at a specific resolution (arWidth x arHeight), with the default camera calibration and coordinate system
  nya = new MultiMarker(this, arWidth, arHeight, camPara, NyAR4PsgConfig.CONFIG_DEFAULT);
  // set the delay after which a lost marker is no longer displayed. by default set to something higher, but here manually set to immediate.
  nya.setLostDelay(1);
  String[] patterns = loadPatternFilenames(patternPath);
  // for the selected number of markers, add the marker for detection
  // create an individual scale, noiseScale and maximum mountainHeight for that marker (= mountain)
  for (int i=0; i<numMarkers; i++) {
    nya.addARMarker(patternPath + "/" + patterns[i], 80);
    scaler[i] = random(0.8f, 1.9f); // scaled a little smaller or bigger
    noiseScale[i] = random(0.02f, 0.075f); // the perlin noise scale to make it look nicely mountainy
    mountainHeight[i] = random(75, 150); // the maximum height of a mountain
  }
}

public void draw() {
  // if there is a cam image coming in...
  if (cam.available()) {
    cam.read(); // read the cam image
    background(0); // a background call is needed for correct display of the marker results
    image(cam, 0, 0, width, height); // display the image at the width and height of the sketch window
    // create a copy of the cam image at the resolution of the AR detection (otherwise nya.detect will throw an assertion error!)
    PImage cSmall = cam.get();
    cSmall.resize(arWidth, arHeight);
    nya.detect(cSmall); // detect markers in the image
    drawMountains(); // draw dynamically flowing mountains on the detected markers (3D)
  }
}

// this function draws correctly placed 3D 'mountains' on top of detected markers
// while the mountains are displayed they grow (up to a certain point), while not displayed they return to the zero-state
public void drawMountains() {
  // set the AR perspective uniformly, this general point-of-view is the same for all markers
  nya.setARPerspective();
  // turn on some general lights (without lights it also looks pretty cool, try commenting it out!)
  lights();
  // for all the markers...
  for (int i=0; i<numMarkers; i++) {
    // if the marker does NOT exist (the ! exlamation mark negates it)...
    if ((!nya.isExistMarker(i))) {
      // if the mountainGrowth is higher than zero, decrease by 0.05 (return to the zero-state), then continue to the next marker
      if (mountainGrowth[i] >  0) { mountainGrowth[i] -= 0.05f; }
      continue;
    }
    // the following code is only reached and run if the marker DOES EXIST
    // if the mountainGrowth is lower than 1, increase by 0.03
    if (mountainGrowth[i] <  1) { mountainGrowth[i] += 0.03f; }
    // the double for loop below sets the values in the 2 dimensional float array for this mountain, based on it's noiseScale, mountainHeight and index (i).
    float xoff = 0.0f;
    for (int x=0; x<resX; x++) {
      xoff += noiseScale[i];
      float yoff = 0;
      for (int y=0; y<resY; y++) {
        yoff += noiseScale[i];
        val[x][y] = noise(i*10+xoff+frameCount*0.05f, yoff) * mountainHeight[i]; // this sets the value
        float distance = dist(x, y, resX/2, resY/2);
        distance = map(distance, 0, resX/2, 1, 0);
        if (distance <  0) { distance = -distance; } // this line causing the four corners to flap upwards (try commenting it out or setting it to zero)
        val[x][y] *= distance; // in the default case this makes the value approach zero towards the outer ends (try commenting it out to see the difference)
      }
    }

    // get the Matrix for this marker and use it (through setMatrix)
    setMatrix(nya.getMarkerMatrix(i));
    scale(1, -1); // turn things upside down to work intuitively for Processing users
    scale(scaler[i]); // scale the mountain by it's individual scaler
    translate(-resX/2, -resY/2); // translate to center the mountain on the marker
    // for the full resolution...
    for (int x=0; x<resX-1; x++) {
      for (int y=0; y<resY-1; y++) {
        // each face is a Shape with a fill color, together they make a colored mountain
        fill(x*20+y*20, 255-x*5, y*5);
        beginShape();
        vertex(x, y, val[x][y] * mountainGrowth[i]);
        vertex((x+1), y, val[x+1][y] * mountainGrowth[i]);
        vertex((x+1), (y+1), val[x+1][y+1] * mountainGrowth[i]);
        vertex(x, (y+1), val[x][y+1] * mountainGrowth[i]);
        endShape(CLOSE);
      }
    }
  }
  // reset to the default perspective
  perspective();
}

// this function loads .patt filenames into a list of Strings based on a full path to a directory (relies on java.io)
public String[] loadPatternFilenames(String path) {
  File folder = new File(path);
  FilenameFilter pattFilter = new FilenameFilter() {
    public boolean accept(File dir, String name) {
      return name.toLowerCase().endsWith(".patt");
    }
  };
  return folder.list(pattFilter);
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "example" });
  }
}
