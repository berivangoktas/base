package com.junited.selenium.test;

import com.junited.selenium.page.HotelSearchPage;
import com.junited.selenium.utils.TestContext;
import com.junited.selenium.utils.UrlFactory;
import com.sun.org.glassfish.gmbal.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Random;

public class HotelSearchTest extends AbstractSeleniumTest
{
    private static final Logger logger = LoggerFactory.getLogger(TestContext.class);
    private HotelSearchPage hotelSearchPage;

    @Before
    public void setUp()
    {
        hotelSearchPage = new HotelSearchPage(browser);

        browser.get(UrlFactory.HOTEL_PAGE);
    }

    @Description("Popüler tatil beldeleirnden birinin seçilip, doğru sayfaya gittiği kontrolü CASE - 3 ")
    @Test
    public void testGoPopularHolidayPage() throws InterruptedException
    {

        browser.scrollToElement(hotelSearchPage.popularHolidayAreaText);
        int popularHolidayAreaIndex = new Random().nextInt(hotelSearchPage.popularHolidayArea.size());
        String popularHolidayAreaName = hotelSearchPage.popularHolidayArea.get(popularHolidayAreaIndex).getText().toLowerCase();

        browser.waitAndClick(hotelSearchPage.popularHolidayArea.get(popularHolidayAreaIndex));
        Thread.sleep(DEFAULT_SLEEP);
        Assert.assertTrue(browser.getCurrentUrl().contains(popularHolidayAreaName));
    }

    @Description("Popüler tatil beldeleirnden random birinin seçilip, çıkan otel sonuçlarından random birine tıklanıp, otel aramsı yapılması CASE - 4 ")
    @Test
    public void searchHotelAndHotelRoom() throws InterruptedException
    {
        randomSelectPapularHolidayArea();

        browser.scrollToElement(hotelSearchPage.map);
        int hotelNameIndex = new Random().nextInt(hotelSearchPage.hotelNameList.size());
        browser.waitAndClick(hotelSearchPage.hotelNameList.get(hotelNameIndex));

        changeTab(1);

        browser.scrollToElement(hotelSearchPage.enterDatesForBestPricesText);

        selectDay();

        selectRoomQuest();

        browser.waitAndClick(hotelSearchPage.searchHotelButton);
        Thread.sleep(DEFAULT_LONG_SLEEP);

        try
        {
            int roomIndex = new Random().nextInt(hotelSearchPage.searchRoomList.size());
            browser.waitAndClick(hotelSearchPage.roomDetailShowButtonList.get(roomIndex));
            changeTab(2);
            Assert.assertTrue(browser.getCurrentUrl().contains("yonlendir"));

        }
        catch (Exception e)
        {
            logger.error("not find a room in the selected criteria");
        }
    }

    @Description("Otel sayfasından fiyata göre artan seçilerek arama yapılıp, ilk otele tıklanarak sayfanın yönlendirildiği kontrolü CASE - 5")
    @Test
    public void searchHotelRisingPrice() throws InterruptedException
    {
        selectDay();
        selectRoomQuest();
        browser.waitAndClick(hotelSearchPage.searchHotelButton);
        Thread.sleep(DEFAULT_LONG_SLEEP);
        browser.waitAndClick(hotelSearchPage.sortListOptionButton.get(1));
        browser.waitAndClick(hotelSearchPage.sortListOptionText.get(3));
        Thread.sleep(DEFAULT_LONG_SLEEP);
        browser.waitAndClick(hotelSearchPage.showButton.get(0));
        changeTab(1);
        Assert.assertTrue(browser.getCurrentUrl().contains("yonlendir"));
    }

    //-----
    private void randomSelectPapularHolidayArea() throws InterruptedException
    {
        browser.scrollToElement(hotelSearchPage.popularHolidayAreaText);
        int popularHolidayAreaIndex = new Random().nextInt(hotelSearchPage.popularHolidayArea.size());
        browser.waitAndClick(hotelSearchPage.popularHolidayArea.get(popularHolidayAreaIndex));
        Thread.sleep(DEFAULT_SLEEP);
    }

    private void changeTab(int tabIndex)
    {
        ArrayList<String> tab = new ArrayList<String>(browser.getWindowHandles());
        browser.switchTo().window(tab.get(tabIndex));
    }

    private void selectDay()
    {
        browser.waitAndClick(hotelSearchPage.checkInCheckOutDate);
        int entryDayIndex = new Random().nextInt(hotelSearchPage.selectDepartureDay.size());
        browser.waitAndClick(hotelSearchPage.selectDepartureDay.get(entryDayIndex));
        int exitDayIndex = new Random().nextInt(hotelSearchPage.selectReturnDay.size());
        browser.waitAndClick(hotelSearchPage.selectReturnDay.get(exitDayIndex));
    }

    private void selectRoomQuest()
    {

        browser.waitAndClick(hotelSearchPage.roomQuest);
        browser.waitAndClick(hotelSearchPage.plusButton.get(0));
        browser.waitAndClick(hotelSearchPage.closeButton);
    }
}
