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
package net.darmo_creations.gui_framework;

import java.awt.Image;
import java.io.InputStream;

import net.darmo_creations.gui_framework.config.DefaultGlobalConfig;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.version.Version;

/**
 * An application must implement this interface then register itself to the
 * {@link ApplicationRegistry}.
 *
 * @author Damien Vergnet
 */
public interface Application {
  /**
   * The application's main frame must be initialized in this method.
   * 
   * @param config the configuration
   * @return the main frame
   */
  ApplicationFrame initFrame(DefaultGlobalConfig config);

  /**
   * @return application's name
   */
  String getName();

  /**
   * @return application's current version
   */
  Version getCurrentVersion();

  /**
   * Returns the imput stream to the language file corresponding to the given language.
   * 
   * @param language the language
   * @return the input stream
   */
  InputStream getLanguageFilesStream(Language language);

  /**
   * This method should return the link to the help documentation in the given language.
   * 
   * @param language the language
   * @return the link to the help documentation
   */
  String getHelpDocumentationLink(Language language);

  /**
   * This method should return the link to the updates rss feed.
   * 
   * @return the link to fetch updates
   */
  String getRssUpdatesLink();

  /**
   * @return the path to the about file (must be in the jar)
   */
  String getAboutFilePath();

  /**
   * @return application's icon
   */
  Image getIcon();

  /**
   * @return license icon
   */
  Image getLicenseIcon();
}
