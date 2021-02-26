package com.siwuxie095.spring.chapter11th.example4th.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Jiajing Li
 * @date 2021-02-26 08:21:07
 */
@SuppressWarnings("all")
public class SpitterRepositoryImpl implements SpitterSweeper {

    @PersistenceContext
    private EntityManager em;

    @Override
    public int eliteSweep() {
        String update =
                "UPDATE Spitter spitter " +
                        "SET spitter.status = 'Elite' " +
                        "WHERE spitter.status = 'Newbie' " +
                        "AND spitter.id IN (" +
                        "SELECT s FROM Spitter s WHERE (" +
                        "  SELECT COUNT(spittles) FROM s.spittles spittles) > 10000" +
                        ")";
        return em.createQuery(update).executeUpdate();
    }

}
