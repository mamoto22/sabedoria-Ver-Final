package br.com.sabedoria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.sabedoria.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService userDetailsServiceImpl;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests((authorize) -> authorize
	                    .requestMatchers("/", "/planos" ,"/sobreNos", "/planos", "/mentores", "/login", "/cadastro", "/cadastrarMentor", "/cadastrarMentorado", "/sucesso", "/sucessoMonitoria", "/sucessoExclusao", "/style/**", "/img/**", "/video/**", "/adminlte/**", "/dist/**", "/plugins/**", "/script/**" )
	                            .permitAll()
	                    .requestMatchers("/perfil", "/teste", "/{id}/editarMentor", "/imagemMentor/{id}", "/imagem/{id}","/{id}/editar", "/cadastrarMonitoria", "/cadastrarMonitoriaUser", "/listarMonitorias", "/excluirMonitoria", "/listarMonitores", "/monitoriasAgendadas")
	                            .hasAnyAuthority("CLIENTE", "MENTOR", "ADMIN")
	                            .requestMatchers("/listar", "/listarMentor", "/mentor/{id}/excluir", "/{id}/excluir").hasAuthority("ADMIN"))
	            .formLogin(form -> form
	            	    .loginPage("/login")
	            	    .loginProcessingUrl("/login")
	            	    .defaultSuccessUrl("/perfil")
	                    .failureUrl("/login?error=true")
	            	    .permitAll())
	            .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());

	    return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	  @Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
	}

	

}
