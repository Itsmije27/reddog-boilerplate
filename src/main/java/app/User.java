package app;

import java.util.Objects;

public class User {
	private final String username;
	private String passwordHash;
	private String role;
	
	public User(String username, String passwordHash, String role) {
		this.username = Objects.requireNonNull(username, "username darf nicht null sein").trim();
		this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash darf nicht null sein").trim();
		this.role = (role == null || role.isBlank()) ? "USER" : role.trim();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = Objects.requireNonNull(passwordHash);
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User user)) return false;
		return username.equalsIgnoreCase(user.username);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username.toLowerCase());
	}

}
