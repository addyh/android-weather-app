package ml.addy.sunshine;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ml.addy.sunshine.data.TestDb;
import ml.addy.sunshine.data.TestPractice;

//JUnit Suite Test
@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestDb.class, TestPractice.class
})

public class FullTestSuite extends TestSuite {
    public static Test suite() {
        return new TestSuite(TestDb.class, TestPractice.class);
    }

    public FullTestSuite() {
        super();
    }
}