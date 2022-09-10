import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SimpleGui2 {
   private static JFrame fr = null;
   private static JButton btn = null;

   public static void main(String args[]) {
      // Let's make a button first
      btn = new JButton("Click Me!");
      btn.setMnemonic(KeyEvent.VK_C); // Now you can hit the button with Alt-C
      btn.addActionListener(new ButtonListener()); // Allow the button to disable itself

      // Let's make the panel with a flow layout.
      // Flow layout allows the components to be
      // their preferred size.
      JPanel pane = new JPanel(new FlowLayout());
      pane.add(btn);  // Add the button to the pane

      // Now for the frame
      fr = new JFrame();
      fr.setContentPane(pane);  // Use our pane as the default pane
      fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program when frame is closed
      fr.setLocation(200, 200); // located at (200, 200)
      fr.pack();                // Frame is ready. Pack it up for display.
      fr.setVisible(true);      // Make it visible
   }

   // Button event handler
   static class ButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         btn.setEnabled(false); // Disable the button
      }
   }
}
