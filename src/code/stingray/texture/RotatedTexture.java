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
 *  Rotated Texture.
 */
public final class RotatedTexture implements Texture
{
  /** Texture. */
  private final Texture t;
  
  /** Rotation axis. */
  private final Vector axis;
  
  /** Rotation amount. */
  private final double radians;
  
  /**
   *  Constructor.
   */
  public RotatedTexture(Texture t, Vector axis, double degrees)
  {
    this.t = t;
    this.axis = axis.normalise();
    this.radians = Math.toRadians(degrees);
  }
  
  /**
   *  Get the pigment specification for the given point.
   *  @return The pigment.
   */
  public Pigment getPigmentAt(Vector point)
  {
    final Vector adjusted = point.rotate(axis, radians);
    return t.getPigmentAt(adjusted);
  }
  
  /**
   *  Get the finish specification for the given point.
   *  @return The finish.
   */
  public Finish getFinishAt(Vector point)
  {
    final Vector adjusted = point.rotate(axis, radians);
    return t.getFinishAt(adjusted);
  }
}