Feature: Validity

#adding airline records
  Scenario: Adding a new airline record with valid name
    Given I am adding new airline data
    When I enter "Dynamite" airline name
    Then airline should be valid

  Scenario: Adding a new airline with invalid name
    Given I am adding new airline data
    When I enter "875~" airline name
    Then airline should not be valid

  Scenario: Adding a new airline record with valid IATA
    Given I am adding new airline data
    When I enter "BTS" airline IATA
    Then airline should be valid

  Scenario: Adding a new airline record with invalid IATA
    Given I am adding new airline data
    When I enter "ANTI⁷" airline IATA
    Then airline should not be valid

  Scenario: Adding a new airline record with valid ICAO
    Given I am adding new airline data
    When I enter "TXT" airline ICAO
    Then airline should be valid

  Scenario: Adding a new airline record with invalid ICAO
    Given I am adding new airline data
    When I enter "RUN9¾" airline ICAO
    Then airline should not be valid

  Scenario: Adding a new airline record with valid country
    Given I am adding new airline data
    When I enter "Magic Island" airline country
    Then airline should be valid

  Scenario: Adding a new airline record with invalid country
    Given I am adding new airline data
    When I enter "134340~" airline country
    Then airline should not be valid

#adding airport records
  Scenario: Adding a new airport record with valid IATA
    Given I am adding new airport data
    When I enter "BTS" airport IATA
    Then airport should be valid

  Scenario: Adding a new airport record with invalid IATA
    Given I am adding new airport data
    When I enter "ANTI⁷" airport IATA
    Then airport should not be valid

  #adding route records
  Scenario: Adding a new route record with valid airline
    Given I am adding new route data
    When I enter "6X" route airline
    Then route should be valid

  Scenario: Adding a new route record with invalid airline
    Given I am adding new route data
    When I enter "T⁷" route airline
    Then route should not be valid