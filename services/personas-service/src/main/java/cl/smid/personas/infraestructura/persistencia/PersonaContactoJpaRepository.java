package cl.smid.personas.infraestructura.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data de {@link PersonaContactoEntity}. La gestión habitual de contactos se
 * realiza por cascada desde {@link PersonaEntity} (cascade ALL + orphanRemoval); este repositorio
 * queda disponible para consultas o mantenimientos directos sobre contactos si fueran necesarios.
 */
public interface PersonaContactoJpaRepository extends JpaRepository<PersonaContactoEntity, Long> {
}
