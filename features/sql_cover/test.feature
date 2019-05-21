# Copyright (C) 2016-2019 ActionTech.
# License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
Feature: basic sql translate/transmission correct, seperate read/write statements, read load balance

 Scenario Outline:#3 check read-write-split work fine and slaves load balance transaction
        Then execute sql in "<filename>" to check read-write-split work fine and log dest slave
        Given clear dirty data yield by sql

        Examples:Types
          | filename                                  |
          | bugs/bug.sql                 |

