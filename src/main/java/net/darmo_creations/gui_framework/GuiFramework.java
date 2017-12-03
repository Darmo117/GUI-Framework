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

import net.darmo_creations.gui_framework.config.DefaultConfigTags;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.dao.ConfigDao;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.I18n;

/**
 * The main method from the application must call {@code GuiFramework.run()} after it has been
 * registered to the {@code ApplicationRegistry}.
 *
 * @author Damien Vergnet
 */
public class GuiFramework {
  /**
   * Starts the framework.
   */
  public static void run(Class<? extends Application> appClass) {
    ApplicationRegistry.registerApplication(appClass);
    Application application = ApplicationRegistry.startApplication();

    WritableConfig.registerTag(DefaultConfigTags.CHECK_UPDATES, application.checkUpdates());
    application.preInit();

    WritableConfig config = ConfigDao.getInstance().load();

    setLanguage(application, config, false);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      JOptionPane.showMessageDialog(null, I18n.getLocalizedString("popup.laf_error.text"), I18n.getLocalizedString("popup.laf_error.title"),
          JOptionPane.ERROR_MESSAGE);
    }

    ApplicationFrame<?> frame = application.initFrame(config);
    frame.setVisible(true);
  }

  private static void setLanguage(Application application, WritableConfig config, boolean error) {
    try {
      I18n.init(application.getLanguageFilesStream(config.getLanguage()));
    }
    catch (NullPointerException | IOException ex) {
      if (error) {
        JOptionPane.showMessageDialog(null, "Could not load default language file! This application will now exit.", "Error",
            JOptionPane.ERROR_MESSAGE);
        System.exit(1);
      }
      else {
        JOptionPane.showMessageDialog(null, "Could not load language file! Swithing to default language.", "Error",
            JOptionPane.ERROR_MESSAGE);
        config.setLanguage(ApplicationRegistry.getDefaultLanguage());
        setLanguage(application, config, true);
      }
    }
  }
}
