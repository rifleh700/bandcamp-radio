package io.rifleh700.bcradio.client;

public interface ClientProxyFactory {

    <T> T build(Class<T> api);
}
