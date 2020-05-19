Feature: Serialization
  In order to use java classes in rdfs
  As a developer
  I want to serialize objects of these classes

  Scenario Outline: Example class is serialized successfully
    Given I use the "<className>" class
    And the "<attribute>" of the "<className>" is "<value>"
    When I serialize the "<className>"
    Then I should see the "<value>" in a "<attribute>" tag

    Examples:
      | className | value            | attribute |
      | Person    | Michael Grove    | name      |
      | Dog       | Golden retriever | breed     |
