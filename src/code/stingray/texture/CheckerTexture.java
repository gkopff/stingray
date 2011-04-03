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

package stingray.texture;

import stingray.*;


/**
 *  Checkered Texture.
 */
public final class CheckerTexture implements Texture
{
  /** First texture. */
  private final Texture t1;
  
  /** Second texture. */
  private final Texture t2;
  
  /** Checker length. */
  private final double length;
  
  /**
   *  Constructor.
   */
  public CheckerTexture(Texture t1, Texture t2, double length)
  {
    this.t1 = t1;
    this.t2 = t2;
    this.length = length;
  }
  
  /**
   *  Get the pigment specification for the given point.
   *  @return The pigment.
   */
  public Pigment getPigmentAt(Vector point)
  {
    return getTextureFor(point).getPigmentAt(point);
  }
  
  /**
   *  Get the finish specification for the given point.
   *  @return The finish.
   */
  public Finish getFinishAt(Vector point)
  {
    return getTextureFor(point).getFinishAt(point);
  }
  
  /**
   *  Get the appropriate texture for the given point.
   *  @param point The point.
   *  @return The appropriate texture.
   */
  private Texture getTextureFor(Vector point)
  {
    boolean choice;                                                  // texture choice
    
    choice = (int) (point.getX() / this.length) % 2 == 0;
    if (point.getX() < 0)
    {
      choice = !choice;
    }
    
    if ((int) (point.getY() / this.length) %2 != 0)
    {
      choice = !choice;
    }
    
    if (point.getY() < 0)
    {
      choice = !choice;
    }
 
    if ((int) (point.getZ() / this.length) %2 != 0)
    {
      choice = !choice;
    }
    
    if (point.getZ() < 0)
    {
      choice = !choice;
    }

    return choice ? this.t1 : this.t2;
  }
}
