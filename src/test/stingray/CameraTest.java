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

import junit.framework.*;

/**
 *  Camera tests.
 */
public final class CameraTest extends TestCase
{
  public void testFoo()
  {
    assertDirection(new Vector(0, 0, 0),                             // position
                    new Vector(0, 0, 1),                             // look at
                    new Vector(0, 0, 1));                            // expected direction
    
    assertDirection(new Vector(0, 0,   0),                           // position
                    new Vector(0, 0, 100),                           // look at
                    new Vector(0, 0,   1));                          // expected direction
    
    assertDirection(new Vector(0, 0, 100),                           // position
                    new Vector(0, 0, 101),                           // look at
                    new Vector(0, 0,   1));                          // expected direction
    
  }
  
  private static final void assertDirection(Vector position, Vector lookAt, Vector expectedDirection)
  {
    // FIXME: use hamcrest
    final Camera camera = new Camera(position, lookAt);
    assertEquals(camera.getDirection(), expectedDirection);
  }
}
