package ColorStreamer; /**
 * Created by Manuel on 28/02/2016.
 */


import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class ColorStreamer extends PApplet {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    PImage foto, histo;
    String filename;

    static public void main(String[] args) {
        PApplet.main(ColorStreamer.class.getName());
    }



    @Override
    public void settings() {
        // TODO Auto-generated method stub
        super.settings();
        size(100, 100,P2D);
    }

    @Override
    public void setup() {

        // esto peta con P2D
        //surface.setResizable(true);
        ClassLoader classLoader = getClass().getClassLoader();
        File dataDir = new File(classLoader.getResource("ColorStreamer/data").getFile());
        File outputDir = new File(classLoader.getResource("ColorStreamer/output").getFile());


        File[] filesList = dataDir.listFiles();


        for (int i = 0; i < filesList.length; i++) {

            foto = loadImage(filesList[i].getAbsolutePath());


            int histoHeight= min(foto.height / 3, foto.width / 3, 200);

            PGraphics pg = createGraphics( foto.width + 100, foto.height + 150 + histoHeight);

            pg.beginDraw();

            colorMode(HSB, 10, 5, 10, 1);
            histo = streamColor(foto);


            colorMode(RGB, 255, 255, 255, 1);
            pg.background(255,255,255);
            pg.image(foto,50,50);

            float meanRed, meanBlue, meanGreen;
            int meanColor;

            PImage miniHisto = new PImage(foto.width,histoHeight);


            // adicionalmente sacamos el valor medio de cada columna
            for (int x = 0; x < histo.width; x++) {
                meanRed = 0;
                meanBlue = 0;
                meanGreen = 0;
                for (int y = 0; y < histo.height; y++) {
                    int color = histo.get(x, y);
                    meanRed = meanRed + red(color) / histo.height;
                    meanBlue = meanBlue + blue(color) / histo.height;
                    meanGreen = meanGreen + green(color) / histo.height;

                }
                meanColor = color(meanRed, meanGreen, meanBlue);
                for (int y = 0; y < histoHeight; y++) {
                    miniHisto.set(x, y , meanColor);
                }

            }

            pg.image(miniHisto,50,100+foto.height);
            pg.endDraw();
            System.out.println("salvando: " + filesList[i].getName());
            // el salvado asincrono sacaba frames en negro
            pg.hint(DISABLE_ASYNC_SAVEFRAME);
            pg.save(outputDir.getAbsolutePath() + filesList[i].getName());
        }
        noLoop();
    }

    @Override
    public void draw() {

        exit();

    }


    PImage streamColor(PImage foto) {

        histo = new PImage(foto.width, foto.height);
        //hay que llamar a loadpixels para acceder al array
        foto.loadPixels();

        ArrayList<ImagePixel> ipHisto = new ArrayList<ImagePixel>();

        for (int i = 0; i < foto.pixels.length; i++) {

            ipHisto.add(new ImagePixel(foto.pixels[i]));

        }
        //ipHisto.forEach(System.out::println);

        ipHisto.sort(Comparator.comparing((ImagePixel p) -> p.iipHue).thenComparing((ImagePixel p) -> p.iipBright).thenComparing((ImagePixel p) -> p.sortValue));
        //ipHisto.sort(Comparator.comparing( (ImagePixel p)->p.sortValue ));

        int i = 0;

        for (ImagePixel hImagePixel : ipHisto) {
            //cambio de orientacion
            histo.pixels[(Math.round(i / histo.height) + (histo.width * (i % histo.height)))] = hImagePixel.ipColor;
            i++;
        }

        histo.updatePixels();
        return histo;

    }


    class ImagePixel {

        int x = -1;
        int y = -1;
        Float ipHue, ipSat, ipBright;

        int iipHue, iipSat, iipBright;
        int ipColor;

        float sortValue;


        ImagePixel(int _ipColor) {

            ipColor = _ipColor;
            ipHue = hue(_ipColor);
            ipSat = saturation(_ipColor);
            ipBright = brightness(_ipColor);

            iipHue = Math.round(ipHue);
            //iipHue = new Float(ipHue * sin((PI)*(ipBright/100))).intValue();
            iipSat = Math.round(ipSat * sin((PI / 2) * (ipBright / 100)));
            iipBright = Math.round(ipBright);

            sortValue = ipBright * 5 + ipSat * 2 + ipHue;

            //Z-order transform
            sortValue = split(ipColor);

            //ipColor = color(iipHue,iipSat,iipBright);
        }

        public int split(int a) {
            // split out the lowest 10 bits to lowest 30 bits
            a = (a | (a << 12)) & 00014000377;
            a = (a | (a << 8)) & 00014170017;
            a = (a | (a << 4)) & 00303030303;
            a = (a | (a << 2)) & 01111111111;
            return a;
        }

        @Override
        public String toString() {
            return "ipHue:" + ipHue + " ipSat: " + ipSat + " ipBright: " + ipBright;
        }
    }

}