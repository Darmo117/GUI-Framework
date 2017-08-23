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

/**
 * This type of tags is associated with short integer values.
 *
 * @author Damien Vergnet
 */
public final class ShortTag extends AbstractTag<Short> {
  public ShortTag(String name) {
    super(name, Short.class);
  }

  @Override
  public String serializeValueGeneric(Short value) {
    return "" + value;
  }

  @Override
  public Short deserializeValue(String value) {
    try {
      return Short.parseShort(value);
    }
    catch (NumberFormatException ex) {
      return null;
    }
  }
}
