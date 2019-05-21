# Copyright (C) 2016-2019 ActionTech.
# License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
Feature: #install dble
  Scenario: #install zk cluster in a clean enviroment
    Given a clean environment in all dble nodes
    Given install dble in all dble nodes
    Given config zookeeper cluster in all dble nodes
    Then start dble in order
    Given change zk cluster to single mode
    Given config zookeeper cluster in all dble nodes
    Then start dble in order
    Given stop all dbles