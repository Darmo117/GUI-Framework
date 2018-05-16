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

import java.util.Objects;

import net.darmo_creations.utils.events.EventsBus;

/**
 * Applications must register itself to this registry to be started. Only one application can be
 * registered.
 *
 * @author Damien Vergnet
 */
public final class ApplicationRegistry {
  /** Application's main event bus */
  public static final EventsBus EVENTS_BUS = new EventsBus();

  private static Application application;

  /**
   * Registers the application. This method can be called only once.
   */
  static void registerApplication(Application app) {
    if (application != null)
      throw new IllegalStateException("Application already regitered!");
    application = Objects.requireNonNull(app);
  }

  /**
   * Returns the application.
   * 
   * @return the registered application
   * @throws IllegalStateException if no application has been registered
   * @see ApplicationRegistry#registerApplication(Application)
   */
  public static Application getApplication() {
    if (application == null)
      throw new IllegalStateException("Application not registered!");
    return application;
  }

  private ApplicationRegistry() {}
}
