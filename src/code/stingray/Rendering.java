/* **********************************************************************************
 * Stingray
 * 
 * Copyright 2010 Greg Kopff
 * All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * ******************************************************************************* */

package stingray;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

/**
 *  The rendered result.
 */
public final class Rendering
{
  /** Rendering name. */
  private final String name;
  
  /** Buffered image. */
  private final BufferedImage image;
  
  private boolean[][] filled;
 
  /**
   *  Constructor.
   */
  public Rendering(String name, int width, int height)
  {
    this.name = name;
    
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < height; y++)
      {
        this.image.setRGB(x, y, 0xFF000000);
      }
    }
    
    filled = new boolean[width][height];
  }
  
  public void set(int x, int y, Colour colour)
  {
    if (filled[x][y])
    {
      Colour original = Colour.fromARGB(getRGB(x, y));

      Colour newColour = original.multiply(0.5f).add(colour.multiply(0.5f));

      // System.out.println("Blending original: " + original + " with new: " + colour + " to produce: " + newColour);
      
      setRGB(x, y, newColour.asARGB());
    }
    else
    {
      // System.out.println("Setting first time: " + colour);
      setRGB(x, y, colour.asARGB());
      filled[x][y] = true;
    }
  }
  
  private final void setRGB(int x, int y, int argb)
  {
    this.image.setRGB(x, transY(y), argb);
  }
  
  private final int getRGB(int x, int y)
  {
    return this.image.getRGB(x, transY(y));
  }
  
  private final int transY(int y)
  {
    return this.image.getHeight() - 1 - y;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  /**
   *  Write the image data out as a PNG.
   *  @param out The output stream to use.
   *  @throws IOException 
   */
  public void writePNG(OutputStream out) throws IOException
  {
    ImageIO.write(this.image, "png", out);
  }
  
  /**
   *  Get a reference to the image.
   *  @return The image.
   */
  public Image getImage()
  {
    return image;
  }
}
