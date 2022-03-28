
java -Djava.library.path=.:./lib:. --add-modules=jdk.incubator.vector -Dkotlin.compiler.classpath=./lib/*:/libKotlin/*  -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops    -Xss5m -Xms2000m -Xmx25000m -jar kotlinLab.jar
