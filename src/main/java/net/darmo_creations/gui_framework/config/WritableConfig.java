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

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.tags.AbstractTag;

/**
 * This class is a default implementation for a config object.
 *
 * @author Damien Vergnet
 */
public class WritableConfig implements Cloneable {
  private static final Map<AbstractTag<?>, Object> DEFAULT_VALUES = new HashMap<>();

  /**
   * Registers a key and its default value. Values should be unmuttable to avoid side effects.
   * 
   * @param key the key
   * @param defaultValue its default value
   */
  public static <T> void registerKey(AbstractTag<T> key, T defaultValue) {
    DEFAULT_VALUES.put(key, DEFAULT_VALUES);
  }

  /**
   * @return all registered keys
   */
  public static Set<AbstractTag<?>> getRegisteredKeys() {
    return new HashSet<>(DEFAULT_VALUES.keySet());
  }

  /**
   * 
   * @param name
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends AbstractTag<?>> Optional<T> getKeyFromName(String name, String type) {
    return (Optional<T>) DEFAULT_VALUES.keySet().stream().filter(k -> k.getValueClass().getName().equals(type)).findFirst();
  }

  /**
   * 
   * @param key
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T getDefaultValue(AbstractTag<T> key) {
    return key == null ? null : (T) DEFAULT_VALUES.get(key);
  }

  private Language language;
  private Map<AbstractTag<?>, Object> map;

  /**
   * Creates a config with default values for all properties.
   */
  public WritableConfig() {
    setLanguage(ApplicationRegistry.getDefaultLanguage());
    this.map = new HashMap<>(DEFAULT_VALUES);
    setValue(DefaultConfigTags.CHECK_UPDATES, true);
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
   * @param key the key
   * @return the value or null
   */
  @SuppressWarnings("unchecked")
  public <T> T getValue(AbstractTag<T> key) {
    return key == null ? null : (T) this.map.get(key);
  }

  /**
   * Sets the value for the given key.
   * 
   * @param key the key
   * @param value the associated value
   * @throws ClassCastException if the value is not null and not of type T
   */
  public <T> void setValue(AbstractTag<T> key, Object value) {
    if (value != null && key.getValueClass() != value.getClass())
      throw new ClassCastException("expected type was " + key.getValueClass() + " but actual type was " + value.getClass());
    this.map.put(key, value);
  }

  /**
   * Makes a clone of this config. It should be noted that the new options map is only a shallow
   * copy of the current one.
   */
  @Override
  public WritableConfig clone() {
    try {
      WritableConfig config = (WritableConfig) super.clone();
      config.map = new HashMap<>(this.map);
      return config;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
