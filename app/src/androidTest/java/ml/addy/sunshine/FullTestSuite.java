package ml.addy.sunshine;

import android.support.annotation.NonNull;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ml.addy.sunshine.data.TestDb;
import ml.addy.sunshine.data.TestPractice;
import ml.addy.sunshine.data.TestProvider;
import ml.addy.sunshine.data.TestUriMatcher;
import ml.addy.sunshine.data.TestWeatherContract;

//JUnit Suite Test
@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestDb.class,
        TestPractice.class,
        TestProvider.class,
        TestUriMatcher.class,
        TestWeatherContract.class,
        TestFetchWeatherTask.class
})

public class FullTestSuite extends TestSuite {
    @NonNull
    public static TestSuite suite() {
        return new TestSuite(
                TestDb.class,
                TestPractice.class,
                TestProvider.class,
                TestUriMatcher.class,
                TestWeatherContract.class,
                TestFetchWeatherTask.class
        );
    }

    public FullTestSuite() {
        super();
    }
}