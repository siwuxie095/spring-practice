package com.siwuxie095.spring.chapter11th.example2nd.db.hibernate4;

import com.siwuxie095.spring.chapter11th.example2nd.db.SpitterRepository;
import com.siwuxie095.spring.chapter11th.example2nd.db.SpittleRepository;
import com.siwuxie095.spring.chapter11th.example2nd.domain.Spitter;
import com.siwuxie095.spring.chapter11th.example2nd.domain.Spittle;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-24 22:59:19
 */
@SuppressWarnings("all")
@Repository
public class HibernateSpittleRepository implements SpittleRepository {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateSpittleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();//<co id="co_RetrieveCurrentSession"/>
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public List<Spittle> findRecent() {
        return findRecent(10);
    }

    @Override
    public List<Spittle> findRecent(int count) {
        return (List<Spittle>) spittleCriteria()
                .setMaxResults(count)
                .list();
    }

    @Override
    public Spittle findOne(long id) {
        return (Spittle) currentSession().get(Spittle.class, id);
    }

    @Override
    public Spittle save(Spittle spittle) {
        Serializable id = currentSession().save(spittle);
        return new Spittle(
                (Long) id,
                spittle.getSpitter(),
                spittle.getMessage(),
                spittle.getPostedTime());
    }

    @Override
    public List<Spittle> findBySpitterId(long spitterId) {
        return spittleCriteria()
                .add(Restrictions.eq("spitter.id", spitterId))
                .list();
    }

    @Override
    public void delete(long id) {
        currentSession().delete(findOne(id));
    }

    public List<Spittle> findAll() {
        return (List<Spittle>) spittleCriteria().list();
    }

    private Criteria spittleCriteria() {
        return currentSession()
                .createCriteria(Spittle.class)
                .addOrder(Order.desc("postedTime"));
    }

}
