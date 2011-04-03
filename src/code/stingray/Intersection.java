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

import stingray.geometry.*;


/**
 *  Ray/geometry intersection data.
 */
public final class Intersection implements Comparable<Intersection>
{
  /** The ray associated with this intersection. */
  private final Ray ray;
  
  /** The object hit by this intersection. */
  private final Geometry geometry;
  
  /** The length down the ray where the intersection occurred. */
  private final double length;
  
  /**
   *  Constructor.
   *  @param ray The ray.
   *  @param geometry The intersected geometry.
   *  @param length The length down the ray where the intersection occurred.
   */
  public Intersection(Ray ray, Geometry geometry, double length)
  {
    this.ray = ray;
    this.geometry = geometry;
    this.length = length;
    
    if (this.length < 0)
    {
      throw new IllegalArgumentException("length must be positive; was: " + length);
    }
  }

  /**
   *  Get the ray.
   */
  public Ray getRay()
  {
    return this.ray;
  }

  /**
   *  Get the geometry that was intersected.
   */
  public Geometry getGeometry()
  {
    return this.geometry;
  }

  /**
   *  Get the length down the ray where the intersection occurred.
   */
  public double getLength()
  {
    return this.length;
  }

  /**
   *  Get the intersection as a vector.
   *  @return A vector representing the point where the intersection occurred.
   */
  public Vector asVector()
  {
    return this.ray.getPointAt(this.length);
  }
  
  /**
   *  Compare intersections.
   *  Note: for efficiency, only the lengths are compared, so it is not useful to
   *  "sort" intersections that are for different rays.
   */
  @Override
  public int compareTo(Intersection that)
  {
    return Double.compare(this.length, that.length);
  }
}
