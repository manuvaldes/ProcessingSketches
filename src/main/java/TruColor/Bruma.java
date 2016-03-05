/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TruColor;

import processing.core.PApplet;

import de.looksgood.ani.*;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import TruColor.BrumaSupport.*;

public class Bruma extends PApplet {


    static public void main(String[] args) {
        PApplet.main(Bruma.class.getName());
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    int numLines = 10000;
    Line[] lines = new Line[numLines]; // define the array
    int visibleLines = 0;
    RandomInRanges paletterGenerator;
    String outputDir;
    @Override
    public void settings() {
        // TODO Auto-generated method stub
        super.settings();
        size(800, 400);
        smooth(8);
    }


    @Override
    public void setup() {


        ClassLoader classLoader = getClass().getClassLoader();
        outputDir = new File(classLoader.getResource("TruColor/output").getFile()).getAbsolutePath();

        // you have to call always Ani.init() first!
        Ani.init(this);
        Ani.setDefaultTimeMode(Ani.FRAMES);

        colorMode(HSB,360,100,100,1);

        frameRate(60);

        this.dataPath("data");

        strokeCap(SQUARE);

        paletterGenerator = new RandomInRanges(0,50);

        paletterGenerator.addRange(150,200);



        for (int i = 0; i < numLines; i++) {
            lines[i] = new Line(random(0,width/9)*10,random(0,height/9)*10,(float)(2f*Math.PI*(60*(4*ThreadLocalRandom.current().nextInt(1,4)))/360f),400f,(float)(numLines- i)/numLines); // fill the array with circles at random positions
        }


        //Ani.to(this, 10f, "visibleLines", numLines, Ani.SINE_IN_OUT);
    }


    @Override
    public void draw() {


    /*    if (visibleLines == numLines) {
            return;
        }*/
        visibleLines= numLines;
        background(color(255,0,255));
        for (int i = 0; i < visibleLines; i++) {
            lines[i].display(); // display all the circles
        }

        if(frameCount<360) {

            // para convertir luego a video con:
            // ffmpeg.exe -framerate 60 -i C:\Users\Manuel\Produccion\Desarrollo\IntelliJ_Processing\TrueColor\outputVideo\%04d.jpg -loop -1 -vcodec libx264 -preset slow -crf 20 movie.mp4
            saveFrame(outputDir + "/####.jpg");
        }
    /*    fill(0,0,100,1);
        rect(0,0,width,50);
        rect(0,0,50,height);
        rect(width-50,0,50,height);
        rect(0,height-50,width,50);

*/
    }

    @Override
    public void mouseReleased() {

        if (mouseButton == RIGHT) {
            save(outputDir + "/test.png");
        }


        for (int i = 0; i < numLines; i++) {
            lines[i] = new Line(random(0,width/9)*10,random(0,height/9)*10,(float)(2f*Math.PI*(60*(4*ThreadLocalRandom.current().nextInt(1,4)))/360f),400f,(float)(numLines- i)/numLines); // fill the array with circles at random positions
        }

        visibleLines = 0;
        Ani.to(this, 10f, "visibleLines", numLines, Ani.SINE_IN_OUT);
        super.mouseReleased();
    }





    class Line {
        float x1, y1,x2,y2; // location
        float weight; // dimension
        int color1;
        float hue, saturation, brightness;

        Line(float x, float y, float rad,float len,float alpha) {
            this.x1 = x;
            this.y1 = y;
            this.x2= x1+ len*sin(rad);
            this.y2= y1- len*cos(rad); // restamos porque el eje y crece hacia abajo

            weight = random(30, 50);
            hue =  paletterGenerator.getRandom();
            saturation = 100;
            brightness = 60;
            color1 = color(hue, saturation, brightness,alpha);


            Ani.to(this, 360, "x1", x + random(200) - 100,Ani.LINEAR);
            Ani.to(this, 360, "y1", y + random(200) - 100,Ani.LINEAR);

        }

        void display() {

            stroke(color1);
            strokeWeight(weight);
            line(x1,y1,x2,y2);
        }

        void ani(int _x, int _y) {

            Ani.to(this, 1.5f, "x", _x + random(50) - 25, Ani.SINE_IN_OUT);
            Ani.to(this, 1.5f, "y", _y + random(50) - 25, Ani.SINE_IN_OUT);
            Ani.to(this, 1.5f, "saturation", random(255), Ani.SINE_IN_OUT);
            Ani.to(this, 1.5f, "brightness", random(255), Ani.SINE_IN_OUT);

        }

    }
}