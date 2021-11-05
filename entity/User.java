package by.epamtc.stanislavmelnikov.entity;

public class User {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private boolean isAdmin;
    private static User currentUser;

    public User() {

    }

    public User(String login, String password, String firstName, String lastName, int age, String email, boolean isAdmin) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return ((null == login) ? 0 : login.hashCode()) + ((null == password) ? 0 : password.hashCode()) +
                ((null == firstName) ? 0 : firstName.hashCode()) + ((null == lastName) ? 0 : lastName.hashCode()) +
                ((null == email) ? 0 : email.hashCode()) + prime * age + ((Boolean) isAdmin).hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        if (age != user.age) {
            return false;
        }
        if (isAdmin != user.isAdmin) {
            return false;
        }
        if (null == login) {
            return (login == user.login);
        } else {
            if (!login.equals(user.login)) {
                return false;
            }
        }
        if (null == password) {
            return (password == user.password);
        } else {
            if (!password.equals(user.password)) {
                return false;
            }
        }
        if (null == firstName) {
            return (firstName == user.firstName);
        } else {
            if (!firstName.equals(user.firstName)) {
                return false;
            }
        }
        if (null == lastName) {
            return (lastName == user.lastName);
        } else {
            if (!lastName.equals(user.lastName)) {
                return false;
            }
        }
        if (null == email) {
            return (email == user.email);
        } else {
            if (!email.equals(user.email)) {
                return false;
            }
        }
        return true;
    }
}