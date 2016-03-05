/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Mosaico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import toxi.geom.Line2D;
import toxi.geom.Polygon2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;
import toxi.processing.ToxiclibsSupport;

public class Mosaico extends PApplet {

	static public void main(String[] args) {
		PApplet.main(Mosaico.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ToxiclibsSupport gfx;
	Voronoi voronoi;
	List<Polygon2D> curr_poly,new_poly;
	PImage imagen;

	int escala = 30;


	@Override
	public void settings() {
		// TODO Auto-generated method stub
		super.settings();
		this.dataPath("data");
        size(100,100,P2D);
	}




	@Override
	public void setup() {

        ClassLoader classLoader = getClass().getClassLoader();
        File dataDir = new File(classLoader.getResource("Mosaico/data").getFile());


        imagen = this.loadImage(dataDir.getAbsolutePath() + "/test2.jpg");

        surface.setSize(imagen.width, imagen.height);
		Random rand = new Random();


		gfx = new ToxiclibsSupport(this);
		voronoi = new Voronoi();
		/* generador cuadrado
		
		  for (int x = 0; x < this.width / escala; x++) { for (int y = 0; y <
		  this.height / escala; y++) { 
			  voronoi.addPoint(new Vec2D(x * escala + (int)( 2*rand.nextGaussian() + min(2,escala / 20)), 
					  					 y * escala + (int)( 2*rand.nextGaussian() + min(2,escala / 20)))); }
		  
		  }*/
		
		// generador hexagonal
		for (int y = 0; y < this.height / escala*3/1; y++) {
			for (int x = 0; x < this.width / escala; x++) {
				voronoi.addPoint(new Vec2D((x + (y % 2 )/2f) * escala + (int)( 2* rand.nextGaussian() + min(2,escala / 20)), 
											y * escala * 1/3 + (int)( 2*rand.nextGaussian() + min(2,escala / 20))));
			}

		}

        curr_poly = voronoi.getRegions();
		  //drawMesh(compressMesh(voronoi.getRegions()));

         //drawPoints();
 		 
	}

	@Override
	public void draw() {

        drawMesh(curr_poly);


	}

	@Override
	public void mouseClicked() {

        curr_poly = vibrateMesh(curr_poly);




	}



    private List<Polygon2D> vibrateMesh(List<Polygon2D> list) {

        ArrayList new_poly = new ArrayList<Polygon2D>();
        Vec2D centroide;

        for (Polygon2D polygon : list) {
            centroide = polygon.getCentroid();
            if (centroide.x > 0 && centroide.y > 0 && centroide.x < this.width
                    && centroide.y < this.height) {

                Polygon2D new_poligono = new Polygon2D();

                for (Vec2D vertice : polygon.vertices) {

                    Vec2D vec_compress = vertice.sub(centroide);

                    new_poligono.vertices.add(vertice.sub(vec_compress
                            .scale((float) 1 / vec_compress.magnitude())));
                }

                new_poly.add(new_poligono);
            }

        }

        return new_poly;
    }



    void drawMesh(List<Polygon2D> new_poly2) {

		clear();
		background(255);

		//dibujamos gradiente por puntas del poligono
		for (Polygon2D polygon : new_poly2) {
		    beginShape();
		    noStroke();
		    for (Vec2D v: polygon.vertices) {
		      
		      fill(imagen.get((int)v.x, (int) v.y));
		      vertex(v.x,v.y);
		    }
		    endShape(CLOSE);
		    
			// esto dibuja en base al pixel del centroide
			
/*			this.fill(imagen.get((int) polygon.getCentroid().x,
					(int) polygon.getCentroid().y));
			gfx.polygon2D(polygon);*/
		}
		


		
	}

	void drawPoints() {

		clear();
		background(255);

		for (Vec2D punto : voronoi.getSites()) {
			gfx.circle(punto, 2f);
			;
		}
	}




    void desconocidoOnClick (){






        Vec2D punto = new Vec2D(mouseX, mouseY);
		/*
		 * voronoi.addPoint(punto); drawVoronoi(voronoi);
		 */

        new_poly= voronoi.getRegions();
        int indice = 0;
        for (Polygon2D polygon : new_poly) {

            if (polygon.containsPoint(punto)) {
                indice = new_poly.indexOf(polygon);
                continue;
            }
        }
        if (indice == 0) {
            return;
        }

        // creamos los poligonos y les agregamos el vertice que les corresponde,
        // mas el centro

        Polygon2D newpol1 = new Polygon2D();
        Polygon2D newpol2 = new Polygon2D();
        Polygon2D newpol3 = new Polygon2D();
        Polygon2D newpol4 = new Polygon2D();

        for (Vec2D vertice : new_poly.get(indice).vertices) {
            if (vertice.x < punto.x) {
                if (vertice.y < punto.y) {
                    newpol1.vertices.add(vertice);
                } else {
                    newpol3.vertices.add(vertice);
                }
            } else {
                if (vertice.y < punto.y) {
                    newpol2.vertices.add(vertice);
                } else {
                    newpol4.vertices.add(vertice);
                }

            }

        }

        // en caso de que haya mas de 4 vertices en el poligono original
        Vec2D refvec12 = null, refvec13 = null, refvec21 = null, refvec24 = null, refvec31 = null, refvec34 = null, refvec42 = null, refvec43 = null;

        for (Vec2D vertice : newpol1.vertices) {
            if (refvec12 == null) {
                refvec12 = vertice;
                refvec13 = vertice;
                continue;
            } else {
                if (vertice.x > refvec12.x) {
                    refvec12 = vertice;

                }
                if (vertice.y > refvec13.y) {
                    refvec13 = vertice;
                }
            }

        }

        for (Vec2D vertice : newpol2.vertices) {
            if (refvec21 == null) {
                refvec21 = vertice;
                refvec24 = vertice;
                continue;
            } else {
                if (vertice.x < refvec21.x) {
                    refvec21 = vertice;

                }
                if (vertice.y > refvec24.y) {
                    refvec24 = vertice;
                }
            }

        }

        for (Vec2D vertice : newpol3.vertices) {
            if (refvec31 == null) {
                refvec31 = vertice;
                refvec34 = vertice;
                continue;
            } else {
                if (vertice.x > refvec34.x) {
                    refvec34 = vertice;

                }
                if (vertice.y < refvec31.y) {
                    refvec31 = vertice;
                }
            }

        }

        for (Vec2D vertice : newpol4.vertices) {
            if (refvec42 == null) {
                refvec42 = vertice;
                refvec43 = vertice;
                continue;
            } else {
                if (vertice.x < refvec43.x) {
                    refvec43 = vertice;

                }
                if (vertice.y < refvec42.y) {
                    refvec42 = vertice;
                }
            }

        }

        newpol1.vertices.add(new Line2D(refvec12, refvec21).getMidPoint().add(
                -1, 0));
        newpol1.vertices.add(punto.sub(new Vec2D(2, 2)));
        newpol1.vertices.add(new Line2D(refvec13, refvec31).getMidPoint().add(
                0, -1));

        newpol2.vertices.add(new Line2D(refvec42, refvec24).getMidPoint().add(
                0, -1));
        newpol2.vertices.add(punto.sub(new Vec2D(-2, 2)));
        newpol2.vertices.add(new Line2D(refvec12, refvec21).getMidPoint().add(
                1, 0));

        newpol3.vertices.add(new Line2D(refvec13, refvec31).getMidPoint().add(
                0, 1));
        newpol3.vertices.add(punto.sub(new Vec2D(2, -2)));
        newpol3.vertices.add(new Line2D(refvec43, refvec34).getMidPoint().add(
                -1, 0));

        newpol4.vertices.add(new Line2D(refvec42, refvec24).getMidPoint().add(
                0, 1));
        newpol4.vertices.add(punto.sub(new Vec2D(-2, -2)));
        newpol4.vertices.add(new Line2D(refvec43, refvec34).getMidPoint().add(
                1, 0));

        // el remove siempre por delante de los add para que no cambien el
        // indice
        new_poly.remove(indice);

        new_poly.add(newpol1);
        new_poly.add(newpol2);
        new_poly.add(newpol3);
        new_poly.add(newpol4);
        drawMesh(new_poly);
    }


}