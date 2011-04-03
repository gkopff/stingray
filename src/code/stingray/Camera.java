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
 *  The camera. <p>
 *  
 *  The camera is defined by it's position and a point to look at.  The look at point
 *  is considered a direction vector that points at the centre pixel of the viewport.
 */
public final class Camera
{
  /** Camera's position/ */
  private final Vector position;
 
  /** Direcion vector (to centre point on viewplane). */
  private final Vector direction;
  
  /** The "up" vector. */
  private final Vector up;
  
  /** The "right" vector. */
  private final Vector right;
  
  /**
   *  Constructor; uses an "up" vector of (0, 1, 0) and a "right" vector of (1, 0, 0).
   *  @param position The camera's position.
   *  @param lookAt The look at point.
   */
  public Camera(Vector position, Vector lookAt)
  {
    this(position, lookAt, Vector.Y_NORMAL, Vector.X_NORMAL);
  }
  
  /**
   *  Constructor.
   *  @param position The camera's position.
   *  @param lookAt The look at point.
   *  @param up The "up" vector.
   *  @param right The "right" vector.
   */
  public Camera(Vector position, Vector lookAt, Vector up, Vector right)
  {
    this.position = position;
    this.direction = lookAt.subtract(this.position).normalise();
//    this.lookAt = lookAt.normalise();
    this.up = up.normalise();
    this.right = right.normalise();
  }

  /**
   *  Get the position of the camera.
   *  @return The position.
   */
  public Vector getPosition()
  {
    return this.position;
  }

  /**
   *  Get the direction to look in.
   *  @return The normalised direction vector.
   */
  public Vector getDirection()
  {
    return this.direction;
  }
  
  /**
   *  Get the "up" vector.
   *  @return The "up" vector.
   */
  public Vector getUp()
  {
    return this.up;
  }
  
  /**
   *  Get the "right" vector.
   *  @return The "right" vector.
   */
  public Vector getRight()
  {
    return this.right;
  }
 
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return "Camera[position: " + position + "; look at: " + direction + "; up: " + up + "; right: " + right + "]"; 
  }
}
