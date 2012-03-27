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
import jp.nyatla.nyar4psg.*;
import codeanticode.gsvideo.*;
import fullscreen.*;

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

void setup(){
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
	
	nya.cfThreshold = 0.4;
}

// draw marker corners and also position in text
void drawMarkerPos(int[][] pos2d)
{
  textFont(font,10.0);
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


void draw(){
	if(cam.available() !=true){
		return;
	}
	
	cam.read();
	
	hint(DISABLE_DEPTH_TEST);
	
	image(cam, 0, 0, width, height);
	
	hint(ENABLE_DEPTH_TEST);
	
	if(nya.detect(cam)){
		hint(DISABLE_DEPTH_TEST);
		
		for(int i = 0; i < nya.markers.length; i++){
			if(nya.markers[i].detected){
				drawMarkerPos(nya.markers[i].pos2d);
			}
		}

	}
	
	hint(ENABLE_DEPTH_TEST);
	
	hint(DISABLE_DEPTH_TEST);
  	textFont(font2d,10.0);
  	textMode(SCREEN);
  	fill(100,100,0);
  	text("frame rate = " + frameRate, 10, 10);
  	textMode(MODEL);
  	hint(ENABLE_DEPTH_TEST);
}
