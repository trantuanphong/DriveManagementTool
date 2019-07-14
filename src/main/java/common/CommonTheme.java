package common;

import javax.swing.JFrame;

public class CommonTheme {
    
    public static void setTheme(JFrame frame) {
        frame.setLocationRelativeTo(null); //center screen
        frame.setTitle(CommonString.TOOL_TITLE);
        frame.setResizable(false);
    }
}
