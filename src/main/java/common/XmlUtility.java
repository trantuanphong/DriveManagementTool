/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Phong
 */
public class XmlUtility {

    private static final String FILE_SETTING = "Setting.xml";
    private static final String LOCAL_LOCATION = "localLocation";
    private static final String CREDENTIAL_LOCACTION = "credentialsLocation";
    
    private static Document getXML(String fileName) {
        try {
        ClassLoader classLoader = new XmlUtility().getClass().getClassLoader();
        File xmlFile = new File(classLoader.getResource(fileName).getFile());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getContentXML(String tagName) {
        Document doc = getXML(FILE_SETTING);
        if (doc == null) {
            return "";
        } return doc.getElementsByTagName(tagName).item(0).getTextContent();
    }

    private static int setContentXML(String tagName, String conent) {
        Document doc = getXML(FILE_SETTING);
        if (doc == null) {
            return CommonString.FALSE;
        }
        doc.getElementsByTagName(tagName).item(0).setTextContent(conent);
        return CommonString.TRUE;
    }
    
    public static String getLocalLocation() {
        return getContentXML(LOCAL_LOCATION);
    }

    public static String getCredentialLocation() {
        return getContentXML(CREDENTIAL_LOCACTION);
    }
    
    public static int setLocalLocation(String content) {
        return setContentXML(LOCAL_LOCATION, content);
    }
    
    public static int setCredentialLocation(String content) {
        return setContentXML(CREDENTIAL_LOCACTION, content);
    }
}
