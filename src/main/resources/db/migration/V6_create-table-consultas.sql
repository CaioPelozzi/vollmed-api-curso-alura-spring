CREATE TABLE consultas(
    id bigint NOT NULL AUTO_INCREMENT,
    medico_id bigint NOT NULL,
    paciente_id bigint NOT NULL,
    data datetime NOT NULL,

    primary key(id),
    constraint fk_consultas_medico_id foreign key(medico_id) references medicos(id),
    constraint fk_consultas_paciente_id foreign key(paciente_id) references pacientes(id)
);