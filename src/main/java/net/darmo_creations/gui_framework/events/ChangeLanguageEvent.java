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

import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.utils.events.AbstractEvent;

/**
 * This event is fired when changing the language.
 *
 * @author Damien Vergnet
 */
public class ChangeLanguageEvent extends AbstractEvent {
  private final Language language;

  /**
   * Creates an event
   * 
   * @param language the desired language
   */
  public ChangeLanguageEvent(Language language) {
    super(true);
    this.language = language;
  }

  /**
   * @return the desired language
   */
  public Language getLanguage() {
    return this.language;
  }
}
