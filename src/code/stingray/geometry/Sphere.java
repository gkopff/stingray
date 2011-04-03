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
 *  A sphere.
 */
public final class Sphere implements Geometry
{
  /** Coordinate of centre. */
  private final Vector centre;
  
  /** Radius. */
  private final double radius;
  
  /** Texture. */
  private final Texture texture;
  
  /**
   *  Constructor.
   *  @param centre The centre of the sphere.
   *  @param radius The radius of the sphere.
   *  @param texture The texture of the sphere.
   */
  public Sphere(Vector centre, double radius, Texture texture)
  {
    this.centre = centre;
    this.radius = radius;
    this.texture = texture;
  }
  
  /**
   *  Test for ray intersections.  If an intersection occurs, add it to the intersection list.
   *  @param ray The ray.
   *  @param intersections The intersection list.
   */
  public void intersect(Ray ray, IntersectionList intersections)
  {
    // Our calculations require the sphere to be centred at the origin, so
    // we first adjust the ray's origin accordingly.
    
    final Vector adjustedRayOrigin;                                  // adjusted ray
    final double[] solutions;                                        // sphere/ray intersection solutions
    
    adjustedRayOrigin = ray.getOrigin().subtract(centre);            // adjust the ray origin
    solutions = QuadraticMath.solve(adjustedRayOrigin,               // solve the sphere/ray intersection
                                    ray.getDirection(), 
                                    radius);
    
    // Now we insepct the solutions.  The solution tells us how far down the ray an intersection occurred.
    // If the distance is negative, the intersection occurs in the "wrong" direction, and can be discarded.
    
    if (solutions != null)                                           // if an intersection occurred ...
    {
      if (solutions[0] > 0)                                          // is the intersection in the ray's positive direction?
      {
        intersections.add(this, solutions[0]);                       // ... yes, retain this intersection
      }
      
      if (solutions[1] > 0)                                          // and is this in the positive direction?
      {
        intersections.add(this, solutions[1]);                       // ...yes, retain this intersection
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
    final Vector intersect;                                          // intersection point
    final Vector normal;                                             // sphere surface normal
    
    // Geometry note: for a sphere, the normal is simply the intersection point, since all points in the 
    // sphere are normal to it's centre.
    
    intersect = intersection.asVector();                             // convert to vector coordinate
    normal = intersect.subtract(this.centre).normalise();            // calculate the shere's surface normal
    
    // if (this.texture instanceof ReflectiveTexture)
    // {
    //   return getColourReflective(stage, intersection, normal, lights);
    // }
    // else
    // {
    return Lighting.getColour(context,
                              recurseNo,
                              intersection, 
                              normal, 
                              lights, 
                              texture);
    // }
  }
  
  /**
   *  Get the surface normal at the given ray/geometry intersection point.
   *  @param intersection The intersection.
   *  @return The surface normal.
   */
  public Vector getSurfaceNormal(Intersection intersection)
  {
    return intersection.asVector().subtract(this.centre).normalise();
  }
  
  /**
   *  Get a new object that is translated by the given vector.
   *  @param translation The translation vector.
   *  @return A new object equivalent to the original object, translated by the translation vector.
   */
  public Sphere translate(Vector translation)
  {
    return new Sphere(this.centre.add(translation), this.radius, this.texture);
  }
  
  /**
   *  Does the geometry enclose the given point.
   *  @param point The point to consider.
   *  @return True if the point is <i>contained</i> in the geometry.
   */
  public boolean contains(Vector point)
  {
    final double distance;
    final Vector adjusted;

    adjusted = point.subtract(centre);                               // centre the sphere
    distance = adjusted.length();                                    // adjusted point's distance from origin

    return distance - radius <= Rays.eps;
  }
  
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return "Sphere[centre: " + centre + "; radius: " + radius + "]"; 
  }
}
