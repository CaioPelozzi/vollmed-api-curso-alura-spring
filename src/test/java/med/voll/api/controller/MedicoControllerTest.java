package med.voll.api.controller;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @MockBean
    private MedicoRepository medicoRepository;


    @Test
    @DisplayName("Deveria devolver código http 400 quando as informações estão inválidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        var response = mock.perform(post("/medicos/cadastrar"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando as informações estão válidas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {

        // 1. Preparação dos Dados (Arrange)
        var dadosEndereco = new DadosEndereco("Rua Teste", "Bairro Teste", "36030340", "Juiz de Fora", "MG", "casa teste", "123");
        var dadosCadastro = new DadosCadastroMedico("Maria", "mariamock@gmail.com", "32999999999", "12345", Especialidade.CARDIOLOGIA, dadosEndereco);

        // Criamos a entidade que simula o que o banco salvaria e "forçamos" um ID nela
        var medicoSalvo = new Medico(dadosCadastro);
        medicoSalvo.setId(1L);

        // O DTO esperado agora tem o ID 1L, igual ao médico salvo
        var dadosDetalhamentoMedico = new DadosDetalhamentoMedico(1L, "Maria", "mariamock@gmail.com",
                "12345", "32999999999", Especialidade.CARDIOLOGIA, medicoSalvo.getEndereco());

        // 2. Comportamento do Mock (A Mágica!)
        // Quando o repository tentar salvar qualquer coisa (any()), devolva o nosso medicoSalvo
        when(medicoRepository.save(any())).thenReturn(medicoSalvo);

        // 3. Execução da Requisição (Act)
        var response = mock.perform(post("/medicos/cadastrar") // Verifique se sua rota é /medicos/cadastrar ou só /medicos
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson())
                ).andReturn()
                .getResponse();

        // 4. Verificações (Assert)
        // Agora ele vai passar no 201 (CREATED) porque não vai quebrar na hora de montar a URI!
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamentoMedico).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }


}
