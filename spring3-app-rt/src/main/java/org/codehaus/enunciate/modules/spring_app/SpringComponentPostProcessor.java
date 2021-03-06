/*
 * Copyright 2006-2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.enunciate.modules.spring_app;

import org.codehaus.enunciate.webapp.ComponentPostProcessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Component post processor for the spring application context.
 *
 * @author Ryan Heaton
 */
public class SpringComponentPostProcessor implements ServletContextListener, ComponentPostProcessor {

  private ServletContext context;

  public void contextInitialized(ServletContextEvent event) {
    this.context =  event.getServletContext();
    this.context.setAttribute(ComponentPostProcessor.class.getName(), this);
  }

  public void contextDestroyed(ServletContextEvent event) {
    //no-op
  }

  /**
   * Post process the specified component.
   *
   * @param component The component.
   */
  public void postProcess(Object component) {
    WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.context);
    if (applicationContext != null) {
      autowire(component, applicationContext);
    }
  }

  /**
   * Autowire the specified component.
   *
   * @param component the component.
   * @param applicationContext the application context to use to autowire the component.
   */
  public static void autowire(Object component, ApplicationContext applicationContext) {
    applicationContext.getAutowireCapableBeanFactory().autowireBean(component);
  }
}
