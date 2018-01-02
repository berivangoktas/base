package com.junited.selenium.test;

import com.junited.selenium.utils.Browser;
import com.junited.selenium.utils.Configuration;
import com.junited.selenium.utils.TestContext;
import com.junited.selenium.utils.rules.RetryRule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSeleniumTest
{

    protected static final int SELECT_DAY = 28;
    protected static final int DEFAULT_SLEEP = 2000;
    protected static final int DEFAULT_LONG_SLEEP = 7000;
    protected static final String[] city = {"İstanbul", "İzmir", "Adana", "Trabzon", "Antalya"};
    protected static final int CITY_SIZE = 4;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSeleniumTest.class);
    private static final int DEFAULT_TEST_TIMEOUT = (int) TimeUnit.MINUTES.toMillis(10);

    protected static TestContext context;
    protected static Browser browser;
    protected static Configuration configuration;

    private static ScreenRecorder screenRecorder;

    private long lStartTime;
    private long lEndTime;
    private TestStatus testStatus;

    //-----

    public RetryRule retryRule = new RetryRule(3);

    public Timeout timeoutRule = new Timeout(DEFAULT_TEST_TIMEOUT)
    {
        @Override
        public Statement apply(Statement base, Description description)
        {
            return description.getTestClass().getSimpleName().contains("SiteMap") || description.getTestClass().getSimpleName().contains("Analytics") ?
                    new FailOnTimeout(base, DEFAULT_TEST_TIMEOUT * 3) : new FailOnTimeout(base, DEFAULT_TEST_TIMEOUT);
        }
    };

    public TestRule statusLogRule = new TestWatcher()
    {
        @Override
        protected void starting(Description description)
        {
            lStartTime = System.currentTimeMillis();
            logger.info("\t==================================================");
            logger.info("\t====> TEST STARTED {}", getDisplayName(description));
            startScreenRecorder();
        }

        @Override
        public void succeeded(Description description)
        {
            testStatus = TestStatus.PASSED;
            logger.info("\t====> TEST PASSED {}", getDisplayName(description));
        }

        protected void skipped(AssumptionViolatedException e, Description description)
        {
            testStatus = TestStatus.SKIPPED;
            logger.info("\t====> TEST SKIPPED {}", getDisplayName(description));
        }

        @Override
        public void failed(Throwable e, Description description)
        {
            testStatus = TestStatus.FAILED;

            String displayName = getDisplayName(description);
            long fileId = System.currentTimeMillis();
            String fileName = fileId + "-" + displayName;

            saveScreenShot(fileName);
            savePageSource(fileName);

            logger.info("\t====> TEST FAILED {}", displayName);
        }

        @Override
        public void finished(Description description)
        {
            printStatus(description);
            stopScreenRecorder(testStatus.equals(TestStatus.PASSED) || testStatus.equals(TestStatus.SKIPPED));
        }
    };

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(statusLogRule).around(retryRule).around(timeoutRule);

    //--

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        context = new TestContext();
        configuration = context.getConfiguration();
        screenRecorder = context.getScreenRecorder();

        try
        {
            browser = context.doCreateBrowser();
        }
        catch (Exception e)
        {
            logger.info("browser re-created for test");
            e.printStackTrace();
            browser = context.doCreateBrowser();
        }

        Assert.assertNotNull("browser could not be initialized!", browser);

        browser.manage().window().setSize(new Dimension(1200, 800));
    }

    @AfterClass
    public static void tearDownClass() throws IOException
    {
        if (context != null)
            context.close();
    }

    //--SCREENSHOT-CAPTURE

    private void saveScreenShot(String fileName)
    {
        if (configuration.getScreenshotCaptureOption())
        {
            File dir = defineTargetDirectory(configuration.getTestScreenshotsDirectory());
            browser.manage().window().setSize(new Dimension(1200, 800));

            try
            {
                Thread.sleep(DEFAULT_SLEEP);
                String imagePath = dir + System.getProperty("file.separator") + fileName + ".png";

                File screenshotFile = ((TakesScreenshot) browser.getWebDriver()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshotFile, new File(imagePath));
                logger.info("Screenshot Image Path = [" + imagePath + "]");
            }
            catch (Exception e)
            {
                logger.info("Cannot take screenshot!!!");
                e.printStackTrace();
            }
        }
    }

    //--HTML-RESOURCE-CAPTURE

    private void savePageSource(String fileName)
    {
        if (configuration.getHtmlCaptureOption())
        {
            File dir = defineTargetDirectory(configuration.getTestHtmlSourcesDirectory());

            String capturedHtmlSource = browser.getPageSource();
            String htmlSourcePath = dir + System.getProperty("file.separator") + fileName + ".html";

            File newHtmlFile = new File(htmlSourcePath);
            try
            {
                FileUtils.writeStringToFile(newHtmlFile, capturedHtmlSource);
            }
            catch (IOException e)
            {
                logger.info("HTML source file path : [" + htmlSourcePath + "]");
                e.printStackTrace();
            }
        }
    }

    //--SCREEN-RECORD-CAPTURE

    private static void startScreenRecorder()
    {
        if (screenRecorder != null)
        {
            try
            {
                screenRecorder.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void stopScreenRecorder(boolean cleanRecords)
    {
        if (screenRecorder != null)
        {
            try
            {
                screenRecorder.stop();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            List<File> createdMovieFiles = screenRecorder.getCreatedMovieFiles();
            for (File movie : createdMovieFiles)
            {
                logger.info("New movie created: " + movie.getAbsolutePath());

                if (cleanRecords)
                {
                    logger.info("Created movies will be deleted!!!");

                    if (movie.delete())
                    {
                        logger.info(movie.getName() + " is deleted!");
                    }
                    else
                    {
                        logger.info(movie.getName() + " is not deleted!");
                    }
                }
                else
                {
                    File dir = defineTargetDirectory(configuration.getTestRecordsDirectory());
                    Path path = null;
                    try
                    {
                        path = Files.move(movie.toPath(), dir.toPath().resolve(movie.getName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    logger.info("Screen Record File Moved into " + path.toString());
                    movie.delete();
                }
            }
        }
    }

    //--LOG

    protected void appendLogToFile(String data, String fileName)
    {
        data = data.replaceAll(",", "");
        File baseDir = new File(configuration.getLogDirectory());

        if (!baseDir.isDirectory())
        {
            if (!baseDir.mkdir())
            {
                throw new RuntimeException("Failed to create base directory for data export: " +
                        baseDir.getAbsolutePath());
            }
        }

        try
        {
            File file = new File(baseDir, fileName);

            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();

            logger.info("\tAppend to : " + fileName + " Done");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void printStatus(Description description)
    {
        lEndTime = System.currentTimeMillis();
        long difference = lEndTime - lStartTime;
        long seconds = (difference / 1000) % 60;
        long minutes = (difference / (1000 * 60)) % 60;
        long hours = (difference / (1000 * 60 * 60)) % 24;

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmm");

        String testResult = String.format("\t====> TEST FINISHED %s in %d hours, %d minutes, %d seconds",
                getDisplayName(description), hours, minutes, seconds);

        String testResultLogFormat = String.format("%s\t%s\t%s\t%s\t%d\t%d\t%d\n",
                dateFormat.format(Calendar.getInstance().getTime()),
                description.getTestClass().getSimpleName(),
                description.getMethodName(),
                testStatus,
                hours,
                minutes,
                seconds);

        appendLogToFile(testResultLogFormat, "testMethodFinishedTime.txt");
        logger.info(testResult);
        logger.info("\t==================================================");
    }

    private static File defineTargetDirectory(String directoryName)
    {
        File dir;
        if (!StringUtils.isBlank(System.getenv("WORKSPACE")))
        {
            dir = new File(System.getenv("WORKSPACE") + System.getProperty("file.separator") + directoryName);
        }
        else
        {
            dir = new File(System.getProperty("user.home") + System.getProperty("file.separator") + directoryName);
        }

        //Create directory if does not exist
        if (dir.exists())
        {
            logger.info("A folder with name '" + directoryName + "' is already exist in the path ");
        }
        else
        {
            dir.mkdirs();
        }
        return dir;
    }

    private String getDisplayName(Description description)
    {
        return description.getMethodName() + " (" + description.getTestClass().getSimpleName() + ")";
    }
    //--

    protected WebElement getElementByText(List<WebElement> elements, String searchText)
    {
        for (WebElement we : elements)
        {
            if (searchText.equals(we.getText()))
            {
                return we;
            }
        }
        throw new NotFoundException("ElementNotFound:" + searchText);
    }

    //-----

    enum TestStatus
    {
        PASSED,
        FAILED,
        SKIPPED
    }
}
