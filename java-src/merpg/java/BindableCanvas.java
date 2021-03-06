package merpg.java;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import clojure.java.api.Clojure;
import clojure.lang.*;

public class BindableCanvas extends JPanel implements Scrollable
{
    public Map statemap = new HashMap();
    
    Agent img_agent;

    @Override
    public Dimension getPreferredSize()
    {
	BufferedImage img = (BufferedImage)img_agent.deref();
	if(img == null) {
	    System.out.println("img == null at BindableCanvas.getPreferredScrollableViewportSize");
	    return new Dimension(0,0);
	}

	return new Dimension(img.getWidth(), img.getHeight());
    }
    
    public BindableCanvas(Agent img_agent)
    {
	this.img_agent = img_agent;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize()
    {
	BufferedImage img = (BufferedImage)img_agent.deref();
	if(img == null) {
	    System.out.println("img == null at BindableCanvas.getPreferredScrollableViewportSize");
	    return new Dimension(0,0);
	}

	return new Dimension(img.getWidth(), img.getHeight());
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
	return 50;
    }
    
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
    {
	return 50;
    }

    @Override
    public boolean getScrollableTracksViewportWidth()
    {
	return false;
    }


    @Override
    public boolean getScrollableTracksViewportHeight()
    {
	return false;
    }

    
    
    @Override
    public void paintComponent(Graphics gg)
    {
	super.paintComponent(gg);
	System.out.println ("Painting a component @ BindableCanvas");	
	Graphics2D g = (Graphics2D)gg;
	BufferedImage img = (BufferedImage)img_agent.deref();

	System.out.println("img's size at BindableCanvas.paintComponent: ["+img.getWidth()+" :by " +img.getHeight()+"]");

	if(img == null) {
	    System.out.println("img == null at BindableCanvas.paintComponent");
	    return;
	}
	if(g == null) {
	    System.out.println("g == null at BindableCanvas.paintComponent");
	    return;
	}

	g.drawImage(img, 0, 0, null);

	g.setColor(Color.BLACK);

	int border_width = 3;
	//vertical border
	g.fill(new Rectangle(getWidth()-border_width, 0, getWidth(), getHeight()));
	//horizontal border
	g.fill(new Rectangle(0, getHeight()-border_width, getWidth(), getHeight()));
	
	
	System.out.println("Component drawn");
    }
}
