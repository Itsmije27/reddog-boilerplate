package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FileUserRepository implements UserRepository{
	private final Path filePath;
	   private final Map<String, User> users = new ConcurrentHashMap<>();
	   public FileUserRepository(Path filePath) {
	       this.filePath = filePath;
	       load();
	   }
	   public final void load() {
	       users.clear();
	       if (!Files.exists(filePath)) {
	           return;
	       }
	       try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
	           String line;
	           while ((line = reader.readLine()) != null) {
	               if (line.isBlank()) continue;
	               String[] parts = line.split(";", -1);
	               if (parts.length >= 3) {
	                   User user = new User(parts[0], parts[1], parts[2]);
	                   users.put(user.getUsername().toLowerCase(), user);
	               }
	           }
	       } catch (IOException e) {
	           throw new IllegalStateException("Fehler beim Laden der Benutzer aus " + filePath, e);
	       }
	   }
	   public void persist() {
	       try {
	           if (filePath.getParent() != null) {
	               Files.createDirectories(filePath.getParent());
	           }
	           try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
	               for (User user : users.values()) {
	                   writer.write(user.getUsername() + ";" + user.getPasswordHash() + ";" + user.getRole());
	                   writer.newLine();
	               }
	           }
	       } catch (IOException e) {
	           throw new IllegalStateException("Fehler beim Speichern der Benutzer in " + filePath, e);
	       }
	   }
	   @Override
	   public Optional<User> findByUsername(String username) {
	       if (username == null) return Optional.empty();
	       return Optional.ofNullable(users.get(username.trim().toLowerCase()));
	   }
	   @Override
	   public void save(User user) {
	       users.put(user.getUsername().toLowerCase(), user);
	       persist();
	   }
	   @Override
	   public boolean existsByUsername(String username) {
	       return findByUsername(username).isPresent();
	   }

}
