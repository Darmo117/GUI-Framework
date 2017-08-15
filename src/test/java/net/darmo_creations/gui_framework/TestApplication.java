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
import java.util.Optional;

import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.version.Version;

public class TestApplication implements Application {
  private static final String NAME = "Test App";
  private static final Version VERSION = new Version(1, 0, 0, false);

  @Override
  public ApplicationFrame initFrame(WritableConfig config) {
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
  public String getIconsLocation() {
    return "/net/darmo_creations/gui_framework/assets/icons/";
  }

  @Override
  public InputStream getLanguageFilesStream(Language language) {
    return TestApplication.class.getResourceAsStream("/net/darmo_creations/gui_framework/assets/langs/" + language.getCode() + ".lang");
  }

  @Override
  public Optional<String> getHelpDocumentationLink(Language language) {
    return Optional.empty();
  }

  @Override
  public boolean checkUpdate() {
    return false;
  }

  @Override
  public Optional<String> getRssUpdatesLink() {
    return Optional.empty();
  }

  @Override
  public boolean hasAboutDialog() {
    return false;
  }

  @Override
  public Optional<String> getAboutFilePath() {
    return Optional.empty();
  }

  @Override
  public Optional<Image> getIcon() {
    return Optional.empty();
  }

  @Override
  public Optional<Image> getLicenseIcon() {
    return Optional.empty();
  }
}
