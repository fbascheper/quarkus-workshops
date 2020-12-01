package com.github.fbascheper.quarkus.zerocode.extension.httpclient;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.jsmart.zerocode.core.httpclient.BasicHttpClient;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Http client supporting an {@code optional} HTTP proxy.
 *
 * @author Erik-Berndt Scheper
 * @see org.jsmart.zerocode.core.httpclient.ssl.SslTrustCorporateProxyHttpClient
 * @since 01-12-2020
 */
public class ProxySupportingHttpClient extends BasicHttpClient {
    private static final Logger LOGGER = getLogger(ProxySupportingHttpClient.class);

    @Inject
    @Named("corporate.proxy.host")
    private String proxyHost;

    @Inject
    @Named("corporate.proxy.port")
    private int proxyPort;

    @Inject
    @Named("corporate.proxy.username")
    private String proxyUserName;

    @Inject
    @Named("corporate.proxy.password")
    private String proxyPassword;

    @Override
    public CloseableHttpClient createHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        LOGGER.info("###Used SSL Enabled Http Client with Corporate Proxy, for both Http and Https connections");

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();

        CookieStore cookieStore = new BasicCookieStore();

        var clientBuilder = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultCookieStore(cookieStore);

        if (proxyHost != null && proxyHost.length() > 0) {
            var httpHost = new HttpHost(proxyHost, proxyPort);
            clientBuilder.setProxy(httpHost);

            if (proxyUserName != null && proxyUserName.length() > 0) {
                CredentialsProvider credsProvider = createProxyCredentialsProvider(proxyHost, proxyPort, proxyUserName, proxyPassword);
                clientBuilder.setDefaultCredentialsProvider(credsProvider);
                LOGGER.warn("Using proxy {}:{} with username {}", proxyHost, proxyPort, proxyUserName);
            } else {
                LOGGER.warn("Using proxy {}:{}", proxyHost, proxyPort);
            }
        } else {
            LOGGER.warn("No proxy configured!");
        }

        return clientBuilder.build();
    }

    private CredentialsProvider createProxyCredentialsProvider(String proxyHost, int proxyPort, String proxyUserName, String proxyPassword) {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(proxyUserName, proxyPassword));

        return credsProvider;

    }

}

