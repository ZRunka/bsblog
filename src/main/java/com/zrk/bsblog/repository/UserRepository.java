package com.zrk.bsblog.repository;

import com.zrk.bsblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long > {

//    User saveOrUpdateUser(User user);
//
//    void deleteUser(Long id);
//
//    User getUserById(Long id);
//
//    List<User> listUser();
    Page<User> findByNameLike(String name, Pageable pageable);
    User findByUsername(String username);
    List<User> findByUsernameIn(Collection<String> usernames);
}
