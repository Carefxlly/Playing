package com.example.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jingbin on 2017/2/14.
 * 网络请求工具类
 * <p>
 * 豆瓣api:
 * 问题：API 限制为每分钟40次，一不小心就超了，马上KEY就被封,用不带KEY的API，每分钟只有可怜的10次。
 * 返回：code:112（rate_limit_exceeded2 IP 访问速度限制）
 * 解决：1.使用每分钟访问次数限制（客户端）2.更换ip (更换wifi)
 * 豆瓣开发者服务使用条款: https://developers.douban.com/wiki/?title=terms
 * 使用时请在"CloudReaderApplication"下初始化。
 */

public class HttpUtils {

    private static HttpUtils instance;
    private Context context;
    private Gson gson;
    private boolean debug;
    // URL
    public final static String API_GANKIO = "http://gank.io/api/";
    public final static String API_DOUBAN = "https://api.douban.com/";
    public final static String API_TING = "https://tingapi.ting.baidu.com/v1/restserver/";
    public final static String API_FIR = "https://api.fir.im/apps/";
    public final static String API_WAN_ANDROID = "https://www.wanandroid.com/";
    public final static String API_NHDZ = "https://ic.snssdk.com/";
    public final static String API_QSBK = "https://m2.qiushibaike.com/";
    //分页数据，每页的数量
    public static int per_page = 10;
    public static int per_page_more = 20;


    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context, boolean debug) {
        this.context = context;
        this.debug = debug;
        HttpHead.init(context);
    }


    public Retrofit.Builder getBuilder(String apiUrl) {
        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(getOkClient())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }


    private Gson getGson() {
        if (gson == null) {
            return new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .setFieldNamingStrategy(new AnnotateNaming())
                    .create();
        } else {
            return gson;
        }
    }

    private OkHttpClient getOkClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            Cache cache = new Cache(new File(context.getCacheDir(), "responses"), 50 * 1024 * 1024);

            return new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .cache(cache)
                    .addInterceptor(getLoggingInterceptor())
                    .addInterceptor(new HttpHeadInterceptor())
                    .addInterceptor(new AddCacheInterceptor(context))
                    .addInterceptor(new AddCookiesInterceptor(context))
                    .addInterceptor(new ReceivedCookiesInterceptor(context))
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (debug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }

    private class HttpHeadInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Accept", "application/json;versions=1");
            if (CheckNetwork.isNetworkConnected(context)) {
                int maxAge = 60;
                builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return chain.proceed(builder.build());
        }
    }

    private class AddCacheInterceptor implements Interceptor {
        private Context context;

        AddCacheInterceptor(Context context) {
            super();
            this.context = context;
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            CacheControl cacheControl =
                    new CacheControl.Builder()
                            .maxAge(0, TimeUnit.SECONDS)
                            .maxStale(365, TimeUnit.DAYS)
                            .build();
            Request request = chain.request();
            if (!CheckNetwork.isNetworkConnected(context)) {
                request = request.newBuilder().cacheControl(cacheControl).build();
            }
            Response originalResponse = chain.proceed(request);
            if (CheckNetwork.isNetworkConnected(context)) {
                // read from cache
                int maxAge = 0;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                // tolerate 4-weeks stale
                int maxStale = 60 * 60 * 24 * 28;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    private class AddCookiesInterceptor implements Interceptor {
        private Context context;

        AddCookiesInterceptor(Context context) {
            super();
            this.context = context;

        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            String cookie = context
                    .getSharedPreferences("config", Context.MODE_PRIVATE)
                    .getString("cookie", "");
            Request.Builder builder = chain.request()
                    .newBuilder()
                    .addHeader("Cookie", cookie);
            return chain.proceed(builder.build());
        }
    }

    private class ReceivedCookiesInterceptor implements Interceptor {
        private Context context;

        ReceivedCookiesInterceptor(Context context) {
            super();
            this.context = context;

        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                List<String> d = originalResponse.headers("Set-Cookie");
                //Log.e("carefxlly", "得到的 cookies:" + d.toString());

                // 返回cookie
                if (!TextUtils.isEmpty(d.toString())) {

                    SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorConfig = sharedPreferences.edit();
                    String oldCookie = sharedPreferences.getString("cookie", "");

                    HashMap<String, String> stringStringHashMap = new HashMap<>();

                    // 之前存过cookie
                    if (!TextUtils.isEmpty(oldCookie)) {
                        String[] substring = oldCookie.split(";");
                        for (String aSubstring : substring) {
                            if (aSubstring.contains("=")) {
                                String[] split = aSubstring.split("=");
                                stringStringHashMap.put(split[0], split[1]);
                            } else {
                                stringStringHashMap.put(aSubstring, "");
                            }
                        }
                    }
                    String join = StringUtils.join(d, ";");
                    String[] split = join.split(";");

                    // 存到Map里
                    for (String aSplit : split) {
                        String[] split1 = aSplit.split("=");
                        if (split1.length == 2) {
                            stringStringHashMap.put(split1[0], split1[1]);
                        } else {
                            stringStringHashMap.put(split1[0], "");
                        }
                    }

                    // 取出来
                    StringBuilder stringBuilder = new StringBuilder();
                    if (stringStringHashMap.size() > 0) {
                        for (String key : stringStringHashMap.keySet()) {
                            stringBuilder.append(key);
                            String value = stringStringHashMap.get(key);
                            if (!TextUtils.isEmpty(value)) {
                                stringBuilder.append("=");
                                stringBuilder.append(value);
                            }
                            stringBuilder.append(";");
                        }
                    }

                    editorConfig.putString("cookie", stringBuilder.toString());
                    editorConfig.apply();
                    //Log.e("carefxlly", "处理后的 cookies:" + stringBuilder.toString());
                }
            }

            return originalResponse;
        }
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

}
