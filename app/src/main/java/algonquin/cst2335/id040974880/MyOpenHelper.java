package algonquin.cst2335.id040974880;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheDatabase";
    public static final int version = 2;
    public static final String TABLE_NAME = "Message";
    public static final String col_message = "Message";
    public static final String col_send_receive = "SendOrReceive";
    public static final String col_time_sent = "TimeSent";
    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table " +
            TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + col_message + " TEXT,"
            +col_send_receive+ " INTERGE,"
            + col_time_sent + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists "+ TABLE_NAME);
    onCreate(db);

    }
}
