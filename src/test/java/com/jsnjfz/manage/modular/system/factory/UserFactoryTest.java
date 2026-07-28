package com.jsnjfz.manage.modular.system.factory;

import com.jsnjfz.manage.modular.system.model.User;
import com.jsnjfz.manage.modular.system.transfer.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserFactoryTest {

    @Test
    void selfProfileEditCannotChangeDepartment() {
        User oldUser = new User();
        oldUser.setDeptid(10);
        oldUser.setName("old");
        UserDto changes = new UserDto();
        changes.setDeptid(99);
        changes.setName("new");

        User updated = UserFactory.editOwnProfile(changes, oldUser);

        assertEquals(10, updated.getDeptid());
        assertEquals("new", updated.getName());
    }

    @Test
    void administratorEditCanChangeDepartment() {
        User oldUser = new User();
        oldUser.setDeptid(10);
        UserDto changes = new UserDto();
        changes.setDeptid(99);

        User updated = UserFactory.editUser(changes, oldUser);

        assertEquals(99, updated.getDeptid());
    }
}
