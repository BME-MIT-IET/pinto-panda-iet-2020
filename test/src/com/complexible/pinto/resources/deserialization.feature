Feature: Deserialization
  In order to use rdfs objects in Java code
  As a developer
  I want to deserialize these objects into Java classes

  Scenario Outline: Example class is read successfully
    Given I have an NTriple of a Person object in "<filePath>"
    And I use the "Person" class
    When I deserialize the object
    Then I should get an object of type "<class>"
    And I should see the "<attribute>" is "<value>"

    Examples:
      | filePath | class | attribute | value |
      | /bdddata/person.txt |   Person    |     name     |    Michael Grove   |