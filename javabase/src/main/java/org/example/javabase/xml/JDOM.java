package org.example.javabase.xml;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.05 16:18
 * @Description:
 */
public class JDOM {
    public static void main(String[] args) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new File("javabase/src/main/java/org/example/xml/test.xml"));
        Element foo = doc.getRootElement();
        List allChildren = foo.getChildren();
        for (int i = 0; i < allChildren.size(); i++) {
            System.out.print("车牌号码:"
                    + ((Element) allChildren.get(i)).getChild("NO")
                    .getText());
            System.out.println("车主地址:"
                    + ((Element) allChildren.get(i)).getChild("ADDR")
                    .getText());
        }
    }

}
