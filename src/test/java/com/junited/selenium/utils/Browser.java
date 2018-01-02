package com.junited.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;


public class Browser implements WebDriver
{
    private static final Logger logger = LoggerFactory.getLogger(Browser.class);
    private static final int DEFAULT_WAIT_TIMEOUT = 45;

    private WebDriver webDriver;
    private WebDriverWait wait;

    //--

    private TestContext context;

    //-----

    public Browser(TestContext context, WebDriver driver)
    {
        this.context = context;
        this.webDriver = driver;
        wait = new WebDriverWait(webDriver, DEFAULT_WAIT_TIMEOUT);
    }

    public TestContext getContext()
    {
        return context;
    }

    public void setContext(TestContext context)
    {
        this.context = context;
    }

    public WebDriver getWebDriver()
    {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver)
    {
        this.webDriver = webDriver;
    }

    public void get(String s)
    {
        getWebDriver().get(s);
    }

    public void get(UrlFactory urlFactory)
    {
        get(urlFactory.pageUrl);
    }

    public String getCurrentUrl()
    {
        return getWebDriver().getCurrentUrl();
    }

    public String getTitle()
    {
        return getWebDriver().getTitle();
    }

    public List<WebElement> findElements(By by)
    {
        return getWebDriver().findElements(by);
    }

    public WebElement findElement(By by)
    {
        return getWebDriver().findElement(by);
    }

    public String getPageSource()
    {
        return getWebDriver().getPageSource();
    }

    public void close()
    {
        getWebDriver().close();
    }

    public void quit()
    {
        getWebDriver().quit();
    }

    public Set<String> getWindowHandles()
    {
        return getWebDriver().getWindowHandles();
    }

    public String getWindowHandle()
    {
        return getWebDriver().getWindowHandle();
    }

    public TargetLocator switchTo()
    {
        return getWebDriver().switchTo();
    }

    public Navigation navigate()
    {
        return getWebDriver().navigate();
    }

    public Options manage()
    {
        return getWebDriver().manage();
    }
    public void scrollToElement(WebElement element)
    {
        try
        {
            int yScrollPosition = element.getLocation().getY() - 200;
            JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
            js.executeScript("window.scroll(0, " + yScrollPosition + ");");
        }
        catch (Exception e)
        {
            logger.info("Could not scrool to element...");
        }
    }

    public void waitForAjax()
    {
        try
        {
            AjaxWaiter.waitForAjaxLoad(this);
        }
        catch (Exception e)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public void waitForAjaxAngular()
    {
        try
        {
            AjaxWaiter.waitForAjaxLoadAngular(this);
        }
        catch (Exception e)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public void waitAndClick(WebElement element)
    {
        waitUntilVisibilityOf(element);
        element.click();
    }

    public void waitAndSendKeys(WebElement element, String keys)
    {
        waitUntilVisibilityOf(element);
        element.sendKeys(keys);
    }

    public void waitAndClearSendKeys(WebElement element, String keys)
    {
        waitUntilVisibilityOf(element);
        element.clear();
        element.sendKeys(keys);
    }


    public void waitUntilVisibilityOf(WebElement element)
    {
        waitForAjax();
        waitForAjaxAngular();

        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitAndSendKeysSlowly(WebElement element, String keys)
    {
        waitUntilVisibilityOf(element);

        for (char c : keys.toCharArray())
        {
            element.sendKeys(String.valueOf(c));
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }



}
