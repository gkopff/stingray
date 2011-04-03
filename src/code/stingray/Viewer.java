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
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;


/**
 *  Render viewer.
 */
public class Viewer extends StingrayFrame
{
  private static final long serialVersionUID = 1L;

  /** Preference key. */
  private static final String PREF_SAVE_DIR = "PREF_SAVE_DIR";
  
  /** The rendering. */
  private final Rendering r;
  
  /** Display label. */
  private final JLabel image;
  
  /**
   *  Constructor.
   */
  public Viewer(Rendering r)
  {
    super("Stingray rendering");
    
    this.r = r;
    this.image = new JLabel(new ImageIcon(r.getImage()));
    
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(new JScrollPane(this.image), BorderLayout.CENTER);
    // this.getContentPane().add(this.image, BorderLayout.CENTER);
    this.getContentPane().add(buttons(), BorderLayout.WEST);
    
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    this.pack();
  }
  
  private JPanel buttons()
  {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final JButton saveB;
    
    panel.setBackground(Color.BLACK);
    
    saveB = new JButton(getIcon("diskette_48_hot.png"));
    saveB.addActionListener(saveAction());
    saveB.setToolTipText("Save");
    saveB.setOpaque(false);
    
    panel.add(saveB);
    
    return panel;
  }
  
  private ActionListener saveAction()
  {
    return new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        final JFileChooser chooser;
        final Preferences pref = Preferences.userNodeForPackage(Viewer.class);
        
        chooser = new JFileChooser(pref.get(PREF_SAVE_DIR, "."));
        chooser.setFileFilter(filter());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setSelectedFile(new File(pref.get(PREF_SAVE_DIR, "."), r.getName() + ".png"));
        
        int result = chooser.showSaveDialog(Viewer.this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
          pref.put(PREF_SAVE_DIR, chooser.getSelectedFile().getParent());
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                r.writePNG(new FileOutputStream(chooser.getSelectedFile()));
                SwingUtilities.invokeLater(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    JOptionPane.showMessageDialog(Viewer.this, "Wrote: " + chooser.getSelectedFile(), "Success", JOptionPane.INFORMATION_MESSAGE);
                  }
                });
              }
              catch (final IOException ex)
              {
                SwingUtilities.invokeLater(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    System.err.println("Exception: " + ex);
                    JOptionPane.showMessageDialog(Viewer.this, "Exception: " + ex, "i/o exception", JOptionPane.ERROR_MESSAGE);
                  }
                });
              }
            }
          }).start();
        }
        else
        {
          System.out.println("aborted.");
        }
      }
    };
  }
  
  private FileFilter filter()
  {
    return new FileFilter()
    {
      @Override
      public String getDescription()
      {
        return "PNG (*.png)";
      }
      
      @Override
      public boolean accept(File f)
      {
        return !f.isDirectory() && f.getAbsolutePath().endsWith(".png");
      }
    };
  }
}
