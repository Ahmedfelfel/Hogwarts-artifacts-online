package com.felfel.hogwarts_artifacts_online.user;

import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

/**
 * The type User service.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final String OBJECT_TYPE="user";

    /**
     * Instantiates a new User service.
     *
     * @param passwordEncoder the password encoder
     * @param userRepository  the user repository
     */
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<User> findAll()
    {
        List<User> foundUsers = this.userRepository.findAll();
        if(foundUsers.isEmpty())
        {
            throw new OpjectNotFoundException(OBJECT_TYPE);
        }
        return foundUsers;
    }

    /**
     * Find user by id user.
     *
     * @param userId the user id
     * @return the user
     */
    public User findUserById(Integer userId)
    {
        return this.userRepository.findById(userId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,userId));
    }

    /**
     * Save user user.
     *
     * @param newUser the new user
     * @return the user
     */
    public User saveUser(User newUser)
    {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    /**
     * Update user user.
     *
     * @param userId  the user id
     * @param newUser the new user
     * @return the user
     */
    public User updateUser(Integer userId, User newUser) {
        return this.userRepository.findById(userId).map(
                (oldUser)->{
                    oldUser.setUsername(newUser.getUsername());
                    oldUser.setEnabled(newUser.getEnabled());
                    oldUser.setRoles(newUser.getRoles());
                    return this.userRepository.save(oldUser);
                }).orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,userId));
    }

    /**
     * Delete user by id.
     *
     * @param userId the user id
     */
    public void deleteUserById(Integer userId)
    {
        this.userRepository.findById(userId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(user -> new UserPrincipals(user)
                )
                .orElseThrow(()->new UsernameNotFoundException("User with username: "+username+" not found"));
    }
}
