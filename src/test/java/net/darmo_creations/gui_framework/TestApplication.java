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

public class TestApplication implements Application {
  private static final String NAME = "Test App";
  private static final Version VERSION = new Version(1, 0, 0, false);

  @Override
  public ApplicationFrame initFrame(DefaultGlobalConfig config) {
    return new TestFrame(config);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Version getCurrentVersion() {
    return VERSION;
  }

  @Override
  public InputStream getLanguageFilesStream(Language language) {
    return null;
  }

  @Override
  public String getHelpDocumentationLink(Language language) {
    return null;
  }

  @Override
  public String getRssUpdatesLink() {
    return null;
  }

  @Override
  public String getAboutFilePath() {
    return null;
  }

  @Override
  public Image getIcon() {
    return null;
  }

  @Override
  public Image getLicenseIcon() {
    return null;
  }
}
