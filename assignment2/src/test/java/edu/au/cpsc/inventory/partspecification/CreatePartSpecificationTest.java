package edu.au.cpsc.inventory.partspecification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreatePartSpecificationTest {

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
  public void given_no_part_specifications_then_none_listed() {
    var specs = useCase.getPartSpecifications();
    assertEquals(0, specs.size());
  }

  @Test
  public void given_one_part_specification_then_one_listed() {
    PartSpecification partSpecification = new PartSpecification();
    partSpecification.setName("name");
    partSpecification.setDescription("description");
    Long id = partSpecificationRepository.save(partSpecification);

    var specs = useCase.getPartSpecifications();

    assertEquals(1, specs.size());
    assertEquals("name", specs.get(0).getName());
    assertEquals("description", specs.get(0).getDescription());
    assertEquals(id, specs.get(0).getId());
  }

  @Test
  public void given_two_part_specification_then_two_listed() {
    partSpecificationRepository.save(new PartSpecification());
    partSpecificationRepository.save(new PartSpecification());

    var specs = useCase.getPartSpecifications();

    assertEquals(2, specs.size());
  }

  @Test
  public void given_no_part_specifications_when_part_saved_then_one_part_present_in_repository() {
    CreatePartSpecification.PartSpecificationModel partSpecificationModel = new CreatePartSpecification.PartSpecificationModel();
    partSpecificationModel.setName("name");
    partSpecificationModel.setDescription("description");
    Long id = useCase.createPartSpecification(partSpecificationModel);

    var specs = partSpecificationRepository.findAll();

    assertEquals(1, specs.size());
    assertEquals(id, specs.get(0).getId());
    assertEquals("name", specs.get(0).getName());
    assertEquals("description", specs.get(0).getDescription());
  }

  @Test
  public void given_a_part_specification_then_specification_has_no_suppliers() {
    PartSpecification partSpecification = new PartSpecification();
    partSpecificationRepository.save(partSpecification);

    assertEquals(0,
        partSpecificationRepository.findOne(partSpecification.getId()).getSuppliers().size());
  }

  @Test
  public void given_a_part_specification_when_supplier_added_then_specification_has_supplier() {
    PartSpecification partSpecification = new PartSpecification();
    partSpecificationRepository.save(partSpecification);
    Supplier supplier = new Supplier();
    supplierRepository.save(supplier);

    useCase.addSupplierToPartSpecification(partSpecification.getId(), supplier.getId());

    assertEquals(1,
        partSpecificationRepository.findOne(partSpecification.getId()).getSuppliers().size());
  }

  @Test
  public void given_no_suppliers_then_none_listed() {
    var suppliers = useCase.getSuppliers();

    assertEquals(0, suppliers.size());
  }

  @Test
  public void given_one_supplier_then_one_listed() {
    Long id = supplierRepository.save(new Supplier());

    var suppliers = useCase.getSuppliers();

    assertEquals(1, suppliers.size());
    assertEquals(id, suppliers.get(0).getId());
  }

  @Test
  public void given_no_suppliers_when_one_created_then_one_in_repository() {
    useCase.createSupplier(new CreatePartSpecification.SupplierModel());
    assertEquals(1, supplierRepository.findAll().size());
  }

  @Test
  public void given_part_specification_then_id_is_null() {
    PartSpecification ps = new PartSpecification();

    assertNull(ps.getId());
  }

  @Test
  public void given_created_part_specification_the_id_not_null() {
    CreatePartSpecification.PartSpecificationModel ps = new CreatePartSpecification.PartSpecificationModel();
    Long id = useCase.createPartSpecification(ps);

    assertNotNull(partSpecificationRepository.findOne(id));
  }

  @Test
  public void given_two_created_part_specifications_their_ids_will_be_different() {
    var ps1 = new CreatePartSpecification.PartSpecificationModel();
    var ps2 = new CreatePartSpecification.PartSpecificationModel();
    Long id1 = useCase.createPartSpecification(ps1);
    Long id2 = useCase.createPartSpecification(ps2);

    assertFalse(id1.equals(id2));
  }

  @Test
  public void given_a_part_specifications_when_part_edited_then_edited_part_is_in_repository() {
    CreatePartSpecification.PartSpecificationModel partSpecificationModel =
        new CreatePartSpecification.PartSpecificationModel();
    partSpecificationModel.setName("name");
    partSpecificationModel.setDescription("description");
    Long id = useCase.createPartSpecification(partSpecificationModel);
    String newName = "New Name";
    String description = "New Description";

    useCase.editPartSpecification(id, newName, description);

    var specs = partSpecificationRepository.findAll();

    assertEquals(1, specs.size());
    assertEquals(id, specs.get(0).getId());
    assertEquals("New Name", specs.get(0).getName());
    assertEquals("New Description", specs.get(0).getDescription());
  }


  @Test
  public void given_a_part_specification_all_previous_suppliers_are_removed() {
    PartSpecification partSpecification = new PartSpecification();
    partSpecificationRepository.save(partSpecification);
    Supplier supplier = new Supplier();
    supplierRepository.save(supplier);
    Supplier supplier2 = new Supplier();
    supplierRepository.save(supplier);

    useCase.addSupplierToPartSpecification(partSpecification.getId(), supplier.getId());
    useCase.addSupplierToPartSpecification(partSpecification.getId(), supplier2.getId());

    assertEquals(2,
        partSpecificationRepository.findOne(partSpecification.getId()).getSuppliers().size());

    useCase.removeSupplierToPartSpecification(partSpecification.getId());

    assertEquals(0,
        partSpecificationRepository.findOne(partSpecification.getId()).getSuppliers().size());


  }

  @Test
  public void given_a_part_specification_requisition_request_created() {

    PartSpecification partSpecification = new PartSpecification();
    partSpecificationRepository.save(partSpecification);
    Supplier supplier = new Supplier();
    supplierRepository.save(supplier);

    useCase.addSupplierToPartSpecification(partSpecification.getId(), supplier.getId());

    CreatePartSpecification.RequestModel requisitionRequestModel =
        new CreatePartSpecification.RequestModel();
    requisitionRequestModel.setId(Long.valueOf(0));
    useCase.createRequisitionRequest(requisitionRequestModel);

    LocalDateTime dateObject = LocalDateTime.now();
    Long quantity = Long.valueOf(0);
    String engineerName = "Engineer Name";

    var rr = partRequisitionRequestRepository
        .findOne(requisitionRequestModel.getId());

    useCase.addRequisitionRequestToPartSpecification(partSpecification.getId(),
        requisitionRequestModel.getId(), quantity, engineerName, dateObject, supplier.getId());

    assertEquals(1,
        partSpecificationRepository.findOne(partSpecification.getId())
            .getPartRequisitionRequests().size());

    var requests = partRequisitionRequestRepository.findAll();

    assertEquals(1, requests.size());
    assertEquals(0, requests.get(0).getId());
    assertEquals(0, requests.get(0).getQuantity());
    assertEquals("Engineer Name", requests.get(0).getEngineerName());
    assertEquals(dateObject, requests.get(0).getRequestDate());
    assertEquals(1, requests.get(0).getSuppliers().size());
  }

}
