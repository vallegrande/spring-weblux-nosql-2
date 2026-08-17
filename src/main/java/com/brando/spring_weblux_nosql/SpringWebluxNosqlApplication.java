package com.brando.spring_weblux_nosql;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringWebluxNosqlApplication implements CommandLineRunner {

	@Autowired
	private MongoClientSettings mongoClientSettings;

	@Autowired
	private ReactiveMongoDatabaseFactory mongoDatabaseFactory;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebluxNosqlApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println();
		System.out.println("========================================================");
		System.out.println("  DIAGNOSTICO REAL DE CONEXION MONGODB");
		System.out.println("  (leyendo MongoClientSettings que USA el driver)");
		System.out.println("========================================================");

		List<ServerAddress> hostsSeed = mongoClientSettings.getClusterSettings().getHosts();
		String srvHost = mongoClientSettings.getClusterSettings().getSrvHost();
		String modo = mongoClientSettings.getClusterSettings().getMode().name();
		boolean tieneCredencial = mongoClientSettings.getCredential() != null;
		boolean sslActivo = mongoClientSettings.getSslSettings().isEnabled();
		String tipoCluster = mongoClientSettings.getClusterSettings().getRequiredClusterType().name();
		String replicaSet = mongoClientSettings.getClusterSettings().getRequiredReplicaSetName();
		String hostsSeedStr = hostsSeed.stream().map(ServerAddress::toString).collect(Collectors.joining(", "));

		System.out.println("  [CLUSTER]  Hosts seed (pre-SRV)  = " + hostsSeedStr);
		System.out.println("  [CLUSTER]  SRV host (post-SRV)   = " + (srvHost != null ? srvHost : "(no usa SRV)"));
		System.out.println("  [CLUSTER]  ReplicaSet requerido  = " + (replicaSet != null ? replicaSet : "ninguno"));
		System.out.println("  [CLUSTER]  Modo cluster          = " + modo);
		System.out.println("  [CLUSTER]  Tipo requerido        = " + tipoCluster);
		System.out.println("  [CLUSTER]  SSL/TLS activado      = " + sslActivo);
		System.out.println("  [CLUSTER]  Credencial presente   = " + tieneCredencial);
		System.out.println("  [CLUSTER]  Usuario credencial    = " + (tieneCredencial ? mongoClientSettings.getCredential().getUserName() : "-"));
		System.out.println();

		boolean usaSrvAtlas = srvHost != null && srvHost.contains("mongodb.net");
		boolean esUir = srvHost != null && srvHost.contains("pofa1rg");
		boolean esLocalhostSinSrv = srvHost == null && hostsSeed.stream().anyMatch(h ->
				h.getHost().contains("localhost") || h.getHost().contains("127.0.0.1"));

		try {
			String dbName = mongoDatabaseFactory.getMongoDatabase().block().getName();
			System.out.println("  [CONEXION REAL] Base de datos activa = " + dbName);
			System.out.println();

			if (usaSrvAtlas || esUir) {
				System.out.println("  >>> ✅ CONECTADO AL CLUSTER DE MONGODB ATLAS / UIR");
				System.out.println("  >>> SRV host = " + srvHost);
				System.out.println("  >>> NO estas en localhost - estas en LA NUBE (AWS SA_EAST_1)");
			} else if (esLocalhostSinSrv) {
				System.out.println("  >>> ❌ CUIDADO: SIGUES EN LOCALHOST/DOCKER");
				System.out.println("  >>> Hosts: " + hostsSeedStr);
			} else {
				System.out.println("  >>> Revisa manualmente los logs arriba.");
			}
		} catch (Exception e) {
			System.out.println("  ERROR al obtener informacion: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("========================================================");
		System.out.println();
	}

}
