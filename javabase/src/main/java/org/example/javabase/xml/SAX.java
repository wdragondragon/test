package org.example.javabase.xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.05 16:08
 * @Description:
 */
public class SAX extends DefaultHandler {
    java.util.Stack tags = new java.util.Stack();
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory sf = SAXParserFactory.newInstance();
        SAXParser sp = sf.newSAXParser();
        SAX reader = new SAX();
        sp.parse(new InputSource("javabase/src/main/java/org/example/xml/test.xml"), reader);
    }
    public void characters(char ch[], int start, int length)
            throws SAXException {
        String tag = (String) tags.peek();
        if (tag.equals("NO")) {
            System.out.print("车牌号码：" + new String(ch, start, length));
        }
        if (tag.equals("ADDR")) {
            System.out.println("地址:" + new String(ch, start, length));
        }
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attrs) {
        tags.push(qName);
    }
}
