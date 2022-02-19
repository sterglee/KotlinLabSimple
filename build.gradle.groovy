

plugins {
    id "org.jetbrains.kotlin.jvm" version "1.5.20"
}
apply plugin: 'java'
apply plugin: 'kotlin'


apply plugin: "application"


    repositories {
        mavenCentral()
        jcenter()
    }

dependencies {
     implementation fileTree(dir: "lib", include: "*.jar")
     
    implementation fileTree(dir: "libkotlin", include: "*.jar")
   
    implementation fileTree(dir: "build/classes/java/main", include: "*.class")
    implementation fileTree(dir: "src/", include: "*.class")
  
}



    
sourceSets {
    main {
       
        java {
             
           Set<File> classpathFiles =  fileTree (dir : "lib", include: "*.jar").getFiles()+fileTree(dir:  "libkotlin", include: "*.jar").getFiles()+ new File("kotlinLab/src/")
   compileClasspath =  files(classpathFiles)
            
            srcDir "src/"
    }
   }
}

jar {
    baseName="kotlinLab"
    includeEmptyDirs = false
    mainClassName="kotlinLabExec.kotlinLab.kotlinLab"
    
     
           

            def libpathfiles = fileTree (dir : "lib", include: "*.jar").getFiles().name
           def kotlinlibpathfiles = fileTree (dir : "libkotlin", include: "*.jar").getFiles().name
           def appendedLib = libpathfiles.collect { item -> "lib/"+item}.join(' ')
           def kotlinappendedLib = kotlinlibpathfiles.collect { item -> "libkotlin/"+item}.join(' ')
           
        
        
         
           
    manifest {
        attributes('Class-Path': appendedLib+' '+kotlinappendedLib,
'Main-Class':'kotlinLabExec.kotlinLab.kotlinLab')
    }

  
def extensions = [ 'class', 'ksci', 'jsci', 'gif', 'm', 'htm', 'html', 'jpg', 'plots-jsci',  'png', 'properties',  'txt', 'javaSGT', 'wav-jsci'] 
 
   from(sourceSets.main.java.srcDirs) {
      extensions.each({extension -> include "**/*.${extension}"})
   }


    
 }


task fatJar(type: Jar) {

    baseName="kotlinLab"
    includeEmptyDirs = false
    mainClassName="kotlinLabExec.kotlinLab.kotlinLab"
    zip64=true

    manifest {
      attributes(
'Main-Class':'kotlinLabExec.kotlinLab.kotlinLab')
    }
  
  
   
  classifier = 'all'
    
  
    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } 
    }
 {
      //  exclude "META-INF/*.SF"
       exclude "META-INF/*.DSA"
     //   exclude "META-INF/*.RSA"
    }

   
    with jar
   
}
