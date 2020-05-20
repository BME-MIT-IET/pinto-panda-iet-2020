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
      | className | filePath            | attribute | value            |
      | Person    | /bdddata/person.txt | name      | Michael Grove    |
      | Dog       | /bdddata/dog.txt    | breed     | Golden retriever |