do
$$
    declare
        id_accounttype_current bigint;
        id_accounttype_deposit bigint;
        id_customer_01 bigint;
        id_customer_02 bigint;
        id_customer_03 bigint;
        id_account_c01_current_01 bigint;
        id_account_c01_current_02 bigint;
        id_account_c01_deposit_01 bigint;
        id_account_c02_current_01 bigint;
        id_account_c02_c03_current_01 bigint;
    begin
        delete from account_history;
        delete from link_customer_account;
        delete from customer;
        delete from account;
        delete from account_type;

        insert into account_type(description, reference) values ('Test current account type', 'TEST_CURRENT');
        insert into account_type(description, reference) values ('Test deposit account type', 'TEST_DEPOSIT');
        select id into id_accounttype_current from account_type where reference = 'TEST_CURRENT';
        select id into id_accounttype_deposit from account_type where reference = 'TEST_DEPOSIT';

        insert into customer(last_name, first_name, email, reference) values ('TestLast01', 'TestFirst01', 'test01@example.com', 'TEST_CUSTOMER_01');
        insert into customer(last_name, first_name, email, reference) values ('TestLast02', 'TestFirst02', 'test02@example.com', 'TEST_CUSTOMER_02');
        insert into customer(last_name, first_name, email, reference) values ('TestLast03', 'TestFirst03', 'test03@example.com', 'TEST_CUSTOMER_03');
        select id into id_customer_01 from customer where reference = 'TEST_CUSTOMER_01';
        select id into id_customer_02 from customer where reference = 'TEST_CUSTOMER_02';
        select id into id_customer_03 from customer where reference = 'TEST_CUSTOMER_03';

        insert into account(description, balance, balance_timestamp, reference, account_type_id) values ('Customer 01 Current account 01', 111.00, now(), 'CUSTOMER_01_CURRENT_01', id_accounttype_current);
        insert into account(description, balance, balance_timestamp, reference, account_type_id) values ('Customer 01 Current account 02', 222.00, now(), 'CUSTOMER_01_CURRENT_02', id_accounttype_current);
        insert into account(description, balance, balance_timestamp, reference, account_type_id) values ('Customer 01 Deposit account 01', 333.00, now(), 'CUSTOMER_01_DEPOSIT_01', id_accounttype_deposit);
        insert into account(description, balance, balance_timestamp, reference, account_type_id) values ('Customer 02 Current account 01', 1212.00, now(), 'CUSTOMER_02_CURRENT_01', id_accounttype_current);
        insert into account(description, balance, balance_timestamp, reference, account_type_id) values ('Customer 02/03 Joint current account 01', 55555.00, now(), 'CUSTOMER_02_CUSTOMER_03_CURRENT_01', id_accounttype_current);
        select id into id_account_c01_current_01 from account where reference = 'CUSTOMER_01_CURRENT_01';
        select id into id_account_c01_current_02 from account where reference = 'CUSTOMER_01_CURRENT_02';
        select id into id_account_c01_deposit_01 from account where reference = 'CUSTOMER_01_DEPOSIT_01';
        select id into id_account_c02_current_01 from account where reference = 'CUSTOMER_02_CURRENT_01';
        select id into id_account_c02_c03_current_01 from account where reference = 'CUSTOMER_02_CUSTOMER_03_CURRENT_01';

        insert into link_customer_account(customer_id, account_id) values (id_customer_01, id_account_c01_current_01);
        insert into link_customer_account(customer_id, account_id) values (id_customer_01, id_account_c01_current_02);
        insert into link_customer_account(customer_id, account_id) values (id_customer_01, id_account_c01_deposit_01);
        insert into link_customer_account(customer_id, account_id) values (id_customer_02, id_account_c02_current_01);
        insert into link_customer_account(customer_id, account_id) values (id_customer_02, id_account_c02_c03_current_01);
        insert into link_customer_account(customer_id, account_id) values (id_customer_03, id_account_c02_c03_current_01);

        raise notice 'id_accounttype_current: %', id_accounttype_current;
        raise notice 'id_accounttype_deposit: %', id_accounttype_deposit;
        raise notice 'id_customer_01: %', id_customer_01;
        raise notice 'id_customer_02: %', id_customer_02;
        raise notice 'id_customer_03: %', id_customer_03;
    end
$$;
