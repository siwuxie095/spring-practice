package com.siwuxie095.spring.chapter11th.example4th.db;

import com.siwuxie095.spring.chapter11th.example4th.domain.Spittle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-26 08:21:41
 */
@SuppressWarnings("all")
public class SpittleRepositoryImpl implements SpittleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Spittle> findRecent() {
        return findRecent(10);
    }

    @Override
    public List<Spittle> findRecent(int count) {
        return (List<Spittle>) entityManager.createQuery("select s from Spittle s order by s.postedTime desc")
                .setMaxResults(count)
                .getResultList();
    }

}
