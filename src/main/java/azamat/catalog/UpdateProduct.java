package azamat.catalog;

import azamat.catalog.entity.Category;
import azamat.catalog.entity.Characteristic;
import azamat.catalog.entity.CharacteristicMeanning;
import azamat.catalog.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UpdateProduct {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        try {
            manager.getTransaction().begin();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Выберите товар: ");
            long updateProduct = Long.parseLong(in.readLine());
            Product product = manager.find(Product.class, updateProduct);
                System.out.print("Введите новое название товара: ");
            String newName = in.readLine();
            if(newName.equals("")){
                product.setName(product.getName());
            }else {
                product.setName(newName);
            }
            System.out.print("Введите новую цену товара: ");
            String newPrice = in.readLine();
            if(newPrice.equals("")){
                product.setPrice(product.getPrice());
            }else {
                boolean isnumber = newPrice.matches("\\d+$");
                while(isnumber == false){
                    System.out.println("Введите число без других символов и букв: ");
                    newPrice = in.readLine();
                    isnumber = newPrice.matches("\\d+$");
                }
                int NewPrice = Integer.parseInt(newPrice);
                product.setPrice(NewPrice);
            }
            TypedQuery<Characteristic> characterisricTypedQuery = manager.createQuery(
                    "select c from Characteristic c where c.category = ?1", Characteristic.class
            );characterisricTypedQuery.setParameter(1, product.getCategory());
            List<Characteristic> characteristics = characterisricTypedQuery.getResultList();
            for(Characteristic characteristic: characteristics ){
                System.out.println("Введите новый " + characteristic.getName() + ": ");
                String newCharMean = in.readLine();
                TypedQuery<CharacteristicMeanning> characterisricMeanTypedQuery = manager.createQuery(
                        "select c from CharacteristicMeanning c where c.product.id = ?1 and c.characteristic.id = ?2", CharacteristicMeanning.class
                );characterisricMeanTypedQuery.setParameter(1, product.getId());
                characterisricMeanTypedQuery.setParameter(2, characteristic.getId());
                if(newCharMean.equals("")){
                    characterisricMeanTypedQuery.getSingleResult().setName(characterisricMeanTypedQuery.getSingleResult().getName());
                }else {
                    characterisricMeanTypedQuery.getSingleResult().setName(newCharMean);
                }
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
