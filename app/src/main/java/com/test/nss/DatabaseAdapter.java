package com.test.nss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.test.nss.Helper.isFirst;

public class DatabaseAdapter {
    protected static final String TAG = "DataAdapter";

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Calendar calobj = Calendar.getInstance();
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DatabaseAdapter(Context context) {
        mDbHelper = new DataBaseHelper(context);
    }

    public DatabaseAdapter createDatabase() {
        try {
            mDbHelper.createDataBase();
        } catch (SQLException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void insertHelpData(String clgName,
                               String post, String name, String email,
                               String contact, String entryYear) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CollegeName", clgName);
            contentValues.put("Post", post);
            contentValues.put("Name", name);
            contentValues.put("EmailID", email);
            contentValues.put("Contact", contact);
            contentValues.put("Entry_year", entryYear);
            mDb.insert("Help", null, contentValues);
            //Log.i(TAG, "Entered data");

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampDetails(String clgName, String campFrom,
                                  String campTo, String campVen,
                                  String campPost, String campTal, String campDist) {

        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("College_name", clgName);
            contentValues.put("Camp_from", campFrom);
            contentValues.put("Camp_to", campTo);
            contentValues.put("Camp_venue", campVen);
            contentValues.put("Camp_post", campPost);
            contentValues.put("Camp_taluka", campTal);
            contentValues.put("Camp_district", campDist);
            mDb.insert("CampDetails", null, contentValues);

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getCampDetails() {
        try {
            String sql = "SELECT * FROM CampDetails";
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    /*public Cursor getClgList() {
        try {
            String sql = "SELECT CollegeName FROM CollegeNames";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur.getCount() == 0) {
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }*/

    public Cursor getClgState(String clgName) {
        try {
            String sql = String.format("SELECT * FROM CollegeNames WHERE CollegeName= \"%s\"", clgName);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void insertClgList(String clgId, String clgName, String state) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("College_id", clgId);
            contentValues.put("CollegeName", clgName);
            contentValues.put("State", state);

            mDb.insert("CollegeNames", null, contentValues);

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getCampId(String campActName) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT CampId FROM CampActivityList WHERE CampActivityName=\"%s\"", campActName);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void insertCampActList(String campActName, String campId) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CampActivityName", campActName);
            contentValues.put("CampId", campId);

            mDb.insert("CampActivityList", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampActListAll(int id, String clgName, String state,
                                     String campActTitle, String campActDec,
                                     String day, String vec, int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("College_Name", clgName);
            contentValues.put("State", state);

            contentValues.put("CampActivityTitle", campActTitle);
            contentValues.put("CampActivityDescription", campActDec);
            contentValues.put("CampDay", day);
            contentValues.put("VEC", vec);
            contentValues.put("Sync", sync);

            mDb.insert("CampActivities", null, contentValues);

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertCampActListAllOff(String campActTitle, String campActDec, String day, int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("CampActivityTitle", campActTitle);
            contentValues.put("CampActivityDescription", campActDec);
            contentValues.put("CampDay", day);
            contentValues.put("Sync", sync);

            mDb.insert("CampActivities", null, contentValues);

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertActAdmin(int id, String clgName, String actType, String actName,
                               String hours, String assignedDate, String fdate) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("College_Name", clgName);
            contentValues.put("activityType", actType);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursAssigned", hours);
            contentValues.put("AssignedDate", assignedDate);
            contentValues.put("finalDate", fdate);

            mDb.insert("ActivityListByAdmin", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertArea(int id, String actType, String actName, String clgName, int yr) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("ActivityName", actType);
            contentValues.put("ABProjectName", actName);
            contentValues.put("CollegeName", clgName);

            if (yr == 1)
                mDb.insert("AreaData", null, contentValues);
            else
                mDb.insert("AreaDataPrev", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertArea " + e.getMessage());
        }
    }

    public void insertWork(String act, int a, int b, int c, int yr) {
        try {
            //ContentValues contentValues = new ContentValues();

            /*contentValues.put("VEC", vec);
            contentValues.put("NatureOfWork", act);
            contentValues.put("TotalHours", a);*/
            //contentValues.put("HoursWorked", b);
            //contentValues.put("RemainingHours", c);

            if (yr == 1)
                mDb.execSQL(String.format(Locale.ENGLISH, "UPDATE WorkHoursFy SET TotalHours = \"%d\", HoursWorked = \"%d\", RemainingHours = \"%d\"  WHERE NatureOfWork=\"%s\"", a, b, c, act));
            else
                mDb.execSQL(String.format(Locale.ENGLISH, "UPDATE WorkHoursSy SET TotalHours = \"%d\", HoursWorked = \"%d\", RemainingHours = \"%d\"  WHERE NatureOfWork=\"%s\"", a, b, c, act));
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertWorkId(int id, String a, int th, int hw, int rh, int actCode, int yr) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("VEC", Helper.VEC);
            contentValues.put("NatureOfWork", a);
            contentValues.put("TotalHours", th);
            contentValues.put("HoursWorked", hw);
            contentValues.put("RemainingHours", rh);
            contentValues.put("actCode", actCode);

            if (yr == 1)
                mDb.insert("WorkHoursFy", null, contentValues);
            else
                mDb.insert("WorkHoursSy", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertUsers(String clgId,
                            String vec,
                            String fname,
                            String lname,
                            String email,
                            String contact,
                            String state,
                            String isLeader) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("College_name", clgId);
            contentValues.put("VEC", vec);
            contentValues.put("First_name", fname);
            contentValues.put("Last_name", lname);
            contentValues.put("Email", email);
            contentValues.put("Contact", contact);
            contentValues.put("State", state);
            contentValues.put("isLeader", isLeader);

            mDb.insert("Registration", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertLeaders(String cont, String name,
                              String email, String vec,
                              String clgName, String leaderId, String state) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("Contact", cont);
            contentValues.put("Name", name);
            contentValues.put("Email", email);
            contentValues.put("VEC", vec);
            contentValues.put("CollegeName", clgName);
            contentValues.put("id", leaderId);
            contentValues.put("State", state);
            mDb.insert("Leaders", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertVolAct(int id,
                             String name,
                             String date,
                             int hours,
                             String vec,
                             String actName,
                             String assActName,
                             String assActCode,
                             String actCode,
                             String state) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("First_name", name);
            contentValues.put("Date", date);
            contentValues.put("Hours", hours);
            contentValues.put("VEC", vec);
            contentValues.put("ActivityName", actName);
            contentValues.put("AssignedActivityName", assActName);
            contentValues.put("AssignedActivityCode", assActCode);
            contentValues.put("ActCode", actCode);
            contentValues.put("State", state);

            mDb.insert("VolAct", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertVolAllAct(int id,
                                String name,
                                String date,
                                String vec) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("First_name", name);
            contentValues.put("Date", date);
            contentValues.put("VEC", vec);

            mDb.insert("VolActAll", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertVolVecAllAct(int id,
                                   String name,
                                   String date,
                                   int hours,
                                   String vec,
                                   String actName,
                                   String assActName,
                                   String state) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", id);
            contentValues.put("First_name", name);
            contentValues.put("Date", date);
            contentValues.put("Hours", hours);
            contentValues.put("VEC", vec);
            contentValues.put("ActivityName", actName);
            contentValues.put("AssignedActivityName", assActName);
            contentValues.put("State", state);

            long r = mDb.insert("VolVecActAll", null, contentValues);
            if (r != -1)
                Log.e(TAG, "insertVolVecAllAct: " + "added");
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertHours(String lvl, int hours) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("Level", lvl);
            contentValues.put("TotalHours", hours);

            mDb.insert("HoursList", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertActAgain(String id, String actName, String state) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("actID", id);
            contentValues.put("ActivityName", actName);
            contentValues.put("State", state);

            mDb.insert("DailyActivityTemp", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertAct(String vec, String actCode,
                          String id,
                          String assignedDate,
                          String actName,
                          String hours,
                          String state,
                          String appBy,
                          String actDesc,
                          int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("actID", id);
            contentValues.put("VEC", vec);
            contentValues.put("ActivityCode", actCode);
            contentValues.put("Date", assignedDate);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursWorked", hours);
            contentValues.put("State", state);
            contentValues.put("If_Added", 1);
            contentValues.put("Approved_by", appBy);
            contentValues.put("Sync", sync);
            contentValues.put("Descr", actDesc);
            contentValues.put("dateAdded", assignedDate);

            mDb.insert("DailyActivity", null, contentValues);
        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public void insertActOff(String vec, String actCode,
                             String assignedDate,
                             String actName,
                             String hours,
                             String desc,
                             int sync) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("VEC", vec);
            contentValues.put("ActivityCode", actCode);
            contentValues.put("Date", assignedDate);
            contentValues.put("ActivityName", actName);
            contentValues.put("HoursWorked", hours);
            contentValues.put("If_Added", 1);
            contentValues.put("State", "Submitted");
            contentValues.put("Descr", desc);
            contentValues.put("Sync", sync);
            contentValues.put("dateAdded", df.format(calobj.getTime()));

            mDb.insert("DailyActivity", null, contentValues);

        } catch (SQLException e) {
            Log.e(TAG, ":insertData " + e.getMessage());
        }
    }

    public Cursor getArea(String actCode) {
        try {
            String sql = String.format("SELECT * FROM AreaData WHERE ActivityName LIKE " + "\"__%s\"", actCode);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.getCount();
            mCur2.moveToFirst();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getArea >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getAreaPrev(String actCode) {
        try {
            String sql = String.format("SELECT * FROM AreaDataPrev WHERE ActivityName LIKE " + "\"__%s\"", actCode);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.getCount();
            mCur2.moveToFirst();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getArea >>" + mSQLException.toString());
            throw mSQLException;
        }
    }


    public Cursor getUpdatedAct() {
        try {
            String sql = "select actID, State, ActivityName from DailyActivityTemp where (not state = \"Deleted\") except Select actID, State, ActivityName from DailyActivity";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.moveToFirst();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getUpdatedAct >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActListOff() {
        try {
            //String a = String.format("aaa %d", act);
            String sql = "SELECT * FROM DailyActivity WHERE sync=0";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.getCount();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getActListOff >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getAllDetHours(int yr) {
        String sql;
        if (yr == 1)
            sql = "SELECT * FROM WorkHoursFy";
        else
            sql = "SELECT * FROM WorkHoursSy";
        Cursor mCur = mDb.rawQuery(sql, null);
        try {
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getAllDetHours >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    /*public Cursor getHoursDet(String lvl, int yr) {
        String sql;
        if (yr == 1)
            sql = String.format("SELECT * FROM WorkHoursFy WHERE NatureOfWork=\"%s\"", lvl);
        else
            sql = String.format("SELECT * FROM WorkHoursSy WHERE NatureOfWork=\"%s\"", lvl);

        Cursor mCur = mDb.rawQuery(sql, null);
        try {
            if (mCur.getCount() == 0) {
            }
            mCur.moveToFirst();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }*/

    public int getHours(String lvl) {
        String sql = String.format("SELECT * FROM HoursList WHERE Level=\"%s\"", lvl);
        try (Cursor mCur = mDb.rawQuery(sql, null)) {
            if (mCur.getCount() == 0) {
                return 0;
            }
            mCur.moveToFirst();
            return mCur.getInt(mCur.getColumnIndex("TotalHours"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getLeaders() {
        try {
            //String a = String.format("aaa %d", act);
            String sql = "SELECT * FROM Leaders WHERE State=\"Appointed\"";
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public String getLeaderName(int id) {
        String sql = String.format(Locale.ENGLISH, "SELECT Name FROM Leaders WHERE id=%d", id);
        try (Cursor mCur = mDb.rawQuery(sql, null)) {
            mCur.moveToFirst();
            if (mCur.getCount() < 1 || mCur.getString(mCur.getColumnIndex("Name")) == null)
                return null;
            else
                return mCur.getString(mCur.getColumnIndex("Name"));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getVec() {
        try {
            String sql = "SELECT DISTINCT VEC FROM VolAct";
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();//Log.e(TAG, "getVec: " + "Empty");
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getVecAll() {
        try {
            String sql = "SELECT DISTINCT VEC FROM VolActAll";
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();//Log.e(TAG, "getVec: " + "Empty");
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setStateVolAct(String state, int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE VolAct SET State = \"%s\" WHERE id=\"%d\"", state, id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setVolActHours(int hour, int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE VolAct SET Hours = %d WHERE id=\"%d\"", hour, id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getVol(String vec) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM VolAct WHERE VEC=\"%s\"", vec);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getVolAll(String vec) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM VolActAll WHERE VEC=\"%s\"", vec);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getVolState(int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM VolAct WHERE id=\"%d\"", id);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getVolDetails(String actName, String vec) {
        try {
            String sql = String.format("SELECT * FROM VolAct WHERE ActivityName=\"%s\" AND VEC=\"%s\"", actName, vec);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public boolean isApproved(String vec) {
        String sql = String.format("SELECT State FROM VolAct WHERE VEC = \"%s\"", vec);
        boolean isApp = true;
        try (Cursor c = mDb.rawQuery(sql, null)) {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    try {
                        isApp = !c.getString(i).equals("Submitted") && !c.getString(i).equals("Modified");
                    } catch (IllegalStateException ignored) {
                    }
                    c.moveToNext();
                }
                if (c.getCount() == 0)
                    isApp = true;
            }
        } catch (SQLException | IllegalArgumentException exception) {
            Log.e(TAG, "getTestData >>" + exception.toString());
        }
        return isApp;
    }

    public Cursor getVolAllDetails(String actName, String vec) {
        try {
            String sql = String.format("SELECT * FROM VolVecActAll WHERE ActivityName=\"%s\" AND VEC=\"%s\"", actName, vec);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getRegDetails(String vec) {
        try {
            //String a = String.format("aaa %d", act);
            String sql = String.format("SELECT * FROM Registration WHERE VEC=\"%s\"", vec);
            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.getCount();
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    /*public Cursor getActId(String actCode) {
        try {
            //String a = String.format("aaa %d", act);
            String sql = "SELECT * FROM CampActivityListByAdmin WHERE AssignedActivityName=" + actCode;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActId: ");
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }*/

    public Cursor getActList(String act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM DailyActivity WHERE ActivityCode=\"%s\" AND (not State=\"Deleted\") ORDER BY Date DESC", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.getCount();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getActLeaderId(int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM DailyActivity WHERE actID=%d AND (not State=\"Deleted\")", id);
            return mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    /*public Cursor getActAssigActName(int act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM DailyActivity WHERE activityType='%d' AND (not State=\"Deleted\")", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            Log.i(TAG, "getActAssigActName: ");
            if (mCur2.getCount() == 0) {
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }*/

    public Cursor getActAssigActNameOff(String act) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM ActivityListByAdmin WHERE activityType LIKE '%s'", act);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.getCount();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public int getCountAssignAct() {
        String sql = "SELECT * FROM ActivityListByAdmin";

        try (Cursor mCur2 = mDb.rawQuery(sql, null)) {
            return mCur2.getCount();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            //throw mSQLException;
        }
        return 0;
    }

    public Cursor getActAssigActNameAdmin(String act) {
        try {
            String sql;
            Cursor c;
            if (isFirst)
                sql = String.format("SELECT * FROM ActivityListByAdmin WHERE ((activityType like \"1%%\") and (ActivityName=\"%s\"))", act);
            else
                sql = String.format("SELECT * FROM ActivityListByAdmin WHERE ((activityType like \"2%%\") and (ActivityName=\"%s\"))", act);
            c = mDb.rawQuery(sql, null);

            return c;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            //throw mSQLException;
        }
        return null;
    }

    public Cursor getActAllAdmin(int actName) {
        try {
            String sql = String.format(Locale.ENGLISH, "SELECT * FROM ActivityListByAdmin WHERE activityType=%d", actName);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            if (mCur2.getCount() == 0) {
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActList() {
        try {
            String sql = "SELECT * FROM CampActivityList";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListId(String actName) {
        try {
            String sql = String.format("SELECT CampId FROM CampActivityList WHERE CampActivityName=\"%s\"", actName);
            Cursor mCur2 = mDb.rawQuery(sql, null);
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListAllById(int id) {
        try {
            String sql = "SELECT * FROM CampActivities WHERE id=" + id;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListAll() {
        try {
            String sql = "SELECT * FROM CampActivities ORDER BY id DESC";
            Cursor mCur2 = mDb.rawQuery(sql, null);
            mCur2.getCount();//Log.e(mContext, "Too bad no data in CampActivityList", )();
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public int getSumHours(String actCode) {
        String sql = String.format("SELECT sum(HoursWorked) FROM DailyActivity WHERE ActivityCode=\"%s\" AND (State=\"Approved\" OR State=\"PoModified\" OR State=\"LeaderModified\")", actCode);
        try (Cursor mCur2 = mDb.rawQuery(sql, null)) {
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
                return 0;
            }
            mCur2.moveToFirst();
            return mCur2.getInt(0);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public int getSumHoursSubmitted(String date, String actCode) {
        String sql = String.format("SELECT sum(HoursWorked) FROM DailyActivity WHERE ActivityCode LIKE \"%s\" AND Date=\"%s\" AND (State=\"Submitted\" OR State=\"Approved\" OR State=\"Modified\" OR State=\"PoModified\" OR State=\"LeaderModified\")", actCode, date);
        try (Cursor mCur2 = mDb.rawQuery(sql, null)) {
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
                return -1;
            }
            mCur2.moveToFirst();
            return mCur2.getInt(0);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCampActListOff(int sync) {
        try {
            String sql = "SELECT * FROM CampActivities WHERE sync=" + sync;
            Cursor mCur2 = mDb.rawQuery(sql, null);
            if (mCur2.getCount() == 0) {
                //Log.e(mContext, "Too bad no data in CampActivityList", )();
            }
            return mCur2;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public ArrayList<String> getHelpData() {
        ArrayList<String> res = new ArrayList<>();
        try {
            String sql = "SELECT CollegeName, Name, EmailID, Contact FROM Help";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur.getCount() == 0) {
                //Log.e(mContext, "Too bad no data", )();
            } else {
                if (mCur.moveToNext()) {
                    res.add(mCur.getString(0));
                    res.add(mCur.getString(1));
                    res.add(mCur.getString(2));
                    res.add(mCur.getString(3));
                }
            }
            mCur.close();
            return res;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setDetails(int hour, String state, int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE DailyActivity SET HoursWorked = %d, State = \"%s\" WHERE actId=\"%d\"", hour, state, id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setApproved(String table) {
        try {
            String sql = String.format("UPDATE %s SET If_Approved=1 WHERE State=\"Approved\"", table);
            String sq2 = String.format("UPDATE %s SET If_Approved=0 WHERE (not State=\"Approved\")", table);
            mDb.execSQL(sql);
            mDb.execSQL(sq2);
        } catch (SQLException e) {
            Log.e(TAG, "setApproved: " + e.getMessage());
        }
    }

    public void setActState() {
        try {
            String sql = "SELECT actID from DailyActivity WHERE (State=\"Submitted\" OR State=\"Modified\") EXCEPT SELECT actID from DailyActivity a, ActivityListByAdmin b where a.ActivityName=b.ActivityName";

            Cursor mCur2 = mDb.rawQuery(sql, null);
            int c = mCur2.getCount();
            mCur2.moveToFirst();

            if (c == 0) {
                //Log.e(mContext, "Too bad no data in DailyActivity", )();
            } else {
                while (c > 0) {
                    mDb.execSQL(String.format(Locale.ENGLISH, "UPDATE DailyActivity SET State=\"Finished\" WHERE actID = %d", mCur2.getInt(mCur2.getColumnIndex("actID"))));
                    c--;
                    mCur2.moveToNext();
                }
                mCur2.close();
            }
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }


    public void dropDetails(int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "DELETE FROM DailyActivity WHERE actID=\"%d\"", id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setDetailsCamp(String desc, int day, int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE CampActivities SET CampActivityDescription = \"%s\", CampDay = %d WHERE id=\"%d\"", desc, day, id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void dropDetailsCamp(int id) {
        try {
            String sql = String.format(Locale.ENGLISH, "DELETE FROM CampActivities WHERE id=\"%d\"", id);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void setSync(String table, int sync) {
        try {
            String sql = String.format(Locale.ENGLISH, "UPDATE %s SET Sync = %d", table, sync);
            mDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    /*public Cursor getTestData() {
        try {
            String sql ="SELECT * FROM NatureOfActivity";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }*/
}
