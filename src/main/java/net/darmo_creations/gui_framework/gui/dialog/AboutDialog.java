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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringJoiner;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.darmo_creations.gui_framework.Application;
import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.ImageLabel;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;
import net.darmo_creations.utils.swing.dialog.DefaultDialogController;

public class AboutDialog extends AbstractDialog {
  private static final long serialVersionUID = -1919314429757828369L;

  public AboutDialog(JFrame owner) {
    super(owner, Mode.CLOSE_OPTION, false);

    Application application = ApplicationRegistry.getApplication();

    setTitle(I18n.getLocalizedString("dialog.about.title"));

    JPanel leftPnl = new JPanel();
    leftPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    leftPnl.setLayout(new BoxLayout(leftPnl, BoxLayout.Y_AXIS));
    ImageLabel icon = new ImageLabel(new ImageIcon(application.getIcon()), true);
    icon.setPreferredSize(new Dimension(100, 100));
    leftPnl.add(icon);
    leftPnl.add(new JLabel(new ImageIcon(application.getLicenseIcon())));
    add(leftPnl, BorderLayout.WEST);

    JEditorPane textPnl = new JEditorPane();
    textPnl.setContentType("text/html");
    textPnl.setEditable(false);
    textPnl.setPreferredSize(new Dimension(600, 200));
    textPnl.setDocument(getDocument(textPnl));
    textPnl.addHyperlinkListener(e -> {
      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        try {
          Desktop.getDesktop().browse(new URI(e.getDescription()));
        }
        catch (IOException | URISyntaxException ex) {
          ex.printStackTrace();
        }
      }
    });
    textPnl.setText(getHtml(application.getAboutFilePath()));
    add(new JScrollPane(textPnl), BorderLayout.CENTER);

    setActionListener(new DefaultDialogController<>(this));

    pack();
    setLocationRelativeTo(owner);
  }

  private String getHtml(String filePath) {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)))) {
      String line;
      StringJoiner sj = new StringJoiner("\n");

      while ((line = br.readLine()) != null) {
        sj.add(line);
      }

      return sj.toString();
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
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
