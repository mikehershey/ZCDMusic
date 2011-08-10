/**
 * Copyright © 2011 Mike Hershey (http://mikehershey.com | http://zcd.me) 
 * 
 * See the LICENSE file included with this project for full permissions. If you
 * did not receive a copy of the license email mikehershey32@gmail.com for a copy.
 * 
 * Among other restrictions you are not permitted to deploy this software for 
 * commercial purposes.
 */
package me.zcd.music.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {

	public static Document createDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document doc = db.parse(is);
        return doc;
	}
	
}
