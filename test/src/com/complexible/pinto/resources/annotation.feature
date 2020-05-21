Feature: Annotation
  In order to use java classes in rdfs
  As a developer
  I want to customize the serialization of these classes

  Scenario: Java bean with @RdfId annotation is serialized successfully
    Given I use the "AnnotatedCat" class
    And the "name" of the "AnnotatedCat" is annotated with @RdfId
    And the "name" of the "AnnotatedCat" is "Garfield" of type String
    And the "weight" of the "AnnotatedCat" is "14" of type Integer
    When I serialize the "AnnotatedCat"
    Then I should see the URI of the "AnnotatedCat" object is generated only by the annotated properties

  Scenario: Java bean with @RdfProperty annotation is serialized successfully
    Given I use the "AnnotatedCat" class
    And the "getter" of the "name" of the "AnnotatedCat" is annotated with @RdfProperty
    And the URI of the "name" property is "value"
    And the "name" of the "AnnotatedCat" is "Garfield" of type String
    And the "weight" of the "AnnotatedCat" is "14" of type Integer
    When I serialize the "AnnotatedCat"
    Then I should see the URI of the "name" property is "value"

  Scenario: Java bean with @RdfsClass annotation is serialize successfully
    Given I use the "AnnotatedCat" class
    And the class is annotated with @RdfsClass
    And the rdf:type of the "AnnotatedCat" class is "value"
    And the "name" of the "AnnotatedCat" is "Garfield" of type String
    And the "weight" of the "AnnotatedCat" is "14" of type Integer
    When I serialize the "AnnotatedCat"
    Then I should see the rdf:type of the "AnnotatedCat" is "value"