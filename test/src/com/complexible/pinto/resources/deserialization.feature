Feature: Deserialization
  In order to use rdfs objects in Java code
  As a developer
  I want to deserialize these objects into Java classes

  Scenario Outline: Example class is read successfully
    Given I have an NTriple of a "<className>" object in "<filePath>"
    And I use the "<className>" class
    When I deserialize the object
    Then I should get an object of type "<className>"
    And I should see the "<attribute>" is "<value>"

    Examples:
      | className    | filePath            | attribute | value            |
      | Person       | /bdddata/person.txt | name      | Michael Grove    |
      | Dog          | /bdddata/dog.txt    | breed     | Golden retriever |
      | Velociraptor | /bdddata/raptor.txt | name      | Pityu            |

  Scenario: Example class with multiple fields is read successfully
    Given I have an NTriple of a "Velociraptor" object in "/bdddata/raptor.txt"
    And I use the "Velociraptor" class
    When I deserialize the object
    Then I should get an object of type "Velociraptor"
    And I should see the "name" is "Pityu"
    And I should see the "velocity" is "7"