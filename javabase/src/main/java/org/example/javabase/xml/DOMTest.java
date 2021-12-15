package org.example.javabase.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.05 15:46
 * @Description:
 */
public class DOMTest {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        File file = new File("javabase/src/main/java/org/example/xml/test.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList nl = doc.getElementsByTagName("VALUE");
        for (int i = 0; i < nl.getLength(); i++) {
            System.out.print("车牌号码:"
                    + doc.getElementsByTagName("NO").item(i)
                    .getFirstChild().getNodeValue());
            System.out.println("车主地址:"
                    + doc.getElementsByTagName("ADDR").item(i)
                    .getFirstChild().getNodeValue());
        }

    }
}
