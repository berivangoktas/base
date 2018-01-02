package com.junited.selenium.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LinkSet
{
    private String baseUrl;
    private List<Link> links = new ArrayList<Link>();

    public LinkSet(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public Link addLink(String url)
    {
        return addLink(url, url);
    }

    public Link addLink(String url, String expectedUrl)
    {
        url = StringUtils.startsWith(url, "http") || StringUtils.startsWith(url, "https") ? url : baseUrl + url;
        expectedUrl = StringUtils.startsWith(expectedUrl, "http") ? expectedUrl : baseUrl + expectedUrl;
        Link link = new Link(url, expectedUrl);
        links.add(link);
        return link;
    }

    public List<Link> getLinks()
    {
        return links;
    }

    public Link findLink(String absoluteUrl)
    {
        for (Link link : links)
        {
            if (StringUtils.equals(absoluteUrl, link.url))
            {
                return link;
            }
        }
        return null;
    }

    public Link findLinkMatches(String url)
    {
        for (Link link : links)
        {
            if (StringUtils.contains(url, link.url))
            {
                return link;
            }
        }
        return null;
    }
}