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

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.gui_framework.util.ImagesUtil;

public class TestFrame extends ApplicationFrame<ApplicationController<TestFrame>> {
  private static final long serialVersionUID = -7892388146063580320L;

  public TestFrame(WritableConfig config) {
    super(config, true, true, true, false, new Dimension(800, 600), true);
  }

  @Override
  protected ApplicationController<TestFrame> preInit(WritableConfig config) {
    return new ApplicationController<TestFrame>(this, config);
  }

  @Override
  protected void initContent(ApplicationController<TestFrame> controller, WritableConfig config) {
    getContentPanel().add(new JLabel("Label"));
  }

  @Override
  protected JToolBar initJToolBar(Map<net.darmo_creations.gui_framework.events.UserEvent.Type, ActionListener> listeners) {
    JToolBar bar = super.initJToolBar(listeners);

    JButton b = new JButton(ImagesUtil.HELP);
    bar.add(b);

    return bar;
  }
}
