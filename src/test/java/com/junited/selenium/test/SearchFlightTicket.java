package com.junited.selenium.test;

import com.junited.selenium.page.SearchFlightTicketPage;
import com.junited.selenium.utils.UrlFactory;
import com.sun.org.glassfish.gmbal.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

public class SearchFlightTicket extends AbstractSeleniumTest
{
    private SearchFlightTicketPage searchFlightTicketPage;

    @Before
    public void setUp()
    {
        searchFlightTicketPage = new SearchFlightTicketPage(browser);
    }

    @Test
    public void testAllWonderAboutLinkContol() throws InterruptedException
    {
        browser.get(UrlFactory.FREQUNTLY_ASKED_QUESTION);
        int topicNumber = 1;
        String[] allWonderBottomAboutLinkArray = new String[30];
        String allWonderBottomAboutLinkTextArray[] = new String[30];
        WebElement allWonderBottomAboutLinkTittle;

        for (int allWonderAboutLink = 0; allWonderAboutLink < searchFlightTicketPage.allWonderAboutLink.size(); allWonderAboutLink++)
        {
            Thread.sleep(DEFAULT_SLEEP);
            browser.get(UrlFactory.FREQUNTLY_ASKED_QUESTION);
            browser.scrollToElement(searchFlightTicketPage.allWonderAboutLink.get(searchFlightTicketPage.allWonderAboutLink.size() - 3));
            browser.waitAndClick(searchFlightTicketPage.allWonderAboutLink.get(allWonderAboutLink));

            String allWonderBottomAboutCssSelector = "//div[@id='topic" + topicNumber + "']/a[@class='nav-link']/small";
            List<WebElement> allWonderBottomAbout = browser.findElements(By.xpath(allWonderBottomAboutCssSelector));

            String allWonderBottomAboutLinkCssSelector = "//div[@id='topic" + topicNumber + "']/a";
            List<WebElement> allWonderBottomAboutLink = browser.findElements(By.xpath(allWonderBottomAboutLinkCssSelector));

            Thread.sleep(DEFAULT_SLEEP);
            for (int allWonderBottomAboutLinkCounter = 0; allWonderBottomAboutLinkCounter <  allWonderBottomAbout.size(); allWonderBottomAboutLinkCounter++)
            {
                if (allWonderBottomAboutLinkCounter == 0)
                {
                    for (int k = 0; k < allWonderBottomAboutLink.size(); k++)
                    {
                        allWonderBottomAboutLinkArray[k] = allWonderBottomAboutLink.get(k).getAttribute("href");
                    }

                    for (int m = 0; m <  allWonderBottomAbout.size(); m++)
                    {

                        allWonderBottomAboutLinkTextArray[m] =  allWonderBottomAbout.get(m).getText();
                    }
                }

                Thread.sleep(DEFAULT_SLEEP);
                allWonderBottomAboutLinkTittle = browser.findElement(By.xpath("//div[@id='topic" + topicNumber + "']/a[@title='"+allWonderBottomAboutLinkTextArray[allWonderBottomAboutLinkCounter].substring(2)+"']"));
                browser.waitAndClick(allWonderBottomAboutLinkTittle);

                Assert.assertTrue(browser.getCurrentUrl().equals(allWonderBottomAboutLinkArray[allWonderBottomAboutLinkCounter]));

                browser.get(UrlFactory.FREQUNTLY_ASKED_QUESTION);
                browser.scrollToElement(searchFlightTicketPage.allWonderAboutLink.get(searchFlightTicketPage.allWonderAboutLink.size() - 3));
                browser.waitAndClick(searchFlightTicketPage.allWonderAboutLink.get(allWonderAboutLink));
                Thread.sleep(DEFAULT_SLEEP);
            }
            topicNumber = topicNumber + 1;
        }

    }

    @Description("Farklı iller seçerek uçak bileti arama testi CASE - 2")
    @Test
    public void testSearchFlightTİcket() throws InterruptedException
    {
        browser.get(UrlFactory.FLIGHT_TICKET_PAGE);
        int cityIndex[] = createRandomCityDifferentTwoIndex();

        enterCityName(searchFlightTicketPage.flightTicketWhere, city[cityIndex[0]]);
        enterCityName(searchFlightTicketPage.flightTicketDestination, city[cityIndex[1]]);

        selectDate(searchFlightTicketPage.departureCalendar, searchFlightTicketPage.departureSelectDays);
        selectDate(searchFlightTicketPage.returnCalendar, searchFlightTicketPage.returnSelectDays);

        browser.waitAndClick(searchFlightTicketPage.flightDirectCheckBox);

        browser.waitAndClick(searchFlightTicketPage.selectPassenger);
        browser.scrollToElement(searchFlightTicketPage.flightDirectCheckBox);
        browser.waitAndClick(searchFlightTicketPage.flightClassSelect);
        selectionValue(searchFlightTicketPage.flightClassSelect, "business");

        browser.waitAndClick(searchFlightTicketPage.okButton);
        Thread.sleep(DEFAULT_SLEEP);
        browser.waitAndClick(searchFlightTicketPage.findCheapTicketButton);
        Thread.sleep(DEFAULT_SLEEP);

        Assert.assertTrue(searchFlightTicketPage.selectPassenger.getText().contains("Business"));
        Assert.assertTrue(searchFlightTicketPage.searchResaultPageDepartureCity.getAttribute("data-label").contains(city[cityIndex[0]]));
        Assert.assertTrue(searchFlightTicketPage.searchResaultPageReturnCity.getAttribute("data-label").contains(city[cityIndex[1]]));
    }

    @Description("nereden nereye alanları boş girildiğinde uyarı verildiği kontrolü")
    @Test
    public void testWarninText() throws InterruptedException
    {
        browser.get(UrlFactory.FLIGHT_TICKET_PAGE);
        browser.waitAndSendKeysSlowly(searchFlightTicketPage.flightTicketWhere, "");
        browser.waitAndSendKeys(searchFlightTicketPage.flightTicketDestination, "");
        browser.waitAndClick(searchFlightTicketPage.findCheapTicketButton);
        Assert.assertEquals(searchFlightTicketPage.warningText.size(), 2);
    }

    //-------

    private int[] createRandomCityDifferentTwoIndex()
    {
        int cityIndexDeparture = new Random().nextInt(CITY_SIZE);
        int cityIndexReturn = new Random().nextInt(CITY_SIZE);

        while (cityIndexDeparture == cityIndexReturn)
        {
            cityIndexReturn = new Random().nextInt(CITY_SIZE);
        }
        int cityIndex[] = {cityIndexDeparture, cityIndexReturn};
        return cityIndex;
    }

    private void selectionValue(WebElement element, String text)
    {
        Select select = new Select(element);
        select.selectByValue(text);
        browser.waitForAjaxAngular();
    }

    private void selectDate(WebElement element, List<WebElement> selectDaysElement)
    {
        browser.waitAndClick(element);
        int selectDays = new Random().nextInt(SELECT_DAY);
        browser.waitAndClick(selectDaysElement.get(selectDays));
    }

    private void enterCityName(WebElement element, String cityName) throws InterruptedException
    {
        browser.waitAndSendKeysSlowly(element, cityName);
        element.sendKeys(Keys.ENTER);
        Thread.sleep(DEFAULT_SLEEP);
    }
}
