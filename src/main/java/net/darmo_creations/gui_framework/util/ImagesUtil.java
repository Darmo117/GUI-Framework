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

import javax.swing.ImageIcon;

/**
 * This class holds all images for the app and provides a method to copy images.
 * 
 * @author Damien Vergnet
 */
public final class ImagesUtil {
  public static final ImageIcon HELP = getIcon("/assets/icons/help.png");
  public static final ImageIcon UPDATE_CHECK_FAILED = getIcon("/assets/icons/updates_check_failed.png");
  public static final ImageIcon CHECKING_UPDATES = getIcon("/assets/icons/checking_updates.gif");
  public static final ImageIcon NEW_UPDATE = getIcon("/assets/icons/update_available.png");

  /**
   * Loads the icon at the given path.
   * 
   * @param path the path
   * @return the icon
   */
  public static ImageIcon getIcon(String path) {
    return new ImageIcon(ImagesUtil.class.getResource(path));
  }

  private ImagesUtil() {}
}
