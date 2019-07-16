/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Phong
 */
public class FileUtility {

    public static int writeToFile(String filePath, String content) {
        try {
            FileWriter fw = new FileWriter(filePath, false);
            fw.write(content);
            fw.close();
            return 1;
        } catch (Exception e) {
            return 0;
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
