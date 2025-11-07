package com.example.usermanagementmicroservice.services;

import com.example.usermanagementmicroservice.entities.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import com.example.usermanagementmicroservice.UserManagementMicroserviceTestConfig;
import com.example.usermanagementmicroservice.dtos.UserDTO;
import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.List;
import java.util.UUID;

@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
public class UserServiceIntegrationTests extends UserManagementMicroserviceTestConfig {

    @Autowired
    UserService userService;

    @Test
    public void testGetCorrect() {
        List<UserDTO> userDTOList = userService.findUsers();
        assertEquals("Test Insert User", 1, userDTOList.size());
    }

    @Test
    public void testInsertCorrectWithGetById() {
        UserDetailsDTO u = new UserDetailsDTO("John", "Somewhere Else street", 22, "MyPassword1234", Role.USER);
        UUID insertedID = userService.insert(u);

        UserDTO insertedUser = new UserDTO(insertedID, u.getUsername(), u.getAge(), u.getRole());
        UserDTO fetchedUser = userService.findUserById(insertedID);

        assertEquals("Test Inserted User", insertedUser, fetchedUser);
    }

    @Test
    public void testInsertCorrectWithGetAll() {
        UserDetailsDTO u = new UserDetailsDTO("John", "Somewhere Else street", 22, "MyPassword1234", Role.USER);
        userService.insert(u);

        List<UserDTO> userDTOList = userService.findUsers();
        assertEquals("Test Inserted Users", 2, userDTOList.size());
    }
}
