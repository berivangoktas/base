package com.junited.selenium.utils;

public enum BrowserType
{
    INTERNET_EXPLORER("iexplorer"),
    CHROME("chrome"),
    FIREFOX("firefox");

    //--

    private String name;

    BrowserType(String name)
    {
        this.name = name;
    }

    //-----

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public static BrowserType findByName(String name)
    {
        for (BrowserType browser : values())
        {
            if (browser.getName().equals(name))
            {
                return browser;
            }
        }

        return null;
    }
}
