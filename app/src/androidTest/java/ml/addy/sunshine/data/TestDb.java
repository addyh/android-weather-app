package ml.addy.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import junit.framework.TestCase;

import java.util.HashSet;

public class TestDb extends TestCase {

    public Context mContext = InstrumentationRegistry.getTargetContext();
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        if (mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME)) {
            Log.v("TEST","DB successfully deleted");
        } else {
            Log.v("TEST", "DB NOT DELETED!");
        }
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.
        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        deleteTheDatabase();

        // =============================
        // Create Database
        // =============================
        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();

        Log.v("TEST", "db created");

        assertEquals(true, db.isOpen());

        // Have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // =============================
        // Verify Tables Created
        // =============================

        Log.v("TEST", "Table Names:");

        // Verify that the tables have been created
        do {
            String tableName = c.getString(0);

            // Log the table names from the Cursor SQL query above
            Log.v("TEST", " - " + tableName);

            // Remove location and weather from the HashSet to verify those tables were created
            tableNameHashSet.remove(tableName);
        } while( c.moveToNext() );

        // If this fails, it means that the database doesn't contain both the location entry
        // and weather entry tables. HashSet should now be empty
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // =============================
        // Verify Location Table Columns
        // =============================

        // Does the location table contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        // Find the column index for the column names
        int columnNameIndex = c.getColumnIndex("name");
        Log.v("TEST", "Location Table Columns:");
        do {
            // Get the column name under the "name" column for this column
            String columnName = c.getString(columnNameIndex);
            Log.v("TEST", " - " + columnName);

            // Remove that column name from the HashSet to verify all columns were created
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // If this fails, it means that your database doesn't contain all of the required location
        // entry columns. HashSet should now be empty
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());

        // =============================
        // Verify Weather Table Columns
        // =============================

        // Does the weather table contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.WeatherEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> weatherColumnHashSet = new HashSet<String>();
        weatherColumnHashSet.add(WeatherContract.WeatherEntry._ID);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_LOC_KEY);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_DATE);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_HUMIDITY);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_PRESSURE);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED);
        weatherColumnHashSet.add(WeatherContract.WeatherEntry.COLUMN_DEGREES);

        // Find the column index for the column names
        columnNameIndex = c.getColumnIndex("name");
        Log.v("TEST", "Weather Table Columns:");
        do {
            // Get the column name under the "name" column for this column
            String columnName = c.getString(columnNameIndex);
            Log.v("TEST", " - " + columnName);

            // Remove that column name from the HashSet to verify all columns were created
            weatherColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // If this fails, it means that your database doesn't contain all of the required location
        // entry columns. HashSet should now be empty
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                weatherColumnHashSet.isEmpty());

        // Close db connection
        c.close();
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testLocationTable() {
        insertLocation();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        long locationRowId = insertLocation();

        // Make sure we have a valid row ID.
        assertFalse("Error: Location Not Inserted Correctly", locationRowId == -1L);

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Weather): Create weather values
        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);

        // Third Step (Weather): Insert ContentValues into database and get a row ID back
        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from location query", weatherCursor.moveToFirst() );

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
                weatherCursor, weatherValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from weather query",
                weatherCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        weatherCursor.close();
        dbHelper.close();
    }

    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
        cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return locationRowId;
    }
}