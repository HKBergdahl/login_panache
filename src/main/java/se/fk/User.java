package fk.se;

import java.util.Objects;

public class User {
    private String id;
    private String name;
    private String email;

    public User() {
    }

    public User (String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return Objects.equals(id, user.id);

//        // Konvertera objektet till Email-typ och jämför fälten
//        Email other = (Email) obj;
//        return id == other.id &&
//                Objects.equals(name, other.name) &&
//                Objects.equals(email, other.email);

    }

    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}