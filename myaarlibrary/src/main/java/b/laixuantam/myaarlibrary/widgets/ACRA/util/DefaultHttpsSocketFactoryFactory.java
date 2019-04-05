package b.laixuantam.myaarlibrary.widgets.ACRA.util;

import android.content.Context;

import org.apache.http.conn.scheme.SocketFactory;

/**
 * Default implementation of a HttpSocketFactoryFactory.
 */
public final class DefaultHttpsSocketFactoryFactory implements HttpsSocketFactoryFactory {

    public static final HttpsSocketFactoryFactory INSTANCE = new DefaultHttpsSocketFactoryFactory();

    @Override
    public SocketFactory create(Context context) {
        return new TlsSniSocketFactory();
    }
}
