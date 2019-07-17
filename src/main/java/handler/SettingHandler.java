/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import common.CommonContent;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author Phong
 */
public class SettingHandler {

    private final String FILE_SETTING = "Setting.xml";
    private final String LOCAL_LOCATION = "localLocation";
    
    private static SettingHandler xmlHandler;
    private final File xmlFile;
    private SettingHandler() {
        xmlFile = new File(this.getClass().getClassLoader()
                .getResource(FILE_SETTING).getFile());
    }
    
    public static SettingHandler getInstance() {
        if (xmlHandler == null) {
            xmlHandler = new SettingHandler();
        }
        return xmlHandler;
    }
    
    private Document getXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlFile);
        } catch (Exception e) {
            return null;
        }
    }

    private String getContentXML(String tagName) {
        Document doc = getXML();
        if (doc == null) {
            return "";
        }
        return doc.getElementsByTagName(tagName).item(0).getTextContent();
    }

    private int setContentXML(String tagName, String conent) {
        Document doc = getXML();
        if (doc == null) {
            return CommonContent.FALSE;
        }
        doc.getElementsByTagName(tagName).item(0).setTextContent(conent);
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            return CommonContent.FALSE;
        }
        return CommonContent.TRUE;
    }

    public String getLocalLocation() {
        return getContentXML(LOCAL_LOCATION);
    }

    public int setLocalLocation(String content) {
        return setContentXML(LOCAL_LOCATION, content);
    }

}
