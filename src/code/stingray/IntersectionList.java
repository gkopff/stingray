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

import java.util.*;

import stingray.geometry.*;


/**
 *  An ordered set of intersections.
 *  Closest intersection appears first.
 */
public final class IntersectionList implements Iterable<Intersection>
{
  /** The ray this is associated with. */
  private final Ray ray;
  
  /** Intersections. */
  private final SortedSet<Intersection> intersections;
  
  /**
   *  Constructor.
   *  @param ray The rays that the intersections will be for.
   */
  public IntersectionList(Ray ray)
  {
    this.ray = ray;
    this.intersections = new TreeSet<Intersection>();
  }
  
  /**
   *  Add the given geometry to the list (unless the geometry is excluded).
   *  @param g The geometry to add.
   *  @param length The length down the ray where the intersection occurred.
   */
  public void add(Geometry g, double length)
  {
    if (length > Rays.eps)                                           // if the hit is not right at the ray start
    {
      this.intersections.add(new Intersection(this.ray, g, length)); // ... add it
    }
  }
  
  /**
   *  Get the number of intersections recorded.
   *  @return The number of intersections.
   */
  public int size()
  {
    return this.intersections.size();
  }
  
  /**
   *  Is the intersection list empty?
   *  @return True if there are no intersections, false otherwise.
   */
  public boolean isEmpty()
  {
    return this.intersections.size() == 0;
  }
  
  /**
   *  Get the closest intersection.
   *  @return The closest intersection.
   *  @throws NoSuchElementException If the intersection list is empty.
   */
  public Intersection closest() throws NoSuchElementException
  {
    return this.intersections.first();
  }

  /**
   *  Get an iterator over the intersections.
   */
  @Override
  public Iterator<Intersection> iterator()
  {
    return this.intersections.iterator();
  }
}
