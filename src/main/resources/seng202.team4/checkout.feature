Feature: Calculations
  Scenario: Calculate carbon emissions based on distance
    Given there is a new Route
    When I set the distance to 100.0
    Then the calculated carbon emissions should be 11.5