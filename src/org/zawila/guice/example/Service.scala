package org.zawila.guice.example

import javax.inject.{Inject, Named}

import com.google.inject.Singleton

trait Service {
  def service: String
}

class FirstService extends Service {
  override def service: String = "FirstService"
}

class SecondService extends Service {
  override def service: String = "SecondService"
}

class InstanceService(name: String) extends Service {
  override def service: String = name
}

class FooService extends Service {
  override def service: String = "FooService"
}

class BarService extends Service {
  override def service: String = "BarService"
}

object SingletonService extends Service {
  override def service: String = "Singleton"
}

class PrototypeService extends Service {
  override def service: String = "Prototype"
}

@Singleton class AnnotationService extends Service {
  override def service: String = "AnnotationService"
}

class Component @Inject()(service: Service) {
  def callTheService: String = service.service
}

class FooComponent @Inject()(@Named("foo") service: Service) {
  def callTheService: String = service.service
}

class BarComponent @Inject()(@Named("bar") service: Service) {
  def callTheService: String = service.service
}

trait SingletonComponentInterface {
  def service: String
}

object SingletonComponent extends SingletonComponentInterface {
  @Inject val serviceObject: Service = null

  override def service: String = serviceObject.service
}
