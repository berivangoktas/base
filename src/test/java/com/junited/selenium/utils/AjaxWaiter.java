package com.junited.selenium.utils;

import org.openqa.selenium.JavascriptExecutor;

import java.util.concurrent.TimeUnit;

public class AjaxWaiter
{
    private static final String AJAX_WAIT_SCRIPT = "return typeof jQuery != 'undefined' && jQuery.active != 0";
    private static final String ANGULAR_WAIT_SCRIPT = "return angular.element(document).injector().get('$http').pendingRequests.length != 0";

    //-----

    public static void waitForAjaxLoad(Browser browser)
    {
        sleep(250L);
        JavascriptExecutor executor = (JavascriptExecutor) browser.getWebDriver();

        boolean stillRunningAjax = (Boolean) executor.executeScript(AJAX_WAIT_SCRIPT);

        int i = 0;
        while (stillRunningAjax && i < 20)
        {
            i++;
            sleep(TimeUnit.SECONDS.toMillis(1L));
            stillRunningAjax = (Boolean) executor.executeScript(AJAX_WAIT_SCRIPT);
        }
    }

    public static void waitForAjaxLoadAngular(Browser browser)
    {
        sleep(250L);
        JavascriptExecutor executor = (JavascriptExecutor) browser.getWebDriver();

        boolean stillRunningAjax = Boolean.valueOf(executor.executeScript(ANGULAR_WAIT_SCRIPT).toString());

        int i = 0;
        while (stillRunningAjax && i < 30)
        {
            i++;
            sleep(TimeUnit.SECONDS.toMillis(1L));
            stillRunningAjax = Boolean.valueOf(((JavascriptExecutor) browser.getWebDriver()).executeScript(ANGULAR_WAIT_SCRIPT).toString());
        }
    }

    private static void sleep(long durationInMillisecond)
    {
        try
        {
            Thread.sleep(durationInMillisecond);
        }
        catch (InterruptedException e1)
        {
            // noop
        }
    }
}
