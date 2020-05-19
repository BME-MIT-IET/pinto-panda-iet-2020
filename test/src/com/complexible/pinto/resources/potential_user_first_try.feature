Feature: Potential user first try
  A potential user wants to try out the example code to see whether it seems to be working

  Scenario Outline: Example class is serialized successfully
    Given I use the Person class
    And the name of a person is "<name>"
    When I serialize the person
    Then I should see the "<serialized>" name in a "<attribute>" tag

    Examples:
    | name | serialized | attribute |
    | Michael Grove | Michael Grove | name |


  Scenario Outline: Example class is read successfully
    Given I have an NTriple of a Person object in "<filePath>"
    And I use the Person class
    When I deserialize the object
    Then I should get an object of type "<class>"
    And I should see the "<attribute>" is "<value>"

    Examples:
    | filePath | class | attribute | value |
    | /bdddata/person.txt |   Person    |     name     |    Michael Grove   |