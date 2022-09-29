--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.21
-- Dumped by pg_dump version 9.5.5

-- Started on 2021-10-12 14:23:55

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2148 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 182 (class 1259 OID 302064)
-- Name: account; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE account (
                         id bigint NOT NULL,
                         description text,
                         balance double precision   ,
                         balance_timestamp timestamp with time zone,
                         reference text,
                         account_type_id bigint
);


--
-- TOC entry 187 (class 1259 OID 302099)
-- Name: account_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE account_history (
                                 id bigint NOT NULL,
                                 account_id bigint,
                                 time_stamp timestamp with time zone,
                                 account double precision   ,
                                 balance double precision   ,
                                 description text
);


--
-- TOC entry 186 (class 1259 OID 302097)
-- Name: account_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE account_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2149 (class 0 OID 0)
-- Dependencies: 186
-- Name: account_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE account_history_id_seq OWNED BY account_history.id;


--
-- TOC entry 181 (class 1259 OID 302062)
-- Name: account_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2150 (class 0 OID 0)
-- Dependencies: 181
-- Name: account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE account_id_seq OWNED BY account.id;


--
-- TOC entry 188 (class 1259 OID 302114)
-- Name: account_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE account_type (
                              id bigint NOT NULL,
                              description text,
                              reference text
);


--
-- TOC entry 189 (class 1259 OID 302127)
-- Name: account_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE account_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2151 (class 0 OID 0)
-- Dependencies: 189
-- Name: account_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE account_type_id_seq OWNED BY account_type.id;


--
-- TOC entry 184 (class 1259 OID 302075)
-- Name: customer; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE customer (
                          id bigint NOT NULL,
                          last_name text,
                          first_name text,
                          email text,
                          reference text
);


--
-- TOC entry 183 (class 1259 OID 302073)
-- Name: customer_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2152 (class 0 OID 0)
-- Dependencies: 183
-- Name: customer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE customer_id_seq OWNED BY customer.id;


--
-- TOC entry 185 (class 1259 OID 302084)
-- Name: link_customer_account; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE link_customer_account (
                                       customer_id bigint,
                                       account_id bigint
);

alter table link_customer_account add primary key(customer_id, account_id); -- JR


--
-- TOC entry 2007 (class 2604 OID 302067)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY account ALTER COLUMN id SET DEFAULT nextval('account_id_seq'::regclass);


--
-- TOC entry 2009 (class 2604 OID 302102)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY account_history ALTER COLUMN id SET DEFAULT nextval('account_history_id_seq'::regclass);


--
-- TOC entry 2010 (class 2604 OID 302129)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY account_type ALTER COLUMN id SET DEFAULT nextval('account_type_id_seq'::regclass);


--
-- TOC entry 2008 (class 2604 OID 302078)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY customer ALTER COLUMN id SET DEFAULT nextval('customer_id_seq'::regclass);


--
-- TOC entry 2021 (class 2606 OID 302107)
-- Name: pk_account_detail_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account_history
    ADD CONSTRAINT pk_account_detail_id PRIMARY KEY (id);


--
-- TOC entry 2014 (class 2606 OID 302072)
-- Name: pk_account_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account
    ADD CONSTRAINT pk_account_id PRIMARY KEY (id);


--
-- TOC entry 2023 (class 2606 OID 302137)
-- Name: pk_account_type_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account_type
    ADD CONSTRAINT pk_account_type_id PRIMARY KEY (id);


--
-- TOC entry 2017 (class 2606 OID 302083)
-- Name: pk_customer_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT pk_customer_id PRIMARY KEY (id);


--
-- TOC entry 2019 (class 2606 OID 302146)
-- Name: uc_customer_first_last_email; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT uc_customer_first_last_email UNIQUE (last_name, first_name, email);


--
-- TOC entry 2011 (class 1259 OID 302143)
-- Name: fki_account_account_type_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_account_account_type_id ON public.account USING btree (account_type_id);


--
-- TOC entry 2012 (class 1259 OID 302113)
-- Name: idx_account_reference; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_account_reference ON public.account USING btree (reference);


--
-- TOC entry 2015 (class 1259 OID 302144)
-- Name: idx_customer_first_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_customer_first_name ON public.customer USING btree (first_name);


--
-- TOC entry 2024 (class 2606 OID 302138)
-- Name: fk_account_account_type_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account
    ADD CONSTRAINT fk_account_account_type_id FOREIGN KEY (account_type_id) REFERENCES account_type(id);


--
-- TOC entry 2027 (class 2606 OID 302108)
-- Name: fk_account_detail_account_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account_history
    ADD CONSTRAINT fk_account_detail_account_id FOREIGN KEY (account_id) REFERENCES account(id);


--
-- TOC entry 2025 (class 2606 OID 302087)
-- Name: fk_account_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY link_customer_account
    ADD CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id);


--
-- TOC entry 2026 (class 2606 OID 302092)
-- Name: fk_customer_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY link_customer_account
    ADD CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customer(id);


-- Completed on 2021-10-12 14:23:55

--
-- PostgreSQL database dump complete
--

