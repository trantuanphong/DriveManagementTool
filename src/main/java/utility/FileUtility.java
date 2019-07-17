/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Phong
 */
public class FileUtility {
    public static boolean createFile(String filePath) {
        try {
            return new File(filePath).createNewFile();
        } catch (IOException ex) {
            return false;
        }
    }
    
    public static boolean writeToFile(String filePath, String content) {
        try {
            FileWriter fw = new FileWriter(filePath, false);
            fw.write(content);
            fw.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String readFromFile(String filePath) {
        String str = "";
        try {
            FileReader fr = new FileReader(filePath);
            int i;
            while ((i = fr.read()) != -1) {
                str += i;
            }
            fr.close();
            return str;
        } catch (Exception e) {
            return null;
        }
    }
}
