/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of GUI-Framework.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.gui_framework.dao;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.darmo_creations.gui_framework.config.ConfigKey;
import net.darmo_creations.gui_framework.config.DefaultGlobalConfig;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.utils.JarUtil;

/**
 * This class handles I/O operations for the {@code GlobalConfig} class.
 *
 * @author Damien Vergnet
 */
public class ConfigDao {
  private static ConfigDao instance;

  public static ConfigDao getInstance() {
    if (instance == null)
      instance = new ConfigDao();
    return instance;
  }

  /**
   * Loads the config stored in the "config.xml" file in the same directory as the jar.
   * 
   * @return the config or null if a fatal error occured
   */
  public DefaultGlobalConfig load() {
    DefaultGlobalConfig config = new DefaultGlobalConfig();

    try {
      File fXmlFile = new File(URLDecoder.decode(JarUtil.getJarDir() + "config.xml", "UTF-8"));
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);

      doc.getDocumentElement().normalize();

      Element root = (Element) doc.getElementsByTagName("Config").item(0);
      if (root != null) {
        Element localeElm = (Element) root.getElementsByTagName("Locale").item(0);
        if (localeElm != null) {
          Language language = Language.fromCode(localeElm.getTextContent());
          if (language != null)
            config.setLanguage(language);
        }

        Element valuesElm = (Element) root.getElementsByTagName("Values").item(0);
        if (valuesElm != null) {
          NodeList valuesList = valuesElm.getElementsByTagName("Value");
          for (int i = 0; i < valuesList.getLength(); i++) {
            Element valueElm = (Element) valuesList.item(i);
            @SuppressWarnings("unchecked")
            Class<ConfigKey<?>> keyClass = (Class<ConfigKey<?>>) Class.forName(valueElm.getAttribute("class"));
            Optional<ConfigKey<?>> key = DefaultGlobalConfig.getKeyFromName(valueElm.getAttribute("name"), keyClass);

            if (key.isPresent()) {
              config.setValue(key.get(), key.get().deserializeValue(valueElm.getTextContent()));
            }
          }
        }
      }
    }
    catch (NullPointerException | ClassCastException | ParserConfigurationException | SAXException | IOException
        | ClassNotFoundException ex) {}

    return config;
  }

  /**
   * Saves the given config to the "config.xml" file in the same directory as the jar.
   * 
   * @param config the config
   */
  public void save(DefaultGlobalConfig config) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      Element root = doc.createElement("Config");

      Element locale = doc.createElement("Locale");
      locale.appendChild(doc.createTextNode(config.getLanguage().getCode()));
      root.appendChild(locale);

      Element nodes = doc.createElement("Values");
      for (ConfigKey<?> key : DefaultGlobalConfig.getRegisteredKeys()) {
        Element node = doc.createElement("Value");
        node.setAttribute("name", key.getName());
        node.appendChild(doc.createTextNode(key.serializeValue(config.getValue(key))));
        nodes.appendChild(node);
      }
      root.appendChild(nodes);

      doc.appendChild(root);

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new File(URLDecoder.decode(JarUtil.getJarDir() + "config.xml", "UTF-8")));

      transformer.transform(new DOMSource(doc), result);
    }
    catch (ParserConfigurationException | TransformerException | UnsupportedEncodingException ex) {}
  }

  private ConfigDao() {}
}
