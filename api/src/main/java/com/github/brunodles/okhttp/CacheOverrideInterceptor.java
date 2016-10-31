package com.github.brunodles.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Override the cache request/response
 * <p>
 * It will use the header as a parameter to know when apply this interceptor.
 * It overrides the <code>max-age</code> to save response on cache.
 * It uses <code>max-stale</code> to get a outdated response. when the request fails.
 */
public class CacheOverrideInterceptor implements Interceptor {
    private static final String TAG = "CacheOverrideInterceptor";

    /**
     * Use this constant to activate this interceptor on a request.
     * it should be <code>HEADER_KEY+":360"</code> where the 360 are seconds to keep the response on cache.
     */
    public static final String HEADER_KEY = "CacheOverride";
    private static final String CACHE_CONTROL = "Cache-Control";
    public static boolean debug = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        int duration = intFrom(request.header(HEADER_KEY));

        if (duration == 0) chain.proceed(request);

        request = request.newBuilder()
                .removeHeader(HEADER_KEY)
                .build();
        log("intercept: Response cache applied");
        try {
            return fromNetworkAndSave(chain, request, duration);
        } catch (IOException e) {
            return tryFromCache(chain, request, duration, e);
        }
    }

    private Response fromNetworkAndSave(Chain chain, Request request, int duration) throws IOException {
        Response response = chain.proceed(request);
        return response.newBuilder()
                .removeHeader(CACHE_CONTROL)
                .addHeader(CACHE_CONTROL, "public, max-age=" + duration)
                .build();
    }

    private Response tryFromCache(Chain chain, Request request, int duration,
                                  IOException oldException) throws IOException {
        try {
            return fromCache(chain, request, duration);
        } catch (IOException e1) {
            throw oldException;
        }
    }

    private Response fromCache(Chain chain, Request request, int duration) throws IOException {
        log("fromCache: Load the cached response");
        String cacheControlValue = "public, only-if-cached";
        if (duration > 0) cacheControlValue += ", max-stale=" + duration;
        request = request.newBuilder()
                .removeHeader(HEADER_KEY)
                .header(CACHE_CONTROL, cacheControlValue)
                .build();
        return chain.proceed(request);
    }

    private int intFrom(String string) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void log(String message) {
        if (debug) System.out.printf("%s - %s\n", TAG, message);
    }
}