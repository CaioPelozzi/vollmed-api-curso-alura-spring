package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private PacienteRepository repository;

    public PacienteController(PacienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(@PageableDefault(size = 1, page = 0, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao)
                .map(paciente -> new DadosListagemPaciente(
                        paciente.getId(),
                        paciente.getNome(),
                        paciente.getEmail(),
                        paciente.getTelefone()));

        return ResponseEntity.ok(page);
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriComponentsBuilder) {
        Paciente paciente = repository.save(new Paciente(dados));

        URI uri = uriComponentsBuilder.path("/pacientes/{id}")
                .buildAndExpand(paciente.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPaciente> detalhar(@PathVariable Long id){
        Paciente paciente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPaciente> atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dadosAtualizacaoPaciente){
        Paciente paciente = repository.getReferenceById(dadosAtualizacaoPaciente.id());
        paciente.atualizarInformacoes(dadosAtualizacaoPaciente);

        return ResponseEntity.ok().body(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        Paciente paciente = repository.getReferenceById(id);
        paciente.setAtivo(false);

        return ResponseEntity.noContent().build();
    }

}
