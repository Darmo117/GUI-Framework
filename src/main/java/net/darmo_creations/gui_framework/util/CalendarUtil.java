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
package net.darmo_creations.gui_framework.util;

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import net.darmo_creations.utils.I18n;

/**
 * Useful functions to handle dates.
 * 
 * @author Damien Vergnet
 */
public final class CalendarUtil {
  /**
   * @return the current date
   */
  public static Calendar getCurrentDate() {
    long offset = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
    // Avoid null if no ID found
    String id = "";

    for (String i : TimeZone.getAvailableIDs()) {
      TimeZone tz = TimeZone.getTimeZone(i);

      if (tz.getRawOffset() == offset) {
        id = i;
        break;
      }
    }
    return Calendar.getInstance(TimeZone.getTimeZone(id), Locale.FRENCH);
  }

  /**
   * Formats the given date.
   * 
   * @param date the date
   * @return the formatted date or nothing if there is no date
   */
  public static Optional<String> formatDate(Optional<Calendar> date) {
    if (date.isPresent()) {
      Calendar c = date.get();
      return Optional.of(I18n.getFormattedDate("" + c.get(Calendar.YEAR), "" + (c.get(Calendar.MONTH) + 1), "" + c.get(Calendar.DATE)));
    }
    return Optional.empty();
  }

  private CalendarUtil() {}
}
