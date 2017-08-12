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
package net.darmo_creations.gui_framework.events;

import net.darmo_creations.utils.events.AbstractEvent;

/**
 * This event is fired when a menu item/button is clicked. This event can be cancelled.
 * 
 * @author Damien Vergnet
 */
public final class UserEvent extends AbstractEvent {
  private final Type type;

  /**
   * Creates an event.
   * 
   * @param type the type
   */
  public UserEvent(Type type) {
    this.type = type;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return this.type;
  }

  public interface Type {}

  /**
   * Default event types.
   *
   * @author Damien Vergnet
   */
  public static enum DefaultType implements Type {
    /** The help has been invoked. */
    HELP,
    /** The about dialog has been invoked. */
    ABOUT,
    /** The application is exiting (fired before {@link #EXIT}). */
    EXITING,
    /**
     * The application will exit right after the event is accepted (fired after {@link #EXITING}).
     */
    EXIT,
    /** The update dialog has been invoked. */
    OPEN_UPDATE,
    /** Updates checking has been toggled. */
    TOGGLE_CHECK_UPDATES;
  }
}
