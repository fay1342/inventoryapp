package edu.au.cpsc.inventory.partspecification;

/**
 * Start the console user interface.  This class currently serves as documentation about the
 * start-up of the system.  Especially in terms of injecting dependencies.
 */
public class Main {

  /**
   * Start the console user interface.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    new CreatePartSpecificationConsoleUserInterface(
        new CreatePartSpecification(new PartSpecificationRepository(),
            new SupplierRepository(), new PartRequisitionRequestRepository())).run();
  }

}
