package com.banquito.gestion_vehiculos.config;

import com.banquito.gestion_vehiculos.enums.RolEnum;
import com.banquito.gestion_vehiculos.model.Usuario;
import com.banquito.gestion_vehiculos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import com.banquito.gestion_vehiculos.model.Concesionario;
import com.banquito.gestion_vehiculos.model.Vendedor;
import com.banquito.gestion_vehiculos.model.Vehiculo;
import com.banquito.gestion_vehiculos.model.IdentificadorVehiculo;
import com.banquito.gestion_vehiculos.enums.EstadoConcesionarioEnum;
import com.banquito.gestion_vehiculos.enums.EstadoVendedorEnum;
import com.banquito.gestion_vehiculos.enums.EstadoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.TipoVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CombustibleVehiculoEnum;
import com.banquito.gestion_vehiculos.enums.CondicionVehiculoEnum;
import com.banquito.gestion_vehiculos.repository.ConcesionarioRepository;
import com.banquito.gestion_vehiculos.repository.IdentificadorVehiculoRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitialDataConfig implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    @Autowired
    private IdentificadorVehiculoRepository identificadorVehiculoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuario admin por defecto si no existe
        if (!usuarioRepository.existsByEmail("admin@administracionsistema.com")) {
            Usuario admin = new Usuario();
            admin.setId(UUID.randomUUID().toString());
            admin.setEmail("admin@administracionsistema.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(RolEnum.ADMIN);
            admin.setActivo(true);
            admin.setVersion(0L);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado: admin@administracionsistema.com / admin123");
        }

        // Crear datos de prueba si no existen concesionarios
        if (concesionarioRepository.count() == 0) {
            // Crear identificador de vehículo
            IdentificadorVehiculo identificador = new IdentificadorVehiculo();
            identificador.setId(UUID.randomUUID().toString());
            identificador.setPlaca("ABC-1234");
            identificador.setChasis("1HGCM82633A004352");
            identificador.setMotor("MTR123456789");
            identificadorVehiculoRepository.save(identificador);

            // Crear concesionario de prueba
            Concesionario concesionario = new Concesionario();
            concesionario.setId(UUID.randomUUID().toString());
            concesionario.setRuc("1234567890001");
            concesionario.setRazonSocial("AutoMax S.A.");
            concesionario.setDireccion("Av. 6 de Diciembre N33-35");
            concesionario.setTelefono("02-2234567");
            concesionario.setEmailContacto("contacto@automax.com");
            concesionario.setEstado(EstadoConcesionarioEnum.ACTIVO);
            concesionario.setVersion(0L);

            // Crear vendedor de prueba
            Vendedor vendedor = new Vendedor();
            vendedor.setId(UUID.randomUUID().toString());
            vendedor.setNombre("Carlos Rodríguez");
            vendedor.setTelefono("0987654321");
            vendedor.setEmail("carlos.rodriguez@automax.com");
            vendedor.setCedula("0102030405");
            vendedor.setEstado(EstadoVendedorEnum.ACTIVO);
            vendedor.setVersion(0L);

            // Crear vehículo de prueba
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setId(UUID.randomUUID().toString());
            vehiculo.setMarca("Toyota");
            vehiculo.setModelo("Corolla");
            vehiculo.setCilindraje(1.8);
            vehiculo.setAnio("2023");
            vehiculo.setValor(new BigDecimal("25000.00"));
            vehiculo.setColor("Blanco");
            vehiculo.setExtras("Aire acondicionado, sistema de sonido premium");
            vehiculo.setEstado(EstadoVehiculoEnum.DISPONIBLE);
            vehiculo.setTipo(TipoVehiculoEnum.SEDAN);
            vehiculo.setCombustible(CombustibleVehiculoEnum.GASOLINA);
            vehiculo.setCondicion(CondicionVehiculoEnum.NUEVO);
            vehiculo.setPlaca("ABC-1234");
            vehiculo.setVersion(0L);

            // Asignar vendedor y vehículo al concesionario
            List<Vendedor> vendedores = new ArrayList<>();
            vendedores.add(vendedor);
            concesionario.setVendedores(vendedores);

            List<Vehiculo> vehiculos = new ArrayList<>();
            vehiculos.add(vehiculo);
            concesionario.setVehiculos(vehiculos);

            concesionarioRepository.save(concesionario);
            System.out.println("Datos de prueba creados: Concesionario AutoMax S.A. con vendedor y vehículo");
        }
    }
} 