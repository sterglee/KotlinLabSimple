

plugins {

    java
    application
    kotlin("jvm") version "1.7.0-Beta"
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))

    }
}

    repositories {
        mavenCentral()
        jcenter()
    }

dependencies {
    implementation(fileTree("./lib/") { include ("*.jar")})
    implementation(fileTree("./libkotlin/"){include ("*.jar")})
}

application {
    mainClassName = "kotlinLabExec.kotlinLab.kotlinLab"
}
  



sourceSets {
    main {
        java.srcDir("src/")
    }
}

tasks.register<Jar>("uberJar") {
    archiveClassifier.set("uber")
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    
    from( {
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith("jar")}.map{ zipTree(it) }
    })
}

var libpathfiles= StringBuilder();
File("./lib").walk().forEach { e ->
    if (e.toString().trim().endsWith(".jar"))
        libpathfiles.append(e.toString().replaceFirst("./", "").trim()).append(" ")

}

    var kotlinlibpathfiles= StringBuilder();
    File("./libkotlin").walk().forEach {
            e-> if (e.toString().trim().endsWith(".jar"))
        kotlinlibpathfiles.append(e.toString().replaceFirst("./","").trim()).append(" ")

    }



    var allClassPath = libpathfiles.toString().trim()+ " "+kotlinlibpathfiles.toString().trim()





tasks.jar {
    baseName = "kotlinLab"

    manifest {
            attributes["Class-Path"] = allClassPath.trim()
            attributes["Main-Class"] = "kotlinLabExec.kotlinLab.kotlinLab"
    }

}


    
 
