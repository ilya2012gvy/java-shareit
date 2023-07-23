package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findById(long id, long user) {
        return get("/" + id, user);
    }

    public ResponseEntity<Object> getAllItems(long id, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );

        return get("?from={from}&size={size}", id, parameters);
    }

    public ResponseEntity<Object> searchByText(String text, long user, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );

        return get("/search?text={text}&from={from}&size={size}", user, parameters);
    }

    public ResponseEntity<Object> addItem(ItemDto item, long id) {
        return post("", id, item);
    }

    public ResponseEntity<Object> updateItem(ItemDto item, long id, long user) {
        return patch("/" + item, id, user);
    }

    public void deleteItem(long id) {
        delete("/" + id);
    }

    public ResponseEntity<Object> addComment(CommentDto comment, long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}