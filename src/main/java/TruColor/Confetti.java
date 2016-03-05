/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TruColor;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Confetti extends PApplet {
	


static public void main(String[] args) {
  PApplet.main(Confetti.class.getName());
}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PGraphics g2d;

	int numCircles = 500;
	Circle[] circles = new Circle[numCircles]; // define the array
	
	
	
	@Override
	public void settings() {
		// TODO Auto-generated method stub
		super.settings();
		size(800, 800);
	}


	@Override
	public void setup() {


		Random rand = new Random();				
		this.dataPath("data");

		g2d =  this.getGraphics();

		smooth();
		noStroke();

		for (int i=0; i<numCircles ; i++) {
			circles[i] = new Circle(random(width),random(height)); // fill the array with circles at random positions
		}
	}


	@Override
	public void draw() {
		background(205);
		for (int i=0; i<numCircles; i++) {
			circles[i].display(); // display all the circles
		}


	

	}
	class Circle {
		float x,y; // location
		float dim; // dimension
		int color1,color2; // color
		
		Circle(float x, float y) {
			this.x = x;
			this.y = y;
			dim = random(20,50);
			color1 = color( random(255),random(255),random(255));
			color2 = color( random(255),random(255),random(255));
		}

		void display() {
			float distance = dist(x,y,mouseX,mouseY); // distance between circle and mouse
			if (distance < 255) { // if distance is smaller than 255
				fill(color2);
			} else { // if distance is bigger than 255
				fill(color1);
			}

			color1 = color( random(255),random(255),random(255));
			color2 = color( random(255),random(255),random(255));
			ellipse(x,y,dim,dim); // a circle at position xy
		}



	}
}