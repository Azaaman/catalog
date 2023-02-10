package azamat.catalog.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;

        @OneToMany(mappedBy = "product")
        private List<CharacteristicMeanning> charsMeaning;

        private String name;

        private Integer price;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Category getCategory() {
                return category;
        }

        public void setCategory(Category category) {
                this.category = category;
        }

        public List<CharacteristicMeanning> getCharsMeaning() {
                return charsMeaning;
        }

        public void setCharsMeaning(List<CharacteristicMeanning> charsMeaning) {
                this.charsMeaning = charsMeaning;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Integer getPrice() {
                return price;
        }

        public void setPrice(Integer price) {
                this.price = price;
        }
}
