#lang: en
Feature: 10 Create Template

  @ignore
  Scenario: User creates a new template
    Given a user has logged in
    | username | ntfusr1 |
    | password | secret  |
    When we call GraphQL with requestBody
    """
    {
      "query": "mutation createTemplate($input: CreateTemplateInput!) { createTemplate(input: $input) { name uuid alertType appCode createdDate } }",
      "variables": {
        "input": {
          "name": "new template 1",
          "alertType": "EMAIL",
          "appCode": "FABK"
        }
      }
    }
    """
    Then we should receive a response code of 200
