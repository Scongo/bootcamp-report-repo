package za.co.paygate.report.repository;

import za.co.paygate.report.database.Order;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class OrderRepository {

        private EntityManager entityManager;

        public OrderRepository(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
        public Optional<Order> findById(Integer id) {
            Order order = entityManager.find(Order.class, id);
            return order != null ? Optional.of(order) : Optional.empty();
        }
        public List<Order> findAll() {
            return entityManager.createQuery("from Order").getResultList();
        }

        public Optional<Order> save(Order order) {
            try {
                entityManager.getTransaction().begin();
                //entityManager.persist(order);
                entityManager.merge(order);
                entityManager.getTransaction().commit();
                return Optional.of(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
}
