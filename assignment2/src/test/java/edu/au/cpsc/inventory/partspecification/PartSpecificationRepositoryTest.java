package edu.au.cpsc.inventory.partspecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartSpecificationRepositoryTest {


  private PartSpecificationRepository partSpecificationRepository;
  private CreatePartSpecification useCase;
  private SupplierRepository supplierRepository;
  private PartRequisitionRequestRepository partRequisitionRequestRepository;

  @BeforeEach
  public void setUp() {
    partSpecificationRepository = new PartSpecificationRepository();
    supplierRepository = new SupplierRepository();
    partRequisitionRequestRepository = new PartRequisitionRequestRepository();
    useCase = new CreatePartSpecification(partSpecificationRepository, supplierRepository,
        partRequisitionRequestRepository);
  }

  @Test
  public void when_part_specification_saved_twice_id_not_changed() {
    PartSpecification ps = new PartSpecification();
    PartSpecificationRepository repository = new PartSpecificationRepository();
    repository.save(ps);
    Long oldId = ps.getId();
    repository.save(ps);
    assertEquals(oldId, ps.getId());
  }

  @Test
  public void same_object_stored_in_repositories_more_than_once() {

    PartSpecification ps = new PartSpecification();
    partSpecificationRepository.save(ps);
    partSpecificationRepository.save(ps);
    var specs = useCase.getPartSpecifications();
    Long idSpecs1 = specs.get(0).getId();
    Long idSpecs2 = specs.get(1).getId();
    Map<Long, PartSpecification> map = new HashMap<>();
    map.put(ps.getId(), ps);
    map.put(ps.getId(), ps);

    assertEquals(1, map.size());
    assertEquals(2, specs.size());
    assertEquals(idSpecs1, idSpecs2);

    Supplier sp = new Supplier();
    supplierRepository.save(sp);
    supplierRepository.save(sp);
    var suppliers = useCase.getSuppliers();

    Long idSuppliers1 = suppliers.get(0).getId();
    Long idSuppliers2 = suppliers.get(1).getId();
    Map<Long, Supplier> mapSupplier = new HashMap<>();
    mapSupplier.put(sp.getId(), sp);
    mapSupplier.put(sp.getId(), sp);

    assertEquals(1, mapSupplier.size());
    assertEquals(2, suppliers.size());
    assertEquals(idSuppliers1, idSuppliers2);


  }
}


