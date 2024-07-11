package com.ticruz.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticruz.algafood.domain.exception.NegocioException;
import com.ticruz.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.ticruz.algafood.domain.model.Grupo;
import com.ticruz.algafood.domain.model.Usuario;
import com.ticruz.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroGrupoService grupoService;
	
	@Transactional
	public Usuario salvar (Usuario usuario) {
		usuarioRepository.detach(usuario); //usado para não persistir no banco antes da pesquisa
		
		//pega o um usuario que tenha este email ou null
		Optional<Usuario> usuarioEmail = usuarioRepository.findByEmail(usuario.getEmail());
		
		//se o usuario foi encontrado e nao for igual o usuario mesmo(atualizar)
		if(usuarioEmail.isPresent() && !usuarioEmail.get().equals(usuario)) {
			throw new NegocioException(
				String.format("Já existe um usuario cadastrado com o e-mail %s",usuario.getEmail()));
		}
		
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		if(usuario.senhaNaoCoincideCom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário");
		}
		usuario.setSenha(novaSenha);
		
	}

	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId).
				orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
	
	@Transactional
	public void dessassociarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = grupoService.buscarOuFalhar(grupoId);
		usuario.removerGrupo(grupo);
	}
	
	@Transactional
	public void associarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = grupoService.buscarOuFalhar(grupoId);
		usuario.adicionarGrupo(grupo);
	}
}
