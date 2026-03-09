package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    private ConsultaRepository consultaReposiory;
    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;
    private List<ValidadorAgendamentoDeConsulta> validadores;

    public AgendaDeConsultas(ConsultaRepository consultaReposiory,
                             MedicoRepository medicoRepository,
                             PacienteRepository pacienteRepository
    , List<ValidadorAgendamentoDeConsulta> validadores) {
        this.consultaReposiory = consultaReposiory;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.validadores = validadores;
    }

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Paciente não encontrado");
        }
        if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Médico não encontrado");
        }

        validadores.forEach(v -> v.validar(dados));


        Paciente paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        Medico medico = escolherMedico(dados);
        if(medico == null) {
            throw new ValidacaoException("Não existe médico disponível nessa data");
        }

        Consulta consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaReposiory.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaReposiory.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }
        Consulta consulta = consultaReposiory.getReferenceById(dados.idConsulta());

        if(consulta.getMotivoCancelamento() != null){
            throw new ValidacaoException("Consulta já foi cancelada!");
        }

        consulta.cancelar(dados.motivo());
    }
}
