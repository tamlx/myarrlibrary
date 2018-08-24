package b.laixuantam.myaarlibrary.helper;

import java.io.IOException;

import b.laixuantam.myaarlibrary.dependency.AppProvider;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthHelper implements Interceptor {

    private static String token = null;

    public void setToken(String newToken) {
        if (newToken != null) {
            this.token = newToken;
        }
    }

    private String getCookieValue(String key, String value) {
        return String.format("%s=%s", key, value);
    }

    public Request getRequest(String url) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (token != null && !token.isEmpty()) {
            builder.addHeader("User-Authorization", token);
        }
        return builder.build();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (token != null && !token.isEmpty()) {
            builder.addHeader("User-Authorization", token);
        } else {
            builder.addHeader("User-Authorization", "Empty");
        }
        InstallationHelper installationHelper = AppProvider.getInstallationHelper();
        builder.addHeader("User-Agent", installationHelper.getUserAgent());

        try {
            return chain.proceed(builder.build());
        } catch (Exception e) {
            MyLog.e("", e.getMessage());
            throw e;
        }
    }
}