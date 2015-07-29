package gov.pppl.androidmessaginglibrary.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class StreamUtil {
    private StreamUtil(){}

    public static void closeQuietly(Closeable closeable) {
        try {
            if(closeable.getClass().isAssignableFrom(OutputStream.class))
                ((OutputStream) closeable).flush();
            closeable.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void write(File file, InputStream stream, boolean close) {
        file.getParentFile().mkdirs();
        if(file.exists())
            file.delete();

        OutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream = new BufferedOutputStream(fileOutputStream);
            IOUtils.copy(stream, fileOutputStream);

        } catch (IOException e) {e.printStackTrace();}
        finally {
            closeQuietly(fileOutputStream);
            if(close)
                closeQuietly(stream);
        }
    }

    public static void writeBuffer(File file, InputStream inputStream, boolean close) {
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(inputStream);

            outputStream = new FileOutputStream(file);
            outputStream = new BufferedOutputStream(outputStream);

            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {e.printStackTrace();}
        finally {
            closeQuietly(outputStream);
            if(close)
                closeQuietly(inputStream);
        }
    }
}
