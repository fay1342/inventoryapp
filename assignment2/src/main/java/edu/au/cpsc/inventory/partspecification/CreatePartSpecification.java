package edu.au.cpsc.inventory.partspecification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the create part specification use case.  Supports listing and create part
 * specifications as well as listing and creating suppliers.
 */
public class CreatePartSpecification {

  private PartSpecificationRepository partSpecificationRepository;
  private SupplierRepository supplierRepository;

  private PartRequisitionRequestRepository partRequisitionRepository;


  public CreatePartSpecification(PartSpecificationRepository partSpecificationRepository,
      SupplierRepository supplierRepository,
      PartRequisitionRequestRepository partRequisitionRepository) {
    this.partSpecificationRepository = partSpecificationRepository;
    this.supplierRepository = supplierRepository;
    this.partRequisitionRepository = partRequisitionRepository;
  }


  /**
   * List all part specifications in my repository.
   *
   * @return list of part specifications as model objects
   */
  public List<PartSpecificationModel> getPartSpecifications() {
    List<PartSpecificationModel> result = new ArrayList<>();
    for (var ps : partSpecificationRepository.findAll()) {
      result.add(partSpecificationToModel(ps));
    }
    return result;
  }

  /**
   * Create a part description by adding it to my part description repository thereby assigning it
   * an id.
   *
   * @param partSpecificationModel the model for the part specification to be created
   * @return the id of the created part specification
   */
  public Long createPartSpecification(PartSpecificationModel partSpecificationModel) {
    PartSpecification partSpecification = modelToPartSpecification(partSpecificationModel);
    return partSpecificationRepository.save(partSpecification);
  }

  /**
   * Edit a part specification by assigning it a different name or description.
   *
   * @param id          of the edited part specification
   * @param name        of the part specification that will be edited
   * @param description of the part specification that will be edited
   */

  public void editPartSpecification(
      Long id, String name, String description) {
    PartSpecification partSpecification = partSpecificationRepository.findOne(id);

    partSpecification.setName(name);
    partSpecification.setDescription(description);

  }

  /**
   * List all suppliers in my repository.
   *
   * @return list of suppliers as model objects
   */
  public List<SupplierModel> getSuppliers() {
    var result = new ArrayList<SupplierModel>();
    for (var supplier : supplierRepository.findAll()) {
      result.add(supplierToModel(supplier));
    }
    return result;
  }

  /**
   * Given a part specification, add a supplier to its list of suppliers.
   *
   * @param partSpecificationId the id of the part specification to be modified
   * @param supplierId          the id of the supplier to be added to the part specification
   */
  public void addSupplierToPartSpecification(Long partSpecificationId, Long supplierId) {
    var ps = partSpecificationRepository.findOne(partSpecificationId);
    var s = supplierRepository.findOne(supplierId);
    ps.addSupplier(s);
  }

  /**
   * Given a part specification, remove all suppliers to its list of suppliers.
   *
   * @param partSpecificationId the id of the part specification to be modified
   */

  public void removeSupplierToPartSpecification(Long partSpecificationId) {
    var ps = partSpecificationRepository.findOne(partSpecificationId);
    ps.removeSupplier();
  }

  private PartSpecificationModel partSpecificationToModel(PartSpecification ps) {
    PartSpecificationModel partSpecificationModel = new PartSpecificationModel();
    partSpecificationModel.setName(ps.getName());
    partSpecificationModel.setDescription(ps.getDescription());
    partSpecificationModel.setId(ps.getId());
    return partSpecificationModel;
  }

  private PartSpecification modelToPartSpecification(PartSpecificationModel psm) {
    PartSpecification partSpecification = new PartSpecification();
    partSpecification.setName(psm.getName());
    partSpecification.setDescription(psm.getDescription());
    return partSpecification;
  }

  private SupplierModel supplierToModel(Supplier s) {
    SupplierModel supplierModel = new SupplierModel();
    supplierModel.setId(s.getId());
    return supplierModel;
  }

