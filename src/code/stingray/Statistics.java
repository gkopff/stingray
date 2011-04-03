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
 *  Statistics collation.
 */
public final class Statistics
{
  /** Number of rays cast. */
  private int raysCast;
  
  /** Number of shadow rays cast. */
  private int shadowRaysCast;
  
  /** Starting time. */
  private long startTime;
  
  /** End time. */
  private long endTime;
  
  /**
   *  Constructor.
   */
  public Statistics()
  {
    this.raysCast = 0;
    this.shadowRaysCast = 0;
  }
  
  public void incRaysCast()
  {
    this.raysCast++;
  }
  
  public void incShadowRaysCast()
  {
    this.shadowRaysCast++;
  }
  
  public void start()
  {
    this.startTime = System.currentTimeMillis();
  }
  
  public void end()
  {
    this.endTime = System.currentTimeMillis();
  }
  
  public long thisDuration()
  {
    return this.endTime - this.startTime;
  }
  
  public int getRaysCast()
  {
    return this.raysCast;
  }
  
  public int thisShadowRaysCast()
  {
    return this.shadowRaysCast;
  }
  
  /**
   *  this a string representation.
   */
  @Override
  public String toString()
  {
    final StringBuilder buff = new StringBuilder();
    
    buff.append(String.format("Duration: %.3f secs; Rays: %,d", (thisDuration() / 1000d), getRaysCast()));
    
    return buff.toString();
  }
}
