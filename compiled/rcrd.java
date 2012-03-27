import processing.core.*; 
import processing.xml.*; 

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
// 03-26-12
// 
// 




String camPara = "camera_para.dat";
String patternPath = "patterns/";
String songpPath = "songs/";
String coverPath = "covers/";

int arWidth = 640;
int arHeight = 480;

int numMarkers = 6;
double markerWidth = 135;

GSCapture cam;
NyARMultiBoard nya;
FullScreen fs;
PFont font, font2d;

public void setup(){
	size(640, 480, P3D);
	colorMode(RGB, 100);
	font = createFont("FFScala", 32);
	font2d = createFont("FFScala", 10);
	
	cam = new GSCapture(this, arWidth, arHeight);
	cam.start();
	
	String[] patterns = {"patt.hiro", "02.patt", "03.patt", "04.patt", "05.patt", "06.patt"};
	
	/*
	String patterns[] = new String[numMarkers]; 
	
	for(int i = 1; i < numMarkers - 1; i++){
		patterns[i-1] = patternPath + "0" + i + ".patt";
	}
	*/
	
	double[] widths = new double[numMarkers];
	Arrays.fill(widths, markerWidth); 
	
	nya = new NyARMultiBoard(this, arWidth, arHeight, camPara, patterns, widths);
	
	nya.gsThreshold = 120;
	
	nya.cfThreshold = 0.4f;
}

// draw marker corners and also position in text
public void drawMarkerPos(int[][] pos2d)
{
  textFont(font,10.0f);
  stroke(100,0,0);
  fill(100,0,0);
  
  // draw ellipses at outside corners of marker
  for(int i=0;i<4;i++){
    ellipse(pos2d[i][0], pos2d[i][1],5,5);
  }
  
  fill(0,0,0);
  for(int i=0;i<4;i++){
    text("("+pos2d[i][0]+","+pos2d[i][1]+")",pos2d[i][0],pos2d[i][1]);
  }
}


public void draw(){
	if(cam.available() !=true){
		return;
	}
	
	cam.read();
	
	hint(DISABLE_DEPTH_TEST);
	
	image(cam, 0, 0, width, height);
	
	hint(ENABLE_DEPTH_TEST);
	
	if(nya.detect(cam)){
		hint(DISABLE_DEPTH_TEST);
		
		for(int i = 0; i <  nya.markers.length; i++){
			if(nya.markers[i].detected){
				drawMarkerPos(nya.markers[i].pos2d);
			}
		}

	}
	
	hint(ENABLE_DEPTH_TEST);
	
	hint(DISABLE_DEPTH_TEST);
  	textFont(font2d,10.0f);
  	textMode(SCREEN);
  	fill(100,100,0);
  	text("frame rate = " + frameRate, 10, 10);
  	textMode(MODEL);
  	hint(ENABLE_DEPTH_TEST);
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "rcrd" });
  }
}
