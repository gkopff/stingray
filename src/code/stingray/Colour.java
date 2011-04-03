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
 *  A colour, with RGB components specified as floats between 0.0 and 1.0.
 */
public final class Colour
{
  public static final Colour BLACK = new Colour(0.0f, 0.0f, 0.0f);
  public static final Colour WHITE = new Colour(1.0f, 1.0f, 1.0f);
  public static final Colour RED = new Colour(1.0f, 0.0f, 0.0f);
  public static final Colour GREEN = new Colour(0.0f, 1.0f, 0.0f);
  public static final Colour BLUE = new Colour(0.0f, 0.0f, 1.0f);
  public static final Colour YELLOW = new Colour(1.0f, 1.0f, 0.0f);
  public static final Colour CYAN = new Colour(0.0f, 1.0f, 1.0f);
  public static final Colour MAGENTA = new Colour(1.0f, 0.0f, 1.0f);
  public static final Colour GREY = new Colour(0.5f, 0.5f, 0.5f);
  public static final Colour DARK_GREY = new Colour(0.2f, 0.2f, 0.2f);
  
  /** Red component. */
  private final float red;
  
  /** Red component. */
  private final float green;
  
  /** Red component. */
  private final float blue;
  
  /**
   *  Constructor.
   *  @param red The red component (0.0 - 1.0).
   *  @param green The green component (0.0 - 1.0).
   *  @param blue The blue component (0.0 - 1.0).
   */
  public Colour(float red, float green, float blue)
  {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public static Colour fromARGB(int argb)
  {
    int r;                                                 // red component
    int g;                                                 // green component
    int b;                                                 // blue component
    
    r = (argb & 0x00FF0000) >>> 16;
    g = (argb & 0x0000FF00) >>>  8;
    b = (argb & 0x000000FF) >>>  0;
    
    return new Colour(r / 0xFF, g / 0xFF, b / 0xFF);
  }
  
  /**
   *  Get an ARGB integer.
   *  @return The colour packed into an ARGB integer.
   */
  public int asARGB()
  {
    int r;                                                 // red component
    int g;                                                 // green component
    int b;                                                 // blue component
    
    r = (int) (0xFF * red);                                // calculate red ...
    g = (int) (0xFF * green);                              // ... green
    b = (int) (0xFF * blue);                               // ... blue
    
    return 0xFF000000 | r << 16 | g << 8 | b << 0;         // pack into an ARGB integer
  }
 
  /**
   *  Add this colour with another.
   *  @param c The other colour.
   *  @return A new colour.
   */
  public Colour add(Colour c)
  {
    return new Colour(clamp(this.red + c.red),
                      clamp(this.green + c.green),
                      clamp(this.blue + c.blue));
  }
  
  public Colour multiply(Colour c)
  {
    return new Colour(clamp(this.red * c.red),
                      clamp(this.green * c.green),
                      clamp(this.blue * c.blue));
  }
  
  /**
   *  Multiple the colour by the given scalar value.
   *  @param d The value to scale by, which will be cast to a float.
   *  @return A new colour.
   */
  public Colour multiply(double d)
  {
    return multiply((float) d);
  }
  
  /**
   *  Multiple the colour by the given scalar value.
   *  @param d The value to scale by.
   *  @return A new colour.
   */
  public Colour multiply(float f)
  {
    return new Colour(clamp(this.red * f),
                      clamp(this.green * f),
                      clamp(this.blue * f));
  }
  
  /**
   *  Clamp the value between 0.0 and 1.0.
   *  @param value The value to clamp.
   *  @return The clamped value.
   */
  private static final float clamp(float value)
  {
    return clamp(value, 0.0f, 1.0f);
  }
  
  /**
   *  Clamp the value between the minimum and the maximum.
   *  @param value The value.
   *  @param min The minimum allowed value.
   *  @param max The maximum allowed value.
   *  @return The clamped value.
   */
  private static final float clamp(float value, float min, float max) 
  {
    if (value > max)                                                 // exceeds maximum value? ...
    {
      return max;                                                    // ... clamp it
    }
    else if (value > min)                                            // within permissable range ...
    {
      return value;                                                  // ... use the value directly
    }
    else                                                             // less than minimum? ...
    {
      return min;                                                    // ... clamp it
    }
  }

  /**
   *  Get the red component.
   */
  public float getRed()
  {
    return this.red;
  }

  /**
   *  Get the green component.
   */
  public float getGreen()
  {
    return this.green;
  }

  /**
   *  Get the blue component.
   */
  public float getBlue()
  {
    return this.blue;
  }
  
  /**
   *  Get a string representation.
   */
  @Override
  public String toString()
  {
    return String.format("(r:%.2f, g:%.2f, b:%.2f)", red, green, blue);
  }
  
  public static void main(String[] args)
  {
    final Colour c1 = Colour.RED;
    final int i1 = c1.asARGB();
    final Colour c2 = Colour.fromARGB(i1);
    
    System.out.println(c2);
  }
}
