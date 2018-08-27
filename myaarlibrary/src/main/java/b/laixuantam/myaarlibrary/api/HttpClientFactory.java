package b.laixuantam.myaarlibrary.api;

import java.io.File;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import b.laixuantam.myaarlibrary.dependency.AppProvider;
import b.laixuantam.myaarlibrary.helper.FileHelper;
import b.laixuantam.myaarlibrary.helper.MyLog;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okio.ByteString;

public class HttpClientFactory {
    private static final int TIMEOUT = 20; // in seconds
    private static final int CACHE_SIZE = 1024 * 1024 * 10; // in MB
    private static final String CERTIFICATES_PATH = "certificates/";
    private static OkHttpClient defaultClient = null;
    private static String HOST_API, MODE;
    private static boolean isTrustCer;

    private void setup(OkHttpClient.Builder builder, String certificatesPath, int timeout) {
        X509Certificate[] certificates = getCertificates(certificatesPath);
        X509EncodedKeySpec[] publicKeys = getPublicKeys(certificatesPath);

        if (isTrustCer) {
            setupPinning(builder, certificates, publicKeys);
        } else {
            setupCertificates(builder, certificates);
        }

        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        builder.writeTimeout(timeout, TimeUnit.SECONDS);

        setupCache(builder);
    }

    private void setupCache(OkHttpClient.Builder builder) {
        File cacheDir = AppProvider.getFileHelper().getCacheDir();
        builder.cache(new Cache(cacheDir, CACHE_SIZE));
    }

    private void setupCertificates(OkHttpClient.Builder builder, final X509Certificate[] certificates) {
        // create a trust manager to validate certificate chains
        TrustManager[] trustManager = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};

        try {
            // install the trust manager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            // create an ssl socket factory with our trust manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            MyLog.e("HttpClientFactory", e.getMessage());
        }
    }

    private void setupPinning(OkHttpClient.Builder builder, Certificate[] certificates, X509EncodedKeySpec[] publicKeys) {
        CertificatePinner.Builder pinnerBuilder = new CertificatePinner.Builder();

        for (Certificate certificate : certificates) {
            String sha1 = "sha1/" + Util.sha1(ByteString.of(certificate.getPublicKey().getEncoded())).base64();
            pinnerBuilder.add(HOST_API, sha1);

            String sha256 = "sha256/" + Util.sha256(ByteString.of(certificate.getPublicKey().getEncoded())).base64();
            pinnerBuilder.add(HOST_API, sha256);

            pinnerBuilder.add(HOST_API, CertificatePinner.pin(certificate));
        }

        for (X509EncodedKeySpec publicKey : publicKeys) {
            byte[] spki = publicKey.getEncoded();
            pinnerBuilder.add(HOST_API, "sha1/" + Util.sha1(ByteString.of(spki)).base64());
            pinnerBuilder.add(HOST_API, "sha256/" + Util.sha256(ByteString.of(spki)).base64());
        }
        builder.certificatePinner(pinnerBuilder.build());
    }

    private X509Certificate[] getCertificates(String certificatesPath) {
        List<X509Certificate> certificates = new ArrayList<>();

        try {
            FileHelper assetHelper = AppProvider.getFileHelper();
            String[] files = assetHelper.getFiles(certificatesPath);

            for (String filePath : files) {
                if (filePath.endsWith("cer")) {
                    String path = String.format("%s/%s", certificatesPath, filePath);
                    InputStream inputStream = assetHelper.getInputStream(path);

                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(inputStream);
                    certificates.add(certificate);
                }
            }
        } catch (Exception e) {
            MyLog.e("HttpClientFactory", e.getMessage());
        }

        X509Certificate[] result = new X509Certificate[certificates.size()];

        return certificates.toArray(result);
    }

    private X509EncodedKeySpec[] getPublicKeys(String certificatesPath) {
        List<X509EncodedKeySpec> publicKeys = new ArrayList<>();

        try {
            FileHelper assetHelper = AppProvider.getFileHelper();
            String[] files = assetHelper.getFiles(certificatesPath);

            for (String filePath : files) {
                if (filePath.endsWith("pub")) {
                    String path = String.format("%s/%s", certificatesPath, filePath);
                    InputStream inputStream = assetHelper.getInputStream(path);
                    byte[] encKey = new byte[inputStream.available()];
                    int read = inputStream.read(encKey);

                    if (read > 0) {
                        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
                        publicKeys.add(pubKeySpec);
                    }
                }
            }
        } catch (Exception e) {
            MyLog.e("HttpClientFactory", e.getMessage());
        }

        X509EncodedKeySpec[] result = new X509EncodedKeySpec[publicKeys.size()];

        return publicKeys.toArray(result);
    }

    public static OkHttpClient get(int timeout) {
        return create((timeout != 0) ? timeout : TIMEOUT);
    }

    public static synchronized OkHttpClient get(String hostAPI, String mode, boolean isTrustCertificate) {
        if (defaultClient == null) {
            defaultClient = create(TIMEOUT);
        }

        HOST_API = hostAPI;

        MODE = mode;

        isTrustCer = isTrustCertificate;


        return defaultClient;
    }

    public static OkHttpClient create(int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        Interceptor interceptor = AppProvider.getAuthHelper();
//        builder.addInterceptor(interceptor);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            public void log(String message) {
                MyLog.d("RETROFIT_LOG", message);
            }
        });
        logging.setLevel(Level.BODY);
        builder.addInterceptor(logging);

        HttpClientFactory httpClientFactory = new HttpClientFactory();
        httpClientFactory.setup(builder, CERTIFICATES_PATH + MODE, timeout);

        return builder.build();
    }
}