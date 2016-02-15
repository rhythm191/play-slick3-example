package models

import org.specs2.specification.AfterEach
import play.api.db.DBApi
import play.api.db.evolutions.Evolutions
import play.api.test._
import testhelpers.Injector

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class ModelSpecs2Spec extends PlaySpecification with AfterEach {

  val projectRepo = Injector.inject[ProjectRepo]

  override def after = {
    val dbapi = Injector.inject[DBApi]
    Evolutions.cleanupEvolutions(dbapi.database("default"))
  }



  "An item " should {

    "be inserted during the first test case" in {
      running(FakeApplication()) {

        val action = projectRepo.create("A")
          .flatMap(_ => projectRepo.all)

        val result = Await.result(action, Duration.Inf)

        result must be_==(List(Project(1, "A")))
      }
    }

    "and not exist in the second test case" in {
      running(FakeApplication()) {

        val action = projectRepo.all

        val result = Await.result(action, Duration.Inf)

        result must be_==(List.empty)
      }
    }


  }

}
