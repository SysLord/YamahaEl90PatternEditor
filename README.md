# YamahaEL90B00Parser
Parser for getting usable data out of the Yamaha EL90 B00 File format.

Example B00 file in exampleB00File folder.

B00 file structure was not reverse engineered by me but this guy: http://serge45.free.fr/electone/texte.htm


# Compilation / running problems

Native libraries:
The lightweight java game library (lwjgl) is used, which needs some native libraries.
At the moment the following classpath entry of the patternEditor project makes it possible to run the patternEditor with OpenAl support from eclipse build.
Maven (atm) does not include the native libs at all, and even then their path needs to be set. I couldn't work it out, yet.

```
<classpathentry kind="src" output="target/classes" path="src/main/java">
	<attributes>
		<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="el90-pattern-editor/src/main/natives"/>
		
```

