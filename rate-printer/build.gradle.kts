plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
    id("au.com.dius.pact")
}

springBoot {
    buildInfo()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("au.com.dius.pact.consumer:junit5:4.6.20")
}

tasks.test {
    systemProperty("pact.rootDir", "build/pacts")
    systemProperty("pact.writer.overwrite", "true")
}

pact {
    publish {
        pactBrokerUrl = "http://localhost:9292"
    }
}
