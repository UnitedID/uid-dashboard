/*
 * Copyright (c) 2011 - 2013 United ID.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.groovy.grails.plugins.quartz.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

/**
 * JobListener implementation which binds Hibernate Session to thread before
 * execution of job and flushes it after job execution.
 *
 * @author Sergey Nebolsin (nebolsin@gmail.com)
 *
 * @since 0.2
 */
public class SessionBinderJobListener extends JobListenerSupport
{
  private static final transient Log LOG = LogFactory.getLog(SessionBinderJobListener.class);

  public static final String NAME = "sessionBinderListener";

  public String getName() {
      return NAME;
  }

  @Override
  public void jobToBeExecuted(final JobExecutionContext context) {
  }

  @Override
  public void jobWasExecuted(final JobExecutionContext context, final JobExecutionException exception) {
  }
}
