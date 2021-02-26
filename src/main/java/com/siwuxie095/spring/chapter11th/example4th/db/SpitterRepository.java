package com.siwuxie095.spring.chapter11th.example4th.db;

import com.siwuxie095.spring.chapter11th.example4th.domain.Spitter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-26 08:19:01
 */
@SuppressWarnings("all")
public interface SpitterRepository extends JpaRepository<Spitter, Long>, SpitterSweeper {

    Spitter findByUsername(String username);

    List<Spitter> findByUsernameOrFullNameLike(String username, String fullName);

}
