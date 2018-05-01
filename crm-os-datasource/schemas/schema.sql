-- SEQUENCE: public.customer_id_seq

-- DROP SEQUENCE public.customer_id_seq;

CREATE SEQUENCE public.customer_id_seq;

ALTER SEQUENCE public.customer_id_seq
OWNER TO admin;

-- Table: public.customer

-- DROP TABLE public.customer;

CREATE TABLE public.customer
(
    id integer NOT NULL DEFAULT nextval('customer_id_seq'::regclass),
    name character(255) COLLATE pg_catalog."default",
    age integer,
    CONSTRAINT customer_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.customer
OWNER to admin;