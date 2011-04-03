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


/**
 *  A ray, comprised of a starting point, and a direction (expressed as a unit vector).
 */
public final class Ray
{
  /** Origin. */
  private final Vector origin;
  
  /** Direction. */
  private final Vector direction;
  
  /**
   *  Constructor.
   *  @param origin The ray's origin.
   *  @param direction The ray's direction (will be normalised internally).
   */
  public Ray(Vector origin, Vector direction)
  {
    this.origin = origin;
    this.direction = direction.normalise();
  }
  
  /**
   *  Get the ray's origin.
   */
  public Vector getOrigin()
  {
    return this.origin;
  }
  
  /**
   *  Get the ray's direction.
   */
  public Vector getDirection()
  {
    return this.direction;
  }
  
  /**
   *  Get the point <i>length</i> units down the ray.
   *  @param length The length down the ray.
   *  @return A vector representing the appropriate point.
   */
  public Vector getPointAt(double length)
  {
    return origin.add(direction.scale(length));
  }
  
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return "Ray[origin: " + origin + "; direction: " + direction + "]"; 
  }
}
