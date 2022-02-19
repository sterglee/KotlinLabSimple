package kotlinLabGlobal.Interpreter

import java.io.File
import java.util.jar.JarFile

object PathUtils {

    // used to initialize the kotlin.script.classpath variable of the Kotlin script engine
    fun classpathFromJar(outJar:File) = run {
        val manifest = JarFile(outJar).manifest
        manifest.mainAttributes.getValue("Class-Path").split(" ").map { File(it).toURI().toURL() }
    }+outJar.toURI().toURL()
}