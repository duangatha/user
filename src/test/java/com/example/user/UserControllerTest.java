package com.example.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest
{

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    private List<User> userList = new ArrayList<>();
    
    private User existingUser;
    
    private User nonExistingUser;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * @throws Exception
     */
    @Before
    public void setup() throws Exception
    {
        this.userRepository.deleteAllInBatch();
        userList.clear();

        userList.add(userRepository.save(new User("userOne", "djohn@doefamily.com", "John", "Doe", "pass1")));
        userList.add(userRepository.save(new User("userTwo", "jsmith@smithfamily.com", "Jane", "Smith", "pass2")));
        
        existingUser = userList.get(0);
        
        nonExistingUser = new User("userThree", "doe@doefamily.com", "Tom", "Doe", "pass3");
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAllUsers() throws Exception
    {
        mockMvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.length()").value(userList.size()))
            .andExpect(jsonPath("$[0].id").value(userList.get(0).getId().intValue()))
            .andExpect(jsonPath("$[0].username").value(userList.get(0).getUsername()))
            .andExpect(jsonPath("$[0].email").value(userList.get(0).getEmail()))
            .andExpect(jsonPath("$[0].firstname").value(userList.get(0).getFirstname()))
            .andExpect(jsonPath("$[0].lastname").value(userList.get(0).getLastname()))
            .andExpect(jsonPath("$[1].id").value(userList.get(1).getId().intValue()))
            .andExpect(jsonPath("$[1].username").value(userList.get(1).getUsername()))
            .andExpect(jsonPath("$[1].email").value(userList.get(1).getEmail()))
            .andExpect(jsonPath("$[1].firstname").value(userList.get(1).getFirstname()))
            .andExpect(jsonPath("$[1].lastname").value(userList.get(1).getLastname()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getUserByUsernameFountd() throws Exception
    {
        mockMvc.perform(get("/user/" + existingUser.getUsername()))
           .andExpect(status().isFound())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.id").value(existingUser.getId().intValue()))
           .andExpect(jsonPath("$.username").value(existingUser.getUsername()))
           .andExpect(jsonPath("$.email").value(existingUser.getEmail()))
           .andExpect(jsonPath("$.firstname").value(existingUser.getFirstname()))
           .andExpect(jsonPath("$.lastname").value(existingUser.getLastname()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getUserByUsernameNotFound() throws Exception
    {
        mockMvc.perform(get("/user/ghostuser"))
        .andExpect(status().isNotFound());
    }

    @Test
    public void createNewUser() throws Exception
    {
        Gson gson = new Gson();
        
        this.mockMvc.perform(post("/user")
            .contentType(contentType)
            .content(gson.toJson(nonExistingUser)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.username").value(nonExistingUser.getUsername()))
            .andExpect(jsonPath("$.email").value(nonExistingUser.getEmail()))
            .andExpect(jsonPath("$.firstname").value(nonExistingUser.getFirstname()))
            .andExpect(jsonPath("$.lastname").value(nonExistingUser.getLastname()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void createExistingUser() throws Exception
    {
        Gson gson = new Gson();
        
        this.mockMvc.perform(post("/user")
            .contentType(contentType)
            .content(gson.toJson(existingUser)))
            .andExpect(status().isConflict());
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteExistingUser() throws Exception
    {
        mockMvc.perform(delete("/user/" + existingUser.getUsername()))
            .andExpect(status().isNoContent())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.id").value(existingUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(existingUser.getUsername()))
            .andExpect(jsonPath("$.email").value(existingUser.getEmail()))
            .andExpect(jsonPath("$.firstname").value(existingUser.getFirstname()))
            .andExpect(jsonPath("$.lastname").value(existingUser.getLastname()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void deleteNonExistingUser() throws Exception
    {
        mockMvc.perform(delete("/user/" + nonExistingUser.getUsername()))
            .andExpect(status().isNotFound());
    }

    /**
     * @throws Exception
     */
    @Test
    public void updateExistingUser() throws Exception
    {
        Gson gson = new Gson();
        
        this.mockMvc.perform(put("/user/" + existingUser.getUsername())
            .contentType(contentType)
            .content(gson.toJson(nonExistingUser)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.id").value(existingUser.getId()))
            .andExpect(jsonPath("$.username").value(nonExistingUser.getUsername()))
            .andExpect(jsonPath("$.email").value(nonExistingUser.getEmail()))
            .andExpect(jsonPath("$.firstname").value(nonExistingUser.getFirstname()))
            .andExpect(jsonPath("$.lastname").value(nonExistingUser.getLastname()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void updateNonExistingUser() throws Exception
    {
        Gson gson = new Gson();
        
        this.mockMvc.perform(put("/user/" + nonExistingUser.getUsername())
            .contentType(contentType)
            .content(gson.toJson(nonExistingUser)))
            .andExpect(status().isNotFound());
    }

}
