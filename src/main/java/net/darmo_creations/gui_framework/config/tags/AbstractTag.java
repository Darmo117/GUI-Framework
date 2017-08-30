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
package net.darmo_creations.gui_framework.config.tags;

import java.util.Objects;

/**
 * {@code ConfigTag}s are used by the {@code WritableConfig} class.
 * 
 * @author Damien Vergnet
 *
 * @param <T> the type of the associated values
 */
public abstract class AbstractTag<T> {
  private final String name;
  private final Class<T> valueClass;

  /**
   * Creates a key with the given name.
   * 
   * @param name the name
   */
  public AbstractTag(String name, Class<T> valueClass) {
    this.name = Objects.requireNonNull(name);
    this.valueClass = Objects.requireNonNull(valueClass);
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + this.name.hashCode();
    result = prime * result + this.valueClass.hashCode();

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof AbstractTag<?>) {
      AbstractTag<?> t = (AbstractTag<?>) o;
      return t.getName().equals(getName()) && t.getValueClass() == getValueClass();
    }
    return false;
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
