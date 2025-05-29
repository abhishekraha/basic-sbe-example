plugins {
    id("java")
}

group = "com.abhishekraha.dev"
version = "1.0-SNAPSHOT"

val generatorImplementation: Configuration by configurations.creating
val schemaList = listOf(
    "${projectDir}/src/main/resources/sbe/schema/sample-message-schema.xml"
)
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


dependencies {
    generatorImplementation("uk.co.real-logic:sbe-all:1.35.0")
    implementation("uk.co.real-logic:sbe-all:1.35.0")
    implementation("org.agrona:agrona:2.2.0")

}

sourceSets {
    main{
        java {
            setSrcDirs(
                listOf(
                    "${projectDir}/src/main/java",
                    "${buildDir}/generated-sources"
                )
            )
        }
    }
}


tasks.register<JavaExec>("genSources"){
    classpath += generatorImplementation
    mainClass.set("uk.co.real_logic.sbe.SbeTool")
    jvmArgs = listOf(
        "-Dsbe.output.dir=${buildDir}/generated-sources",
        "-Dsbe.xinclude.aware=true",
        "-Dsbe.java.generate.interfaces=true"
    )
    args = schemaList
}

tasks.compileJava{
    dependsOn("genSources")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}