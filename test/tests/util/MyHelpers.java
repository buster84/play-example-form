package tests.util;

import play.*;

import play.mvc.*;
import play.api.test.Helpers$;
import play.libs.*;
import play.libs.F.*;
import play.test.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.htmlunit.*;
import org.openqa.selenium.phantomjs.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Capabilities;


import java.util.*;

/**
 * Helper functions to run tests.
 */
public class MyHelpers extends play.test.Helpers {

    public static Class<? extends WebDriver> PHAMTOMJS = PhantomJSDriver.class;

    public static DesiredCapabilities getDesiredCapabilities() {
         DesiredCapabilities sCaps = new DesiredCapabilities();
         String phantomjsPath = null;

         if(null != System.getenv("PHANTOMJS_PATH")){
           phantomjsPath = System.getenv("PHANTOMJS_PATH");
         } else {
           phantomjsPath = System.getProperty("PHANTOMJS_PATH");
         }

         sCaps.setJavascriptEnabled(true);
         sCaps.setCapability("takesScreenshot", true);
         sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjsPath);

         // Disable "web-security", enable all possible "ssl-protocols" and "ignore-ssl-errors" for PhantomJSDriver
         sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{
           "--web-security=false",
           "--ssl-protocol=any",
           "--ignore-ssl-errors=true"
         });
         return sCaps;
    }

    /**
     * Executes a block of code in a running server, with a test browser.
     */
    public static synchronized void running(TestServer server, Class<? extends WebDriver> webDriver, Capabilities sCaps, final Callback<TestBrowser> block) {
        TestBrowser browser = null;
        TestServer startedServer = null;
        try {
            start(server);
            startedServer = server;
            browser = testBrowser(webDriver, sCaps);
            block.invoke(browser);
        } catch(Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if(browser != null) {
                browser.quit();
            }
            if(startedServer != null) {
                stop(startedServer);
            }
        }
    }

    /**
     * Creates a Test Browser.
     */
    public static TestBrowser testBrowser(Class<? extends WebDriver> webDriver, Capabilities sCaps) {
        return testBrowser(webDriver, Helpers$.MODULE$.testServerPort(), sCaps);
    }
    /**
     * Creates a Test Browser.
     */
    public static TestBrowser testBrowser(Class<? extends WebDriver> webDriver, int port, Capabilities sCaps) {
        try {
            WebDriver webDriverInstance = webDriver.getDeclaredConstructor(Capabilities.class).newInstance(sCaps);
            return new TestBrowser(webDriverInstance, "http://localhost:" + port);
        } catch(RuntimeException e) {
            throw e;
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
