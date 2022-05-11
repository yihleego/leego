package io.leego.commons.sequence.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceErrorException;
import io.leego.commons.sequence.exception.SequenceNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Leego Yih
 */
public class HttpClientSequenceProvider implements SequenceProvider {
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected final HttpClient httpClient = HttpClient.newBuilder().build();
    protected final String uri;

    public HttpClientSequenceProvider(String uri) {
        this.uri = uri.endsWith("/") ? uri : uri + "/";
    }

    /**
     * Obtains the next sequence.
     *
     * @param key the key of the sequence.
     * @return the next sequence.
     */
    @Override
    public Long next(String key) {
        return this.next(key, 1).getBegin();
    }

    /**
     * Obtains the next segment from HTTP API.
     * <p><b>Example</b>
     * <pre>
     * ---> GET https://host:port/path/foo?size=1
     * Content-Type: application/json
     * ---> END HTTP
     *
     * <--- HTTP/1.1 200
     * Content-Type: application/json
     *
     * {"begin":1,"end":1,"increment":1}
     * <--- END HTTP
     * </pre>
     *
     * @param key  the key of the sequence.
     * @param size the size to be obtained
     * @return the next segment.
     */
    @Override
    public Segment next(String key, int size) {
        try {
            HttpRequest request = newRequest(this.uri + key + "?size=" + size, "GET", null);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Segment.class);
            } else if (response.statusCode() == 400) {
                throw new SequenceNotFoundException("There is no sequence '" + key + "'");
            } else {
                throw new SequenceErrorException("Failed to obtain sequence '" + key + "'");
            }
        } catch (IOException | InterruptedException e) {
            throw new SequenceErrorException("Failed to obtain sequence '" + key + "'", e);
        }
    }

    /**
     * Creates a sequence.
     * <p><b>Example</b>
     * <pre>
     * ---> POST https://host:port/path/foo
     * Content-Type: application/json
     *
     * {"key":"foo","value":0,"increment":1}
     * ---> END HTTP
     *
     * <--- HTTP/1.1 200
     * Content-Type: text/plain
     * <--- END HTTP
     * </pre>
     *
     * @param key       the key of the sequence.
     * @param value     the initialized value of the sequence.
     * @param increment the increment of the sequence.
     * @return <code>true</code> if the sequence is created.
     */
    @Override
    public boolean create(String key, Long value, Integer increment) {
        try {
            String body = "{\"key\":\"%s\",\"value\":%d,\"increment\":%d}".formatted(key, value, increment);
            HttpRequest request = newRequest(this.uri + key, "POST", body);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new SequenceErrorException("Failed to create sequence '" + key + "'", e);
        }
    }

    /**
     * Updates a sequence.
     * <p><b>Example</b>
     * <pre>
     * ---> PUT https://host:port/path/foo
     * Content-Type: application/json
     *
     * {"key":"foo","increment":1}
     * ---> END HTTP
     *
     * <--- HTTP/1.1 200
     * Content-Type: text/plain
     * <--- END HTTP
     * </pre>
     *
     * @param key       the key of the sequence.
     * @param increment the increment of the sequence.
     * @return <code>true</code> if the sequence is updated.
     */
    @Override
    public boolean update(String key, Integer increment) {
        try {
            String body = "{\"key\":\"%s\",\"increment\":%d}".formatted(key, increment);
            HttpRequest request = newRequest(this.uri + key, "PUT", body);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new SequenceErrorException("Failed to update sequence '" + key + "'", e);
        }
    }

    private HttpRequest newRequest(String uri, String method, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method(method, body != null ? HttpRequest.BodyPublishers.ofString(body) : HttpRequest.BodyPublishers.noBody())
                .build();
    }
}
