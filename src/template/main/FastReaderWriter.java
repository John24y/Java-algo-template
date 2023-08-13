package template.main;


import java.io.*;

class FastInput {
    private final InputStream is;
    private byte[] buf = new byte[1 << 13];
    private int bufLen;
    private int bufOffset;
    private int next;

    public FastInput(InputStream is) {
        this.is = is;
    }

    public void readInt(int[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = readInt();
        }
    }

    private int read() {
        while (bufLen == bufOffset) {
            bufOffset = 0;
            try {
                bufLen = is.read(buf);
            } catch (IOException e) {
                bufLen = -1;
            }
            if (bufLen == -1) {
                return -1;
            }
        }
        return buf[bufOffset++];
    }

    public void skipBlank() {
        while (next >= 0 && next <= 32) {
            next = read();
        }
    }

    public int[] readInts(int n) {
        int[] ans = new int[n];
        readInt(ans);
        return ans;
    }

    public int readInt() {
        long l = readLong();
        if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) throw new RuntimeException("int overflow!");
        return (int) l;
    }

    public long readLong() {
        boolean rev = false;
        skipBlank();
        if (next == '+' || next == '-') {
            rev = next == '-';
            next = read();
        }
        long val = 0;
        while (next >= '0' && next <= '9') {
            val = val * 10 - next + '0';
            next = read();
        }
        return rev ? val : -val;
    }

    public String readString() {
        skipBlank();
        StringBuilder sb = new StringBuilder();
        while (next > 32) {
            sb.append((char) next);
            next = read();
        }
        return sb.toString();
    }
}

class FastOutput {
    private static final int THRESHOLD = 32 << 10;
    private final Writer os;
    private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

    public FastOutput(OutputStream os) {
        this.os = new OutputStreamWriter(os);
    }

    public FastOutput print(CharSequence csq) {
        cache.append(csq);
        return this;
    }

    public FastOutput print(char c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput print(int c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput print(long c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput println() {
        print('\n');
        flush();
        return this;
    }

    private void afterWrite() {
        if (cache.length() >= THRESHOLD) {
            flush();
        }
    }

    public FastOutput flush() {
        try {
            os.append(cache);
            os.flush();
            cache.setLength(0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }
}

public class FastReaderWriter {
    static FastInput in = new FastInput(System.in);
    static FastOutput out = new FastOutput(System.out);
    static String readString() { return in.readString(); }
    static int readInt() { return in.readInt(); }
    static long readLong() { return in.readLong(); }

}
