package io.silverware.demos.quickstarts.cdi;

import io.silverware.microservices.providers.camel.CamelMicroserviceProvider;
import io.silverware.microservices.providers.cdi.CdiMicroserviceProvider;
import io.silverware.microservices.util.BootUtil;
import io.silverware.microservices.util.Utils;

import org.testng.annotations.Test;

import javax.enterprise.inject.spi.BeanManager;

/**
 * @author Martin Večeřa <marvenec@gmail.com>
 */
public class CamelCdiMicroserviceTest {

   @Test
   public void testHello() throws Exception {
      final BootUtil bootUtil = new BootUtil();
      final Thread platform = bootUtil.getMicroservicePlatform(this.getClass().getPackage().getName(), CdiMicroserviceProvider.class.getPackage().getName(), CamelMicroserviceProvider.class.getPackage().getName());
      platform.start();

      BeanManager beanManager = null;
      while (beanManager == null) {
         beanManager = (BeanManager) bootUtil.getContext().getProperties().get(CdiMicroserviceProvider.BEAN_MANAGER);
         Thread.sleep(200);
      }

      Utils.sleep(10 * 60 * 1000);

      platform.interrupt();
      platform.join();
   }
}