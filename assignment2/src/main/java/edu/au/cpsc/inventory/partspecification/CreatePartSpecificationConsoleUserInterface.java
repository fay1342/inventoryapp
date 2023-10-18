package edu.au.cpsc.inventory.partspecification;

import edu.au.cpsc.inventory.partspecification.CreatePartSpecification.RequestModel;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Console user interface for the part specification creation use case.
 */
public class CreatePartSpecificationConsoleUserInterface {

  private final Scanner scanner;
  private CreatePartSpecification createPartSpecification;


  public CreatePartSpecificationConsoleUserInterface(
      CreatePartSpecification createPartSpecification) {
    this.createPartSpecification = createPartSpecification;
    scanner = new Scanner(System.in);
  }

  /**
   * Execute this user interface's main loop.
   */
  public void run() {
    displayMainMenu();
    MenuResponse selection = getMenuResponse();
    while (selection != MenuResponse.QUIT) {
      handleMenuSelection(selection);
      displayMainMenu();
      selection = getMenuResponse();
    }
  }

  private void handleMenuSelection(MenuResponse selection) {
    switch (selection) {
      case QUIT:
        break;
      case CREATE_PART_SPECIFICATION:
        createPartSpecification();
        break;
      case ASSIGN_SUPPLIER:
        assignSupplier();
        break;
      case CREATE_SUPPLIER:
        createSupplier();
        break;
      case EDIT_PART_SPECIFICATION:
        editPartSpecification();
        break;
      case PART_REQUISITION_REQUEST:
        partRequisitionRequest();
        break;
      case INVALID:
      default:
        System.out.println("Invalid menu selection");
    }
  }

  private void editPartSpecification() {
    if (createPartSpecification.getPartSpecifications().isEmpty()) {
      System.out.println("No part specifications, create one first");
      return;
    }

    System.out.println("Existing part specifications: ");
    for (var m : createPartSpecification.getPartSpecifications()) {
      System.out.printf("%d) %s: %s\n", m.getId(), m.getName(), m.getDescription());
    }
    System.out.println("");
    System.out.print("Enter id of the part specification to modify: ");
    final long partSpecificationId = scanner.nextLong();
    scanner.nextLine();

    var model = createPartSpecification
        .getPartSpecifications().get((int) partSpecificationId);
    System.out.println("");
    System.out.println("The part specification selected:");
    System.out.printf("%d) %s: %s\n", model.getId(), model.getName(), model.getDescription());
    System.out.println("");

    System.out.print("Enter part specification's new name. (To keep current value press enter): ");
    String name = scanner.nextLine();
    if (name == "") {
      name = model.getName();
    }

    System.out.print("Enter part specification's new description."
        + "(To keep current value press enter): ");
    String description = scanner.nextLine();
    if (description == "") {
      description = model.getDescription();
    }

    createPartSpecification.editPartSpecification(partSpecificationId, name, description);

    System.out.println("");
    System.out.println("Do you want to modify the supplier? (Enter n for no)");
    String response = scanner.nextLine();
    if (response.toLowerCase().equals("n")) {
      return;
    }

    if (createPartSpecification.getSuppliers().isEmpty()) {
      System.out.println("No suppliers, create one first");
      return;
    }

    System.out.println("Suppliers: ");
    for (var m : createPartSpecification.getSuppliers()) {
      System.out.printf("%d\n", m.getId());
    }

    System.out.print("Enter id of the supplier to add. "
        + "All previously assigned suppliers will be removed: ");
    final Long supplierId = scanner.nextLong();
    scanner.nextLine();
    createPartSpecification.removeSupplierToPartSpecification(partSpecificationId);
    createPartSpecification.addSupplierToPartSpecification(partSpecificationId, supplierId);

  }