  private Supplier modelToSupplier(SupplierModel sm) {
    return new Supplier();
  }


  /**
   * Create a supplier by adding it to my supplier repository.
   *
   * @param supplier the supplier to be saved.
   */
  public void createSupplier(SupplierModel supplier) {
    supplierRepository.save(modelToSupplier(supplier));
  }

  /**
   * List all suppliers assigned to the part specification.
   *
   * @param partSpecificationId the id of the part specification that has assigned suppliers
   * @return list of suppliers of the part specification
   */

  public List<SupplierModel> getSuppliersOfPartSpecification(Long partSpecificationId) {
    var ps = partSpecificationRepository.findOne(partSpecificationId);
    var result = new ArrayList<SupplierModel>();
    for (var supplier : ps.getSuppliers()) {
      result.add(supplierToModel(supplier));
    }
    return result;
  }

  /**
   * List of all requisition requests of the part specification.
   *
   * @return list of all requisition requests
   */

  public List<PartRequisitionRequest> getRequests() {

    var requisitionRequestsList = new ArrayList<PartRequisitionRequest>();
    for (var requisitionRequest : partRequisitionRepository.findAll()) {
      requisitionRequestsList.add(requisitionRequest);
    }
    return requisitionRequestsList;
  }

  /**
   * Given a part specification, add a created requisition request. Including the requisition
   * request's quantity, engineer name, date and time, and supplier.
   *
   * @param partSpecificationId  the id of the part specification that will have a requisition
   *                             request
   * @param requisitionRequestId the id of the requisition request added
   * @param quantity             long of the quantity of the requisition request
   * @param engineerName         String of the requisition request
   * @param date                 LocalDateTime  of the requisition request
   * @param supplierId           the id of the supplier of the requisition request
   */

  public void addRequisitionRequestToPartSpecification(Long partSpecificationId,
      Long requisitionRequestId, Long quantity, String engineerName,
      LocalDateTime date, Long supplierId) {
    var ps = partSpecificationRepository.findOne(partSpecificationId);
    var rr = partRequisitionRepository.findOne(requisitionRequestId);
    var s = supplierRepository.findOne(supplierId);
    ps.addSupplier(s);
    rr.setQuantity(quantity);
    rr.setEngineerName(engineerName);
    rr.setRequestDate(date);

    rr.addSupplier(s);
    ps.addPartRequisitionRequest(rr);

  }

  /**
   * Create a requisition request by adding it to my part requisition request repository.
   *
   * @param requestModel the requestModel to be saved.
   */

  public void createRequisitionRequest(RequestModel requestModel) {
    partRequisitionRepository.save(modelToRequest(requestModel));
  }

  private RequestModel requestToModel(PartRequisitionRequest request) {
    RequestModel requestModel = new RequestModel();
    requestModel.setId(request.getId());
    return requestModel;
  }

  private PartRequisitionRequest modelToRequest(RequestModel requestModel) {
    return new PartRequisitionRequest();
  }


  /**
   * A "part specification model" can provide data about a part specification that is relevant to
   * this use case.
   */
  public static class PartSpecificationModel {

    private String name;
    private String description;
    private Long id;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  /**
   * A "supplier model" can provide data about a supplier that is relevant to this use case.
   */
  public static class SupplierModel {

    private Long id;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  /**
   * A "request model" can provide data about a requisition request that is relevant to this use
   * case.
   */

  public static class RequestModel {

    private Long id;

    private long quantity;
    private String engineerName;

    private LocalDateTime requestDate;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }


    public long getQuantity() {
      return this.quantity;
    }

    public void setQuantity(Long quantity) {
      this.quantity = quantity;
    }

    public String getEngineerName() {
      return this.engineerName;
    }

    public void setEngineerName(String engineerName) {
      this.engineerName = engineerName;
    }

    public LocalDateTime getRequestDate() {
      return this.requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
      this.requestDate = requestDate;
    }

  }

}
