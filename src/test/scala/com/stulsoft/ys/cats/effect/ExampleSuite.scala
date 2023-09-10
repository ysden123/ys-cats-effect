/*
 * Copyright (c) 2022. StulSoft
 */

package com.stulsoft.ys.cats.effect

import cats.effect.IO
import com.typesafe.scalalogging.StrictLogging
import munit.CatsEffectSuite

class ExampleSuite extends CatsEffectSuite, StrictLogging {
  test("make sure IO computes the right result") {
    logger.info("==>test1")
    IO.pure(1).map(_ + 2) flatMap { result =>
      logger.info("inside flatMap")
      IO(assertEquals(result, 3))
      IO(logger.info("Inside IO"))
    }
  }
}
