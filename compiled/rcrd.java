import processing.core.*; 
import processing.xml.*; 

import java.io.*; 
import processing.opengl.*; 
import jp.nyatla.nyar4psg.*; 
import codeanticode.gsvideo.*; 
import fullscreen.*; 

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

public class rcrd extends PApplet {

//                                           88  
//                                           88  
//                                           88  
// 8b,dPPYba,  ,adPPYba, 8b,dPPYba,  ,adPPYb,88  
// 88P'   "Y8 a8"     "" 88P'   "Y8 a8"    `Y88  
// 88         8b         88         8b       88  
// 88         "8a,   ,aa 88         "8a,   ,d88  
// 88          `"Ybbd8"' 88          `"8bbdP"Y8
//
// Stephen Cormier
// 03-12-12
// 
// 







String camPara = "/Users/Steve/Documents/Processing/libraries/nyar4psg/data/camera_para.dat";
String patternPath = "/Users/Steve/Documents/Processing/libraries/nyar4psg/patternMaker/examples/ARToolKit_Patterns";

int arWidth = 640;
int arHeight = 360;

int numMarkers = 6;

GSCapture cam;
MultiMarker nya;
FullScreen fs;

public void setup(){
	size(1680, 1050, OPENGL);
	cam = new GSCapture(this, 640, 360);
	cam.start();
	noStroke();
	fs = new FullScreen(this);
	fs.enter();
}

public void draw(){
	if(cam.available()){
		cam.read();
		background(0);
		image(cam, 0, 0, width, height);
	}
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "rcrd" });
  }
}
