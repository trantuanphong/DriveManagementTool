/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

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
public class XmlUtility {

    private static final String FILE_SETTING = "Setting.xml";
    private static final String LOCAL_LOCATION = "localLocation";

    private static final ClassLoader classLoader = new XmlUtility().getClass().getClassLoader();
    private static final File xmlFile = new File(classLoader.getResource(FILE_SETTING).getFile());

    private static Document getXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlFile);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getContentXML(String tagName) {
        Document doc = getXML();
        if (doc == null) {
            return "";
        }
        return doc.getElementsByTagName(tagName).item(0).getTextContent();
    }

    private static int setContentXML(String tagName, String conent) {
        Document doc = getXML();
        if (doc == null) {
            return CommonString.FALSE;
        }
        doc.getElementsByTagName(tagName).item(0).setTextContent(conent);
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            return CommonString.FALSE;
        }
        return CommonString.TRUE;
    }

    public static String getLocalLocation() {
        return getContentXML(LOCAL_LOCATION);
    }

    public static int setLocalLocation(String content) {
        return setContentXML(LOCAL_LOCATION, content);
    }

}
