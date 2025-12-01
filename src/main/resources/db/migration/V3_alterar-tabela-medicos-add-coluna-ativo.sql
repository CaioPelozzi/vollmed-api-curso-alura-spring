ALTER TABLE medicos ADD COLUMN ativo tinyint;
UPDATE medicos SET ativo = 1;