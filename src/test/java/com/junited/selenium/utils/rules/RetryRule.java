package com.junited.selenium.utils.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RetryRule implements TestRule
{
    private int retryCount;

    public RetryRule(int retryCount)
    {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description)
    {
        return statement(base, description);
    }

    private Statement statement(final Statement base, final Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                Throwable caughtThrowable = null;

                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmm");

                // implement retryRule logic here
                for (int i = 0; i < retryCount; i++)
                {
                    try
                    {
                        base.evaluate();
                        return;
                    }
                    catch (Throwable t)
                    {
                        caughtThrowable = t;

                        String retryCountLog = String.format("%s\t%s\t%s\t%d-Run\n",
                                dateFormat.format(Calendar.getInstance().getTime()),
                                description.getTestClass().getSimpleName(),
                                description.getMethodName(),
                                (i + 1));

                        System.err.println(retryCountLog);
                        appendLogToFile(retryCountLog, "testMethodRetryLog.txt");
                    }
                }

                String retryLog = String.format("%s\t%s\t%s\t%d-Failures\n",
                        dateFormat.format(Calendar.getInstance().getTime()),
                        description.getTestClass().getSimpleName(),
                        description.getMethodName(),
                        retryCount);

                appendLogToFile(retryLog, "testMethodRetryLog.txt");
                System.err.println(retryLog);
                throw caughtThrowable;
            }
        };
    }

    private void appendLogToFile(String data, String fileName)
    {
        File baseDir = new File("/opt/junited/data/test");

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
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
