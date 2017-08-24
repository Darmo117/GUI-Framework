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
package net.darmo_creations.gui_framework;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.dao.ConfigDao;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.I18n;

/**
 * The main method from the application must call Start.run() after it has been registered to the
 * {@code ApplicationRegistry}.
 *
 * @author Damien Vergnet
 */
public class Start {
  /**
   * Starts the application.
   */
  public static void run() {
    Application application = ApplicationRegistry.getApplication();
    WritableConfig config = ConfigDao.getInstance().load();

    try {
      I18n.init(application.getLanguageFilesStream(config.getLanguage()));
    }
    catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Could not load language file!", "Error", JOptionPane.ERROR_MESSAGE);
      System.exit(1);
    }

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      JOptionPane.showMessageDialog(null, I18n.getLocalizedString("popup.laf_error.text"), I18n.getLocalizedString("popup.laf_error.title"),
          JOptionPane.ERROR_MESSAGE);
    }

    application.preInit(config);

    ApplicationFrame<?> frame = application.initFrame(config);
    frame.setVisible(true);
  }
}
