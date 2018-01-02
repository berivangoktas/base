package com.junited.selenium.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration
{
    private Properties configProps = new Properties();
    private BrowserType browserType;
    private String logDirectory;

    private String testRecordsDirectory;
    private String testScreenshotsDirectory;
    private String testHtmlSourcesDirectory;

    private boolean screenRecorderOption;
    private boolean screenshotCaptureOption;
    private boolean htmlCaptureOption;

    //-----

    public Configuration() throws IOException
    {
        loadConfigProperties();
        this.browserType = getBrowserProp();

        this.logDirectory = configProps.getProperty("test.log.directory");
        this.testRecordsDirectory = configProps.getProperty("test.record.output.directory");
        this.testScreenshotsDirectory = configProps.getProperty("test.screenshot.output.directory");
        this.testHtmlSourcesDirectory = configProps.getProperty("test.html.output.directory");
    }

    private void loadConfigProperties() throws IOException
    {
        String env = System.getProperties().getProperty("selenium_env");
        if (StringUtils.isBlank(env))
        {
            env = "pre_production";
            System.out.println("No ENV option is set, using " + env +
                    ", please set -Dselenium_env in java to override.");
        }

        String configFile = env + "_config.properties";
        InputStream in = ClassLoader.getSystemResourceAsStream(configFile);

        configProps.load(in);
    }

    private BrowserType getBrowserProp()
    {
        String browserType = System.getProperties().getProperty("webdriver.browser");
        return StringUtils.isBlank(browserType) ? BrowserType.findByName(configProps.getProperty("webdriver.browser")) : BrowserType.findByName(browserType);
    }

    public BrowserType getBrowserType()
    {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType)
    {
        this.browserType = browserType;
    }


    public String getLogDirectory()
    {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory)
    {
        this.logDirectory = logDirectory;
    }

    public String getTestRecordsDirectory()
    {
        return testRecordsDirectory;
    }

    public void setTestRecordsDirectory(String testRecordsDirectory)
    {
        this.testRecordsDirectory = testRecordsDirectory;
    }

    public String getTestScreenshotsDirectory()
    {
        return testScreenshotsDirectory;
    }

    public void setTestScreenshotsDirectory(String testScreenshotsDirectory)
    {
        this.testScreenshotsDirectory = testScreenshotsDirectory;
    }

    public String getTestHtmlSourcesDirectory()
    {
        return testHtmlSourcesDirectory;
    }

    public void setTestHtmlSourcesDirectory(String testHtmlSourcesDirectory)
    {
        this.testHtmlSourcesDirectory = testHtmlSourcesDirectory;
    }

    public boolean getHtmlCaptureOption()
    {
        return htmlCaptureOption;
    }

    public void setHtmlCaptureOption(boolean htmlCaptureOption)
    {
        this.htmlCaptureOption = htmlCaptureOption;
    }

    public boolean getScreenshotCaptureOption()
    {
        return screenshotCaptureOption;
    }

    public void setScreenshotCaptureOption(boolean screenshotCaptureOption)
    {
        this.screenshotCaptureOption = screenshotCaptureOption;
    }

    public boolean getScreenRecorderOption()
    {
        return screenRecorderOption;
    }

    public void setScreenRecorderOption(boolean screenRecorderOption)
    {
        this.screenRecorderOption = screenRecorderOption;
    }
}
