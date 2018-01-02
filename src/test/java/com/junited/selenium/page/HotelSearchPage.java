package com.junited.selenium.page;

import com.junited.selenium.utils.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class HotelSearchPage extends PageObject<HotelSearchPage>
{
    public HotelSearchPage(Browser browser)
    {
        super(browser);
    }

    @FindBy(xpath = "//div[@class='panel-heading']/h2[text()='Popüler Tatil Bölgeleri']")
    public WebElement popularHolidayAreaText;

    @FindBy(xpath = "//span[@class='city pull-left']")
    public List<WebElement> popularHolidayArea;

    @FindBy(id = "checkin-checkout-date")
    public WebElement checkInCheckOutDate;

    @FindBy(xpath = "//div[@class='day toMonth  valid']")
    public List<WebElement> selectDepartureDay;

    @FindBy(xpath = "//div[@class='day toMonth  valid tmp']")
    public List<WebElement> selectReturnDay;

    @FindBy(id = "room-guest")
    public WebElement roomQuest;

    @FindBy(xpath = "//i[@class='fa fa-plus-circle']")
    public List<WebElement> plusButton;

    @FindBy(xpath = "//button[text()='Kapat']")
    public WebElement closeButton;

    @FindBy(xpath = "//button[@class='btn btn-success btn-search-hotel pull-right']")
    public WebElement searchHotelButton;

    @FindBy(xpath = "//div[@class='eu-multiple-map']")
    public WebElement map;

    @FindBy(xpath = "//h2/a[@class='otel-name']")
    public List<WebElement> hotelNameList;

    @FindBy(xpath = "//div[@id='date-select-message']")
    public WebElement enterDatesForBestPricesText;

    @FindBy(xpath = "//li[@class='list-group-item clearfix']")
    public List<WebElement> searchRoomList;

    @FindBy(xpath = "//a[@class='btn btn-default eu-btn-booking']")
    public List<WebElement> roomDetailShowButtonList;

    @FindBy(id = "otel-region")
    public WebElement hotelRegion;

    @FindBy(id = "sort-list")
    public WebElement sortList;

    @FindBy(xpath = "//i[@class='fa fa-caret-down']/parent::div[@id='result-sort-box']")
    public List<WebElement> sortListOptionButton;

    @FindBy(xpath = "//ul[@id='sort-list']/li")
    public List<WebElement> sortListOptionText;

    @FindBy(xpath = "//a[@class='btn btn-success choice-btn'][text()='Göster ']")
    public List<WebElement> showButton;
}
