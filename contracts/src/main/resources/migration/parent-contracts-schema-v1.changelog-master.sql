--liquibase formatted sql

--changeset Vodafone.Corda.Generated:initial_schema_for_UseCaseContractsSchemaV1

CREATE TABLE parent_table
(
    output_index      INT           NOT NULL,
    transaction_id    VARCHAR(64)   NOT NULL,
    identifier        UUID          NOT NULL,
    name              NVARCHAR(64)  NOT NULL,
    PRIMARY KEY (output_index, transaction_id)
);

CREATE TABLE child_table
(
    output_index   INT         NOT NULL,
    transaction_id VARCHAR(64) NOT NULL,
    name           VARCHAR(64) NOT NULL,
    type           VARCHAR(64) NOT NULL,
    PRIMARY KEY (output_index, transaction_id),
    CONSTRAINT fk_child_table
        FOREIGN KEY (output_index, transaction_id)
            REFERENCES parent_table (output_index, transaction_id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
);

CREATE INDEX parent_table_identifier_idx ON parent_table (identifier);
