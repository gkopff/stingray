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

import javax.script.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;


/**
 *  Scene editor.
 */
public class Editor extends StingrayFrame
{
  private static final long serialVersionUID = 1L;

  /** Script engine manager. */
  private static final ScriptEngineManager manager = new ScriptEngineManager();
  
  /** Preference key: last used directory. */
  private static final String PREF_DIR = "PREF_DIR";
  /** Preference key: last loaded file. */
  private static final String PREF_FILE = "PREF_FILE";
  
  /** Text area. */
  private final JTextArea ta;
  
  /** Production listener. */
  private final ProductionListener listener;
  
  /** Source code file, or null. */
  private File file;
  
  /** File modified? */
  private boolean modified;
  
  /**
   *  Constructor.
   */
  public Editor(ProductionListener listener)
  {
    super();
    setFile(null);
    
    this.listener = listener;
    
    this.ta = new JTextArea();
    this.ta.setFont(new Font("Courier", Font.PLAIN, 11));
    this.ta.getDocument().addDocumentListener(documentListener());
    
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(new JScrollPane(this.ta), BorderLayout.CENTER);
    this.getContentPane().add(buttons(), BorderLayout.NORTH);
    
    try
    {
      final String filename = Preferences.userNodeForPackage(Editor.class).get(PREF_FILE, null);
      if (filename != null)
      {
        final File file = new File(filename);
        this.ta.setText(readFile(file));
        setFile(file);
      }
    }
    catch (IOException ex)
    {
      ;
    }
    
    this.setSize(900, 700);
  }
  
  private JPanel buttons()
  {
    final JPanel panel = new JPanel(new BorderLayout());
    final JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    final JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    final JButton newB;
    final JButton openB;
    final JButton saveB;
    final JButton traceB;
    
    newB = new JButton(getIcon("document_48_hot.png"));
    newB.addActionListener(newFileAction());
    newB.setToolTipText("Default scene");
    
    openB = new JButton(getIcon("folder_48_hot.png"));
    openB.addActionListener(openFileAction());
    openB.setToolTipText("Open source file");
    
    saveB = new JButton(getIcon("diskette_48_hot.png"));
    saveB.addActionListener(saveFileAction());
    saveB.setToolTipText("Save source file");
    
    traceB = new JButton(getIcon("camera_48_hot.png"));
    traceB.addActionListener(traceAction());
    traceB.setToolTipText("Render the scene");
    
    p1.add(newB);
    p1.add(openB);
    p1.add(saveB);
    // p1.add(Box.createHorizontalStrut(48));
    p1.add(traceB);
    
    p2.add(new JLabel(getIcon("ray.png")));
    
    panel.add(p1, BorderLayout.WEST);
    panel.add(p2, BorderLayout.EAST);
    
    return panel;
  }
  
