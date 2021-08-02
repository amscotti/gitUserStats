# gitUserStats

An application that is able to give an overview of a Git repository user activity, showing the number of commits, first and last commit dates. This is also an experiment of testing Groovy with using static typing, to be able try to compile the Jar into a native image with [GraalVM](https://www.graalvm.org/), sadly I was unable to create an native image with GraalVM version 21.2.0.r16.

This project uses
* Groovy for the Programming language
* Eclipse's jgit Library for reading the commit data from Git
* ASCIITable for displaying the output
* Picocli for dealing with command line arguments

## Building an executable Jar
There is an Gradle Wrapper installed, you can use `gradlew` or `gradlew.bat` based on your OS
Run `./gradlew build` to create an executable Jar file called `/build/libs/gitUserStats-all.jar`. You can run this with `java -jar /build/libs/gitUserStats-all.jar`

## Running with Gradle
You can also use Gradle to run the project with `./gradlew run --arg <PATH TO GIT REPO>`

## Output of error from native-image
For anyone interested, here is the output when trying to create an `native-image` with `21.2.0.r16`. I will be coming this project around to keep trying when newer versions of GraalVM come out.

```
Error: Unsupported features in 2 methods
Detailed message:
Error: com.oracle.graal.pointsto.constraints.UnsupportedFeatureException: Detected an instance of Random/SplittableRandom class in the image heap. Instances created during image generation have cached seed values and don't behave as expected.  Object has been initialized by the org.eclipse.jgit.internal.storage.file.WindowCache class initializer with a trace:
 	at java.util.Random.<init>(Random.java:105)
	at org.eclipse.jgit.internal.storage.file.WindowCache.<clinit>(WindowCache.java:336)
. Try avoiding to initialize the class that caused initialization of the object. The object was probably created by a class initializer and is reachable from a static field. You can request class initialization at image runtime by using the option --initialize-at-run-time=<class-name>. Or you can write your own initialization methods and call them explicitly from your main entry point.
Trace:
	at parsing org.eclipse.jgit.internal.storage.file.WindowCache.evict(WindowCache.java:656)
Call path from entry point to org.eclipse.jgit.internal.storage.file.WindowCache.evict():
	at org.eclipse.jgit.internal.storage.file.WindowCache.evict(WindowCache.java:655)
	at org.eclipse.jgit.internal.storage.file.WindowCache.getOrLoad(WindowCache.java:616)
	at org.eclipse.jgit.internal.storage.file.WindowCache.get(WindowCache.java:385)
	at org.eclipse.jgit.internal.storage.file.WindowCursor.pin(WindowCursor.java:327)
	at org.eclipse.jgit.internal.storage.file.WindowCursor.copy(WindowCursor.java:226)
	at org.eclipse.jgit.internal.storage.file.PackInputStream.read(PackInputStream.java:37)
	at com.oracle.svm.reflect.InputStream_read_440398ed669560ee7c315f4ebe9086c404f8daa0_1075.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Method.java:567)
	at javax.xml.transform.TransformerException.printStackTrace(TransformerException.java:345)
	at javax.xml.transform.TransformerException.printStackTrace(TransformerException.java:285)
	at com.oracle.svm.jni.functions.JNIFunctions.ExceptionDescribe(JNIFunctions.java:777)
	at com.oracle.svm.core.code.IsolateEnterStub.JNIFunctions_ExceptionDescribe_b5412f7570bccae90b000bc37855f00408b2ad73(generated:0)
Error: com.oracle.graal.pointsto.constraints.UnsupportedFeatureException: Detected an instance of Random/SplittableRandom class in the image heap. Instances created during image generation have cached seed values and don't behave as expected.  Object has been initialized by the org.eclipse.jgit.util.FileUtils class initializer with a trace:
 	at java.util.Random.<init>(Random.java:105)
	at org.eclipse.jgit.util.FileUtils.<clinit>(FileUtils.java:60)
. Try avoiding to initialize the class that caused initialization of the object. The object was probably created by a class initializer and is reachable from a static field. You can request class initialization at image runtime by using the option --initialize-at-run-time=<class-name>. Or you can write your own initialization methods and call them explicitly from your main entry point.
Trace:
	at parsing org.eclipse.jgit.util.FileUtils.delay(FileUtils.java:1017)
Call path from entry point to org.eclipse.jgit.util.FileUtils.delay(long, long, long):
	at org.eclipse.jgit.util.FileUtils.delay(FileUtils.java:1014)
	at org.eclipse.jgit.internal.storage.file.FileReftableStack.reload(FileReftableStack.java:224)
	at org.eclipse.jgit.internal.storage.file.FileReftableStack.<init>(FileReftableStack.java:125)
	at org.eclipse.jgit.internal.storage.file.FileReftableDatabase.<init>(FileReftableDatabase.java:79)
	at org.eclipse.jgit.internal.storage.file.FileReftableDatabase.<init>(FileReftableDatabase.java:71)
	at org.eclipse.jgit.internal.storage.file.FileRepository.<init>(FileRepository.java:183)
	at org.eclipse.jgit.storage.file.FileRepositoryBuilder.build(FileRepositoryBuilder.java:55)
	at App.getCommits(App.groovy:29)
	at com.oracle.svm.reflect.App_getCommits_59f153149aafd1e71215e9b1d9ec4564f60d127c_1832.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Method.java:567)
	at javax.xml.transform.TransformerException.printStackTrace(TransformerException.java:345)
	at javax.xml.transform.TransformerException.printStackTrace(TransformerException.java:285)
	at com.oracle.svm.jni.functions.JNIFunctions.ExceptionDescribe(JNIFunctions.java:777)
	at com.oracle.svm.core.code.IsolateEnterStub.JNIFunctions_ExceptionDescribe_b5412f7570bccae90b000bc37855f00408b2ad73(generated:0)
```