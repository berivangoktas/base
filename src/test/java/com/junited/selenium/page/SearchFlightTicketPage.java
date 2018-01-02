package com.junited.selenium.page;

import com.junited.selenium.utils.Browser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SearchFlightTicketPage extends PageObject<SearchFlightTicketPage>
{
    public SearchFlightTicketPage(Browser browser)
    {
        super(browser);
    }

    @FindBy(xpath = "//a[@class='nav-link btn-hidden-box black']")
    public List<WebElement> allWonderAboutLink;

    @FindBy(xpath = "//div[@id='topic1']/a[@class='nav-link']/small")
    public List<WebElement> allWonderAboutBottomLink;

    @FindBy(className = "scrolldown_ad_close")
    public WebElement closePopUP;

    @FindBy(xpath = "//input[@class='form-control trip-autocomplete originSelector ui-autocomplete-input']")
    public WebElement flightTicketWhere;

    @FindBy(xpath = "//input[@class='form-control trip-autocomplete destinationSelector ui-autocomplete-input']")
    public WebElement flightTicketDestination;

    @FindBy(xpath = "//div[@class='popover-content']")
    public List<WebElement> warningText;

    @FindBy(xpath = "//button[@class='btn btn-success form-control search-flight-btn']")
    public WebElement findCheapTicketButton;

    @FindBy(xpath = "//td[@data-handler='selectDay']")
    public List<WebElement> departureSelectDays;

    @FindBy(xpath = "//td[@data-handler='selectDay']")
    public List<WebElement> returnSelectDays;

    @FindBy(id = "departure-date-input")
    public WebElement departureCalendar;

    @FindBy(id = "return-date-input")
    public WebElement returnCalendar;

    @FindBy(id = "flight-direct")
    public WebElement flightDirectCheckBox;

    @FindBy(id = "select-passenger-btn")
    public WebElement selectPassenger;

    @FindBy(id = "flight-class-select")
    public WebElement flightClassSelect;

    @FindBy(xpath = "//button[@class='btn btn-link text-success passenger-selector-close']")
    public WebElement okButton;

    @FindBy(xpath = "//input[@id='from-input']")
    public WebElement searchResaultPageDepartureCity;

    @FindBy(xpath = "//input[@id='to-input']")
    public WebElement searchResaultPageReturnCity;

}
