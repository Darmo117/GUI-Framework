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

import javax.swing.JLabel;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;

public class TestFrame extends ApplicationFrame {
  private static final long serialVersionUID = -7892388146063580320L;

  public TestFrame(WritableConfig config) {
    super(config, true, true, true);
  }

  @Override
  protected ApplicationController preInit(WritableConfig config, boolean hasMenuBar, boolean hasToolBar, boolean isFullyExtended) {
    return new ApplicationController(this, config);
  }

  @Override
  protected void initContent(ApplicationController controller, WritableConfig config, boolean hasMenuBar, boolean hasToolBar,
      boolean isFullyExtended) {
    getContentPanel().add(new JLabel("Label"));
  }
}
