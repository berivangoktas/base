package com.junited.selenium.utils;

import org.apache.commons.lang3.StringUtils;

public class Link
{
    String url;
    String expectedUrl;

    public Link(String url, String expectedUrl)
    {
        this.url = url;
        this.expectedUrl = expectedUrl;
    }

    public String getUrl()
    {
        return url;
    }

    public String getExpectedUrl()
    {
        return expectedUrl;
    }

    @Override
    public String toString()
    {
        return "Link{" +
                "url='" + url + '\'' +
                (StringUtils.equals(url, expectedUrl) ? "" : ", expectedUrl='" + expectedUrl + '\'') +
                '}';
    }
}