  private void partRequisitionRequest() {
    if (createPartSpecification.getPartSpecifications().isEmpty()) {
      System.out.println("No part specifications, create one first");
      return;
    }

    System.out.println("Existing part specifications: ");
    for (var m : createPartSpecification.getPartSpecifications()) {
      System.out.printf("%d) %s: %s\n", m.getId(), m.getName(), m.getDescription());
    }
    System.out.println("");
    System.out.print("To see a list of all requisition requests for a part specification"
        + "\nEnter id of the part specification: ");
    final long partSpecificationId = scanner.nextLong();
    scanner.nextLine();

    System.out.println("Requisition requests for that part specification: ");
    for (var m : createPartSpecification.getRequests()) {
      System.out.printf("%d\n", m.getId());
    }

    if (createPartSpecification.getRequests().isEmpty()) {
      System.out.println("");
      System.out.println("No requisition requests for that part specification.");
    }

    System.out.println("");
    System.out.println("Do you want to create a new requisition "
        + "request for that part specification? (Enter n for no)");
    String response = scanner.nextLine();
    if (response.toLowerCase().equals("n")) {
      return;
    }
    createPartSpecification.createRequisitionRequest(new RequestModel());

    System.out.println("Requisition requests for that part specification: ");
    for (var m : createPartSpecification.getRequests()) {
      System.out.printf("id: %d\n", m.getId());
    }

    System.out.print("Enter the id of the new requisition request created: ");
    final Long requisitionId = scanner.nextLong();
    scanner.nextLine();

    System.out.print("Enter the quantity for the new requisition request: ");
    final Long quantity = scanner.nextLong();
    scanner.nextLine();

    System.out.print("Enter the name of the requesting engineer: ");
    final String engineerName = scanner.nextLine();

    System.out.println("Select a supplier for the requisition request."
        + "\nEnter a supplier id from the list: ");
    System.out.println("");

    if (createPartSpecification.getSuppliersOfPartSpecification(partSpecificationId).size() >= 1) {
      System.out.println("List of suppliers assigned to the part specification: ");
      for (var m : createPartSpecification.getSuppliersOfPartSpecification(partSpecificationId)) {
        System.out.printf("%d\n", m.getId());
      }
    }

    if (createPartSpecification.getSuppliersOfPartSpecification(partSpecificationId).isEmpty()) {
      System.out.println("List of all suppliers: ");
      for (var m : createPartSpecification.getSuppliers()) {
        System.out.printf("%d\n", m.getId());
      }
    }

    System.out.print("Enter id of the supplier to add: ");
    final Long supplierId = scanner.nextLong();
    scanner.nextLine();

    LocalDateTime dateObject = LocalDateTime.now();
    createPartSpecification.addRequisitionRequestToPartSpecification(partSpecificationId,
        requisitionId, quantity, engineerName, dateObject, supplierId);

  }

  private void createSupplier() {
    createPartSpecification.createSupplier(new CreatePartSpecification.SupplierModel());
    System.out.println("Created!");
  }

  private void assignSupplier() {
    if (createPartSpecification.getSuppliers().isEmpty()) {
      System.out.println("No suppliers, create one first");
      return;
    }
    if (createPartSpecification.getPartSpecifications().isEmpty()) {
      System.out.println("No part specifications, create one first");
      return;
    }
    System.out.println("Create part\n");
    System.out.println("Existing part specifications: ");
    for (var m : createPartSpecification.getPartSpecifications()) {
      System.out.printf("%d) %s: %s\n", m.getId(), m.getName(), m.getDescription());
    }

    System.out.print("Enter id of the part specification to modify: ");
    final Long partSpecificationId = scanner.nextLong();
    scanner.nextLine();

    System.out.println("Suppliers: ");
    for (var m : createPartSpecification.getSuppliers()) {
      System.out.printf("%d\n", m.getId());
    }

    System.out.print("Enter id of the supplier to add: ");
    final Long supplierId = scanner.nextLong();
    scanner.nextLine();

    createPartSpecification.addSupplierToPartSpecification(partSpecificationId, supplierId);
  }

  private void createPartSpecification() {
    System.out.println("Create part\n");
    System.out.println("Existing part specifications: ");
    for (var m : createPartSpecification.getPartSpecifications()) {
      System.out.printf("%s: %s\n", m.getName(), m.getDescription());
    }
    System.out.println("Are you sure you want to create a new one?");
    String response = scanner.nextLine();
    if (response.toLowerCase().equals("n")) {
      return;
    }
    System.out.print("Enter part specification name: ");
    String name = scanner.nextLine();
    System.out.print("Enter part specification description: ");
    String description = scanner.nextLine();
    var model = new CreatePartSpecification.PartSpecificationModel();
    model.setName(name);
    model.setDescription(description);
    createPartSpecification.createPartSpecification(model);
  }

  private MenuResponse getMenuResponse() {
    System.out.print("Enter selection> ");
    String line = scanner.nextLine();
    for (MenuResponse candidate : MenuResponse.values()) {
      if (candidate.getInputCharacter() == line.charAt(0)) {
        return candidate;
      }
    }
    return MenuResponse.INVALID;
  }

  private void displayMainMenu() {
    System.out.println("\n\nMain menu");
    for (MenuResponse r : MenuResponse.values()) {
      if (r != MenuResponse.INVALID) {
        System.out.println(r);
      }
    }
  }

  private enum MenuResponse {
    CREATE_PART_SPECIFICATION('c', "Create part specification"),
    ASSIGN_SUPPLIER('a', "Assign supplier to part specification"),
    INVALID('?', "INVALID"),
    CREATE_SUPPLIER('s', "Create supplier"),
    EDIT_PART_SPECIFICATION('e', "Edit part specification"),
    PART_REQUISITION_REQUEST('r', "Create requisition request"),
    QUIT('q', "Quit");

    private final char inputCharacter;
    private final String description;

    MenuResponse(char inputCharacter, String description) {
      this.inputCharacter = inputCharacter;
      this.description = description;
    }

    public char getInputCharacter() {
      return inputCharacter;
    }

    @Override
    public String toString() {
      return inputCharacter + ") " + description;
    }
  }
}
