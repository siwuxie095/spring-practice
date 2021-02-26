package com.siwuxie095.spring.chapter11th.example4th.db;

import com.siwuxie095.spring.chapter11th.example4th.domain.Spittle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-26 08:20:25
 */
@SuppressWarnings("all")
public interface SpittleRepository extends JpaRepository<Spittle, Long>, SpittleRepositoryCustom {

    List<Spittle> findBySpitterId(long spitterId);

}
