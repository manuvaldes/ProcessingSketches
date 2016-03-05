package CardGenerator;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;

import processing.awt.PGraphicsJava2D;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class CardGenerator extends PApplet {

	private static final long serialVersionUID = 1L;

	static public void main(String[] args) {
		PApplet.main(CardGenerator.class.getName());
	}


	PImage fondo;
	PFont myFont,myFont2;

	// PFont puede ser insuficiente.
	PathIterator iterator;
	String someText = "Título de la Carta";
	String fontName = "Cambria";

	@Override
	public void settings() {
		//hay que usar JAVA2D para poder pintar fuentes con java
		size(100, 100, JAVA2D);
		smooth(8);
	}

	@Override
	
	public void setup() {
		ClassLoader classLoader = getClass().getClassLoader();
		String dataDir = new String(classLoader.getResource("CardGenerator/data").getPath());
		
		//cargamos el fondo
		this.dataPath("data");
		fondo = this.loadImage(dataDir + "/Fondo.jpg");

		//cargamos la fuente
		 myFont = createFont("Cambria", 32,true);
		
		  // Java font for outline rendering
		  Graphics2D g2d = ((PGraphicsJava2D) g).g2;

		  FontRenderContext frc = g2d.getFontRenderContext();
		  Font font = new Font("Cambria", Font.PLAIN, 50);
		  GlyphVector gv = font.createGlyphVector(frc, someText);
		  Shape glyph = gv.getOutline(50, 300);
		  iterator = glyph.getPathIterator(null);

		myFont = createFont("Cambria", 50);
		textFont(myFont);
		 
		 
		 
		surface.setSize(fondo.width, fondo.height);
		// noLoop debe ser la ultima llamada en setup
		noLoop();
		
	}

	@Override
	public void draw() {
		background(1);
		this.image(fondo, 0, 0);

		  textAlign(LEFT, CENTER);
		  textFont(myFont);
		  fill(0);
		 // text("Título de la Carta", 50, 50);
		
		 drawText();
		

	}

	
	public void drawText() {
	 
		
		
	  float xStart, yStart; // Segment start
	  float x, y, x1, y1; // Current and end points
	  float xc1, yc1, xc2, yc2; // Cubic control points
	  float xc, yc; // Quadratic control point
	 
	  x = xStart = 50f;
	  y = yStart = 300f;
	 
	  fill(1f,1f,0f);
	  stroke(204, 102, 0);
	  strokeWeight(2);
	  
	  beginShape();
	  // Draw outline using PathIterator
	  while (!iterator.isDone()) {
	    float[] coordinates = new float[6];
	    int segType = iterator.currentSegment(coordinates);
	    switch (segType) {
	      case PathIterator.SEG_MOVETO:
	        PApplet.println("move to " + coordinates[0] + ", " + coordinates[1]);
	        x = coordinates[0];
	        y = coordinates[1];
	        //ellipse(x, y, 2, 2); // Visualize the point
	        xStart = x;
	        yStart = y;
	        break;
	      case PathIterator.SEG_LINETO:
	        PApplet.println("line to " + coordinates[0] + ", " + coordinates[1]);
	        x1 = coordinates[0];
	        y1 = coordinates[1];
	        line(x, y, x1, y1);
	        //ellipse(x1, y1, 2, 2); // Visualize the point
	        x = x1;
	        y = y1;
	        break;
	      case PathIterator.SEG_QUADTO:
	        PApplet.println("quadratic to " + coordinates[0] + ", " + coordinates[1] +
	                           ", " + coordinates[2] + ", " + coordinates[3]);
	        xc = coordinates[0];
	        yc = coordinates[1];
	        x1 = coordinates[2];
	        y1 = coordinates[3];
	        xc1 = x + 2.0f * (xc - x) / 3.0f;  // x + (2.0/3.0) * (xc - x)
	        yc1 = y + 2.0f * (yc - y) / 3.0f;  // y + (2.0/3.0) * (yc - y)
	        xc2 = xc1 + (x1 - x) / 3.0f;    // xc1 + (1.0/3.0) * (x1 - x)
	        yc2 = yc1 + (y1 - y) / 3.0f;    // yc1 + (1.0/3.0) * (y1 - y)
	        //noFill(); 
	        bezier(x, y, xc1, yc1, xc2, yc2, x1, y1); 
	        //ellipse(x1, y1, 2, 2); // Visualize the point
	        x = x1;
	        y = y1;
	        break;
	      case PathIterator.SEG_CUBICTO:
	        PApplet.println("cubic to " + coordinates[0] + ", " + coordinates[1] + ", "
	                            + coordinates[2] + ", " + coordinates[3] + ", "
	                            + coordinates[4] + ", " + coordinates[5]);
	        xc1 = coordinates[0];
	        yc1 = coordinates[1];
	        xc2 = coordinates[2];
	        yc2 = coordinates[3];
	        x1  = coordinates[4];
	        y1  = coordinates[5];
	        //noFill(); 
	        bezier(x, y, xc1, yc1, xc2, yc2, x1, y1); 
	        
	        //ellipse(x1, y1, 2, 2); // Visualize the point
	        x = x1;
	        y = y1;
	        break;
	      case PathIterator.SEG_CLOSE:
	        PApplet.println("close");
	        line(x, y, xStart, yStart);
	        endShape(CLOSE);
	        beginShape();
	        break;
	      default:
	        break;
	    } // switch
	    iterator.next();
	  } // while
	
	
	}
}

