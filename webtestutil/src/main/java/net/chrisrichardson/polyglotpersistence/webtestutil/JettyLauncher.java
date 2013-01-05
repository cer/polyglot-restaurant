package net.chrisrichardson.polyglotpersistence.webtestutil;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Assert;

import java.io.File;

public class JettyLauncher {

  private Server server;
  private String contextPath;
  private int port;
  private String srcWebApp;

  public void start() {
    server = new Server(8080);
    WebAppContext context = new WebAppContext();

    context.setDescriptor(srcWebApp + "/WEB-INF/web.xml");
    context.setResourceBase(srcWebApp);
    context.setContextPath(contextPath);
    context.setParentLoaderPriority(true);

    server.setHandler(context);
    
    System.out.println("Starting");

    try {
      server.start();
      // server.join();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    System.out.println("started");
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setSrcWebApp(String srcWebApp) {
    Assert.assertTrue("Should be directory: " + srcWebApp, new File(srcWebApp).isDirectory());
    this.srcWebApp = srcWebApp;
  }

  public void stop() {
  }

  public int getActualPort() {
    return 8080;
  }

  public void main(String[] args) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("implement me");
  }

  public String makeUrl(String suffix) {
    String s = String.format("http://localhost:%d%s/%s",
            getActualPort(), contextPath, suffix);
    System.out.println("url=" + s);
    return s;
  }
}
