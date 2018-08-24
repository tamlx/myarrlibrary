package b.laixuantam.myaarlibrary.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import b.laixuantam.myaarlibrary.dependency.AppProvider;
import b.laixuantam.myaarlibrary.helper.ConnectivityHelper;
import b.laixuantam.myaarlibrary.helper.MyLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiRequest<S, T, P> {
    protected static final String TYPE_JSON_UTF8 = "application/json";
    protected static final String CONTENT_TYPE_JSON = "Content-Type: " + TYPE_JSON_UTF8;
    protected static final String REST_ENDPOINT = "get_api/?detect=1";
    private static final ExecutorService threadPoolExecutor = new ThreadPoolExecutor(5, 10, 20000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    protected final S service;
    private final RequestOrigin origin;
    protected P params;
    private static Gson mGson = new GsonBuilder().setLenient().create();


    protected ApiRequest(Class<S> service, RequestOrigin requestOrigin, String hostAPI, String mode, boolean isEnableLog, boolean isTrustCertificate) {
        this.service = getStaticAdapter(hostAPI, mode, isEnableLog, isTrustCertificate).create(service);
        this.origin = requestOrigin;
    }

    protected ApiRequest(Class<S> service, Retrofit adapterService, RequestOrigin requestOrigin) {
        this.service = adapterService.create(service);
        this.origin = requestOrigin;
    }

    public static Retrofit getStaticAdapter(String hostAPI, String mode, boolean isEnableLog, boolean isTrustCertificate) {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(hostAPI);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(HttpClientFactory.get(hostAPI, mode, isEnableLog, isTrustCertificate));

        return builder.build();
    }

    public static Retrofit getCustomAdapter(String host, String mode, boolean isEnableLog, boolean isTrustCertificate) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(host);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(HttpClientFactory.get(host, mode, isEnableLog, isTrustCertificate));

        return builder.build();
    }

    public S getService() {
        return service;
    }

    protected abstract void postAfterRequest(T result) throws Exception;

    protected void setParams(P params) {
        this.params = params;
    }

    protected abstract Call<T> call(P params);

    public void process(Call<T> call, final ApiCallback<T> callback) {
        try {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (response.isSuccessful()) {
                        try {
                            postAfterRequest(response.body());
                            if (callback != null) {
                                callback.onSuccess(response.body());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (callback != null) {
                                processFailure(e, callback);
                            }
                        }
                    } else if (callback != null) {
                        String dataError = "";

                        dataError = response.errorBody().toString();
                        MyLog.e("REQUEST_ERROR", dataError);
                        ErrorApiResponse errorMessage = null;
                        int code = response.code();
                        if (code == 401 || code == 403) {
                        }
                        try {
                            errorMessage = mGson.fromJson(response.errorBody().string(), ErrorApiResponse.class);
                            callback.onError(errorMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (callback != null) {
                                processFailure(e, callback);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    if (callback != null) {
                        processFailure(t, callback);
                    }
                }
            });
        } catch (Exception e) {
            if (callback != null) {
                processFailure(e, callback);
            }
        }
    }

    protected void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

    protected void processFailure(Throwable throwable, ApiCallback errorCallback) {
        MyLog.e("ApiRequest", throwable.getMessage());

        RequestError requestError = getNetworkError(throwable);

        errorCallback.onFail(requestError);
    }

    private RequestError getNetworkError(Throwable throwable) {
        ConnectivityHelper connectivityHelper = AppProvider.getConnectivityHelper();

        String cause = (throwable.getCause() != null) ? throwable.getCause().getClass().getSimpleName() : "";
        String message = (throwable.getMessage() != null) ? throwable.getMessage().toLowerCase() : "";

        if (throwable instanceof IOException) {
            return RequestError.ERROR_NETWORK_CANCELLED;
        } else if (cause.contains("SocketTimeoutException") || message.contains("timeout") || message.contains("failed to connect")) {
            return RequestError.ERROR_NETWORK_TIMEOUT;
        } else if (!connectivityHelper.hasInternetConnection()) {
            return RequestError.ERROR_NETWORK_NO_CONNECTION;
        } else {
            return RequestError.ERROR_NETWORK_OTHER;
        }
    }

    // =============================================================================================

    public enum RequestError {
        ERROR_NETWORK_CANCELLED,                        // network call cancelled
        ERROR_NETWORK_TIMEOUT,                          // network timeout
        ERROR_NETWORK_NO_CONNECTION,                    // no Internet connection
        ERROR_NETWORK_OTHER                            // other network error
    }

    public enum RequestOrigin {
        NONE, //
        AUTHENTICATE
    }

    public interface ApiCallback<ResponseType> {
        void onSuccess(ResponseType body);

        void onError(ErrorApiResponse error);

        void onFail(RequestError error);
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApiName {
        String value();
    }
}
