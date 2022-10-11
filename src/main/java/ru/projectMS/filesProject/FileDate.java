package ru.projectMS.filesProject;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class FileDate {


    public static Timestamp lastModified(String files) {
        File file = new File(files);

        Timestamp timestamp = new Timestamp( file.lastModified());

        return timestamp;

    }
}