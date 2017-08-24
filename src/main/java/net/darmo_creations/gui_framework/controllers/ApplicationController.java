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
package net.darmo_creations.gui_framework.controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.swing.JOptionPane;

import net.darmo_creations.gui_framework.Application;
import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.DefaultConfigTags;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.dao.ConfigDao;
import net.darmo_creations.gui_framework.events.ChangeLanguageEvent;
import net.darmo_creations.gui_framework.events.UpdateEvent;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.gui_framework.util.UpdatesChecker;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.JarUtil;
import net.darmo_creations.utils.events.SubsribeEvent;

/**
 * This is controller handles events from the MainFrame class.
 * 
 * @author Damien Vergnet
 */
public class ApplicationController<T extends ApplicationFrame<?>> {
  /** The frame */
  protected final T frame;
  /** The configuration */
  protected final WritableConfig config;
  /** Updates checker */
  protected final UpdatesChecker updatesChecker;
  /** If true updates checking is enabled */
  protected final boolean checkUpdatesEnabled;

  /**
   * Creates an application controller.
   * 
   * @param frame the frame
   * @param config the configuration
   */
  public ApplicationController(T frame, WritableConfig config) {
    this.frame = frame;
    this.config = config;
    this.checkUpdatesEnabled = ApplicationRegistry.getApplication().checkUpdates();
    this.updatesChecker = new UpdatesChecker();
  }

  /**
   * Initializes the controller.
   */
  public void init() {
    this.frame.setCheckUpdatesItemSelected(this.config.getValue(DefaultConfigTags.CHECK_UPDATES));
    this.frame.setUpdateLabelText(ApplicationFrame.CHECKING_UPDATES, null);
    this.updatesChecker.checkUpdate();
  }

  /**
   * Called when a UserEvent is fired.
   * 
   * @param e the event
   */
  @SuppressWarnings("incomplete-switch")
  @SubsribeEvent
  public void onUserEvent(UserEvent e) {
    if (e.isCanceled()) {
      return;
    }

    UserEvent.Type type = e.getType();
    if (type instanceof UserEvent.DefaultType) {
      switch ((UserEvent.DefaultType) type) {
        case HELP:
          showHelp();
          break;
        case ABOUT:
          this.frame.showAboutDialog();
          break;
        case EXITING:
          if (!exit())
            e.setCanceled();
          break;
        case OPEN_UPDATE:
          openUpdate();
          break;
        case TOGGLE_CHECK_UPDATES:
          toggleCheckUpdates();
          break;
      }
    }
  }

  /**
   * Called when a ChangeLanguageEvent is fired.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onChangeLanguage(ChangeLanguageEvent e) {
    int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.change_language.confirm.text"));

    if (choice == JOptionPane.YES_OPTION) {
      try {
        this.config.setLanguage(e.getLanguage());
        restartApplication();
      }
      catch (IOException | URISyntaxException __) {
        this.frame.showErrorDialog(I18n.getLocalizedString("popup.change_language.restart_error.text"));
        System.exit(1);
      }
    }
  }

  @SubsribeEvent
  public void onUpdateChecking(UpdateEvent.Checking e) {
    if (!this.checkUpdatesEnabled || !this.config.getValue(DefaultConfigTags.CHECK_UPDATES)) {
      this.frame.setUpdateLabelText(ApplicationFrame.UPDATES_BLOCKED, null);
      e.setCanceled();
    }
  }

  @SubsribeEvent
  public void onNewUpdate(UpdateEvent.NewUpdate e) {
    Application application = ApplicationRegistry.getApplication();
    this.frame.setUpdateLabelText(ApplicationFrame.NEW_UPDATE, " - " + application.getName() + " " + e.getVersion());
  }

  @SubsribeEvent
  public void onNoUpdate(UpdateEvent.NoUpdate e) {
    this.frame.setUpdateLabelText(ApplicationFrame.NO_UPDATE, null);
  }

  @SubsribeEvent
  public void onUpdateCheckFailed(UpdateEvent.CheckFailed e) {
    this.frame.setUpdateLabelText(ApplicationFrame.UPDATES_CHECK_FAILED, null);
  }

  /**
   * Opens the help in the browser.
   */
  private void showHelp() {
    try {
      Application application = ApplicationRegistry.getApplication();
      Optional<String> link = application.getHelpDocumentationLink(this.config.getLanguage());
      if (link.isPresent())
        Desktop.getDesktop().browse(new URI(link.get()));
    }
    catch (IOException | URISyntaxException ex) {}
  }

  /**
   * Opens the update dialog if an update is available.
   */
  private void openUpdate() {
    if (this.updatesChecker.isUpdateAvailable()) {
      this.frame.showUpdateDialog(this.updatesChecker.getVersion(), this.updatesChecker.getLink(), this.updatesChecker.getChangelog());
    }
  }

  /**
   * Toggles updates checking on startup.
   */
  private void toggleCheckUpdates() {
    boolean checked = this.frame.isCheckUpdatesItemSelected();
    this.config.setValue(DefaultConfigTags.CHECK_UPDATES, checked);
    if (checked) {
      this.frame.setUpdateLabelText(ApplicationFrame.CHECKING_UPDATES, null);
      this.updatesChecker.checkUpdate();
    }
  }

  /**
   * Exits the application. A UserEvent of type UserEvent.DefaultType.EXIT is fired. If it is
   * cancelled, the application will not exit.
   * 
   * @return true if the application exited; false otherwise
   */
  protected boolean exit() {
    UserEvent event = new UserEvent(UserEvent.DefaultType.EXIT);
    ApplicationRegistry.EVENTS_BUS.dispatchEvent(event);

    if (!event.isCanceled()) {
      ConfigDao.getInstance().save(this.config);
      this.frame.dispose();
      return true;
    }
    return false;
  }

  /**
   * Restarts the application.
   */
  private void restartApplication() throws IOException, URISyntaxException {
    UserEvent event = new UserEvent(UserEvent.DefaultType.EXITING);
    ApplicationRegistry.EVENTS_BUS.dispatchEvent(event);

    if (!event.isCanceled()) {
      JarUtil.restartApplication(".jar");
    }
  }
}
