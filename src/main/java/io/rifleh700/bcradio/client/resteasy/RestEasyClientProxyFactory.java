package io.rifleh700.bcradio.client.resteasy;

import io.rifleh700.bcradio.client.ClientProxyFactory;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class RestEasyClientProxyFactory implements ClientProxyFactory {

    private final ResteasyWebTarget target;

    public RestEasyClientProxyFactory(String uri) {

        //TODO: close
        this.target = (ResteasyWebTarget) ClientBuilder.newClient().target(uri);
    }

    @Override
    public <T> T build(Class<T> api) {

        return target.proxy(api);
    }
}
