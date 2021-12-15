package i01_finger;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

final class Handler extends URLStreamHandler {

    @Override
    public int getDefaultPort() {
        return FingerURLConnection.DEFAULT_PORT;
    }


    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new FingerURLConnection(u);
    }
}
