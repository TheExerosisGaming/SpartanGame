package gov.pppl.androidmessaginglibrary.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class IOUtils {
    private static byte[] SKIP_BYTE_BUFFER;

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }
        if (length == 0L) {
            return 0L;
        }
        int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if ((length > 0L) && (length < bufferLength)) {
            bytesToRead = (int) length;
        }

        long totalRead = 0L;
        int read;
        while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))) {
            read = 0;
            output.write(buffer, 0, read);
            totalRead += read;
            if (length <= 0L)
                continue;
            bytesToRead = (int) Math.min(length - totalRead, bufferLength);
        }

        return totalRead;
    }
    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }

        if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[2048];
        }
        long remain = toSkip;
        while (remain > 0L) {
            long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, 2048L));
            if (n < 0L) {
                break;
            }
            remain -= n;
        }
        return (toSkip - remain);
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        long skipped = skip(input, toSkip);
        if (skipped != toSkip)
            throw new IllegalArgumentException("Bytes to skip: " + toSkip + " actual: " + skipped);
    }
}
