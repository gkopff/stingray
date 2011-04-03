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
 *  Helper methods for solving quadratic equations.
 */
public abstract class QuadraticMath
{
  /** Prevent construction. */
  private QuadraticMath() { ; }
  
  /**
   *  Solve the quadratic equation, given the two vectors.
   *  @return The length down the ray the intersections occur, or null if no intersection occurs.
   */
  public static double[] solve(Vector u, Vector v, double radius)
  {
    final double a;
    final double b;
    final double c;
    
    a = v.squared();
    b = u.scale(2).dot(v);
    c = u.squared() - (radius * radius);
    
    final double bb;
    final double ac4;
    final double t1;
    final double t2;
 
    bb = b * b;
    ac4 = 4 * a * c;
    
    if (bb < ac4)                                                    // discriminant will be less than zero ...
    {
      return null;                                                   // ... no solution exists
    }
    
    if (b > 0)
    {
      t1 = (-b - Math.sqrt(bb - ac4)) / (2 * a);
    }
    else
    {
      t1 = (-b + Math.sqrt(bb - ac4)) / (2 * a);
    }
    
    t2 = c / (a * t1);
    
    return new double[] { t1, t2 };
  }
}