package azamat.catalog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DeleteProduct {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Выберите товар: ");
            long deleteProduct = Long.parseLong(in.readLine());
            Query query1 = manager.createQuery(
                    "delete from Product p where p.id = ?1 "
            );
            query1.setParameter(1,deleteProduct);
            Query query2 = manager.createQuery(
                    "delete from CharacteristicMeanning c where c.product.id = ?1 "
            );
            query2.setParameter(1,deleteProduct);
            query2.executeUpdate();
            query1.executeUpdate();
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}