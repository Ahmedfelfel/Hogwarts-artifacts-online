package com.felfel.hogwarts_artifacts_online.user;

import com.felfel.hogwarts_artifacts_online.system.exception.OpjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final String OBJECT_TYPE="user";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll()
    {
        List<User> foundUsers = this.userRepository.findAll();
        if(foundUsers.isEmpty())
        {
            throw new OpjectNotFoundException(OBJECT_TYPE);
        }
        return foundUsers;
    }
    public User findUserById(Integer userId)
    {
        return this.userRepository.findById(userId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,userId));
    }
    public User saveUser(User newUser)
    {
        return this.userRepository.save(newUser);
    }
    public User updateUser(Integer userId, User newUser) {
        return this.userRepository.findById(userId).map(
                (oldUser)->{
                    oldUser.setUsername(newUser.getUsername());
                    oldUser.setEnabled(newUser.getEnabled());
                    oldUser.setRoles(newUser.getRoles());
                    return this.userRepository.save(oldUser);
                }).orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,userId));
    }
    public void deleteUserById(Integer userId)
    {
        this.userRepository.findById(userId)
                .orElseThrow(()->new OpjectNotFoundException(OBJECT_TYPE,userId));
        this.userRepository.deleteById(userId);
    }
}
