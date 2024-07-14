Feature: Product

  Scenario: : Register new Product
    When register a new product
    Then the product is registered successfully
    And must be presented


  Scenario: Find Product
      Given that a product has already been published
      When search for the message
      Then the product is displayed successfully


  Scenario: Update Message
      Given that a product has already been published
      When make request to change message
      Then the product is successfully shown
      And must be presented


  Scenario: Remove Product
      Given that a product has already been published
      When request product removal
      Then the product is removed successfully
