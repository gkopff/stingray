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
 *  The stage on which our scene will be set.
 */
public final class Stage
{
  /** Max recursion depth. */
  private final static int MAX_RECURSE = 8;
  
  // private static final Vector[] aa = { new Vector( 0.0d,  0.0d, 0.0d) };
  
  /** Camera. */
  private final Camera camera;
  
  /** Objects in the scene. */
  private final List<Geometry> geometry;
  
  /** Lighting. */
  private final List<Light> lights;
  
  /**
   *  Constructor.
   */
  public Stage(Camera camera)
  {
    this.camera = camera;
    this.geometry = new ArrayList<Geometry>();
    this.lights = new ArrayList<Light>();
  }
  
  /**
   *  Render the stage, with the given dimensions.
   *  @param name The name for the rendering.
   *  @param width The width, in pixels.
   *  @param height The height, in pixels.
   *  @return A rendering of the stage.
   */
  public Rendering render(String name, int width, int height)
  {
    final Rendering r = new Rendering(name, width, height);
    final RenderContext context = new RenderContext(this, new Statistics());
    
    context.getStatistics().start();
    
    // The basis of the viewplane is this: caculations are done with the camera at the origin.
    // The centre point of the viewplane is (width / 2) units in the positive Z direction.
    // We calculate where each pixel would be on this viewplane, and then rotate based on the
    // look at point.
    
    final Vector virtualCentre = new Vector(0, 0, width / 2);
    final Vector virtualBottomLeft = virtualCentre.subtract(new Vector(width / 2, height / 2, 0));
 
    final Vector xzdir = new Vector(camera.getDirection().getX(),
                                    0,
                                    camera.getDirection().getZ()).normalise();
    final double xzangle = Math.acos(xzdir.dot(Vector.Z_NORMAL));
    // System.out.println("Rendering.render(): xzangle: " + Math.toDegrees(xzangle) + " degrees rotation.");
    
    final Vector ydir = new Vector(0, 
                                   camera.getDirection().getY(), 
                                   camera.getDirection().getZ()).normalise();
    final double yangle = Math.acos(ydir.dot(Vector.Z_NORMAL));
    // System.out.println("Rendering.render(): yangle: " + Math.toDegrees(yangle) + " degrees rotation.");
    
    for (int x = 0; x < width; x++)
    {
      for (int y = 0; y < width; y++)
      {
        final Vector dir1;
        final Vector dir2;
        final Vector dir3;
        
        dir1 = virtualBottomLeft.add(new Vector(x, y, 0));
        dir2 = dir1.rotate(Vector.X_NORMAL, yangle);
        dir3 = dir2.rotate(Vector.Y_NORMAL, xzangle);
        
        final Ray ray = new Ray(camera.getPosition(), dir3);
        context.getStatistics().incRaysCast();
        r.set(x, y, getColourFor(context, ray, 0, Colour.BLACK));
      }
    }
    
    context.getStatistics().end();
    System.out.println(context.getStatistics());
    
    return r;
  }
  
  public Colour getColourFor(RenderContext context, Ray ray, int recurseNo, Colour miss)
  {
    if (recurseNo > MAX_RECURSE)
    {
      return miss;
    }
    
    final IntersectionList intersections = new IntersectionList(ray);
    for (Geometry g : getGeometry())
    {
      g.intersect(ray, intersections);
    }

    if (intersections.size() > 0)
    {
      Intersection i = intersections.closest();
      return i.getGeometry().getColourAt(context, recurseNo, i, Rays.getIlluminatingLights(this, 
                                                                                           this.getLights(), 
                                                                                           i.asVector()));
    }
    else
    {
      return miss;
    }
  }
  
//  /**
//   *  Render the stage, with the given dimensions.
//   *  @param name The name for the rendering.
//   *  @param width The width, in pixels.
//   *  @param height The height, in pixels.
//   *  @return A rendering of the stage.
//   */
//  // Produces a skewed and up-side-down image.
//  public Rendering render(String name, int width, int height)
//  {
//    final Vector left;
//    final Vector topLeft;
//    final Rendering r = new Rendering(name, width, height); 
//    
//    System.out.println(camera);
//    
//    left = camera.getLookAt().rotate(camera.getUp(), Math.toRadians(45));
//    topLeft = left.rotate(camera.getRight(), Math.tanh((height / 2.0d) / (width / 2.0d)));
//    
//    System.out.println("tl  : " + topLeft);
//    
//    final Vector right;
//    
//    right = topLeft.rotate(camera.getUp(), Math.toRadians(-90));
//    System.out.println("right : " + right);
//    
//    final double xRotation = Math.toRadians(-90) / width;
//    System.out.println("x rotation amount: " + xRotation);
//    
//    
//    final double halfVertAngle = Math.tanh((height / 2.0d) / (width / 2.0d));
//    final double vertAngle = 2.0d * halfVertAngle;
//    
//    final double yRotation =  -vertAngle / height;
//    System.out.println("y rotation amount: " + yRotation);
//    
//    Vector startOfLine;
//    for (int line = 0; line < height; line++)
//    {
//      startOfLine = topLeft.rotate(camera.getRight(), yRotation * (double) line);
//      
//      for (int x = 0; x < width; x++)
//      {
//        final Vector v = startOfLine.rotate(camera.getUp(), xRotation * (double) x);
//        System.out.println("vector for line: " + line + ": " + v);
//        
//        final Ray[] rays = new Ray[aa.length];
//        for (int i = 0; i < rays.length; i++)
//        {
//          rays[i] = new Ray(camera.getPosition(), v.add(aa[i]));
//        }
//        
//        for (Ray ray : rays)
//        {
//          final IntersectionList intersections = new IntersectionList(ray);
//          for (Geometry g : getGeometry())
//          {
//            g.intersect(ray, intersections);
//          }
//
//          if (intersections.size() > 0)
//          {
//            Intersection i = intersections.closest();
//            r.set(x, line, i.getGeometry().getColourAt(this, i, Rays.getIlluminatingLights(this, 
//                                                                                           this.getLights(), 
//                                                                                           i.asVector(), 
//                                                                                           i.getGeometry())));
//          }
//        }
//      }
//    }
//    
//    return r;
//  }
  
  
  /**
   *  Add the given piece of geometry to the scene.
   *  @param g The geometry to add.
   */
  public void addGeometry(Geometry g)
  {
    this.geometry.add(g);
  }
  
  /**
   *  Add the given light to the scene.
   *  @param l The light to add.
   */
  public void addLight(Light l)
  {
    this.lights.add(l);
  }
  
  /**
   *  Get the camera object.
   *  @return The camera.
   */
  public Camera getCamera()
  {
    return this.camera;
  }
  
  /**
   *  Get the geometry.
   *  @return An immutable list of the geometry.
   */
  public List<Geometry> getGeometry()
  {
    return Collections.unmodifiableList(this.geometry);
  }
  
  /**
   *  Get the lights.
   *  @return An immutable list of the lights.
   */
  public List<Light> getLights()
  {
    return Collections.unmodifiableList(this.lights);
  }
  
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return "Stage[" + camera + "; geometry: " + geometry.size() + " items; lights: " + lights.size() + " lights]"; 
  }
}
