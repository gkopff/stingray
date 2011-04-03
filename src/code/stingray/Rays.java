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
 *  Ray casting logic.
 */
public abstract class Rays
{
  public static final double eps = 0.00001;
  
//  private static final Vector[] aa = { new Vector(-0.5d,  0.5d, 0.0d),
//                                       new Vector( 0.5d,  0.5d, 0.0d), 
//                                       new Vector(-0.5d, -0.5d, 0.0d), 
//                                       new Vector( 0.5d, -0.5d, 0.0d) };
  
//  private static final Vector[] aa = { new Vector(-0.5d,  0.0d, 0.0d),
//                                       new Vector( 0.5d,  0.0d, 0.0d) };
  
  private static final Vector[] aa = { new Vector( 0.0d,  0.0d, 0.0d) };
  
  public static void rayTraceStage(RenderContext context, int x, int y, Rendering r, int width, int height, double z)
  {
    final Stage stage = context.getStage();
    final Vector direction = new Vector(x - (width / 2), y - (height / 2), z);
    // final Ray ray = new Ray(stage.getCamera().getPosition(), direction);
    final Ray[] rays = new Ray[aa.length];
    // final Ray[] rays = new Ray[1];
    
    rays[0] = new Ray(stage.getCamera().getPosition(), direction);
    for (int i = 0; i < rays.length; i++)
    {
      rays[i] = new Ray(stage.getCamera().getPosition(), direction.add(aa[i]));
    }
    
    for (Ray ray : rays)
    {
      final IntersectionList intersections = new IntersectionList(ray);
      for (Geometry g : stage.getGeometry())
      {
        g.intersect(ray, intersections);
      }

      if (intersections.size() > 0)
      {
        Intersection i = intersections.closest();
        r.set(x, y, i.getGeometry().getColourAt(context, 0, i, Rays.getIlluminatingLights(stage, 
                                                                                          stage.getLights(), 
                                                                                          i.asVector())));
      }
    }
  }
  
  // FIXME: change this approach
  public static final List<Light> getIlluminatingLights(Stage stage, List<Light> candidates, Vector intersection)
  {
    // First, determine which light sources are actually lighting this point ...
    
    final List<Light> illuminating = new ArrayList<Light>();
    for (Light light : candidates)
    {
      final Vector direction = light.getPosition().subtract(intersection);
      final Ray shadowRay = new Ray(intersection, direction);
      final IntersectionList intersections = new IntersectionList(shadowRay);
      
      for (Geometry g : stage.getGeometry())
      {
        g.intersect(shadowRay, intersections);
      }
      
      if (intersections.isEmpty())                                 // if we didn't hit anything ...
      {
        illuminating.add(light);                                   // ... light gets through
      }
      else                                                         // we hit something ...
      {
        final Intersection closest = intersections.closest();
        final double lightDist;
        
        lightDist = direction.length();                            // determine the distance to the light ...
        if (closest.getLength() > lightDist)                       // was the hit beyond where the light was?
        {
          illuminating.add(light);                                 // ... yes, light is closer
        }
      }
    }
    
    return illuminating;
  }
  
  
  /** Private constructor. */
  private Rays() { ; }
}
