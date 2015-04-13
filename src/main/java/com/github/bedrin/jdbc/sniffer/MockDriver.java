package com.github.bedrin.jdbc.sniffer;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Enable JDBC Sniffer by adding a {@code sniffer:} prefix to your JDBC URL.
 * For example:
 * {@code sniffer:jdbc:h2:~/test}
 *
 * After that you'll be able to verify the number of executed statements using the {@link Sniffer} class
 * @see Sniffer
 */
public class MockDriver implements Driver {

    private static final MockDriver INSTANCE = new MockDriver();

    static {
        load();
    }

    private static void load() {
        try {
            DriverManager.registerDriver(INSTANCE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        String originUrl = extractOriginUrl(url);
        Driver originDriver = DriverManager.getDriver(originUrl);
        Connection delegateConnection = originDriver.connect(originUrl, info);

        return Connection.class.cast(Proxy.newProxyInstance(
                MockDriver.class.getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionInvocationHandler(delegateConnection)
        ));
    }

    private Driver getOriginDriver(String url) throws SQLException {
        String originUrl = extractOriginUrl(url);
        return DriverManager.getDriver(originUrl);
    }

    private static String extractOriginUrl(String url) {
        if (null == url) return null;
        if (url.startsWith(Constants.DRIVER_PREFIX)) return url.substring(Constants.DRIVER_PREFIX.length());
        return url;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return null != url && url.startsWith(Constants.DRIVER_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        Driver originDriver = getOriginDriver(url);
        return originDriver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return Constants.MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return Constants.MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
