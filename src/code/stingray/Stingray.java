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

import javax.swing.*;

/**
 *  Application entry point.
 */
public class Stingray
{
  private static ProductionListener productionListener()
  {
    return new ProductionListener()
    {
      @Override
      public void handleStage(final Stage stage, final String name)
      {
        final Thread thread = new Thread(new Runnable()
        {
          @Override
          public void run()
          {
            doRender(stage, name);
          }
        }, "Renderer");
        
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
      }
    };
  }
  
  private static void doRender(Stage stage, String name)
  {
    final int width = 800;
    final int height = 800;
    
//    final Rendering r = new Rendering(name, width, height); 
//    
//    System.out.println(stage.toString());
//    
//    // x, y, and z form the direction vector.
//    // camera is at the camera vector.
//    
//    final double z = stage.getCamera().getPosition().getZ() + (width / 2);
//    
//    // System.out.println("Viewplane is from : " + new Vector(minX, minY, z) + " to: " + new Vector(minX + width, minY + height, z));
//    
//    for (int x = 0; x < width; x += 1)
//    {
//      for (int y = 0; y < height; y+= 1)
//      {
//        Rays.rayTraceStage(stage, x, y, r, width, height, z);
//      }
//    }
    
    final Rendering r = stage.render(name, width, height);
    
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        final Viewer v = new Viewer(r);
        v.setLocationByPlatform(true);
        v.setVisible(true);
      }
    });
  }
  
  /**
   *  Main method.
   *  @param args Command line argumens.
   */
  public static void main(String[] args)
  {
    final Editor editor;
    
    editor = new Editor(productionListener());
    editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    editor.setLocationRelativeTo(null);
    editor.setVisible(true);
  }
}
