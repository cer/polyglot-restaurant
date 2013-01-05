package net.chrisrichardson.polyglotpersistence.ordermanagement.hibernate;

import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.Order;
import net.chrisrichardson.polyglotpersistence.ordermanagement.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateOrderRepositoryImpl extends HibernateDaoSupport implements OrderRepository {

  @Autowired
  public HibernateOrderRepositoryImpl(HibernateTemplate template) {
    setHibernateTemplate(template);
  }

  @Override
  public void add(Order order) {
    getHibernateTemplate().save(order);
  }
}
