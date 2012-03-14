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

import java.io.*;
import processing.opengl.*;
import jp.nyatla.nyar4psg.*;
import codeanticode.gsvideo.*;
import fullscreen.*;

String camPara = "/Users/Steve/Documents/Processing/libraries/nyar4psg/data/camera_para.dat";
String patternPath = "/Users/Steve/Documents/Processing/libraries/nyar4psg/patternMaker/examples/ARToolKit_Patterns";

int arWidth = 640;
int arHeight = 360;

int numMarkers = 6;

GSCapture cam;
MultiMarker nya;
FullScreen fs;

void setup(){
	size(1680, 1050, OPENGL);
	cam = new GSCapture(this, 640, 360);
	cam.start();
	noStroke();
	fs = new FullScreen(this);
	fs.enter();
}

void draw(){
	if(cam.available()){
		cam.read();
		background(0);
		image(cam, 0, 0, width, height);
	}
}

