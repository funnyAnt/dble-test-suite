# Created by MAOFEI at 2017/9/25
Feature: manager commands test
  # Enter feature description here

  @smoke
  Scenario Outline:
    Then execute sql in "<filename>" to check manager work fine

        Examples:Types
          | filename                           |
          | manager/manager.sql                |
