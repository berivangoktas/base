package com.junited.selenium.page;

import com.junited.selenium.utils.Browser;
import com.junited.selenium.utils.UrlFactory;
import org.openqa.selenium.support.PageFactory;

public abstract class PageObject<T extends PageObject>
{
    protected Browser browser;

    //-----

    protected PageObject(Browser browser)
    {
        this(browser, true);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        PageFactory.initElements(browser, this);
    }

    protected PageObject(Browser browser, boolean navigate)
    {
        this.browser = browser;
        if (navigate)
        {
            navigateToPage(browser);
        }

        PageFactory.initElements(browser, this);
    }

    //--

    protected String getBaseUrl()
    {
        return UrlFactory.BASE_URL.pageUrl;
    }

    public String getPageUrl()
    {
        return browser.getCurrentUrl();
    }

    protected void navigateToPage(Browser browser)
    {
        String url = getPageUrl();
        if (!browser.getCurrentUrl().contains(url))
        {
            browser.get(url);
        }
    }
}
