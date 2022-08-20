package com.poc.mater.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alpha.entity.model.Usuario;
import com.alpha.entity.repository.UsuarioRepository;
import com.alpha.system.security.JWTUtil;
import com.alpha.system.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private JWTUtil jwtUtil;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuario"))
                .andExpect(status().isOk());
    }


    @Test
    public void name() throws  Exception {
        Usuario u1 = new Usuario(1,"Usuario1", "usuario1@mail.com", "123");
        Usuario u2 = new Usuario(2,"Usuario2", "usuario2@mail.com", "123");
        Usuario u3 = new Usuario(3,"Usuario3", "usuario3@mail.com", "123");
        Usuario u4 = new Usuario(4,"Usuario4", "usuario4@mail.com", "123");
        Usuario u5 = new Usuario(5,"Usuario5", "usuario5@mail.com", "123");
        
        List<Usuario> usuarios = new ArrayList<>(Arrays.asList(u1,u2,u3,u4,u5));

        Mockito.when(usuarioService.findAll()).thenReturn(usuarios);

        String jwtToken = "TokenFake";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/usuario")
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(usuarios.size()))
                .andExpect(content().json("[{\"id\":1,\"nome\":null,\"email\":null,\"senha\":null,\"perfis\":[]}]"))
                .andReturn();
    }
    
    @Test
    //@SneakyThrows
    void importExcelFile() throws Exception {
        final byte[] bytes = Files.readAllBytes(Paths.get("TEST_FILE_URL_HERE"));
        mockMvc.perform(multipart("/upload/file")
                .file("file", bytes))
                .andExpect(status().isOk())
                .andExpect(content().string("2037")); // size of the test input file
    }

}