# GrInjector
[![Maven Central](https://img.shields.io/maven-central/v/com.github.henryco/grinjector.svg)](http://repo1.maven.org/maven2/com/github/henryco/grinjector/)  [![GitHub license](https://img.shields.io/badge/license-MIT-yellow.svg)](https://raw.githubusercontent.com/henryco/GrInjector/master/LICENSE)
##### Lightweight reflective dependency injector for java and Android.  
  ____

  
**Gradle**  
```Groovy
    compile 'com.github.henryco:grinjector:1.2.1'
```  
**Maven**  
```XML
    <dependency>
        <groupId>com.github.henryco</groupId>
        <artifactId>grinjector</artifactId>
        <version>1.2.1</version>
    </dependency>
```

____

> ##### **This is guide for version `1.2.+` for version `1.1.0` and below see [this](https://bitbucket.org/tinder-samurai/grinjector/wiki/Version%201.1.0%20and%20below) .**

# **About**
**GrInjector** is simple reflective dependency injector for Java and Android that provides out of the box all functionality you may need. The whole user part of library contains a small number of annotations:

* **`@Module`**
* **`@Provide`**
* **`@TargetInterface`**

plus standard [**`JSR-330`**](https://docs.oracle.com/cd/E19798-01/821-1841/gjxvg/index.html) annotations from package [**`javax.inject`**](https://mvnrepository.com/artifact/javax.inject/javax.inject/1) which are:

* **`@Named`**
* **`@Inject`**
* **`@Singleton`**


each of them will be described below.  
  
 ****
  
# **Module declaration**
The first thing you should know is that the dependency injector needs to declare at least one root module. In order to declare a class a module, it's enough just to add the **`@Module`** annotation. Modules can also include other modules and get dependencies on them, for this case annotation has field: 
**`Class<?>[] include() default {}; `**


```java

@Module (			// <- This annotation marks class as dependency module
	include = ModuleGE.class // <- Include statement adds child modules
) public class ModuleGC {


	@Provide("prv_not_com_a")
	public NotAnnotatedComponentA prvCompA(@Named("strArr") String[] arr,
										   List<Integer> list) {
		return new NotAnnotatedComponentA(arr, (ArrayList<Integer>) list);
	}


	@Provide("strArr")
	public String[] provideStringArray() {
		return new String[]{"1", "2", "3", "4"};
	}


	@Provide @Singleton
	public float provideFloatB(Scanner scanner) {
		return 23;
	}

}

```



The necessary module, before it will be used, should be added to the main library of dependencies of the library via static method call **`GrInjector.addModules()`** as example is shown below.

```java
public final class SomeMainClass {

	public static void main(String[] args) {
		GrInjector.addModules(ModuleGA.class);
		//Some application code below
	}
}
```


****

# **Dependency recipients**
To comply with the control inversion, that where it is necessary to introduce dependence should indicate where these dependencies are created and processed - in the module. For this, the annotation has fields: 
```java 
Class<?>[] targets() default {};           // eg. {ClassA.class, ClassB.class, ClaccC.class}
Class<?>[] targetsRootClass() default {};  // eg. net.henryco.some.package.SomeClass.class
String[] targetsRootPath() default {};     // eg. "net.henryco.some.package"
```
Instead of the fields listed above, you can use the **`@TargetInterface`** annotation which should note the interface in which dependency recipients are specified. The class of this interface should be passed to the module in the **`@Module`** annotation through its **`targets`** field. In order to specify the dependency receiver(s) in the interface, you should create a method marked with an **`@Inject`** annotation, in which case the recipients (targets) well be arguments of this method. The number of methods, as well as their names, doesn't matter.
An example of such an interface below.
```java
@TargetInterface
public interface ExampleTargetInterface {

	@Inject void inject(
		SomeControllerA target1,
		SomeControllerB target2
		// .
		// .
		// .
		// Other targets
	);
}
```


****

# **Providing dependencies (beans)**
To create your own bean, simply create a method in the module and mark it with **`@Provide`** annotation. Such a method can have arguments - dependencies that will be automatically provided to it. You should be careful to avoid self injecting otherwise you will fall into an infinite loop of recursive self-instancing.
```java
@Module(targets = {SomeTarget.class})
public final class SomeModule {

        @Provide // bean will have method name which is: "provideNumberString"
        public String provideNumberString(Long number) { // argument will be automatically injected
                return "number: " + number.toString();
        }
        
        @Provide("abc") // bean will have name "abc"
        public int abcBean() {
                return 42;
        }
}
```

****

# **Components binding**
Components are like beans that already defined outside the module (and also can be can be marked with **`@Singleton`**). GrInjector allows you to automatically create and link components and provide them required dependencies. For this, the **`class`** component should be marked with the **`@Provide`** annotation, and added to the module. For this case, **`@Module`** annotation has the following fields:
```java
Class<?>[] components() default {};
Class<?>[] componentsRootClass() default {};
String[] componentsRootPath() default {};
```

****

# **Singletons**
To make the necessary component (or bean) a singleton, it's enough to mark it with **`@Singleton`** annotation and its instance will be created only once.


****

# **Injection**
**`GrInjector`** library allows dependency injection via **`FIELD`**, **`SETTER`** or **`CONSTRUCTOR`**, you just need to add an **`@Inject`** annotation and library will resolve required instance by type or name, automatically. Pay attention, that injection via **`CONSTRUCTOR`** works only with components, that's because components are created and stored in a dependency container, in other cases for injection you should add target class to module and then call the static method **`GrInjector.inject()`** in that class, also you can just get single component by calling method **`GrInjector.getComponent();`**. Since version **`1.2.0`** you **should** use **`@Named`** annotation with methods, fields and constructors parameters to resolve required dependencies by their names, like: 
```java
@Inject
public SomeConstructor(@Named("wow") String wow) { ... }
```


****

# **Custom IClassFinder implementation**
Sometimes there is a need to use own implementation of the class search engine, since the standard implementation is not able to find classes in extraneous jar's. In this case, the library contains the interface IClassFinder, the implementation of which should be passed to the method:

```java
GrInjector.addModules(IClassFinder imp, Class<?>[] modules);

```
**`IClassFinder`** interface contains only one method which means that you can use a lambda expression:
```java
package com.github.henryco.injector.meta.resolver;
import java.util.List;

@functionalinterface 
public interface IClassFinder {
	List<Class<?>> getClassesForPackage(String pckgname) throws ClassNotFoundException;
}
```


  ****

# **Full code example**

```java
@Module(targets = {SomeController.class})
public class SomeModule {
	// Beans and components here
}

@Singleton
@Provide("some_comp")
public class SomeComponent {
       
        private final Sting abc;
        
        @Inject // By constructor and type
        public SomeComponent(String text) {
                this.abc = text;
        }
}

public class SomeController {

        // By field, resolved by name
	@Inject @Named("some_comp") 	
	private SomeComponent comp;

	private float someValue;

	public SomeController() {
		init();
	}

	private void init() {
                
                // Inject via @Inject annotations
		GrInjector.inject(this);

                // Get component manually from dependency container
                OtherComponent other = GrInjector.getComponent("component_name");
		
                // some code
	}

	@Inject // By Setter, resolved by type
	public void setSomeValue(float someValue) {
		this.someValue = someValue;
	}

}

public class SomeMain {
        public static void main(String[] args) {
                GrInjector.addModules(SomeModule.class);
		SomeController controller = new SomeController();
		
                // some code
	}
}
```
