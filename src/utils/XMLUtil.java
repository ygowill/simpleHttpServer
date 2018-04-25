package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

//XML的解析辅助
public class XMLUtil {

    private static Logger logger = Logger.getLogger(XMLUtil.class);
    private static SAXReader reader = new SAXReader();

    public static Element getRootElement(String xmlPath) {
        Document document = null;;
        try {
            System.out.println(System.getProperty("user.dir"));
            document = reader.read(new File(xmlPath));
        } catch (DocumentException e) {
            logger.error("找不到指定的xml文件的路径" + xmlPath + "！");
            return null;
        }
        return document.getRootElement();
    }

    public static List<Element> getElements(Element element) {
        return element.elements();
    }

    public static Element getElement(Element element, String name) {
        Element childElement = element.element(name);
        if(childElement == null) {
            logger.error(element.getName() + "节点下没有子节点" + name);
            return null;
        }
        return childElement;
    }

    public static String getElementText(Element element) {
        return element.getText();
    }

    public static void updateElement(String xmlPath,String tag,String value) {
        Document document = null;
        try {
            document = reader.read(new File(xmlPath));
        } catch (DocumentException e) {
            logger.error("找不到指定的xml文件的路径" + xmlPath + "！");
        }
        try{
            Element elem=document.getRootElement().element(tag);
            elem.setText(value);
        }catch(NullPointerException e){
            logger.error("找不到节点");
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter output;//创建输出流
        try {
            output = new XMLWriter(new FileWriter(xmlPath), format); //这里的path是修改后需要保存的路径，建议和未修改前位置一样
            output.write(document);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}