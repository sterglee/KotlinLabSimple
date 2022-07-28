
java -Djava.library.path=./lib;.  -Dkotlin.compiler.classpath=%cd%\lib\*;%cd%\libKotlin\*  -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops    -Xss5m -Xms2000m -Xmx25000m -jar kotlinLab.jar
