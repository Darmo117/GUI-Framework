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
package net.darmo_creations.gui_framework.config;

import java.util.Locale;

/**
 * This class represents a language. It also provides methods to handle available languages.
 *
 * @author Damien Vergnet
 */
public final class Language {
  private final String name, code;
  private final Locale locale;

  public Language(String name, Locale locale) {
    this.name = name;
    this.code = locale.toString();
    this.locale = locale;
  }

  /**
   * @return the name written in the language
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return language code
   */
  public String getCode() {
    return this.code;
  }

  /**
   * @return a copy of the corresponding locale
   */
  public Locale getLocale() {
    return (Locale) this.locale.clone();
  }

  @Override
  public String toString() {
    return getName();
  }
}