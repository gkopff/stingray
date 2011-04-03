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

import java.awt.*;
import java.net.*;

import javax.swing.*;


/**
 *  Stringray frame.
 */
public class StingrayFrame extends JFrame
{
  private static final long serialVersionUID = 1L;

  /**
   *  Common constructor code.
   */
  private void init()
  {
    this.setIconImage(getIcon("ray.png").getImage());
  }
  
  /**
   *  Constructor.
   */
  public StingrayFrame() throws HeadlessException
  {
    super();
    init();
  }

  /**
   *  Constructor.
   */
  public StingrayFrame(GraphicsConfiguration gc)
  {
    super(gc);
    init();
  }

  /**
   *  Constructor.
   */
  public StingrayFrame(String title, GraphicsConfiguration gc)
  {
    super(title, gc);
    init();
  }

  /**
   *  Constructor.
   */
  public StingrayFrame(String title) throws HeadlessException
  {
    super(title);
    init();
  }

  /**
   *  Load a image resource from the classpath.
   *  @param imageName The image resource name.
   *  @return The resource.
   */
  protected static ImageIcon getIcon(String imageName)
  {
    try
    {
      URL url = StingrayFrame.class.getClassLoader().getResource(imageName);
      return new ImageIcon(url);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

}
