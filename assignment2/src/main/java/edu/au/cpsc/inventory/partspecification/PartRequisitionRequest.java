package edu.au.cpsc.inventory.partspecification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Requisition Request is created for a part specification. The request will have a quantity, name
 * of the requesting engineer, the date and time the request was entered, and a selected supplier.
 */
public class PartRequisitionRequest extends Entity {

  private long quantity;
  private String engineerName;

  private LocalDateTime requestDate;

  private List<Supplier> suppliers;

  public PartRequisitionRequest() {
    suppliers = new ArrayList<>();
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

  /**
   * Add a supplier to the list of suppliers that manufacture or supply this part.
   *
   * @param supplier the supplier to add
   */
  public void addSupplier(Supplier supplier) {
    suppliers.add(supplier);
  }

  public List<Supplier> getSuppliers() {
    return suppliers;
  }


}
