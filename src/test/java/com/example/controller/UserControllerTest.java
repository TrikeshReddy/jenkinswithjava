package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.repository.User;
import com.example.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Test
    public void testGetUsers() throws Exception {
    	User user1=new User(1L, "John","hello");
    	User user2= new User(2L, "Jane","yelo");
        List<User> userList = Arrays.asList(user1,user2);

        when(userRepository.findAll()).thenReturn(userList);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{'id': 1, 'name': 'John'}, {'id': 2, 'name': 'Jane'}]"));
    }

    @Test
    public void testGetUser() throws Exception {
        User user = new User(1L, "John","hello");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{'id': 1, 'name': 'John'}"));
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User(1L, "John","hello");

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .content("{\"name\":\"John\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"id\":1,\"name\":\"John\"}", content);
    }

    // Add similar tests for other methods like updateUser, deleteUser, and any other custom methods.

    // Note: For a complete test suite, you should also write tests for exception handling, invalid input, etc.
}
