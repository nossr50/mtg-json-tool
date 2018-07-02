package com.gmail.nossr50.tools;

import java.io.IOException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import io.magicthegathering.javasdk.resource.Card;

/**
 * This class manages the current image being displayed for our card art in the GUI
 * @author nossr50
 */
public class CardImageManager {

    private Image       cardArt         = null;
    private GC          gc              = null;

    private String      defaultImage    = "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=439390&type=card";
    private Composite   cardArtCanvas;

    /**
     * Constructor for the manager, loads a default image which is shown until a new one is requested
     * @param canvas The area in which the card art will be drawn
     * @param display The Display being used by our GUI
     */
    public CardImageManager(Composite canvas, Display display) {
        cardArtCanvas = canvas; //The area in which the card art will be drawn
        loadDefaultImage(display); //Loads a default image
        AddListener(canvas); //Listens for new images
    }


    /**
     * @return The Image object for our card art
     */
    public Image getImage() {
        return cardArt;
    }

    /**
     * Synchronized method
     * <p> Loads an image from the web and draws it to our GUI
     * @param display The display our GUI is using
     * @param cardURL the URL to load the image from
     */
    synchronized public void loadImage(Display display, URL cardURL) {
        try {
            //setVisibility(false);
            cardArt = new Image(display, cardURL.openStream());
            
            //This draws the image
            gc = new GC(cardArt);
            gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));

            /*
            int width = cardArt.getBounds().width;
            int height = cardArt.getBounds().height;

            System.out.println("W/H: " + width + " " + height);

            //gc.drawImage(cardArt,0,0,width,height,0,0,width*2,height*2);

            */
            
            //Draw and then update the widget
            cardArtCanvas.redraw();
            cardArtCanvas.update();
            
            //setVisibility(true);

            //Dispose of GC since we're done with it
            gc.dispose();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Synchronized method
     * <p> Loads an image from a URL which updates the card art on our GUI with the new image
     * @param display The display being used by our GUI
     * @param card The card to draw on the GUI
     */
    synchronized public void loadImage(Display display, Card card) {
        try {
            //Download and load the new card art
            URL cardURL = new URL(card.getImageUrl());
            loadImage(display, cardURL);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads the default image so we have something to look at while we wait for requests
     * @param display the display being used by our GUI
     */
    public void loadDefaultImage(Display display) {
        try {
            URL cardURL = new URL(defaultImage);
            loadImage(display, cardURL);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets a Rectangle of our Cards bounds
     * @return the bounds of our card art
     */
    public Rectangle getBounds() {
        return cardArt.getBounds();
    }

    /**
     * Sets up a listener for draw events which updates the card art as new ones come in
     * @param composite the composite which we are listening to
     */
    public void AddListener(Composite composite) {
        System.out.println("Painter listener added!");
        composite.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {
                e.gc.drawImage(cardArt, 0, 0, cardArt.getBounds().width, cardArt.getBounds().height, 0, 0,
                        cardArt.getBounds().width, cardArt.getBounds().height);

            }
        });

        System.out.println("Painter listener added!");
    }
    
    public void setVisibility(boolean visibility)
    {
        cardArtCanvas.setVisible(visibility);
        cardArtCanvas.update();
    }
}
