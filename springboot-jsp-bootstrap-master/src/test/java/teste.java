import com.mightyjava.model.Users;
import com.mightyjava.repository.RoleRepository;
import com.mightyjava.repository.UserRepository;
import com.mightyjava.service.impl.UserServiceImpl;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class teste {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Users user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setId(1L);
        user.setUserName("testUser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
    }


    @Test
    public void testAddUserSuccess() throws JSONException {
        when(userRepository.save(any(Users.class))).thenReturn(user);

        String response = userService.addUser(user);

        verify(userRepository, times(1)).save(user);


        JSONObject jsonResponse = new JSONObject(response);
        assertEquals("success", jsonResponse.getString("status"));
        assertEquals("Updated Confirmation", jsonResponse.getString("title"));
        assertEquals("null Updated successfully.", jsonResponse.getString("message"));
    }

    @Test
    public void testFindOneSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Users foundUser = userService.findOne(1L);

        verify(userRepository, times(1)).findById(1L);
        assertEquals("testUser", foundUser.getUserName());
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        String response = userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);


        String expectedResponse = "{\"message\":\"User Deleted Successfully.\"}";
        assertEquals(expectedResponse, response);
    }
}
