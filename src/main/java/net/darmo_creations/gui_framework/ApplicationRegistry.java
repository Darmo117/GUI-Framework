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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.utils.events.EventsBus;

/**
 * An application must register itself to this registry to be started. Only one application can be
 * registered.
 *
 * @author Damien Vergnet
 */
public final class ApplicationRegistry {
  /** Application's main event bus */
  public static final EventsBus EVENTS_BUS = new EventsBus();

  private static Application application;

  /**
   * Registers the application.
   * 
   * @param application
   */
  public static void registerApplication(Application app) {
    application = Objects.requireNonNull(app);
  }

  /**
   * @return the registered application
   * @throws IllegalStateException if no application has been registered
   */
  public static Application getApplication() {
    if (application == null)
      throw new IllegalStateException("no application registered");
    return application;
  }

  /** List of available languages */
  private static List<Language> languages;
  /** Default language */
  private static Language defaultLanguage;

  /**
   * Intializes the available languages. They will appear in the menu in the same order as they are
   * in the list. The first language in the list will be the default if the config could not be
   * loaded.
   * 
   * @param langs the languages
   * @throws IllegalArgumentException if the list is empty
   */
  public static void setLanguages(Language... langs) {
    setLanguages(Arrays.asList(langs));
  }

  /**
   * Intializes the available languages. They will appear in the menu in the same order as they are
   * in the list. The first language in the list will be the default if the config could not be
   * loaded.
   * 
   * @param langs the languages
   * @throws IllegalArgumentException if the list is empty
   */
  public static void setLanguages(List<Language> langs) {
    languages = new ArrayList<>(langs);
    if (languages.isEmpty())
      throw new IllegalArgumentException("empty languages list");
    defaultLanguage = languages.get(0);
  }

  /**
   * @return all available languages
   */
  public static Language[] getLanguages() {
    return languages.stream().toArray(Language[]::new);
  }

  /**
   * @return the default language
   */
  public static Language getDefaultLanguage() {
    return defaultLanguage;
  }

  /**
   * Returns the language matching the code.
   * 
   * @param code language code
   * @return the matching value
   */
  public static Language getLanguageFromCode(String code) {
    for (Language l : languages) {
      if (l.getCode().equals(code))
        return l;
    }
    return null;
  }

  private ApplicationRegistry() {}
}
