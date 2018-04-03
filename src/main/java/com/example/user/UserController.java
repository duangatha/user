package com.example.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user REST endpoints
 */
@RestController
public class UserController
{
    private final UserRepository userRepository;

    @Autowired
    UserController(UserRepository userRepository)
    {
        this.userRepository = userRepository;

    }

    /**
     * Gets the list of all users in the repository
     * @return The list of users
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    /**
     * Get the user with the specific user name
     * @param username The username of the entity to get
     * @return A response containing the user entity and found HTTP status if found, otherwise returns a response with the not found status
     */
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable String username)
    {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<User>(user.get(), HttpStatus.FOUND);
    }

    /**
     * Adds a new user to the repository 
     * @param user The user to add
     * @return A response containing the created user entity
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent())
        {
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }

        User savedUser = userRepository.save(user);

        return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Deletes the user with the specified username
     * @param username The username of the entity to remove
     * @return A response containing the user entity and no content HTTP status if deleted, otherwise returns a response with the not found status
     */
    @RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable String username)
    {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent())
        {
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(existingUser.get());

        return new ResponseEntity<User>(existingUser.get(), HttpStatus.NO_CONTENT);
    }

    /**
     * Updates the user with the specified username
     * @param username The username of the entity to update
     * @param user The user entity to update it with
     * @return A response containing the user entity and ok HTTP status if deleted, otherwise returns a response with the not found status
     */
    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user)
    {
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        
        user.setId(existingUser.get().getId());
        
        if(!user.hasPassword())
        {
            user.setHashedPassword(existingUser.get().getHashedPassword());
        }
        
        User savedUser = userRepository.save(user);

        return new ResponseEntity<User>(savedUser, HttpStatus.OK);
    }

}
