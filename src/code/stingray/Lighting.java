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

import stingray.texture.*;


/**
 *  Common lighting routines.
 */
public abstract class Lighting
{
  public static final Colour getColour(RenderContext context,
                                       int recurseNo,
                                       Intersection intersection, 
                                       Vector surfaceNormal, 
                                       List<Light> lights, 
                                       Texture texture)
  {
    final Vector intersect = intersection.asVector();
    final Finish finish = texture.getFinishAt(intersect);
  
    if (finish.getReflection() > 0)
    {
      return mirror(context, recurseNo, intersection, surfaceNormal, lights, texture);
    }
    else
    {
      return regular(context, recurseNo, intersection, surfaceNormal, lights, texture);
    }
  }
  
  
  private static final Colour mirror(RenderContext context,
                                     int recurseNo,
                                     Intersection intersection, 
                                     Vector surfaceNormal, 
                                     List<Light> lights, 
                                     Texture texture)
  {
    final Vector intersect = intersection.asVector();
    final Vector reflection;                                       // reflected sight say direction
    
    reflection = intersection.getRay().getDirection().getReflectedDirection(surfaceNormal);
    
    final Ray r = new Ray(intersect, reflection);
    return context.getStage().getColourFor(context, r, recurseNo + 1, Colour.BLACK);
  }
  
  private static final Colour regular(RenderContext context,
                                      int recurseNo,
                                      Intersection intersection, 
                                      Vector surfaceNormal, 
                                      List<Light> lights, 
                                      Texture texture)
  { 
    final Vector intersect = intersection.asVector();
    final Pigment pigment = texture.getPigmentAt(intersect);
    final Finish finish = texture.getFinishAt(intersect);
    Colour colour;
    
    colour = pigment.getColourAt(intersect).multiply(finish.getAmbient());
    
    for (Light light : lights)                                       // for each point of illumination ...
    {
      double dot;                                                    // simple light/surface normal angle calculation
      final Vector lightDirection;
      final Vector reflection;                                       // reflected light direction

      // Calculate the direction of the light source, normalising the result.  Then, apply "dot" vector
      // multiplication to calculate the "angle" of the light and the sphere's surface normal.

      lightDirection = light.getPosition().subtract(intersect).normalise();
      reflection = lightDirection.getReflectedDirection(surfaceNormal);

      // Diffuse lighting ... 
     
      dot = surfaceNormal.dot(lightDirection);

      if (dot > 0)                                                   // if the surface is facing the light ...
      {
        final double diffuseBrightness;
        
        diffuseBrightness = (dot * finish.getDiffuse());             // ... accumulate the intensity
        colour = colour.add(light.getColour().multiply(diffuseBrightness).multiply(pigment.getColourAt(intersect)));
      }
      
      // Specular lighting ...

      dot = intersection.getRay().getDirection().dot(reflection);
      if (dot > 0)
      {
        final double specularBrightness;
      
        specularBrightness = Math.pow(dot, finish.getShininess()) * finish.getSpecular();   // ... accumulate the intensity
        colour = colour.add(light.getColour().multiply(specularBrightness));
      }
    }
    
    return colour;
  }
  
  /** Private constructor. */
  private Lighting() { ; }
}
