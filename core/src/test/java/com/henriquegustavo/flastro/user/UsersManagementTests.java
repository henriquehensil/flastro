package com.henriquegustavo.flastro.user;

import com.henriquegustavo.flasto.impl.DatabaseImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

// todo concurrency tests
final class UsersManagementTests {

    private final @NotNull Users users = new DatabaseImpl().getRoot().getUsers();

    @Test
    public void create() {
        final @NotNull User user = users.create("test", "test".toCharArray());
        final @NotNull User user2 = users.create("test", "test".toCharArray());

        Assertions.assertNotNull(user);
        Assertions.assertNotEquals(user2, user); // Ids must be different

        Assertions.assertTrue(users.get(user.getId()).isPresent());
        Assertions.assertTrue(users.get(user2.getId()).isPresent());

        Assertions.assertFalse(users.get(user.getName()).isEmpty());
        Assertions.assertFalse(users.get(user2.getName()).isEmpty());
    }

    @Test
    public void passwordSearch() {
        @NotNull Set<@NotNull User> usersCreated = Set.of(
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray())
                );

        // find by password
        @NotNull Set<@NotNull User> usersFound = users.get("test".toCharArray());
        Assertions.assertEquals(3, usersFound.size());

        Assertions.assertTrue(usersFound.containsAll(usersCreated));
    }

    @Test
    public void passwordSearchNotFound() {
        @NotNull Set<@NotNull User> usersCreated = Set.of(
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray())
        );

        @NotNull Set<@NotNull User> usersFound = users.get("non existent password".toCharArray());
        Assertions.assertEquals(0, usersFound.size());
        Assertions.assertTrue(usersFound.isEmpty());

        Assertions.assertFalse(usersFound.containsAll(usersCreated));
    }

    @Test
    @DisplayName("test the comum search by name scenario")
    public void nameSearch() {
        @NotNull Set<@NotNull User> usersCreated = Set.of(
                users.create("Henrique Gustavo Sousa", "test".toCharArray()),
                users.create("Alana Sousa Santos", "test".toCharArray()),
                users.create("Vicente Roger Santos de Sousa", "test".toCharArray())
        );

        // Test the first combination "Sousa"
        @NotNull Set<@NotNull User> usersFound = users.get("sousa"); // ignore case

        Assertions.assertEquals(3, usersFound.size());
        Assertions.assertTrue(usersFound.containsAll(usersCreated));

        for (@NotNull User user : usersFound) {
            Assertions.assertTrue(user.getName().toLowerCase().contains("sousa".toLowerCase()));
        }

        // Test the first combination "Santos"
        usersFound = users.get("SANTOS"); // ignore case
        Assertions.assertEquals(2, usersFound.size());

        Assertions.assertFalse(usersFound.containsAll(usersCreated));
        Assertions.assertTrue(usersCreated.containsAll(usersFound));

        for (@NotNull User user : usersFound) {
            Assertions.assertTrue(user.getName().toLowerCase().contains("SANTOS".toLowerCase()));
        }
    }

    @Test
    public void NameSearchNotFound() {
        @NotNull Set<@NotNull User> usersCreated = Set.of(
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray())
        );

        @NotNull Set<@NotNull User> usersFound = users.get("A non existent name");
        Assertions.assertTrue(usersFound.isEmpty());
        Assertions.assertFalse(usersFound.containsAll(usersCreated));
    }

    @Test
    @DisplayName("found the user with exactly same name")
    public void nameSearchExactly() {
        users.create("Henrique Gustavo Sousa", "test".toCharArray());

        @NotNull Set<@NotNull User> usersFound = users.get("Henrique Gustavo SOUSA"); // ignore case
        Assertions.assertFalse(usersFound.isEmpty());
        Assertions.assertEquals(1, usersFound.size());
    }

    @Test
    @DisplayName("Test the unmodifiable collections")
    public void unmodifiableUsers() {
        @NotNull Set<@NotNull User> usersCreated = Set.of(
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray()),
                users.create("", "test".toCharArray())
        );

        @NotNull Collection<@NotNull User> all = users.getAll();

        Assertions.assertEquals(all.size(), usersCreated.size());

        Assertions.assertThrows(UnsupportedOperationException.class, all::clear);
    }

    @Test
    public void removeById() {
        @NotNull UUID id = users.create("Shaolin", "test".toCharArray()).getId();
        Assertions.assertTrue(users.get(id).isPresent());

        Assertions.assertTrue(users.remove(id));
        Assertions.assertFalse(users.remove(UUID.randomUUID())); // non existent id
    }

    @Test
    public void removeByUser() {
        @NotNull User user = users.create("Shaolin", "test".toCharArray());
        Assertions.assertTrue(users.get(user.getId()).isPresent());
        Assertions.assertTrue(users.remove(user.getId()));
        Assertions.assertFalse(users.remove(user));
    }

    @Test
    public void setUserName() {
        @NotNull User user = users.create("Shaolin", "test".toCharArray());
        Assertions.assertFalse(users.get("shaolin").isEmpty());

        @NotNull String firstName = user.getName();
        Assertions.assertEquals(user.getName(), firstName);

        Assertions.assertTrue(users.setName(user, "Frederico"));

        Assertions.assertNotEquals(user.getName(), firstName);

        Assertions.assertTrue(users.get("shaolin").isEmpty());
        Assertions.assertFalse(users.get("Frederico").isEmpty());
    }

    @Test
    @DisplayName("set name and password of a user that not exist")
    public void setUserNotFound() {
        Assertions.assertFalse(users.setName(UUID.randomUUID(), "Test"));
        Assertions.assertFalse(users.setPassword(UUID.randomUUID(), "Test".toCharArray()));
    }

    @Test
    public void setUserPassword() {
        @NotNull User user = users.create("Shaolin", "myPassword1".toCharArray());
        Assertions.assertFalse(users.get("myPassword1".toCharArray()).isEmpty());

        char @NotNull [] firstPassword = user.getPassword();
        Assertions.assertEquals(user.getPassword(), firstPassword);

        Assertions.assertTrue(users.setPassword(user, "passwordChanged".toCharArray()));

        Assertions.assertNotEquals(user.getPassword(), firstPassword);

        Assertions.assertTrue(users.get("myPassword1".toCharArray()).isEmpty());
        Assertions.assertFalse(users.get("passwordChanged".toCharArray()).isEmpty());
    }
}