package com.github.brunodles.okhttp;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Func0;

/**
 * Created by bruno on 30/10/16.
 */

public class GithubAuthInterceptor implements Interceptor {

    public static final String HEADER_KEY = "GithubAuth";
    public static final String HEADER = HEADER_KEY + ":true";

    private final ClientAuthProvider provider;

    public GithubAuthInterceptor(Func0<String> clientIdProvider, Func0<String> clientSecretProvider) {
        this.provider = new RxClientProvider(clientIdProvider, clientSecretProvider);
    }

    public GithubAuthInterceptor(ClientAuthProvider provider) {
        this.provider = provider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean header = booleanFrom(request.header(HEADER_KEY));
        if (header) {
            return addKeys(chain, request);
        }
        return chain.proceed(request);
    }

    private boolean booleanFrom(String string) {
        try {
            return Boolean.valueOf(string);
        } catch (Exception e) {
            return false;
        }
    }

    private Response addKeys(Chain chain, Request request) throws IOException {
        HttpUrl url = request.url();
        Request.Builder builder = request.newBuilder();
        String divider = dividerFor(url);
        builder.url(url.url().toString() + divider + keys());
        return chain.proceed(builder.build());
    }

    private String dividerFor(HttpUrl url) {
        return url.queryParameterNames().size() == 0 ? "?" : "&";
    }

    private String keys() {
        return String.format("client_id=%s&client_secret=%s", provider.clientId(),
                provider.clientSecret());
    }

    public static interface ClientAuthProvider {
        String clientId();

        String clientSecret();
    }

    private static class RxClientProvider implements ClientAuthProvider {

        private final Func0<String> clientIdProvider;
        private final Func0<String> clientSecretProvider;

        public RxClientProvider(Func0<String> clientIdProvider, Func0<String> clientSecretProvider) {
            this.clientIdProvider = clientIdProvider;
            this.clientSecretProvider = clientSecretProvider;
        }

        @Override
        public String clientId() {
            return clientIdProvider.call();
        }

        @Override
        public String clientSecret() {
            return clientSecretProvider.call();
        }
    }
}
