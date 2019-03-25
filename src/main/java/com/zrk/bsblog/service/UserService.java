package com.zrk.bsblog.service;

import com.zrk.bsblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveOrUpateUser(User user);

    User registerUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    void removeUsersInBatch(List<User> users);

    List<User> listUsersByUsernames(Collection<String> usernames);

    Page<User> listUsersByNameLike(String name, Pageable pageable);

}
