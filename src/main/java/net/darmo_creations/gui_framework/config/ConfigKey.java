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

/**
 * {@code ConfigKey}s are used by the {@code GlobalConfig} class.
 * 
 * @author Damien Vergnet
 *
 * @param <T> the type of the associated values
 */
public abstract class ConfigKey<T> {
  private final String name;
  private final Class<T> valueClass;

  /**
   * Creates a key with the given name.
   * 
   * @param name the name
   */
  public ConfigKey(String name, Class<T> valueClass) {
    this.name = name;
    this.valueClass = valueClass;
  }

  /**
   * @return this key's name
   */
  public final String getName() {
    return this.name;
  }

  /**
   * @return the class of the associated values
   */
  public final Class<T> getValueClass() {
    return this.valueClass;
  }

  /**
   * Serializes a value.
   * 
   * @param value the value
   * @return the serialized value
   * @throws ClassCastException if the value is not of type T
   */
  @SuppressWarnings("unchecked")
  public final String serializeValue(Object value) {
    return serializeValueGeneric((T) value);
  }

  /**
   * Serializes a value.
   * 
   * @param value the value
   * @return the serialized value
   */
  protected abstract String serializeValueGeneric(T value);

  /**
   * Deserializes a value.
   * 
   * @param value the serialized value
   * @return the deserialized value
   */
  public abstract T deserializeValue(String value);
}
