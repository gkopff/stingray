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
 *  A three dimensional vector.  The vector is a representation of direction
 *  and magnitude, with the magnitude being expressed as the distance from the
 *  origin. <p>
 *  
 *  A vector is immutable.
 */
public final class Vector
{
  public static final Vector X_NORMAL = new Vector(1, 0, 0);
  public static final Vector Y_NORMAL = new Vector(0, 1, 0);
  public static final Vector Z_NORMAL = new Vector(0, 0, 1);
  
  /** X. */
  private final double x;
  
  /** Y. */
  private final double y;
  
  /** Z. */
  private final double z;
  
  /**
   *  Constructor.
   *  @param x X.
   *  @param y Y.
   *  @param z Z.
   */
  public Vector(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   *  Normalise the vector.
   *  @return A normalised vector.
   */
  public Vector normalise()
  {
    double len = this.length();
    return new Vector(this.getX() / len, this.getY() / len, this.getZ() / len);
  }
 
  /**
   *  Are the vectors equivalent?
   */
  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    else
    {
      if (o instanceof Vector)
      {
        final Vector that = (Vector) o;
        
        return Math.abs(this.x - that.x) < Rays.eps &&
               Math.abs(this.y - that.y) < Rays.eps &&
               Math.abs(this.z - that.z) < Rays.eps;
      }
      else
      {
        return false;
      }
    }
  }
  
  /**
   *  Compute the hashcode.
   */
  @Override
  public int hashCode()
  {
    return 1 * hashCode(x) +
           3 * hashCode(y) +
           5 * hashCode(z);
  }
  
  /**
   *  Double hash code calculation.
   *  @param value The double value to calculate the hashcode for.
   *  @return The hashcode for the double value.
   */
  private static final int hashCode(double value)
  {
    long bits = Double.doubleToLongBits(value);
    return (int) (bits ^ (bits >>> 32));
  }
  
  /**
   *  Get X.
   */
  public double getX()
  {
    return this.x;
  }

  /**
   *  Get Y.
   */
  public double getY()
  {
    return this.y;
  }

  /**
   *  Get Z.
   */
  public double getZ()
  {
    return this.z;
  }

  /**
   *  Add the components of this vector and the supplied vector together, returning a new vector.
   *  @param that The other vector.
   *  @return The new vector.
   */
  public Vector add(Vector that)
  {
    return new Vector(this.x + that.x, this.y + that.y, this.z + that.z);
  }
  
  /**
   *  Subtract the components of the supplied vector from this vector, returning a new vector.
   *  @param that The other vector.
   *  @return The new vector.
   */
  public Vector subtract(Vector that)
  {
    return new Vector(this.x - that.x, this.y - that.y, this.z - that.z);
  }
  
  /**
   *  Scale the vector by the given amount, returning a new vector.
   *  @param scalar The amount to scale the vector.
   *  @return The new vector.
   */
  public Vector scale(double scalar)
  {
    return new Vector(x * scalar, y * scalar, z * scalar);
  }
  
  /**
   *  Scale the vector by division by the given amount, returning a new vector.
   *  @param divisor The amount to scale the vector.
   *  @return The new vector.
   */
  public Vector divide(double divisor)
  {
    return new Vector(x / divisor, y / divisor, z / divisor);
  }
  
  /**
   *  Calculate the dot product.
   *  @param that The other vector
   *  @return The dot product.
   */
  public double dot(Vector that)
  {
    final double rx;
    final double ry;
    final double rz;
    
    rx = this.x * that.x;
    ry = this.y * that.y;
    rz = this.z * that.z;
    
    return rx + ry + rz;
  }
  
  /**
   *  Calculate the cross product.
   *  @param that The other vector.
   *  @return The cross product.
   */
  public Vector cross(Vector that) 
  {
    double rx = y * that.z - that.y * z;
    double ry = z * that.x - that.z * x;
    double rz = x * that.y - that.x * y;
    
    return new Vector(-rx, -ry, -rz);
  }
  
  /**
   *  Rotate this vector around the given axis.
   *  @param axis The axis of rotation.
   *  @param radians The angle of rotation, in radians.
   *  @return The rotated vector.
   */
  public Vector rotate(Vector axis, double radians)
  {
    // V_rot = V_vec cos(angle) + (z cross v) sin(angle) + z(z dot v)(1 - cos(angle))
    
    final double cos = Math.cos(radians);
    final double sin = Math.sin(radians);
    final Vector firstTerm;
    final Vector secondTerm;
    final Vector thirdTerm;
    
    firstTerm = this.scale(cos);
    secondTerm = axis.cross(this).scale(sin);
    thirdTerm = axis.scale(axis.dot(this)).scale(1 - cos);
    
    return firstTerm.add(secondTerm).add(thirdTerm);
  }
  
  
  /**
   *  Compute the length of the vector.
   *  @return The length.
   */
  public double length() 
  {
    return Math.sqrt(squared());
  }

  /**
   *  Square the vector (v.v).
   *  @return The dot product of itself dot itself.
   */
  public double squared()
  {
    return this.dot(this);
  }
  
  public Vector getReflectedDirection(Vector surfaceNormal)
  {
    return this.subtract(surfaceNormal.scale(2.0d * surfaceNormal.dot(this)));
  }
  
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return String.format("<%.2f, %.2f, %.2f>", x, y, z);
    // return "<" + x + ", " + y + ", " + z + ">";
  }
  
  public static void main(String[] args)
  {
    final Vector axis;
    final Vector point;
    final Vector rot;
    
    axis = new Vector(0, 1, 0);
    point = new Vector(2, 0, 0);
    
    rot = point.rotate(axis, Math.toRadians(360));
    
    System.out.println("Point  : " + point);
    System.out.println("Axis   : " + axis);
    System.out.println("Rotated: " + rot);
  }
}
