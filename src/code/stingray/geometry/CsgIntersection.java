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

package stingray.geometry;

import java.util.*;

import stingray.*;
import stingray.Vector;


/**
 *  A CSG intersection.
 */
public final class CsgIntersection implements Geometry
{
  /** First geometry. */
  private final Geometry first;
  
  /** Second geometry. */
  private final Geometry second;
  
  /** The texture. */
  private final Texture texture;
  
  /**
   *  Constructor.
   */
  public CsgIntersection(Geometry first, Geometry second, Texture texture)
  {
    this.first = first;
    this.second = second;
    this.texture = texture;
  }
  
  /**
   *  Test for ray intersections.  If an intersection occurs, add it to the intersection list.
   *  @param ray The ray.
   *  @param intersections The intersection list.
   */
  public void intersect(Ray ray, IntersectionList intersections)
  {
    final IntersectionList il = new IntersectionList(ray);
    
    first.intersect(ray, il);
    second.intersect(ray, il);
    
    for (Intersection i : il)
    {
      final Vector v = i.asVector();
      if (first.contains(v) && second.contains(v))
      {
        intersections.add(i.getGeometry(), i.getLength());
        System.out.println("CsgIntersection.intersect(): found point: " + v);
      }
    }
  }
 
  /**
   *  Get the colour at the given ray/geometry intersection point.  <p>
   *  
   *  It can be relied upon that an intersection <i>does</i> in fact occur at this point (if 
   *  because of a bug, an intersection does not occur here, a <code>RayMissedException</code> 
   *  can be raised).  <p>
   *  
   *  The given list of lights are the lights that are <i>actually</i> illuminating this geometry.
   *  The lights in the list may be actual light sources, or pseudo light sources synthesised
   *  as a result of a reflected light ray.
   *  
   *  @param context The render context.
   *  @param recurseNo Recursion count.
   *  @param intersection The intersection where the light strikes the geometry.
   *  @param lights The illuminating lights.
   *  @return The colour at this point.
   *  @throws RayMissedException If we cannot compute a colour for this point.
   */
  public Colour getColourAt(RenderContext context, int recurseNo, Intersection intersection, List<Light> lights) throws RayMissedException
  {
    Geometry g = intersection.getGeometry();
    
    return Lighting.getColour(context,
                              recurseNo,
                              intersection,
                              g.getSurfaceNormal(intersection),
                              lights,
                              texture);
  }
  
  /**
   *  Get the surface normal at the given ray/geometry intersection point.
   *  @param intersection The intersection.
   *  @return The surface normal.
   */
  public Vector getSurfaceNormal(Intersection intersection)
  {
    throw new UnsupportedOperationException("FIXME: implement");
  }
  
  /**
   *  Get a new object that is translated by the given vector.
   *  @param translation The translation vector.
   *  @return A new object equivalent to the original object, translated by the translation vector.
   */
  public CsgIntersection translate(Vector translation)
  {
    return this;
  }
  
  /**
   *  Does the geometry enclose the given point.
   *  @param point The point to consider.
   *  @return True if the point is <i>contained</i> in the geometry.
   */
  public boolean contains(Vector point)
  {
    return false;
  }
  
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return "CSG Intersection[first: " + first + "; second: " + second + "]"; 
  }
}
