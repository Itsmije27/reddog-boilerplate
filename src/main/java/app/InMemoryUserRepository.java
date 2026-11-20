package app;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// TODO: Später ggf. durch die echte Datenschicht-Implementierung ersetzen.
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User darf nicht null sein.");
        }
        users.put(normalize(user.username()), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(normalize(username)));
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null) {
            return false;
        }
        return users.containsKey(normalize(username));
    }

    private String normalize(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }
}