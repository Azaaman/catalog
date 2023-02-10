package azamat.catalog;

import azamat.catalog.entity.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaLesson {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        Category category = manager.find(Category.class, 2L);
        System.out.println(category.getName());
        try {
            manager.getTransaction().begin();
            Category category3 = new Category();
            category3.setName("Клавиатуры");
            manager.persist(category3);
            manager.getTransaction().commit();
        }catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
