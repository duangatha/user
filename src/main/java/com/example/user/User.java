package com.example.user;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Model of a user entity
 */
@Entity
public class User
{
    /**
     * Auto-generated numeric id of the user 
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The username of the user
     */
    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The email of the user
     */
    @NotNull
    @Column(nullable = false)
    @Convert(converter = StringConverter.class)
    private String email;

    /**
     * The first name of the user
     */
    @Convert(converter = StringConverter.class)
    private String firstname;

    /**
     * The last name of the user
     */
    @Convert(converter = StringConverter.class)
    private String lastname;

    /**
     * The password of the user
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", insertable = false, updatable = false)
    private String password;

    /**
     * The hashed password to be saved
     */
    @JsonIgnore
    @NotNull
    @Column(nullable = false)
    private String hashedPassword;

    private User()
    {
    }

    public User(final String username, final String email, final String firstname, final String lastname, final String password)
    {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }
    

    /**
     * Update the hashed password before database persistence
     */
    @PrePersist
    @PreUpdate
    private void hashPassword()
    {
        if (hasPassword())
        {
            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        }
    }
    
    /**
     * Helper function for checking if the entity has plain text password set on it
     * @return {@code True} if the entity has plain text password, {@code false} otherwise
     */
    public boolean hasPassword()
    {
        return !StringUtils.isEmpty(password);
    }

    /**
     * @return The id of the user
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @return The username of the user
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param id The id to set on the suer 
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return The user's email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @return The user's first name
     */
    public String getFirstname()
    {
        return firstname;
    }

    /**
     * @return The user's last name
     */
    public String getLastname()
    {
        return lastname;
    }
    
    /**
     * @param hashedPassword The hased password to set
     */
    public void setHashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    /**
     * @return The hashed password of the entity
     */
    public String getHashedPassword()
    {
        return hashedPassword;
    }


}
