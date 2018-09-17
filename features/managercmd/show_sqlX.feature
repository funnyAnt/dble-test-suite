Feature: #show @@sql, show @@sql.resultset
  Scenario: #1 show @@sql.resultset
    Given add xml segment to node with attribute "{'tag':'root'}" in "schema.xml"
    """
    <schema dataNode="dn1" name="mytest" sqlMaxLimit="100">
        <table dataNode="dn1,dn2,dn3" name="ta" rule="hash-three" />
    </schema>
    """
    Given add xml segment to node with attribute "{'tag':'root'}" in "server.xml"
    """
    <system>
	    <property name="maxResultSet">1024</property>
    </system>
    """
    Given Restart dble in "dble-1"
    Then execute sql in "dble-1" use "test"
        | user | passwd | conn   | toClose  | sql                            | expect     | db     |
        | test | 111111 | conn_0 | False    | drop table if exists ta        | success    | mytest |
        | test | 111111 | conn_0 | False    | create table ta(id int,k varchar(1500))    | success    | mytest |
        | test | 111111 | conn_0 | False    | insert into ta value(1, repeat('a', 1100)) | success    | mytest |
        | test | 111111 | conn_0 | False    | insert into ta value(2, repeat('b', 1500)) | success    | mytest |
        | test | 111111 | conn_0 | False    | insert into ta value(3, repeat('c', 100))  | success    | mytest |
        | test | 111111 | conn_0 | True     | update ta set k="c" where id=3   | success    | mytest |
        | test | 111111 | conn_0 | True     | select * from ta               | success    | mytest |
        | test | 111111 | conn_0 | True     | select * from ta limit 1       | success    | mytest |
        | test | 111111 | conn_0 | True     | select * from ta where id=2    | success    | mytest |
        | test | 111111 | conn_0 | True     | delete from ta where id=1      | success    | mytest |
        | test | 111111 | conn_0 | True     | alter table ta drop column k   | success    | mytest |
    Then execute admin cmd "show @@sql" and get result
        | ID   | USER | SQL                                        |
        |    1 | test | delete from ta where id=1                  |
        |    2 | test | SELECT * FROM ta WHERE id = 2 LIMIT 100    |
        |    3 | test | SELECT * FROM ta LIMIT 1                   |
        |    4 | test | SELECT * FROM ta LIMIT 100                 |
        |    5 | test | update ta set k="c" where id=3             |
        |    6 | test | insert into ta value(3, repeat('c', 100))  |
        |    7 | test | insert into ta value(2, repeat('b', 1500)) |
        |    8 | test | insert into ta value(1, repeat('a', 1100)) |

    Then execute admin cmd "show @@sql.resultset" and get result
        | ID   | USER | FREQUENCY | SQL                      | RESULTSET_SIZE |
        |    1 | test |         1 | SELECT * FROM ta LIMIT ? |           2721 |

    Given delete the following xml segment
      |file        | parent | child                                           |
      |server.xml  |root    | {'tag':'system'}  |
