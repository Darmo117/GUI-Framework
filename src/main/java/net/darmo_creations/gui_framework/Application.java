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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.darmo_creations.gui_framework.config.DefaultConfigTags;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.dao.ConfigDao;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.version.Version;

/**
 * Applications must extend this class.
 *
 * @author Damien Vergnet
 */
public abstract class Application {
  public static final String ICONS_LOCATION = "/assets/icons/";

  private static boolean launched = false;

  /**
   * Launches a standalone application. This method is typically called from the main method. It
   * must not be called more than once or an exception will be thrown.
   *
   * <p>
   * The launch method does not return until the application has exited, either via a call to
   * System.exit or all of the application windows have been closed.
   * </p>
   *
   * <p>
   * Typical usage is:
   * 
   * <pre>
   * public static void main(String[] args) {
   *   Application.launch(MyApp.class, args);
   * }
   * </pre>
   * 
   * where {@code MyApp} is a subclass of Application.
   * </p>
   *
   * @param applicationClass the application class that is constructed and executed by the launcher
   * @param args the command line arguments passed to the application
   *
   * @throws IllegalStateException if this method is called more than once
   */
  public static void launch(Class<? extends Application> applicationClass, String... args) {
    if (launched)
      throw new IllegalStateException("Application already launched!");

    Application application;
    try {
      application = applicationClass.newInstance();
    }
    catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    ApplicationRegistry.registerApplication(application);

    WritableConfig.registerTag(DefaultConfigTags.CHECK_UPDATES, application.checkUpdates());
    application.preInit();

    WritableConfig config = ConfigDao.getInstance().load();

    setLanguage(application, config, false);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      JOptionPane.showMessageDialog(null, I18n.getLocalizedString("popup.laf_error.text"), I18n.getLocalizedString("popup.laf_error.title"),
          JOptionPane.ERROR_MESSAGE);
    }

    ApplicationFrame<?> frame = application.initFrame(config);
    launched = true;
    frame.setVisible(true);
  }

  private static void setLanguage(Application application, WritableConfig config, boolean error) {
    try {
      I18n.init(application.getLanguageFilesStream(config.getLanguage()));
    }
    catch (NullPointerException | IOException ex) {
      if (error) {
        JOptionPane.showMessageDialog(null, "Could not load default language file! This application will now exit.", "Error",
            JOptionPane.ERROR_MESSAGE);
        System.exit(1);
      }
      else {
        JOptionPane.showMessageDialog(null, "Could not load language file! Swithing to default language.", "Error",
            JOptionPane.ERROR_MESSAGE);
        config.setLanguage(application.getDefaultLanguage());
        setLanguage(application, config, true);
      }
    }
  }

  /** List of available languages */
  private Language[] languages;
  private Language defaultLanguage;

  protected Application() {}

  /**
   * This method is called before any other from this class. Languages should be registered in this
   * method by calling {@link #setLanguages(Language...)} method.
   */
  protected void preInit() {}

  /**
   * The application's main frame must be initialized in this method.
   * 
   * @param config the configuration
   * @return the main frame
   */
  protected abstract ApplicationFrame<?> initFrame(WritableConfig config);

  /**
   * Returns application's name.
   */
  public abstract String getName();

  /**
   * Returns application's version.
   */
  public abstract Version getCurrentVersion();

  /**
   * Returns the input stream to the lang file corresponding to the given language.
   */
  protected abstract InputStream getLanguageFilesStream(Language language);

  /**
   * @return true if the application has an update checker; false otherwise
   */
  public final boolean checkUpdates() {
    return getRssUpdatesLink() != null;
  }

  /**
   * Returns the link to the updates rss feed.
   */
  public String getRssUpdatesLink() {
    return null;
  }

  /**
   * Indicates if a help documention is available online in at least one language.
   */
  public final boolean hasHelpDocumentation() {
    return Arrays.stream(this.languages).anyMatch(l -> getHelpDocumentationLink(l) != null);
  }

  /**
   * Return the link to the help documentation for the given language.
   */
  public Optional<String> getHelpDocumentationLink(Language language) {
    return null;
  }

  /**
   * Indicates if an about dialog is available.
   */
  public final boolean hasAboutDialog() {
    return getAboutFilePath() != null;
  }

  /**
   * Returns the path to the about file (must be in the jar).
   */
  public String getAboutFilePath() {
    return null;
  }

  /**
   * Returns application's icon path.
   */
  public Optional<String> getIcon() {
    return Optional.empty();
  }

  /**
   * Returns license icon path.
   */
  public Optional<String> getLicenseIcon() {
    return Optional.empty();
  }

  /**
   * Returns a copy of all available languages
   */
  public Language[] getLanguages() {
    return Arrays.copyOf(this.languages, this.languages.length);
  }

  /**
   * Intializes the available languages. They will appear in the menu in the same order as they are
   * in the array. The first language in the list will be the default. This default language is the
   * one used if the config could failed to load or the current language is not available.
   * 
   * @param languages the languages
   * @throws IllegalArgumentException if the list is empty
   */
  public void setLanguages(Language... languages) {
    if (languages.length == 0)
      throw new IllegalArgumentException("empty languages list");
    this.languages = Arrays.copyOf(languages, languages.length);
    this.defaultLanguage = this.languages[0];
  }

  /**
   * Returns the default language.
   */
  public Language getDefaultLanguage() {
    return this.defaultLanguage;
  }

  /**
   * Returns the language matching the given code.
   * 
   * @param code language code
   * @return the matching value
   */
  public Language getLanguageFromCode(String code) {
    for (Language l : this.languages) {
      if (l.getCode().equals(code))
        return l;
    }
    return null;
  }
}
