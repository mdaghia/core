/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.tests.decorators.genericobserver;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.weld.tests.category.Broken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * 
 *  5.5.6: "Invoke the observer method on the resulting instance, if any, as a business method invocation, 
 *  		as defined in Section 7.2, “Container invocations and interception”.
 *  
 *  7.2  : "Invocations of producer, disposer and observer methods by the container are business method invocations 
 *  		and are intercepted by method interceptors and decorators."
 *  
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 *
 */
@RunWith(Arquillian.class)
public class DecorateGenericObserverTest
{
   @Deployment
   public static Archive<?> deploy()
   {
      return ShrinkWrap.create(BeanArchive.class)
                  .decorate(ServiceDecorator.class)
                  .addPackage(DecorateGenericObserverTest.class.getPackage());
   }

   @Inject
   private Event<Dog> dogEvent;

   /*
    * description = "WELD-579"
    */
   @Category(Broken.class)
   @Test
   public void shouldInvokeDecoratorsWhenObservingGenericEvents()
   {
      ServiceImpl.invocationCount = 0;
      ServiceDecorator.invocationCount = 0;
      
      dogEvent.fire(new Dog());
      
      Assert.assertEquals(1, ServiceImpl.invocationCount);
      Assert.assertEquals(1, ServiceDecorator.invocationCount);
   }
}