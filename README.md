# Just Input

Java implementation of primitive to create an input stream based on a variety of common locations including the class path, file system and over the network.  It is just that simple, but packaged all in one place for ease of use.   

This code is so common, I have been copying it from myself for years.  It ia put here to end the need to clone the code anymore.  Hope it helps others do the same.


Build status:[![build_status](https://travis-ci.org/nofacepress/justinput.svg?branch=master)](https://travis-ci.org/nofacepress/justinput)


## Maven Setup

```xml
<dependency>
  <groupId>com.nofacepress</groupId>
  <artifactId>justinput</artifactId>
  <version>0.4.0</version>
</dependency>
```

## Path Location Strategy

As much as possible, the commonly used path interpretation have been applied, so the library should be an easy replacement for less flexible implementations. 

| Path  | Interpretation |
| ------------- | ------------- |
| `classpath:`XYZ  | Will search the class path for file *XYZ*.  |
| .\*`://`.\*  | Will use a URL to fetch the data.  |
| `file:`XYZ  | Will search the file system for file *XYZ*.  |
| *XYZ*  | Will first look in the file system and then the class path for *XYZ*.  |


## Non Static Usage 

As with anything in java that needs to load a resource, a ClassLoader is required.  By using the non-static methods, the ClassLoader is determined from the class itself, simplifying 99% of use cases.  If a different class loader is required, or if this is being used in high performance code, consider using the static methods instead.

```java
		InputStream input = new JustInput("classpath:myreource.json").getInputStream();
		InputStream input = new JustInput("http://mysite.com/myreource.json").getInputStream();
		InputStream input = new JustInput("file:../myreource.json").getInputStream();
		InputStream input = new JustInput("myreource.json").getInputStream();

		Reader input = new JustInput("classpath:myreource.json").getReader();
		Reader input = new JustInput("http://mysite.com/myreource.json").getReader();
		Reader input = new JustInput("file:../myreource.json").getReader();
		Reader input = new JustInput("myreource.json").getReader();

		URL url = new JustInput("classpath:myreource.json").getUrl();
		URL url = new JustInput("http://mysite.com/myreource.json").getUrl();
		URL url = new JustInput("file:../myreource.json").getUrl();
		URL url = new JustInput("myreource.json").getUrl();
```

## Static Usage 


```java
		ClassLoader myClassLoader = getClass().getClassLoader();
		
		InputStream input = JustInput.newInputStream("classpath:myreource.json", myClassLoader);
		InputStream input = JustInput.newInputStream("http://mysite.com/myreource.json", myClassLoader);
		InputStream input = JustInput.newInputStream("file:../myreource.json", myClassLoader);
		InputStream input = JustInput.newInputStream("myreource.json", myClassLoader);

		Reader input = JustInput.newReader("classpath:myreource.json", myClassLoader);
		Reader input = JustInput.newReader("http://mysite.com/myreource.json", myClassLoader);
		Reader input = JustInput.newReader("file:../myreource.json", myClassLoader);
		Reader input = JustInput.newReader("myreource.json", myClassLoader);

		URL url = JustInput.newUrl("classpath:myreource.json", myClassLoader);
		URL url = JustInput.newUrl("http://mysite.com/myreource.json", myClassLoader);
		URL url = JustInput.newUrl("file:../myreource.json", myClassLoader);
		URL url = JustInput.newUrl("myreource.json", myClassLoader);
```
