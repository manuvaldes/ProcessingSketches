/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TruColor;

import de.looksgood.ani.Ani;
import processing.core.PApplet;

public class CircleFollower extends PApplet {


    static public void main(String[] args) {
        PApplet.main(CircleFollower.class.getName());
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    int numCircles = 5;
    Circle[] circles = new Circle[numCircles]; // define the array


    @Override
    public void settings() {
        // TODO Auto-generated method stub
        super.settings();
        size(800, 800);
    }


    @Override
    public void setup() {



        colorMode(HSB,255,255,255);



        this.dataPath("data");

        smooth();
        noStroke();


        for (int i = 0; i < numCircles; i++) {
            circles[i] = new Circle(random(width), random(height)); // fill the array with circles at random positions
        }


        // you have to call always Ani.init() first!
        Ani.init(this);
    }


    @Override
    public void draw() {
        background(color(255,0,255));
        for (int i = 0; i < numCircles; i++) {
            circles[i].display(); // display all the circles
        }


    }

    @Override
    public void mouseReleased() {
        for (int i = 0; i < numCircles; i++) {
            circles[i].ani(mouseX, mouseY); // display all the circles
        }
        super.mouseReleased();
    }

    class Circle {
        float x, y; // location
        float dim; // dimension
        int color1, color2; // color
        float hue, saturation, brightness;

        Circle(float x, float y) {
            this.x = x;
            this.y = y;
            dim = random(20, 50);
            hue = 100;
            saturation = random(255);
            brightness = random(255);
            color1 = color(hue, saturation, brightness);
        }

        void display() {

            color1 = color(hue, saturation, brightness);
            fill(color1);


            ellipse(x, y, dim, dim); // a circle at position xy
        }

        void ani(int _x, int _y) {

            Ani.to(this, 1.5f, "x", _x + random(50) - 25, Ani.SINE_IN_OUT);
            Ani.to(this, 1.5f, "y", _y + random(50) - 25, Ani.SINE_IN_OUT);
            Ani.to(this, 1.5f, "saturation", random(255), Ani.SINE_IN_OUT);
            Ani.to(this, 1.5f, "brightness", random(255), Ani.SINE_IN_OUT);

        }

    }
}