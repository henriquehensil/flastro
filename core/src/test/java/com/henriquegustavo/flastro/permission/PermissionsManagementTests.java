package com.henriquegustavo.flastro.permission;

import com.henriquegustavo.flastro.exception.NoSuchUserException;
import com.henriquegustavo.flastro.user.Root;
import com.henriquegustavo.flastro.user.User;
import com.henriquegustavo.flastro.user.Users;
import com.henriquegustavo.flasto.impl.DatabaseImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

// todo concurrency test
final class PermissionsManagementTests {

    // Classes

    @Nested
    // todo the user object must to be into to Users
    final class UserPermissionsTest {

        private final @NotNull Users users;
        private final @NotNull UserPermissions userPermissions;

        UserPermissionsTest() {
            @NotNull Permission permission = Permission.CREATE_FILE;
            @NotNull Root root = new DatabaseImpl().getRoot();

            this.users = root.getUsers();
            this.userPermissions = this.users.getUserPermissions(permission.getCode());

            Assertions.assertEquals(userPermissions.getPermission(), permission);
        }

        @Test
        public void putUser() {
            @NotNull User user = users.create("User", "".toCharArray());

            Assertions.assertTrue(userPermissions.put(user));
            Assertions.assertFalse(userPermissions.put(user));

            Assertions.assertTrue(userPermissions.has(user));

            user = users.create("anotherUser", "".toCharArray());
            Assertions.assertFalse(userPermissions.has(user));
        }

        @Test
        public void removeUser() {
            @NotNull User user = users.create("User", "".toCharArray());
            userPermissions.put(user);

            Assertions.assertTrue(userPermissions.remove(user));
            Assertions.assertFalse(userPermissions.has(user));

            user = users.create("anotherUser", "".toCharArray());
            Assertions.assertFalse(userPermissions.remove(user));
        }

        @Test
        @DisplayName("test if users is not founded in Users class")
        public void userNotFound() {
            @NotNull User user = users.create("User", "".toCharArray());
            users.remove(user);

            Assertions.assertThrows(NoSuchUserException.class, () -> userPermissions.put(user));
        }
    }
}
