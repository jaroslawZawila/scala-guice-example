package org.zawila.guice.example

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice}
import org.scalatest.{FunSpec, Matchers}

class InjectionBasicTest extends FunSpec with Matchers {

  describe("Guice injection") {

    it("Basic injection test") {
      class ScalaModule extends AbstractModule {
        override protected def configure(): Unit = bind(classOf[Service]).to(classOf[FirstService])
      }

      class ScalaModule2 extends AbstractModule {
        override protected def configure(): Unit = bind(classOf[Service]).to(classOf[SecondService])
      }

      val injector = Guice.createInjector(new ScalaModule)
      val component = injector.getInstance(classOf[Component])

      val injector2 = Guice.createInjector(new ScalaModule2)
      val component2 = injector2.getInstance(classOf[Component])

      component.callTheService should equal ("FirstService")
      component2.callTheService should equal ("SecondService")
    }
  }

  it("Avoid to create new instance all the time") {
    class ScalaModule extends AbstractModule {
      override protected def configure(): Unit = {
        val instance1 = new InstanceService("InstanceService")
        bind(classOf[Service]).toInstance(instance1)
      }
    }

    val injector = Guice.createInjector(new ScalaModule)
    val service = injector.getInstance(classOf[Service])

    service.service should equal ("InstanceService")
  }

  it("binding with name annotation") {
    class ScalaModule extends AbstractModule {
      override def configure(): Unit = {
        bind(classOf[Service]).annotatedWith(Names.named("foo")).to(classOf[FooService])
        bind(classOf[Service]).annotatedWith(Names.named("bar")).to(classOf[BarService])
      }
    }

    val injector = Guice.createInjector(new ScalaModule)
    val fooComponent = injector.getInstance(classOf[FooComponent])
    val barComponent = injector.getInstance(classOf[BarComponent])

    fooComponent.callTheService should equal ("FooService")
    barComponent.callTheService should equal ("BarService")
  }

  it("Singleton or Prototype") {
    class ScalaModuleSingleton extends AbstractModule {
      override def configure(): Unit = {
        bind(classOf[Service]).toInstance(SingletonService)
      }
    }

    class ScalaModulePrototype extends AbstractModule {
      override def configure(): Unit = {
        bind(classOf[Service]).to(classOf[PrototypeService])
      }
    }

    val singletonInjector = Guice.createInjector(new ScalaModuleSingleton)
    val singleton = singletonInjector.getInstance(classOf[Service])
    singleton.service should equal ("Singleton")

    val secondInstance = singletonInjector.getInstance(classOf[Service])
    singleton should equal (secondInstance)

    val prototypeInjector = Guice.createInjector(new ScalaModulePrototype)
    val prototype = prototypeInjector.getInstance(classOf[Service])
    prototype.service should equal ("Prototype")

    val secondPrototypeInstance = prototypeInjector.getInstance(classOf[Service])
    prototype should not be(secondPrototypeInstance)
  }

  it("Singleton annotation") {
    class ScalaModuleSingleton extends AbstractModule {
      override def configure(): Unit = {
        bind(classOf[Service]).to(classOf[AnnotationService])
      }
    }

    val injector = Guice.createInjector(new ScalaModuleSingleton)
    val singleton = injector.getInstance(classOf[Service])
    val singletonSecond = injector.getInstance(classOf[Service])

    singleton.service should equal ("AnnotationService")
    singleton should equal (singletonSecond)
  }


  ignore("inject service example") {
    class InjectExampleModule extends AbstractModule {
      override def configure(): Unit = {
        val instance1 = new InstanceService("Instance 1")
        bind(classOf[Service]).toInstance(instance1)
        bind(classOf[SingletonComponentInterface]).toInstance(SingletonComponent)
      }
    }

    val injector = Guice.createInjector(new InjectExampleModule)
    val component = injector.getInstance(classOf[SingletonComponentInterface])

    component.service should be ("Instance 1")
  }
}
