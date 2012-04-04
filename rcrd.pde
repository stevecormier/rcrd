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
// 04-01-12
// 
// 
import jp.nyatla.nyar4psg.*;
import codeanticode.gsvideo.*;
import fullscreen.*;
import ddf.minim.*;

GSCapture cam;
Minim minim;
NyARMultiBoard nya;
PFont font, font2d;

float markerWidth = 135.0;
int numMarkers = 6;

AudioPlayer songs[] = new AudioPlayer[numMarkers];
PImage covers[] = new PImage[numMarkers];

void setup(){
    
    double[] widths = new double[numMarkers];
    
    FullScreen fs;
    
    int arHeight = 480;
    int arWidth = 640;
    
    String camPara = "camera_para.dat";
    String patternPath = "patterns/";
    String songPath = "songs/";
    String coverPath = "covers/";
    String[] patterns = new String[numMarkers];
    String[] songTitles = {"Trilla",
                           "SaturdayNightCrapoRama",
                           "PassingMeBy",
                           "NYStateOfMind",
                           "InTheAeroplaneOverTheSea",
                           "BirdsOnIce"};
    
	size(640, 480, P3D);
	colorMode(RGB, 100);
	font = createFont("FFScala", 32);
	font2d = createFont("FFScala", 10);
	
	
	Arrays.fill(widths, (double)markerWidth);
	
	cam = new GSCapture(this, arWidth, arHeight);
	cam.start();

	minim = new Minim(this);

	for(int i = 0; i < numMarkers ; i++){
	    patterns[i] = patternPath + "0" + (i+1) + ".patt";
	}
	
	for(int j = 0; j < numMarkers; j++){
		covers[j] = loadImage(coverPath + songTitles[j] + ".jpg");
	}
	
	for(int k = 0; k < numMarkers; k++){
		songs[k] = minim.loadFile(songPath + songTitles[k] + ".aif");
	}
		
	nya = new NyARMultiBoard(this, arWidth, arHeight, camPara, patterns, widths);
	
	nya.gsThreshold = 70;
	nya.cfThreshold = 0.7;
	nya.lostDelay = 7;
	
	//fs = new FullScreen(this);
	//fs.enter();
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

        for (int i=0; i < nya.markers.length; i++)
	    {
            if (nya.markers[i].detected)
            {
                nya.markers[i].beginTransform();
                image(covers[i], -(markerWidth/2), -(markerWidth/2), markerWidth, markerWidth);
                nya.markers[i].endTransform();
                songs[i].play();
                
                songs[i].skip(25 * (int)(nya.markers[i].angle.z * 10));
       
            }else{
                songs[i].pause();
            }
        }
    }else{
        for(int j = 0; j < nya.markers.length; j++){
            songs[j].pause();
        }
    }
}
