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

import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.version.Version;

public class TestApplication extends Application {
  private static final String NAME = "Test App";
  private static final Version VERSION = new Version(1, 0, 0, false);

  // "/net/darmo_creations/gui_framework/assets/icons/"
  @Override
  public void preInit() {
    setLanguages(new Language("English", Locale.US), new Language("Français", Locale.FRANCE));
  }

  @Override
  public ApplicationFrame<ApplicationController<TestFrame>> initFrame(WritableConfig config) {
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
    return getClass().getResourceAsStream("/assets/langs/" + language.getCode() + ".lang");
  }

  @Override
  public String getRssUpdatesLink() {
    return "https://github.com/Darmo117/Jenealogio/releases.atom";
  }

  @Override
  public Optional<String> getHelpDocumentationLink(Language language) {
    return Optional.of(String.format("http://darmo-creations.net/products/jenealogio/help-doc/%s/", language.getCode()));
  }

  @Override
  public String getAboutFilePath() {
    return "/assets/about.html";
  }

  @Override
  public Optional<String> getIcon() {
    return Optional.of("/assets/icons/jenealogio_icon.png");
  }

  @Override
  public Optional<String> getLicenseIcon() {
    return Optional.of("/assets/icons/gplv3-127x51.png");
  }

  public static void main(String[] args) {
    launch(TestApplication.class, args);
  }
}
