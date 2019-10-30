package hp;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// https://github.com/piomin/spring-boot-logging/blob/master/src/main/java/pl/piomin/logging/filter/SpringLoggingFilter.java

public class ServletOutputStreamWrapper extends ServletOutputStream {

    private OutputStream outputStream;
    private ByteArrayOutputStream copy;

    public ServletOutputStreamWrapper(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        copy.write(b);
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }

//    @Override
//    public boolean isReady() {
//        return true;
//    }
//
//    @Override
//    public void setWriteListener(WriteListener writeListener) {
//
//    }
}
