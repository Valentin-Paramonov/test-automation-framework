package paramonov.valentine.taf

import groovy.transform.PackageScope
import groovyx.net.http.RESTClient
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.params.HttpParams

@PackageScope
class PoolingRestClient extends RESTClient {
    PoolingRestClient(Object defaultURI) {
        super(defaultURI)
        client.params.with {
            setParameter('http.connection.timeout', 10_000)
            setParameter('http.socket.timeout', 30_000)
        }
    }

    @Override
    protected HttpClient createClient(HttpParams params) {
        new DefaultHttpClient(new PoolingClientConnectionManager(), params)
    }
}
