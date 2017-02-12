package com.thaison.btvn_buoi18_ailatrieuphu;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shin on 9/7/2016.
 */
public class QuestionManager {
    private static final String PATH = Environment.getDataDirectory() +
            File.separator + "data" + File.separator + "com.thaison.btvn_buoi18_ailatrieuphu" +
            File.separator + "database";
    private static final String NAME_DATA = "Question";
    private static final String TAG = QuestionManager.class.getSimpleName();
    private Context mContext;
    private SQLiteDatabase sqLiteDatabase;

    public QuestionManager(Context mContext) {
        this.mContext = mContext;
        copyDatabase();
    }

    private void copyDatabase() {
        try {
            new File(PATH).mkdir();
            File file = new File(PATH + File.separator + NAME_DATA);
            if (file.exists()) {
                return;
            } else {
                file.createNewFile();

                AssetManager assetManager = mContext.getAssets();
                InputStream inputStream = assetManager.open("Question");
                OutputStream outputStream = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int length = inputStream.read(b);
                while (length > 0) {
                    outputStream.write(b, 0, length);
                    length = inputStream.read(b);
                }
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDatabase() {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            sqLiteDatabase = SQLiteDatabase.openDatabase(PATH + File.separator + NAME_DATA,
                    null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public void closeDatabase() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

    public List<Question> get15Questions() {
        openDatabase();
        List<Question> questions = new ArrayList<>();
        String sqlQuery15Questions = "SELECT * FROM (SELECT * FROM Question ORDER BY random())" +
                "GROUP BY level ORDER BY level ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery15Questions, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        int indexQuestion = cursor.getColumnIndex("question");
        int indexLevel = cursor.getColumnIndex("level");
        int indexCaseA = cursor.getColumnIndex("casea");
        int indexCaseB = cursor.getColumnIndex("caseb");
        int indexCaseC = cursor.getColumnIndex("casec");
        int indexCaseD = cursor.getColumnIndex("cased");
        int indexTrueCase = cursor.getColumnIndex("truecase");

        while (!cursor.isLast()) {
            String question = cursor.getString(indexQuestion);
            int level = cursor.getInt(indexLevel);
            String caseA = cursor.getString(indexCaseA);
            String caseB = cursor.getString(indexCaseB);
            String caseC = cursor.getString(indexCaseC);
            String caseD = cursor.getString(indexCaseD);
            int trueCase = cursor.getInt(indexTrueCase);
            Question question1 = new Question(level, question, caseA, caseB, caseC, caseD, trueCase);
            questions.add(question1);
            cursor.moveToNext();
        }

        String question = cursor.getString(indexQuestion);
        int level = cursor.getInt(indexLevel);
        String caseA = cursor.getString(indexCaseA);
        String caseB = cursor.getString(indexCaseB);
        String caseC = cursor.getString(indexCaseC);
        String caseD = cursor.getString(indexCaseD);
        int trueCase = cursor.getInt(indexTrueCase);
        Question question1 = new Question(level, question, caseA, caseB, caseC, caseD, trueCase);
        questions.add(question1);
        return questions;
    }
}
