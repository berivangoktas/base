package com.junited.selenium.utils;

public enum UrlFactory
{
    BASE_URL("https://www.enuygun.com/"),

    FREQUNTLY_ASKED_QUESTION(BASE_URL,"ucak-bileti/sikca-sorulan-sorular/"),

    FLIGHT_TICKET_PAGE(BASE_URL,"ucak-bileti/"),

    HOTEL_PAGE(BASE_URL,"otel/");
    //--

    public final String pageUrl;

    UrlFactory(String pageUrl)
    {
        this.pageUrl = pageUrl;
    }

    UrlFactory(UrlFactory baseUrl, String pageUrl)
    {
        this.pageUrl = baseUrl.pageUrl + pageUrl;
    }
}
