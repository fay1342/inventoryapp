package edu.au.cpsc.inventory.partspecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PartSpecificationTest {

  @Test
  public void given_new_part_specification_then_suppliers_empty() {
    PartSpecification ps = new PartSpecification();
    assertEquals(0, ps.getSuppliers().size());
  }

  @Test
  public void given_no_suppliers_when_supplier_added_then_has_one_supplier() {
    PartSpecification ps = new PartSpecification();
    ps.addSupplier(new Supplier());
    assertEquals(1, ps.getSuppliers().size());
  }

  @Test
  public void remove_all_previously_assigned_suppliers_to_a_part_specification() {
    PartSpecification ps = new PartSpecification();
    ps.addSupplier(new Supplier());
    ps.addSupplier(new Supplier());
    assertEquals(2, ps.getSuppliers().size());
    ps.removeSupplier();
    assertEquals(0, ps.getSuppliers().size());
  }

}
