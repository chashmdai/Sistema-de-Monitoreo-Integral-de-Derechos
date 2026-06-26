package cl.smid.smidservice.config;

import cl.smid.smidservice.entity.*;
import cl.smid.smidservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(EjeRepository ejeRepo,
                                   DerechoRepository derechoRepo,
                                   InstitucionRepository instRepo,
                                   FactorRiesgoRepository factorRepo) {
        return args -> {
            if (ejeRepo.count() == 0) {
                Eje vida = ejeRepo.save(Eje.builder().nombre("Vida y Supervivencia").build());
                Eje desarrollo = ejeRepo.save(Eje.builder().nombre("Desarrollo").build());
                Eje proteccion = ejeRepo.save(Eje.builder().nombre("Protección").build());
                Eje participacion = ejeRepo.save(Eje.builder().nombre("Participación").build());

                derechoRepo.saveAll(Arrays.asList(
                    Derecho.builder().nombre("Derecho a la Salud").eje(vida).build(),
                    Derecho.builder().nombre("Derecho a la Educación").eje(desarrollo).build(),
                    Derecho.builder().nombre("Vivir en Familia").eje(proteccion).build(),
                    Derecho.builder().nombre("No ser discriminado").eje(proteccion).build(),
                    Derecho.builder().nombre("Derecho a ser Oído").eje(participacion).build(),
                    Derecho.builder().nombre("Identidad").eje(vida).build()
                ));

                factorRepo.saveAll(Arrays.asList(
                    FactorRiesgo.builder().nombre("LGTBQ+").build(),
                    FactorRiesgo.builder().nombre("Migrante").build(),
                    FactorRiesgo.builder().nombre("Discapacidad").build(),
                    FactorRiesgo.builder().nombre("Pueblos Originarios").build(),
                    FactorRiesgo.builder().nombre("Primera Infancia").build()
                ));

                instRepo.saveAll(Arrays.asList(
                    Institucion.builder().nombre("Hospital Regional de Talca").esEspecializado(true).region("Maule").build(),
                    Institucion.builder().nombre("Carabineros de Chile - 3ra Comisaría").esEspecializado(false).region("Maule").build(),
                    Institucion.builder().nombre("SLEP Maule Costa").esEspecializado(true).region("Maule").build(),
                    Institucion.builder().nombre("Hospital Regional Rancagua").esEspecializado(true).region("O'Higgins").build(),
                    Institucion.builder().nombre("Juzgado de Familia").esEspecializado(true).region("Maule").build(),
                    Institucion.builder().nombre("Municipalidad de San Clemente").esEspecializado(false).region("Maule").build()
                ));
            }
        };
    }
}
