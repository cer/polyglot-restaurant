package net.chrisrichardson.polyglotpersistence.availablerestaurantmanagement;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AvailableRestaurantManagementMain {

  public static void main(String[] args) {
    if (new CloudEnvironment().isCloudFoundry()) {
      // activate cloud profile
      System.setProperty("spring.profiles.active", "cloud");
    }
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/appctx/*.xml");
    System.out.println("Running");
  }
}
