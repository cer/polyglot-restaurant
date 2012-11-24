package net.chrisrichardson.polyglotpersistence.restauranteventpublisher;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RestaurantEventPublisherMain {

  public static void main(String[] args) {
    if (new CloudEnvironment().isCloudFoundry()) {
      // activate cloud profile
      System.setProperty("spring.profiles.active", "cloud");
    }
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/appctx/*.xml");
  }
}
