Feature: Calculations

  Scenario: Calculate carbon emissions based on a distance of 100 m.
    Given there is a new Route
    When I set the distance to 100.0
    Then the calculated carbon emissions should be 11.5

  Scenario: Calculate carbon emissions based on a distance of 200 m.
    Given there is a new Route
    When I set the distance to 200.0
    Then the calculated carbon emissions should be 23.0

  Scenario: Calculate carbon emissions based on a distance of 750 m.
    Given there is a new Route
    When I set the distance to 750.0
    Then the calculated carbon emissions should be 86.25

  Scenario: Calculate carbon emissions based on a distance of 0 m.
    Given there is a new Route
    When I set the distance to 0.0
    Then the calculated carbon emissions should be 0.0