  private DocumentListener documentListener()
  {
    return new DocumentListener()
    {
      @Override
      public void removeUpdate(DocumentEvent e)
      {
        update();
      }
      
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        update();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e)
      {
        update();
      }
      
      private void update()
      {
        if (! modified)
        {
          modified = true;
          updateTitle();
        }
      }
    };
  }
  
  private ActionListener saveFileAction()
  {
    return new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (file != null)
        {
          saveExistingFile();
        }
        else
        {
          saveNewFile();
        }
      }
    };
  }
  
  private void saveExistingFile()
  {
    final String contents = this.ta.getText();
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          writeFile(file, contents);
          modified = false;
          updateTitle();
        }
        catch (final IOException ex)
        {
          SwingUtilities.invokeLater(new Runnable()
          {
            @Override
            public void run()
            {
              System.err.println("Exception: " + ex);
              JOptionPane.showMessageDialog(Editor.this, "Exception: " + ex, "i/o exception", JOptionPane.ERROR_MESSAGE);
            }
          });
        }
      }
    }).start();
  }
  
  private void saveNewFile()
  {
    final JFileChooser chooser;
    final Preferences pref = Preferences.userNodeForPackage(Viewer.class);
    
    chooser = new JFileChooser(pref.get(PREF_DIR, "."));
    chooser.setFileFilter(filter());
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
    int result = chooser.showSaveDialog(Editor.this);
    if (result == JFileChooser.APPROVE_OPTION)
    {
      pref.put(PREF_DIR, chooser.getSelectedFile().getParent()); 
      pref.put(PREF_FILE, chooser.getSelectedFile().getAbsolutePath());
      setFile(chooser.getSelectedFile());
      saveExistingFile();
    }
  }
  
  private ActionListener openFileAction()
  {
    return new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        final JFileChooser chooser;
        final Preferences pref = Preferences.userNodeForPackage(Viewer.class);
        
        chooser = new JFileChooser(pref.get(PREF_DIR, "."));
        chooser.setFileFilter(filter());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = chooser.showOpenDialog(Editor.this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
          pref.put(PREF_DIR, chooser.getSelectedFile().getParent());
          pref.put(PREF_FILE, chooser.getSelectedFile().getAbsolutePath());
          
          new Thread(new Runnable()
          {
            public void run()
            {
              try
              {
                final String text = readFile(chooser.getSelectedFile());
                
                SwingUtilities.invokeLater(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    ta.setText(text);
                    setFile(chooser.getSelectedFile());
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
                    JOptionPane.showMessageDialog(Editor.this, "Exception: " + ex, "i/o exception", JOptionPane.ERROR_MESSAGE);
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
  
  private void setFile(File file)
  {
    this.file = file;
    this.modified = false;
    updateTitle();
  }
  
  private void updateTitle()
  {
    setTitle("Stingray editor" + (file != null ? " [" + file.getName() + "]" + (modified ? "*" : "") : "")); 
  }
  
  private static final void writeFile(File file, String contents) throws IOException
  {
    final BufferedWriter bw = new BufferedWriter(new FileWriter(file));
    
    bw.write(contents);
    bw.close();
  }
  
  private static final String readFile(File file) throws IOException
  {
    final StringBuilder buff = new StringBuilder();
    final BufferedReader br = new BufferedReader(new FileReader(file));
    String line;
    
    while ((line = br.readLine()) != null)
    {
      buff.append(line);
      buff.append("\n");
    }
    
    br.close();
    
    return buff.toString();
  }
  
  private ActionListener newFileAction()
  {
    return new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ta.setText(defaultText);
        setFile(null);
      }
    };
  }
  
  private ActionListener traceAction()
  {
    return new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ScriptEngine engine;
       
        engine = manager.getEngineByName("JavaScript");
        
        try
        {
          Object result;
          engine.eval(ta.getText());
          
          final Invocable invocable = (Invocable) engine;
          result = invocable.invokeFunction("setup");
          if (result instanceof Stage)
          {
            final Stage stage = (Stage) result;
            final String name;
            
            if (file != null)
            {
              if (file.getName().endsWith(".sray"))
              {
                name = file.getName().substring(0, file.getName().length() - ".sray".length());
              }
              else
              {
                name = file.getName();
              }
            }
            else
            {
              name = "default";
            }
            
            listener. handleStage(stage, name);
          }
          else
          {
            JOptionPane.showMessageDialog(Editor.this, "Script must return a stage.", "Script exception", JOptionPane.ERROR_MESSAGE);
          }
        }
        catch (ScriptException ex)
        {
          System.err.println("Exception: " + ex);
          JOptionPane.showMessageDialog(Editor.this, "Exception: " + ex, "Script exception", JOptionPane.ERROR_MESSAGE);
        }
        catch (NoSuchMethodException ex)
        {
          System.err.println("Exception: " + ex);
          JOptionPane.showMessageDialog(Editor.this, "Exception: " + ex, "Script exception", JOptionPane.ERROR_MESSAGE);
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
        return "Stingray source (*.sray)";
      }
      
      @Override
      public boolean accept(File f)
      {
        return !f.isDirectory() && f.getAbsolutePath().endsWith(".sray");
      }
    };
  }
  
  private static final String defaultText = 
    "importPackage(Packages.stingray);" + "\n" +
    "importPackage(Packages.stingray.geometry);" + "\n" +
    "importPackage(Packages.stingray.texture);" + "\n" +
    "" + "\n" +
    "/**" + "\n" +
    " *  Scene setup." + "\n" +
    " *  This function must return a reference to the stage." + "\n" +
    " */" + "\n" +
    "function setup()" + "\n" +
    "{" + "\n" +
    "  var stage = new Stage(new Camera(new Vector(0, 100, 0)));" + "\n" +
    "" + "\n" +
    "  return stage;" + "\n" +
    "}";
}
