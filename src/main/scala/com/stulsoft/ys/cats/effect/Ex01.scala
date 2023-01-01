/*
 * Copyright (c) 2022. StulSoft
 */

package com.stulsoft.ys.cats.effect

import cats.effect.{IO, IOApp}
import com.typesafe.scalalogging.StrictLogging

object Ex01 extends IOApp.Simple, StrictLogging:
  override def run: IO[Unit] =
    logger.info("==>run")
    IO.println("Hello, World!")

