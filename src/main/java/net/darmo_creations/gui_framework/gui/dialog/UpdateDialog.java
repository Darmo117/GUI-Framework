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
package net.darmo_creations.gui_framework.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;
import net.darmo_creations.utils.swing.dialog.DefaultDialogController;
import net.darmo_creations.utils.version.Version;

/**
 * This dialog shows the last available update.
 *
 * @author Damien Vergnet
 */
public class UpdateDialog extends AbstractDialog {
  private static final long serialVersionUID = -5448335368489639409L;

  private JEditorPane patchnotePnl;

  public UpdateDialog(JFrame owner) {
    super(owner, Mode.CLOSE_OPTION, true);

    setTitle(I18n.getLocalizedString("dialog.update.title"));
    setPreferredSize(new Dimension(400, 300));
    setMinimumSize(getPreferredSize());

    this.patchnotePnl = new JEditorPane();
    this.patchnotePnl.setEditable(false);
    this.patchnotePnl.setContentType("text/html");
    this.patchnotePnl.setDocument(getDocument(this.patchnotePnl));
    this.patchnotePnl.addHyperlinkListener(e -> {
      try {
        if (e.getEventType() == EventType.ACTIVATED)
          Desktop.getDesktop().browse(e.getURL().toURI());
      }
      catch (IOException | URISyntaxException __) {}
    });

    add(new JScrollPane(this.patchnotePnl), BorderLayout.CENTER);

    setActionListener(new DefaultDialogController<>(this));

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Sets the dialog's content.
   * 
   * @param version update's version
   * @param link update's link
   * @param changelog update's changelog
   */
  public void setInfo(Version version, String link, String changelog) {
    String title = ApplicationRegistry.getApplication().getName() + " " + version;
    String linkTitle = I18n.getLocalizedString("label.download_link.text");
    String listTitle = I18n.getLocalizedString("label.changelog.text");
    String html = String.format("<html><h1>%1$s</h1>%2$s<br /><a href=\"%3$s\">%3$s</a><h4>%4$s</h4>%5$s</html>", title, linkTitle, link,
        listTitle, changelog);
    this.patchnotePnl.setText(html);
  }

  private Document getDocument(JEditorPane textPane) {
    HTMLEditorKit kit = new HTMLEditorKit();
    textPane.setEditorKit(kit);

    StyleSheet styleSheet = kit.getStyleSheet();
    styleSheet.addRule("body {font-family: sans serif; margin: 4px; font-size: 10px}");
    styleSheet.addRule("h2 {margin-top: 0}");

    return kit.createDefaultDocument();
  }
}
