package common;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class CommonTheme {
    
    public static void setTheme(JFrame frame) {
        frame.setLocationRelativeTo(null); //center screen
        frame.setTitle(CommonContent.TOOL_TITLE);
        frame.setResizable(false);
    }
    
    public static void setTheme(JDialog dialog) {
        dialog.setLocationRelativeTo(null); //center screen
        dialog.setTitle(CommonContent.TOOL_TITLE);
        dialog.setResizable(false);
    }
}
