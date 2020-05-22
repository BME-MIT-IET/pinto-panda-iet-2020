Feature: Serialization
  In order to use java classes in rdfs
  As a developer
  I want to serialize objects of these classes

  Scenario Outline: Example class is serialized successfully
    Given I use the "<className>" class
    And the "<attribute>" of the "<className>" is "<value>" of type String
    When I serialize the "<className>"
    Then I should see the "<value>" in a "<attribute>" tag

    Examples:
      | className | value            | attribute |
      | Person    | Michael Grove    | name      |
      | Dog       | Golden retriever | breed     |

  Scenario: Example class with multiple fields is serialized successfully
    Given I use the "Velociraptor" class
    And the "name" of the "Velociraptor" is "Pityu" of type String
    And the "velocity" of the "Velociraptor" is "7" of type Integer
    When I serialize the "Velociraptor"
    Then I should see the "Pityu" in a "name" tag
    And I should see the "7" in a "velocity" tag
