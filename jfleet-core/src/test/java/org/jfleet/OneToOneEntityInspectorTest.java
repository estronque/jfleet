/**
 * Copyright 2017 Jerónimo López Bezanilla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jfleet;

import static org.junit.Assert.assertEquals;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.jfleet.EntityFieldType.FieldTypeEnum;
import org.junit.Test;

public class OneToOneEntityInspectorTest {

    /*
     * If autogenerated IDs configured, related entity Id must be different from main class Id, and entity must be
     * previously persisted.
     * If not, related entity will not be persisted by JFleet and information will be lost.
     * The behaviour is the same of ManyToOne as related entity is not managed by JFleet and IDs must exists previously
     */
    @Entity
    public class ProductDetail {

        @Id
        private Long id;
        private String reference;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

    }

    @Entity
    public class Product {

        @Id
        private Long id;
        private String name;
        @OneToOne
        private ProductDetail product;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String reference) {
            this.name = reference;
        }

        public ProductDetail getProduct() {
            return product;
        }

        public void setProduct(ProductDetail product) {
            this.product = product;
        }

    }


    @Entity
    public class ProductJoin {

        @Id
        private Long id;
        @OneToOne
        @JoinColumn(name = "ref_id_product_fk")
        private ProductDetail productRef;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ProductDetail getProductRef() {
            return productRef;
        }

        public void setProduct(ProductDetail productRef) {
            this.productRef = productRef;
        }

    }

    @Test
    public void inspectAnEntityWithOneToOneRelationShip() {
        JpaEntityInspector inspector = new JpaEntityInspector(Product.class);
        EntityInfo entityInfo = inspector.inspect();

        FieldInfo id = entityInfo.findField("id");
        assertEquals(FieldTypeEnum.LONG, id.getFieldType().getFieldType());
        assertEquals("id", id.getColumnName());

        FieldInfo name = entityInfo.findField("name");
        assertEquals(FieldTypeEnum.STRING, name.getFieldType().getFieldType());
        assertEquals("name", name.getColumnName());

        FieldInfo street = entityInfo.findField("product.id");
        assertEquals(FieldTypeEnum.LONG, street.getFieldType().getFieldType());
        assertEquals("product_id", street.getColumnName());
    }


    @Test
    public void inspectAnEntityWithOneToOneJoinColumn() {
        JpaEntityInspector inspector = new JpaEntityInspector(ProductJoin.class);
        EntityInfo entityInfo = inspector.inspect();

        FieldInfo id = entityInfo.findField("id");
        assertEquals(FieldTypeEnum.LONG, id.getFieldType().getFieldType());
        assertEquals("id", id.getColumnName());

        FieldInfo street = entityInfo.findField("productRef.id");
        assertEquals(FieldTypeEnum.LONG, street.getFieldType().getFieldType());
        assertEquals("ref_id_product_fk", street.getColumnName());
    }

}
