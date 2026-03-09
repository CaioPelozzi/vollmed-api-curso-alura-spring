package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private MedicoRepository medicoRepository;

    public MedicoController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = medicoRepository.save(new Medico(dados));
        var uri =  uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 1, page = 0, sort = {"nome"}) Pageable paginacao) {
        var page = medicoRepository.findAllByAtivoTrue(paginacao)
                .map(medico -> new DadosListagemMedico(
                        medico.getId(),
                        medico.getNome(),
                        medico.getEmail(),
                        medico.getCrm(),
                        medico.getEspecialidade()));

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody DadosAtualizacaoMedico dados) {
            var medico = medicoRepository.getReferenceById(dados.id());
            medico.atualizarInformacoes(dados);

            return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable  Long id) {
        var medico = medicoRepository.getReferenceById(id);
        medico.desativar();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
