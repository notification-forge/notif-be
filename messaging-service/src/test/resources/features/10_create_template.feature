#lang: en
Feature: 10 Create Template

  Scenario: User creates a new template
    Given a user has logged in
    | username | ntfusr1 |
    | password | secret  |
    When we call GraphQL with requestBody
    """
    mutation CREATE_TEMPLATE{
      createTemplate(input:{
        name: "new template 1"
        alertType: EMAIL
        appCode: "FABK"
      }) {
        name
        uuid
        alertType
        appCode
        createdDate
      }
    }
    """
    Then we should receive a response code of 200
