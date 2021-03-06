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
package net.darmo_creations.gui_framework.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;

import net.darmo_creations.gui_framework.Application;
import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.events.ChangeLanguageEvent;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.gui_framework.gui.dialog.AboutDialog;
import net.darmo_creations.gui_framework.gui.dialog.UpdateDialog;
import net.darmo_creations.gui_framework.util.ImagesUtil;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.Nullable;
import net.darmo_creations.utils.swing.status_bar.StatusBar;
import net.darmo_creations.utils.version.Version;

/**
 * The main frame of the application.
 *
 * @author Damien Vergnet
 */
public abstract class ApplicationFrame<T extends ApplicationController<?>> extends JFrame {
  private static final long serialVersionUID = 2426665404072947885L;

  private AboutDialog aboutDialog;
  private UpdateDialog updateDialog;

  private JPanel contentPnl;
  private JCheckBoxMenuItem checkUpdatesItem;
  private JMenu optionsMenu, helpMenu;
  private JToolBar toolBar;
  private StatusBar statusBar;
  private JLabel updateLbl;

  protected final boolean hasMenuBar, hasToolBar, hasStatusBar;

  protected final Map<UserEvent.Type, ActionListener> listeners;

  public ApplicationFrame(WritableConfig config, boolean hasMenuBar, boolean hasToolBar, boolean hasStatusBar, boolean isFullyExtended,
      Dimension minSize, boolean resizable) {
    T controller = preInit(config);
    Application application = ApplicationRegistry.getApplication();

    setTitle(getBaseTitle());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setResizable(resizable);
    if (minSize != null)
      setMinimumSize(minSize);
    if (application.getIcon().isPresent())
      setIconImage(ImagesUtil.getIcon(ApplicationRegistry.getApplication().getIcon().get()).getImage());

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(UserEvent.DefaultType.EXITING));
      }
    });

    this.hasMenuBar = hasMenuBar;
    this.hasToolBar = hasToolBar;
    this.hasStatusBar = hasStatusBar;

    if (application.hasAboutDialog())
      this.aboutDialog = new AboutDialog(this);
    this.updateDialog = new UpdateDialog(this);

    this.listeners = new HashMap<>();
    for (UserEvent.DefaultType type : UserEvent.DefaultType.values())
      this.listeners.put(type, e -> ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(type)));

    if (this.hasMenuBar) {
      JMenuBar bar = initJMenuBar(this.listeners, config);
      for (int i = 0; i < bar.getMenuCount();) {
        if (bar.getMenu(i).getMenuComponentCount() == 0) {
          bar.remove(i);
        }
        else
          i++;
      }
      setJMenuBar(bar);
    }
    if (this.hasToolBar) {
      this.toolBar = initJToolBar(this.listeners);
      add(this.toolBar, BorderLayout.NORTH);
    }

    this.contentPnl = new JPanel();
    add(this.contentPnl, BorderLayout.CENTER);

    if (this.hasStatusBar) {
      this.statusBar = new StatusBar();
      add(this.statusBar, BorderLayout.SOUTH);
    }

    if (this.hasStatusBar && application.checkUpdates()) {
      this.updateLbl = new JLabel();
      this.updateLbl.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(UserEvent.DefaultType.OPEN_UPDATE));
        }
      });
      this.statusBar.addRightComponent(this.updateLbl);
    }

    initContent(controller, config);

    ApplicationRegistry.EVENTS_BUS.register(controller);

    controller.init();

    pack();
    setLocationRelativeTo(null);
    if (isFullyExtended)
      setExtendedState(MAXIMIZED_BOTH);
  }

  /**
   * Called before the frame is initialized.
   * 
   * @param config config object
   * @return the application's controller
   */
  protected abstract T preInit(WritableConfig config);

  /**
   * The frame's content must be initialized in this method.
   * 
   * @param controller this frames' controller
   * @param config config object
   */
  protected abstract void initContent(T controller, WritableConfig config);

  /**
   * Initializes the menu bar.
   * 
   * @param listeners the action listeners
   * @return the menu bar
   */
  protected JMenuBar initJMenuBar(Map<UserEvent.Type, ActionListener> listeners, WritableConfig config) {
    Application application = ApplicationRegistry.getApplication();
    JMenuBar menuBar = new JMenuBar();
    JMenuItem i;

    // 'Options' menu
    this.optionsMenu = new JMenu(I18n.getLocalizedString("menu.options.text"));
    this.optionsMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.options"));

    if (application.checkUpdates()) {
      this.optionsMenu.add(this.checkUpdatesItem = new JCheckBoxMenuItem(I18n.getLocalizedString("item.check_updates.text")));
      this.checkUpdatesItem.setMnemonic(I18n.getLocalizedMnemonic("item.check_updates"));
      this.checkUpdatesItem.addActionListener(listeners.get(UserEvent.DefaultType.TOGGLE_CHECK_UPDATES));
    }

    if (ApplicationRegistry.getApplication().getLanguages().length > 1) {
      JMenu langMenu = new JMenu(I18n.getLocalizedString("menu.lang.text"));
      langMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.lang"));
      this.optionsMenu.add(langMenu);
      ButtonGroup bg = new ButtonGroup();
      for (Language l : ApplicationRegistry.getApplication().getLanguages()) {
        langMenu.add(i = new JRadioButtonMenuItem(l.getName()));
        i.setSelected(l == config.getLanguage());
        i.setIcon(ImagesUtil.getIcon(Application.ICONS_LOCATION + "flag-" + l.getCode() + ".png"));
        i.addActionListener(e -> ApplicationRegistry.EVENTS_BUS.dispatchEvent(new ChangeLanguageEvent(l)));
        bg.add(i);
      }
    }

    menuBar.add(this.optionsMenu);

    // 'Help' menu
    this.helpMenu = new JMenu(I18n.getLocalizedString("menu.help.text"));
    this.helpMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.help"));

    if (application.hasHelpDocumentation()) {
      this.helpMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.help.text")));
      i.setIcon(ImagesUtil.HELP);
      i.setMnemonic(I18n.getLocalizedMnemonic("item.help"));
      i.addActionListener(listeners.get(UserEvent.DefaultType.HELP));
    }

    if (application.hasAboutDialog()) {
      this.helpMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.about.text")));
      i.setMnemonic(I18n.getLocalizedMnemonic("item.about"));
      i.addActionListener(listeners.get(UserEvent.DefaultType.ABOUT));
    }

    menuBar.add(this.helpMenu);

    return menuBar;
  }

  /**
   * Initializes the tool bar.
   * 
   * @param listeners the action listeners
   * @return the tool bar
   */
  protected JToolBar initJToolBar(Map<UserEvent.Type, ActionListener> listeners) {
    JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);

    toolBar.setFloatable(false);
    toolBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

    return toolBar;
  }

  /**
   * @return the frame's base title
   */
  public String getBaseTitle() {
    Application application = ApplicationRegistry.getApplication();
    return application.getName() + " " + application.getCurrentVersion();
  }

  protected Optional<JMenu> getOptionsMenu() {
    return this.hasMenuBar ? Optional.of(this.optionsMenu) : Optional.empty();
  }

  protected Optional<JMenu> getHelpMenu() {
    return this.hasMenuBar ? Optional.of(this.helpMenu) : Optional.empty();
  }

  protected Optional<JToolBar> getToolBar() {
    return this.hasToolBar ? Optional.of(this.toolBar) : Optional.empty();
  }

  /**
   * @return the panel where all content should be added
   */
  protected JPanel getContentPanel() {
    return this.contentPnl;
  }

  protected Optional<StatusBar> getStatusBar() {
    return this.hasStatusBar ? Optional.of(this.statusBar) : Optional.empty();
  }

  /**
   * @return true if the check updates item is seleted
   */
  public boolean isCheckUpdatesItemSelected() {
    return this.hasMenuBar && this.checkUpdatesItem.isSelected();
  }

  /**
   * Sets the selection of the check updates item.
   */
  public void setCheckUpdatesItemSelected(boolean selected) {
    if (this.hasMenuBar && ApplicationRegistry.getApplication().checkUpdates())
      this.checkUpdatesItem.setSelected(selected);
  }

  public static final int UPDATES_BLOCKED = 0;
  public static final int CHECKING_UPDATES = 1;
  public static final int NEW_UPDATE = 2;
  public static final int NO_UPDATE = 3;
  public static final int UPDATES_CHECK_FAILED = 4;

  /**
   * Sets the text and icon of the updates status.
   * 
   * @param mode one of these values: {@link #UPDATES_BLOCKED}, {@link #CHECKING_UPDATES},
   *          {@link #NEW_UPDATE}, {@link #UPDATES_CHECK_FAILED}, {@link #NO_UPDATE}
   * @param str an optional string to append to the end of the status
   */
  public void setUpdateLabelText(int mode, @Nullable String str) {
    if (this.updateLbl == null)
      return;

    Icon icon = null;
    String s = null;

    switch (mode) {
      case UPDATES_BLOCKED:
        icon = ImagesUtil.UPDATE_CHECK_FAILED;
        s = I18n.getLocalizedString("label.updates_check_blocked.text");
        break;
      case CHECKING_UPDATES:
        icon = ImagesUtil.CHECKING_UPDATES;
        s = I18n.getLocalizedString("label.checking_updates.text");
        break;
      case NEW_UPDATE:
        icon = ImagesUtil.NEW_UPDATE;
        s = I18n.getLocalizedString("label.update_available.text");
        break;
      case NO_UPDATE:
        icon = null;
        s = I18n.getLocalizedString("label.no_update.text");
        break;
      case UPDATES_CHECK_FAILED:
        icon = ImagesUtil.UPDATE_CHECK_FAILED;
        s = I18n.getLocalizedString("label.update_check_failed.text");
        break;
    }

    if (s != null && str != null) {
      s += str;
    }

    this.updateLbl.setIcon(icon);
    this.updateLbl.setText(s);
  }

  /**
   * Shows the "about" dialog.
   */
  public void showAboutDialog() {
    if (this.aboutDialog != null)
      this.aboutDialog.setVisible(true);
  }

  /**
   * Shows the update dialog.
   * 
   * @param version update's version
   * @param link update's link
   * @param changelog update's changelog
   */
  public void showUpdateDialog(Version version, String link, String changelog) {
    this.updateDialog.setInfo(version, link, changelog);
    this.updateDialog.setVisible(true);
  }

  /**
   * Shows an error message.
   * 
   * @param message the message
   */
  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, I18n.getLocalizedString("popup.error.title"), JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Shows a confirm dialog.
   * 
   * @param message the message
   * @return an integer indicating the option selected by the user (either YES_OPTION, NO_OPTION,
   *         CANCEL_OPTION or CLOSED_OPTION)
   */
  public int showConfirmDialog(String message) {
    return showConfirmDialog(message, JOptionPane.YES_NO_OPTION);
  }

  /**
   * Shows a confirm dialog.
   * 
   * @param message the message
   * @param optionType an integer designating the options available on the dialog: YES_NO_OPTION,
   *          YES_NO_CANCEL_OPTION, or OK_CANCEL_OPTION
   * @return an integer indicating the option selected by the user (either YES_OPTION, NO_OPTION,
   *         CANCEL_OPTION or CLOSED_OPTION)
   */
  public int showConfirmDialog(String message, int optionType) {
    return JOptionPane.showConfirmDialog(this, message, I18n.getLocalizedString("popup.confirm.title"), optionType,
        JOptionPane.QUESTION_MESSAGE);
  }

  /**
   * Shows a warning message.
   * 
   * @param message the message
   */
  public void showWarningDialog(String message) {
    JOptionPane.showMessageDialog(this, message, I18n.getLocalizedString("popup.warning.title"), JOptionPane.WARNING_MESSAGE);
  }
}
