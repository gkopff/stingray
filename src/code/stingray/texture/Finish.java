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


/**
 *  Surface finish.
 */
public final class Finish
{
  /** Ambient amount. */
  private final double ambient;
  
  /** Diffuse amount. */
  private final double diffuse;
  
  /** Shininess amount. */
  private final int shininess;
  
  /** Reflection amount. */
  private final double reflection;
  
  /**
   *  Constructor.
   *  @param ambient The ambient amount.
   *  @param diffuse The diffuse amount.
   *  @param shininess The amount of shine.
   *  @param reflection The reflection amount.
   */
  public Finish(double ambient, double diffuse, int shininess, double reflection)
  {
    this.ambient = ambient;
    this.diffuse = diffuse;
    this.shininess = shininess;
    this.reflection = reflection;
  }

  /**
   *  Get the ambient amount.
   *  @return The ambient amount.
   */
  public double getAmbient()
  {
    return this.ambient;
  }
  
  /**
   *  Get the reflection amount.
   *  @return An amount between 0.0 (no reflection) and 1.0 (perfect mirror).
   */
  public double getReflection()
  {
    return this.reflection;
  }
  
  /**
   *  Get the diffuse amount.
   *  @return The diffuse amount.
   */
  public double getDiffuse()
  {
    return this.diffuse;
  }
  
  /**
   *  Get the specular amount.
   *  @return The specular amount.
   */
  public double getSpecular()
  {
    return 1.0d - getDiffuse();
  }
  
  /**
   *  Get the shininess amount.
   *  @return The shininess amount.
   */
  public double getShininess()
  {
    return this.shininess;
  }
}
