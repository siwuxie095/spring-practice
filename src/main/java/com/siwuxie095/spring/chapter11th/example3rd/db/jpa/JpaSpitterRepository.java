package com.siwuxie095.spring.chapter11th.example3rd.db.jpa;

import com.siwuxie095.spring.chapter11th.example3rd.db.SpitterRepository;
import com.siwuxie095.spring.chapter11th.example3rd.domain.Spitter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-25 21:46:42
 */
@SuppressWarnings("all")
@Repository
public class JpaSpitterRepository implements SpitterRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public Spitter save(Spitter spitter) {
        entityManager.persist(spitter);
        return spitter;
    }

    @Override
    public Spitter findOne(long id) {
        return entityManager.find(Spitter.class, id);
    }

    @Override
    public Spitter findByUsername(String username) {
        return (Spitter) entityManager.createQuery("select s from Spitter s where s.username=?").setParameter(1, username).getSingleResult();
    }

    @Override
    public List<Spitter> findAll() {
        return (List<Spitter>) entityManager
                .createQuery("select s from Spitter s")
                .getResultList();
    }

}
