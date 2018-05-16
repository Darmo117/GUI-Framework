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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import net.darmo_creations.gui_framework.config.tags.AbstractTag;

/**
 * This config class is used to associate values to tags as well as default values.
 *
 * @author Damien Vergnet
 */
public class WritableConfig implements Cloneable {
  private static final Map<AbstractTag<?>, Object> DEFAULT_VALUES = new HashMap<>();

  /**
   * Registers a tag and its default value. Values should be unmodifiable to avoid side effects.
   * 
   * @param tag the tag
   * @param defaultValue its default value
   */
  public static <T> void registerTag(AbstractTag<T> tag, T defaultValue) {
    DEFAULT_VALUES.put(tag, defaultValue);
  }

  /**
   * Returns all registered tags.
   */
  public static Set<AbstractTag<?>> getRegisteredTags() {
    return new HashSet<>(DEFAULT_VALUES.keySet());
  }

  /**
   * Returns the tag corresponding to the given name and class name.
   */
  @SuppressWarnings("unchecked")
  public static <T extends AbstractTag<?>> Optional<T> getTagFromName(String name, String className) {
    return (Optional<T>) DEFAULT_VALUES.keySet().stream().filter(
        k -> k.getName().equals(name) && k.getValueClass().getName().equals(className)).findFirst();
  }

  /**
   * Returns the default value for the given tag.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getDefaultValue(AbstractTag<T> tag) {
    return tag == null ? null : (T) DEFAULT_VALUES.get(tag);
  }

  private Language language;
  private Map<AbstractTag<?>, Object> values;

  /**
   * Creates a config with default values for all properties.
   * 
   * @param defaultLanguage the default language
   */
  public WritableConfig(Language defaultLanguage) {
    setLanguage(defaultLanguage);
    this.values = new HashMap<>(DEFAULT_VALUES);
  }

  /**
   * @return the current language
   */
  public Language getLanguage() {
    return this.language;
  }

  /**
   * Sets the language. Cannot be null.
   * 
   * @param language the new language
   */
  public void setLanguage(Language language) {
    this.language = Objects.requireNonNull(language);
  }

  /**
   * Returns the value for the given key. If the key is null or no value was found, null is
   * returned.
   * 
   * @param tag the key
   * @return the value or null
   */
  @SuppressWarnings("unchecked")
  public <T> T getValue(AbstractTag<T> tag) {
    return tag == null ? null : (T) this.values.get(tag);
  }

  /**
   * Sets the value for the given tag.
   * 
   * @param tag the tag
   * @param value the associated value
   * @throws ClassCastException if the value is not null and not of type T
   */
  public <T> void setValue(AbstractTag<T> tag, Object value) {
    if (value != null && tag.getValueClass() != value.getClass())
      throw new ClassCastException("expected type was " + tag.getValueClass() + " but actual type was " + value.getClass());
    this.values.put(tag, value);
  }

  /**
   * Tells if the given tag has been modified.
   * 
   * @param tag the tag
   * @return true if the current value is not the default; false otherwise
   */
  public boolean isModified(AbstractTag<?> tag) {
    return !this.values.get(tag).equals(DEFAULT_VALUES.get(tag));
  }

  /**
   * Makes a clone of this config. It should be noted that the new options map is only a shallow
   * copy of the current one.
   */
  @Override
  public WritableConfig clone() {
    try {
      WritableConfig config = (WritableConfig) super.clone();
      config.values = new HashMap<>(this.values);
      return config;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
